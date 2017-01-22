package cn.edu.njust.test1;

public class L0 extends Level {
	boolean v0 = false;
	Level sub = LevelFactory.makeLevel(1);

	void doA() {
		sub.doA();
		v0 = true;
	}

	boolean doB() {
		boolean ok = true;
		if (v0)
			ok = sub.doB();
		assert ok;
		return ok;
	}
}
