package cn.edu.njust.dpi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.edu.njust.dpi.MemoryModel.Obj;

import com.sun.jdi.ObjectReference;
import com.sun.jdi.Value;

public class DefUse {
	public Map<ObjectReference,Value> defuses=new HashMap<>();
	public void insertDefUse(ObjectReference obj,Value val){
		defuses.put(obj, val);
	}
	public void printDefUses(){
		 Set<Map.Entry<ObjectReference,Value>> set=defuses.entrySet();  
	       Iterator<Map.Entry<ObjectReference,Value>> iter=set.iterator();  
	       System.out.println("---defuse-");
	       while(iter.hasNext()){  
	           Map.Entry<ObjectReference, Value> entry=iter.next();  
	           System.out.println(entry.getKey()+"-->"+entry.getValue());  
	       }  
	}
}
