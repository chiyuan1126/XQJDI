package cn.edu.njust.test;

public class TestCase1 {

  public static void main(String[] args) {
    A a1 = new A();
    A a2 = new A();
    B b = new B();
    a1.setB(b);
    a2.setB(b);
    String str = "Hello world!";
    System.out.println(str);
    Z z = new Z(a1);
    z.doSmt();
  }

}
