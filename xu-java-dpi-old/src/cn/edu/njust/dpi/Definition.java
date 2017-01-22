package cn.edu.njust.dpi;

import com.sun.jdi.Location;

public class Definition {
	public final Location location;
	public final MemoryModel.Obj value;
	
	public Definition(Location l, MemoryModel.Obj o) {
		location = l;
		value = o;
	}
}
