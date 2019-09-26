package me.mrCookieSlime.Slimefun.utils;

public enum MachineTier {
	
	BASIC("&eBasic"),
	AVERAGE("&6Average"),
	MEDIUM("&aMedium"),
	GOOD("&2Good"),
	ADVANCED("&6Advanced"),
	END_GAME("&4End-Game");
	
	private String prefix;
	
	private MachineTier(String prefix) {
		this.prefix = prefix;
	}
	
	@Override
	public String toString() {
		return prefix;
	}
	
	public String and(MachineType type) {
		return this + " " + type;
	}

}
