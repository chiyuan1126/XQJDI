package cn.edu.njust.dpi;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sun.jdi.Field;
import com.sun.jdi.Location;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.PrimitiveValue;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.Value;

public class MemoryModel {
	private int u = 0;
	public class Obj{
		private final ReferenceType myType;
		public Obj(ReferenceType rt){
			myType =rt;
		}
		private final int id= ++u;
		//用来记录类中变量的定义
		private Map<Field,Definition> fields=new HashMap<>();
		//set方法可以在发现新的定义的时候，把它归入到fields当中
		public void set(Field field, Obj convert, Location loc) {
		
			Definition df=new Definition(loc,convert);
			fields.put(field, df);
			// TODO Auto-generated method stub
			
		}
		
		public Definition getDefinition(Field field){
			return fields.get(field);
		}
		
		public Set<ClassStateVariable> getClassStateVariables(){
			
			HashSet<ClassStateVariable> classStateVariables=new HashSet<ClassStateVariable>();
			if(this!=primval){
				for (Map.Entry<Field, Definition> e : this.fields.entrySet()) {
					ClassStateVariable csv=new  ClassStateVariable(e.getKey());
					// System.out.println("field="+e.getKey());
					classStateVariables.add(csv);
					Definition definition=e.getValue();
					for (ClassStateVariable csv2 : definition.value.getClassStateVariables()) {
						ClassStateVariable csv3=new ClassStateVariable(e.getKey(), csv2);
						classStateVariables.add(csv3);
					}
					
				}  
			}
			return classStateVariables;
			
			//return ClassStateVariable;
		}
		/*public Map<Field,Definition> getfields(){
			return fields;
		}*/
		
		@Override
		public String toString() {
			if (this == primval) return "*";
			StringBuilder sb = new StringBuilder();
			sb.append(myType.name() + "(" + id);
			for (Map.Entry<Field, Definition> e : fields.entrySet()) {
				sb.append(",");
				sb.append(e.getKey().name());
				sb.append("->");
				Obj v = e.getValue().value;
				if (v ==primval) sb.append("_");
				else 
				sb.append("Obj("+v.id + ")");
				sb.append("@");
				sb.append(e.getValue().location);
			}
			sb.append(")");
			return sb.toString();
		}
	}

	public Collection<Obj> allobjects(){
		return objects.values();
	}
	private Map<ObjectReference,Obj> objects=new HashMap<ObjectReference,Obj> ();
	private Obj primval=new Obj(null);
	public void changeField(ObjectReference object, Field field, Value valueToBe,Location loc) {
		Obj source=convert(object);
		
		source.set(field,convert(valueToBe),loc);
		// TODO Auto-generated method stub
		
	}
	
	private Obj convert(Value v) {
		if(v instanceof PrimitiveValue){
			return primval;
		}
		else if(v instanceof ObjectReference){
			Obj obj = objects.get(v);
			if(obj==null){
			 ObjectReference ref = (ObjectReference) v;
			 obj=new Obj(ref.referenceType());
			objects.put(ref, obj);
			}
			return obj;
		}
		return null; // TODO: fix this
		
	}

	public void print() {
		System.out.println("------");
		for (Obj o : allobjects()){
			System.out.println(o);
		}
		// TODO Auto-generated method stub
		
	}
	
	

	public Definition getdef(ObjectReference object, Field field) {
		// TODO Auto-generated method stub
		Obj obj=objects.get(object);
		if(obj==null){
			return null;
		}
		Definition def=obj.getDefinition(field);
		
		return def;

	}
	
	public Set<ClassStateVariable> allReachable(ObjectReference o) {
		Obj obj=objects.get(o);
		Set<ClassStateVariable> has= new HashSet<ClassStateVariable>();
		if(obj!=null){
			has.addAll(obj.getClassStateVariables());
		} 
			//obj=objects.get(obj);	
		return has; // TODO
	}
	
	
}
