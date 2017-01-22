package cn.edu.njust.test1;

public class L2 extends Level {
	boolean v2 = false;

	void doA() {
		v2 = true;
	}

	boolean doB() {
		if (v2)
			return false;
		return true;
	}
}
