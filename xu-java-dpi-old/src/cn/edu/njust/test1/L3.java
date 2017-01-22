package cn.edu.njust.test1;

public class L3 extends Level {
	boolean v3 = false;
	Level sub = LevelFactory.makeLevel(4);

	void doA() {
		if (v3)
			sub.doA();
		v3 = sub.doB();
	}

	boolean doB() {
		if (v3)
			sub.doA();
		v3 = sub.doB();
		return true;
	}
}