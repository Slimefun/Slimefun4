package io.github.thebusybiscuit.slimefun5.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun5.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun5.core.attributes.Radioactivity;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;

/**
 * This utility class builds hardcoded English lore strings in code.
 *
 * @deprecated Building lore in code is exactly what the unified lore system replaces. Stat/usage lines
 *             (energy, speed, material, hunger, range, radioactivity, the RIGHT/CROUCH hints) belong in
 *             {@code languages/en/items.yml} as {@code stats}/{@code usage} block entries, where they are
 *             localizable and follow the fixed Type/Description/Stats/Usage structure. Do not add new
 *             {@code LoreBuilder} calls; migrate existing ones into the item's {@code en/items.yml} entry.
 *
 * @author TheBusyBiscuit
 *
 * @see SlimefunItems
 *
 */
@Deprecated
public final class LoreBuilder {

    public static final String HAZMAT_SUIT_REQUIRED = "&8\u21E8 &4Hazmat Suit required!";
    public static final String RAINBOW = "&dCycles through all Colors of the Rainbow!";
    public static final String RIGHT_CLICK_TO_USE = "&eRight Click&7 to use";
    public static final String RIGHT_CLICK_TO_OPEN = "&eRight Click&7 to open";
    public static final String CROUCH_TO_USE = "&eCrouch&7 to use";

    private static final DecimalFormat hungerFormat = new DecimalFormat("#.0", DecimalFormatSymbols.getInstance(Locale.ROOT));

    private LoreBuilder() {}

    public static @Nonnull String radioactive(@Nonnull Radioactivity radioactivity) {
        return radioactivity.getLore();
    }

    public static @Nonnull String machine(@Nonnull MachineTier tier, @Nonnull MachineType type) {
        return tier + " " + type;
    }

    public static @Nonnull String speed(float speed) {
        return "&8\u21E8 &b\u26A1 &7Speed: &b" + speed + 'x';
    }

    public static @Nonnull String powerBuffer(int power) {
        return power(power, " Buffer");
    }

    public static @Nonnull String powerPerSecond(int power) {
        return power(power, "/s");
    }

    public static @Nonnull String power(int power, @Nonnull String suffix) {
        return "&8\u21E8 &e\u26A1 &7" + power + " J" + suffix;
    }

    public static @Nonnull String powerCharged(int charge, int capacity) {
        return "&8\u21E8 &e\u26A1 &7" + charge + " / " + capacity + " J";
    }

    public static @Nonnull String material(@Nonnull String material) {
        return "&8\u21E8 &7Material: &b" + material;
    }

    public static @Nonnull String hunger(double value) {
        return "&7&oRestores &b&o" + hungerFormat.format(value) + " &7&oHunger";
    }

    public static @Nonnull String range(int blocks) {
        return "&7Range: &c" + blocks + " blocks";
    }

    public static @Nonnull String usesLeft(int usesLeft) {
        return "&e" + usesLeft + ' ' + (usesLeft > 1 ? "Uses" : "Use") + " &7left";
    }

}

