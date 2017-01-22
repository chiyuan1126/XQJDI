package cn.edu.njust.test1;

public class LevelFactory {
	static Level makeLevel(int selector) {
		switch (selector) {
		case 0:
			return new L0();
		case 1:
			return new L1();
		case 2:
			return new L2();
		case 3:
			return new L3();
		default:
			return null;
		}
	}
}