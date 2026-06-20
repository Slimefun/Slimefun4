package org.bukkit.potion;

/**
 * Compile-only stub for {@code org.bukkit.potion.PotionData} (Minecraft 1.9+, deprecated 1.20.2+). Not
 * shaded; on modern servers the real concrete class is used at runtime. Stubbed as a class (not an
 * interface) so that virtual method calls resolve to {@code invokevirtual} and {@code new PotionData(..)}
 * resolves to the real constructor, matching the real type.
 */
public class PotionData {

    public PotionData(PotionType type) {
        // stub
    }

    public PotionData(PotionType type, boolean extended, boolean upgraded) {
        // stub
    }

    public PotionType getType() {
        return null;
    }

    public boolean isUpgraded() {
        return false;
    }

    public boolean isExtended() {
        return false;
    }
}
