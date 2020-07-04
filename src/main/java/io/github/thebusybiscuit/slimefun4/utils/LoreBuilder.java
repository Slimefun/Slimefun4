package io.github.thebusybiscuit.slimefun4.utils;

import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactivity;

public final class LoreBuilder {

    public static final String HAZMAT_SUIT_REQUIRED = "&8\u21E8 &4需要防化服!";
    public static final String RIGHT_CLICK_TO_USE = "&e右键&7 使用";
    public static final String RIGHT_CLICK_TO_OPEN = "&e右键&7 打开";
    public static final String CROUCH_TO_USE = "&e按住 &eShift&7 使用";

    private LoreBuilder() {
    }

    public static String radioactive(Radioactivity radioactivity) {
        return radioactivity.getLore();
    }

    public static String machine(MachineTier tier, MachineType type) {
        return tier + " " + type;
    }

    public static String speed(float speed) {
        return "&8\u21E8 &b\u26A1 &7速度: &b" + speed + 'x';
    }

    public static String powerBuffer(int power) {
        return power(power, " 可储存");
    }

    public static String powerPerSecond(int power) {
        return power(power, "/s");
    }

    public static String power(int power, String suffix) {
        return "&8\u21E8 &e\u26A1 &7" + power + " J" + suffix;
    }

    public static String powerCharged(int charge, int capacity) {
        return "&8\u21E8 &e\u26A1 &7" + charge + " / " + capacity + " J";
    }

    public static String material(String material) {
        return "&8\u21E8 &7材料: &b" + material;
    }

}
