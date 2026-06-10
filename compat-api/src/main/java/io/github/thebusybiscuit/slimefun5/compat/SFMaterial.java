package io.github.thebusybiscuit.slimefun5.compat;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * An abstraction to bridge legacy 1.8 data values (e.g., WOOL:14) 
 * to modern 1.13+ flattened materials (e.g., RED_WOOL).
 */
public enum SFMaterial {

    // Wools
    WHITE_WOOL("WOOL", 0, "WHITE_WOOL"),
    ORANGE_WOOL("WOOL", 1, "ORANGE_WOOL"),
    MAGENTA_WOOL("WOOL", 2, "MAGENTA_WOOL"),
    LIGHT_BLUE_WOOL("WOOL", 3, "LIGHT_BLUE_WOOL"),
    YELLOW_WOOL("WOOL", 4, "YELLOW_WOOL"),
    LIME_WOOL("WOOL", 5, "LIME_WOOL"),
    PINK_WOOL("WOOL", 6, "PINK_WOOL"),
    GRAY_WOOL("WOOL", 7, "GRAY_WOOL"),
    LIGHT_GRAY_WOOL("WOOL", 8, "LIGHT_GRAY_WOOL"),
    CYAN_WOOL("WOOL", 9, "CYAN_WOOL"),
    PURPLE_WOOL("WOOL", 10, "PURPLE_WOOL"),
    BLUE_WOOL("WOOL", 11, "BLUE_WOOL"),
    BROWN_WOOL("WOOL", 12, "BROWN_WOOL"),
    GREEN_WOOL("WOOL", 13, "GREEN_WOOL"),
    RED_WOOL("WOOL", 14, "RED_WOOL"),
    BLACK_WOOL("WOOL", 15, "BLACK_WOOL"),

    // Wood
    OAK_LOG("LOG", 0, "OAK_LOG"),
    SPRUCE_LOG("LOG", 1, "SPRUCE_LOG"),
    BIRCH_LOG("LOG", 2, "BIRCH_LOG"),
    JUNGLE_LOG("LOG", 3, "JUNGLE_LOG"),
    ACACIA_LOG("LOG_2", 0, "ACACIA_LOG"),
    DARK_OAK_LOG("LOG_2", 1, "DARK_OAK_LOG"),

    // Ores
    IRON_ORE("IRON_ORE", 0, "IRON_ORE"),
    GOLD_ORE("GOLD_ORE", 0, "GOLD_ORE"),
    DIAMOND_ORE("DIAMOND_ORE", 0, "DIAMOND_ORE"),
    EMERALD_ORE("EMERALD_ORE", 0, "EMERALD_ORE"),
    COAL_ORE("COAL_ORE", 0, "COAL_ORE"),

    // Dyes
    INK_SAC("INK_SACK", 0, "INK_SAC"),
    ROSE_RED("INK_SACK", 1, "ROSE_RED"),
    CACTUS_GREEN("INK_SACK", 2, "CACTUS_GREEN"),
    COCOA_BEANS("INK_SACK", 3, "COCOA_BEANS"),
    LAPIS_LAZULI("INK_SACK", 4, "LAPIS_LAZULI"),
    PURPLE_DYE("INK_SACK", 5, "PURPLE_DYE"),
    CYAN_DYE("INK_SACK", 6, "CYAN_DYE"),
    LIGHT_GRAY_DYE("INK_SACK", 7, "LIGHT_GRAY_DYE"),
    GRAY_DYE("INK_SACK", 8, "GRAY_DYE"),
    PINK_DYE("INK_SACK", 9, "PINK_DYE"),
    LIME_DYE("INK_SACK", 10, "LIME_DYE"),
    DANDELION_YELLOW("INK_SACK", 11, "DANDELION_YELLOW"),
    LIGHT_BLUE_DYE("INK_SACK", 12, "LIGHT_BLUE_DYE"),
    MAGENTA_DYE("INK_SACK", 13, "MAGENTA_DYE"),
    ORANGE_DYE("INK_SACK", 14, "ORANGE_DYE"),
    BONE_MEAL("INK_SACK", 15, "BONE_MEAL");

    private final String legacyName;
    private final byte legacyData;
    private final String modernName;

    private Material cachedMaterial = null;
    private boolean initialized = false;
    private boolean isLegacy = false;

    SFMaterial(String legacyName, int legacyData, String modernName) {
        this.legacyName = legacyName;
        this.legacyData = (byte) legacyData;
        this.modernName = modernName;
    }

    public String getLegacyName() {
        return legacyName;
    }

    public byte getLegacyData() {
        return legacyData;
    }

    public String getModernName() {
        return modernName;
    }

    /**
     * Resolves the correct Bukkit Material for the current server version.
     * 
     * @return The modern Material if available, otherwise the legacy Material.
     */
    public Material toMaterial() {
        if (initialized) {
            return cachedMaterial;
        }

        try {
            cachedMaterial = Material.valueOf(modernName);
            isLegacy = false;
        } catch (IllegalArgumentException e) {
            try {
                cachedMaterial = Material.valueOf(legacyName);
                isLegacy = true;
            } catch (IllegalArgumentException e2) {
                cachedMaterial = null;
            }
        }
        
        initialized = true;
        return cachedMaterial;
    }

    /**
     * Creates an ItemStack of the given amount safely applying legacy data values if necessary.
     * 
     * @param amount The size of the stack
     * @return A valid ItemStack for the current server version
     */
    @SuppressWarnings("deprecation")
    public ItemStack toItemStack(int amount) {
        Material mat = toMaterial();
        if (mat == null) {
            return null;
        }

        ItemStack item = new ItemStack(mat, amount);
        
        // If we fell back to the legacy material, we must apply the byte data / durability
        // to get the correct variant (e.g. Red Wool instead of White Wool).
        if (isLegacy && !legacyName.equals(modernName)) {
            item.setDurability(legacyData);
        }

        return item;
    }

    /**
     * Creates a single ItemStack of this material.
     * 
     * @return A valid ItemStack for the current server version
     */
    public ItemStack toItemStack() {
        return toItemStack(1);
    }
}