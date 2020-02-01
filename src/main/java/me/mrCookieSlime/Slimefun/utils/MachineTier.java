package me.mrCookieSlime.Slimefun.utils;

@Deprecated
public enum MachineTier {

    BASIC("&e基础"),
    AVERAGE("&6普通"),
    MEDIUM("&a中型"),
    GOOD("&2优秀"),
    ADVANCED("&6高级"),
    END_GAME("&4终极");
	
	private final String prefix;
	
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
