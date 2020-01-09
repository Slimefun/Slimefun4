package io.github.thebusybiscuit.slimefun4.utils;

import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactivity;

public final class LoreBuilder {
	
	public static final String HAZMAT_SUIT_REQUIRED = "&8\u21E8 &4Hazmat Suit required!";

	private LoreBuilder() {}
	
	public static String radioactive(Radioactivity radioactivity) {
		return radioactivity.getLore();
	}
	
	public static String machine(MachineTier tier, MachineType type) {
		return tier + " " + type;
	}
	
	public static String speed(float speed) {
		return "&8\u21E8 &b\u26A1 &7Speed: &b" + speed + "x";
	}
	
	public static String powerBuffer(int power) {
		return power(power, " Buffer");
	}
	
	public static String powerPerSecond(int power) {
		return power(power, "/s");
	}
	
	public static String power(int power, String suffix) {
		return "&8\u21E8 &e\u26A1 &7" + power + " J" + suffix;
	}
	
}
