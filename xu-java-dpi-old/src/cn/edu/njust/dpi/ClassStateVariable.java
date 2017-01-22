package cn.edu.njust.dpi;

import java.util.ArrayList;
import java.util.List;

import com.sun.jdi.Field;
import com.sun.jdi.ObjectReference;

public class ClassStateVariable {
	private List<Field> path=new ArrayList<Field>();
	public ClassStateVariable(Field field){
		path.add(field);
	}
	public ClassStateVariable(ClassStateVariable stateVariable,Field field){
		path.addAll(stateVariable.path);
		path.add(field);
	}
	
	public ClassStateVariable(Field field,ClassStateVariable stateVariable){
		path.add(field);
		path.addAll(stateVariable.path);
	}
	
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (this == o) {  
            return true;  
        }  
        // �������������ͬһ���ͣ���Ƚ�������ֵ�Ƿ���ͬ���������ͬ����˵����������Ҳ��ͬ������˵��������������ͬ��  
        if (o instanceof ClassStateVariable) {  
        	ClassStateVariable co = (ClassStateVariable) o;  
            return path.equals(co.path);
        }  
        return false;  
		//return super.equals(arg0);
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return path.hashCode();
		//return super.hashCode();
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("ClassStateVariable" + "(" );
		sb.append(path.get(0).name());
		for(int i=1;i<path.size();i++){
			sb.append(".");
			sb.append(path.get(i).name());			
		}
		sb.append(")");
		return sb.toString();
	}
	
	public Definition getDefinition(ObjectReference o, MemoryModel mm) {
		// TODO
		mm.allobjects();
		return null;
	}
	
}
