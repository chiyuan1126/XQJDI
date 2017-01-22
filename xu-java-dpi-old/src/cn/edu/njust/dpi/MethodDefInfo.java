package cn.edu.njust.dpi;

import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class MethodDefInfo {
	Set<Definition> usedDuring; 
    Set<Definition> defdDuring; 
    Stack<MethodDefInfo> methodStack;
    MethodDefInfo currentMethodInfo() {
    	return methodStack.peek(); 
    }
    Map<Integer,MethodDefInfo> collectedMethodInfo;
}
