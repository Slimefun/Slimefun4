package me.mrCookieSlime.Slimefun.Android;

public enum AndroidType {
	
	NONE,
	MINER,
	FARMER,
	ADVANCED_FARMER,
	WOODCUTTER,
	FIGHTER,
	FISHERMAN,
	NON_FIGHTER;

	public boolean isType(AndroidType type) {
		return type.equals(NONE) || type.equals(this) || (type.equals(NON_FIGHTER) && !this.equals(FIGHTER));
	}

}
