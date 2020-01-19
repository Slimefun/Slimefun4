package io.github.thebusybiscuit.slimefun4.core.attributes;

public enum MachineTier {
	
	BASIC("&eBasic"),
	AVERAGE("&6Average"),
	MEDIUM("&aMedium"),
	GOOD("&2Good"),
	ADVANCED("&6Advanced"),
	END_GAME("&4End-Game");
	
	private final String prefix;
	
	private MachineTier(String prefix) {
		this.prefix = prefix;
	}
	
	@Override
	public String toString() {
		return prefix;
	}

}
