package cn.edu.njust.test3;

public class ClassUT {
	private Nest nest;

	ClassUT(Nest n) {
		nest = n;
	}

	void m1(int p) {
		if (p < 0)
			nest.n();
	}

	void m2() {
		if (!(nest instanceof NestA))
			return;
		nest.n();
	}

	void m3(int p) {
		if (!(nest instanceof NestB))
			return;
		m1(p);
	}

	void m4(int p) {
		p += 3;
		if (p > 1)
			m1(p);
	}

	void m5() {
		Nest ref = nest.f();
		ref.n();
	}

	void doSomething() {
		int v = nest.i;
	}
}
