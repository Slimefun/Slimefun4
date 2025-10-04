package io.github.thebusybiscuit.slimefun4.utils.compatibility;

import org.bukkit.Material;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Utility class providing {@link Material} constants that may not exist on
 * older Minecraft versions. Each field is initialised using
 * {@link Material#matchMaterial(String)} to avoid {@link NoSuchFieldError}
 * when running on legacy servers.
 *
 * <p>This class centralises lookups for new 1.21 materials so that other
 * classes can reference them safely.</p>
 */
public final class VersionedMaterial {

    private VersionedMaterial() {
    }

    // 1.21 items
    public static final Material ARMADILLO_SCUTE = get("ARMADILLO_SCUTE");
    public static final Material BREEZE_ROD = get("BREEZE_ROD");
    public static final Material WIND_CHARGE = get("WIND_CHARGE");
    public static final Material CREAKING_HEART = get("CREAKING_HEART");
    public static final Material OMINOUS_BOTTLE = get("OMINOUS_BOTTLE");
    public static final Material TRIAL_KEY = get("TRIAL_KEY");
    public static final Material VAULT = get("VAULT");
    public static final Material TRIAL_SPAWNER = get("TRIAL_SPAWNER");
    public static final Material HEAVY_CORE = get("HEAVY_CORE");
    public static final Material MACE = get("MACE");
    public static final Material WOLF_ARMOR = get("WOLF_ARMOR");
    public static final Material CRAFTER = get("CRAFTER");
    public static final Material COPPER_BULB = get("COPPER_BULB");
    public static final Material BREEZE_SPAWN_EGG = get("BREEZE_SPAWN_EGG");

    @Nullable
    private static Material get(@Nonnull String name) {
        return Material.matchMaterial(name);
    }
}
