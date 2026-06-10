package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import javax.annotation.Nullable;

import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

/**
 * Java-8 universal port: compatibility helpers for the base-potion API that does not exist at the
 * 1.8.8 compile floor.
 * <p>
 * {@code PotionMeta#getBasePotionData}/{@code setBasePotionData} (and the {@link PotionData} class)
 * arrived in 1.9, while {@code getBasePotionType}/{@code setBasePotionType} replaced them in 1.20.2.
 * {@code PotionType#isExtendable}/{@code isUpgradeable} are likewise absent on legacy. All are reached
 * reflectively so the code compiles against 1.8.8 while keeping full behaviour on modern servers.
 */
public final class PotionCompat {

    private PotionCompat() {}

    @Nullable
    public static PotionType getBasePotionType(PotionMeta meta) {
        return (PotionType) ReflectionCompat.invoke(meta, "getBasePotionType");
    }

    public static void setBasePotionType(PotionMeta meta, PotionType type) {
        ReflectionCompat.invoke(meta, "setBasePotionType", type);
    }

    @Nullable
    public static PotionData getBasePotionData(PotionMeta meta) {
        return (PotionData) ReflectionCompat.invoke(meta, "getBasePotionData");
    }

    public static void setBasePotionData(PotionMeta meta, PotionData data) {
        ReflectionCompat.invoke(meta, "setBasePotionData", data);
    }

    public static boolean hasBasePotionType(PotionMeta meta) {
        return Boolean.TRUE.equals(ReflectionCompat.invoke(meta, "hasBasePotionType"));
    }

    public static boolean isExtendable(PotionType type) {
        return Boolean.TRUE.equals(ReflectionCompat.invoke(type, "isExtendable"));
    }

    public static boolean isUpgradeable(PotionType type) {
        return Boolean.TRUE.equals(ReflectionCompat.invoke(type, "isUpgradeable"));
    }
}
