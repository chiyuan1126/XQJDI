package cn.edu.njust.test1;

public class L1 extends Level {
	boolean v1 = false;
	Level sub = LevelFactory.makeLevel(2);

	void doA() {
		if (v1)
			sub.doA();
	}

	boolean doB() {
		v1 = true;
		boolean ret = sub.doB();
		return ret;
	}
}
