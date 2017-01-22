package cn.edu.njust.test3;

public class TestCase {
	 public static void main(String[] args){
		 NestA nesta=new NestA();
		 ClassUT classut=new ClassUT(nesta);
		 classut.doSomething();
		 int p=-1;
		 classut.m1(p);
		 classut.m2();
		 classut.m3(p);
		 classut.m4(p);
		 classut.m5();
	 }
	}

