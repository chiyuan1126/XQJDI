package cn.edu.njust.test3;

public class Nest {
	protected int i;

	Nest() {
		i = 0;
	}

	void n() {
		i = 1;
	}

	Nest f() {
		return this;
	}

}