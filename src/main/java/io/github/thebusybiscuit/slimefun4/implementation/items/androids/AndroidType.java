package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

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
		return type == NONE || type == this || (type == NON_FIGHTER && this != FIGHTER);
	}

}
