package cn.edu.njust.dpi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.ClassType;
import com.sun.jdi.Field;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.Connector.Argument;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.AccessWatchpointEvent;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.MethodExitEvent;
import com.sun.jdi.event.ModificationWatchpointEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.request.AccessWatchpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.MethodEntryRequest;
import com.sun.jdi.request.MethodExitRequest;
import com.sun.jdi.request.ModificationWatchpointRequest;

public class RunProgram {

  public RunProgram() {
    // TODO Auto-generated constructor stub
  }

  public static void main(String args[]) {
    VirtualMachineManager vmm = Bootstrap.virtualMachineManager();
    LaunchingConnector lc = vmm.defaultConnector();
    Map<String, Argument> subargs = lc.defaultArguments();
    subargs.get("main").setValue("cn.edu.njust.test.TestCase1");// class name
    subargs.get("options").setValue("-cp  /Users/yuanchi/Documents/javaworkspace/xu-java-dpi-old/bin");
    //subargs.get("options").setValue("-cp /Users/boyland/Documents/Eclipse/workspace/test-java-dpi/bin");
    for (Map.Entry<?,?> e : subargs.entrySet()) {
      System.out.println("for " + e.getKey() + " we have " + e.getValue());
    }
    try {
      VirtualMachine launch = lc.launch(subargs);
      MemoryModel mm=new MemoryModel();
      DefUse defuse=new DefUse();
      System.out.println("Launched: " + launch.description());
      traceThings(launch);
      launch.resume();
      drainEventQueue(launch,mm,defuse);
      InputStream is = launch.process().getInputStream();
      BufferedReader subbr = new BufferedReader(new InputStreamReader(is));
      String s;
      while ((s = subbr.readLine()) != null) {
        System.out.println("Program wrote " + s);
      }
      subbr.close();
      subbr = new BufferedReader(new InputStreamReader(launch.process().getErrorStream()));
      while ((s = subbr.readLine()) != null) {
        System.out.println("Program error wrote " + s);
      }
      subbr.close();
      System.out.println("Process ended: " + launch.process().exitValue());
      mm.print();
      //mm.printuse();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalConnectorArgumentsException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (VMStartException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  private static void traceThings(VirtualMachine vm) {
    EventRequestManager erm = vm.eventRequestManager();
    vm.allClasses();
    List<ReferenceType> allClasses = vm.allClasses();
    ClassPrepareRequest classPrepare = erm.createClassPrepareRequest();
    classPrepare.enable();
   
  }
  
  private static void drainEventQueue(VirtualMachine vm,MemoryModel mm,DefUse defuse) {
	    EventQueue q = vm.eventQueue();
	    try {
	      for (;;) {
	        EventSet events = q.remove();
	        for (Event e : events) {
	         // System.out.println("Got event : " + e.toString());
	          if (e instanceof ClassPrepareEvent) {
	            ClassPrepareEvent cep = (ClassPrepareEvent)e;
	            ReferenceType rt = cep.referenceType();
	            if (rt.name().startsWith("cn.edu.njust")) {
	            	for (Field f : rt.allFields()) {
	            		ModificationWatchpointRequest arq = vm.eventRequestManager().createModificationWatchpointRequest(f);
	            	 	arq.enable();
	            		AccessWatchpointRequest awe=vm.eventRequestManager().createAccessWatchpointRequest(f);
	            		awe.enable();
	            	}
	            	MethodEntryRequest mer=vm.eventRequestManager().createMethodEntryRequest();
	            	mer.addClassFilter(rt.name());
	            	mer.enable();
	            	MethodExitRequest mexit=vm.eventRequestManager().createMethodExitRequest();
	            	mexit.addClassFilter(rt.name());
	            	mexit.enable();
	            System.out.println("Preparing " + rt);
	            }
	          }else if(e instanceof ModificationWatchpointEvent){
	        	  ModificationWatchpointEvent mt=(ModificationWatchpointEvent)e;
	        	 // Field ft=(Field) mt.field();
	        	  System.out.println("definition");
	        	 System.out.println("Field "+mt.field() + " on " +mt.location());
	        	 mm.changeField(mt.object(),mt.field(),mt.valueToBe(),mt.location());
	        	// System.out.println("Field"+ft);
	        	 ObjectReference reference = mt.object();
	          }else if(e instanceof AccessWatchpointEvent){
	        	  AccessWatchpointEvent at=(AccessWatchpointEvent)e;
	        	 Definition definition= mm.getdef(at.object(),at.field());
	        	 if(definition==null){
	        		 System.out.println(at.field()+":No Definition-->"+at.location());
	        	 }
	        	 //System.out.println(at.field()+":"+definition.location+"-->"+at.location());
	        	  System.out.println("Access Field"+at.object()+" typename="+ at.field().typeName()+ " on " +at.location()+"--valuetype--"+at.valueCurrent().type());
	        	 mm.allReachable(at.object());
	          }else if(e instanceof MethodEntryEvent){
	        	  MethodEntryEvent me=(MethodEntryEvent)e;
	        	  System.out.println("MethodEntryEvent"+me.toString());
	        	  ObjectReference ref = getCurrentThis(me.thread());
	        	  System.out.println("all class state variables = "+mm.allReachable(ref));
	          }else if(e instanceof MethodExitEvent){
	        	  MethodExitEvent me=(MethodExitEvent)e;
	        	  System.out.println("MethodExitEvent"+me.toString());
	        	  ObjectReference ref = getCurrentThis(me.thread());
		        	 System.out.println("all class state variables = "+mm.allReachable(ref));
	          }else if (e instanceof VMDeathEvent) return;
	        }
	        events.resume();
	      }
	    } catch (InterruptedException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
	  }
  
  public static ObjectReference getCurrentThis(ThreadReference tr) {
	  try {
		return tr.frame(0).thisObject();
	} catch (IncompatibleThreadStateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
  }
}
