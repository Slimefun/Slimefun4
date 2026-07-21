package io.github.thebusybiscuit.slimefun5.core.guide.categories;

import javax.annotation.Nonnull;

import com.cryptomorin.xseries.XMaterial;

/** Registers Slimefun's canonical guide categories (the set that replaced the old GuideTheme enum). */
public final class DefaultGuideCategories {

    public static final String WEAPONS = "weapons";
    public static final String TOOLS = "tools";
    public static final String ARMOR = "armor";
    public static final String MACHINES = "machines";
    public static final String ENERGY_TECH = "energy_tech";
    public static final String RESOURCES = "resources";
    public static final String MAGIC = "magic";
    public static final String FOOD = "food";
    public static final String LOGISTICS = "logistics";
    public static final String DECORATION = "decoration";
    public static final String MISC = "misc";

    private DefaultGuideCategories() {}

    public static void registerInto(@Nonnull GuideCategoryRegistry registry) {
        registry.register(new GuideCategory(WEAPONS, "&cWeapons", XMaterial.DIAMOND_SWORD, 10));
        registry.register(new GuideCategory(TOOLS, "&aTools", XMaterial.DIAMOND_PICKAXE, 20));
        registry.register(new GuideCategory(ARMOR, "&bArmor", XMaterial.DIAMOND_CHESTPLATE, 30));
        registry.register(new GuideCategory(MACHINES, "&6Machines", XMaterial.FURNACE, 40));
        registry.register(new GuideCategory(ENERGY_TECH, "&eEnergy & Tech", XMaterial.REDSTONE, 50));
        registry.register(new GuideCategory(RESOURCES, "&fResources", XMaterial.IRON_INGOT, 60));
        registry.register(new GuideCategory(MAGIC, "&dMagic", XMaterial.ENCHANTED_BOOK, 70));
        registry.register(new GuideCategory(FOOD, "&2Food & Farming", XMaterial.BREAD, 80));
        registry.register(new GuideCategory(LOGISTICS, "&3Logistics", XMaterial.CHEST, 90));
        registry.register(new GuideCategory(DECORATION, "&5Decoration", XMaterial.PAINTING, 100));
        registry.register(new GuideCategory(MISC, "&7Misc", XMaterial.CHEST_MINECART, 110));
    }
}
