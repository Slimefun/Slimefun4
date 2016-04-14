package me.mrCookieSlime.Slimefun.URID;

import java.util.HashMap;
import java.util.Map;

public class URID {
	
	public static Map<URID, Object> objects = new HashMap<URID, Object>();
	public static Map<Integer, URID> ids = new HashMap<Integer, URID>();
	
	private static int next = 0;
	private int id;
	private boolean dirty;
	
	public URID(Object object, boolean dirty) {
		this.id = next;
		next++;
		objects.put(this, object);
		ids.put(toInteger(), this);
	}
	
	public int toInteger() {
		return id;
	}
	
	public static URID nextURID(Object object, boolean dirty) {
		URID urid = new URID(object, dirty);
		return urid;
	}
	
	public static URID fromInteger(int id) {
		return ids.get(id);
	}

	public static Object decode(URID urid) {
		return objects.get(urid);
	}
	
	public void markDirty() {
		if (dirty) {
			ids.remove(toInteger());
			objects.remove(this);
		}
	}

}
