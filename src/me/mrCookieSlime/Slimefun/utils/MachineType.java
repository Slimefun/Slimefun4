package me.mrCookieSlime.Slimefun.utils;

public enum MachineType {
	
	CAPACITOR("Capacitor"),
	GENERATOR("Generator"),
	MACHINE("Machine");
	
	private String suffix;
	
	private MachineType(String suffix) {
		this.suffix = suffix;
	}
	
	@Override
	public String toString() {
		return suffix;
	}

}
