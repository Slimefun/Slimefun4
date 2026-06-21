package io.github.thebusybiscuit.slimefun5.core.guide.themes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.cryptomorin.xseries.XMaterial;

/**
 * The fixed catalogue of top-level guide themes. Every {@link io.github.thebusybiscuit.slimefun5.api.items.ItemGroup}
 * is bucketed into exactly one of these (its declared theme, else {@link #MISC}). Icons are all materials
 * that exist on Minecraft 1.8 so the guide renders on the legacy floor.
 */
public enum GuideTheme {

    WEAPONS("weapons", "&cWeapons", XMaterial.DIAMOND_SWORD, 10),
    TOOLS("tools", "&aTools", XMaterial.DIAMOND_PICKAXE, 20),
    ARMOR("armor", "&bArmor", XMaterial.DIAMOND_CHESTPLATE, 30),
    MACHINES("machines", "&6Machines", XMaterial.FURNACE, 40),
    ENERGY_TECH("energy_tech", "&eEnergy & Tech", XMaterial.REDSTONE, 50),
    RESOURCES("resources", "&fResources", XMaterial.IRON_INGOT, 60),
    MAGIC("magic", "&dMagic", XMaterial.ENCHANTED_BOOK, 70),
    FOOD("food", "&2Food & Farming", XMaterial.BREAD, 80),
    LOGISTICS("logistics", "&3Logistics", XMaterial.CHEST, 90),
    DECORATION("decoration", "&5Decoration", XMaterial.PAINTING, 100),
    MISC("misc", "&7Misc", XMaterial.CHEST_MINECART, 110);

    private final String id;
    private final String defaultName;
    private final XMaterial icon;
    private final int order;

    GuideTheme(String id, String defaultName, XMaterial icon, int order) {
        this.id = id;
        this.defaultName = defaultName;
        this.icon = icon;
        this.order = order;
    }

    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    public String getDefaultName() {
        return defaultName;
    }

    @Nonnull
    public XMaterial getIcon() {
        return icon;
    }

    public int getOrder() {
        return order;
    }

    @Nullable
    public static GuideTheme byId(@Nullable String id) {
        if (id == null) {
            return null;
        }

        for (GuideTheme theme : values()) {
            if (theme.id.equals(id)) {
                return theme;
            }
        }

        return null;
    }
}
