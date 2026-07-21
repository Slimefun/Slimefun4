package io.github.thebusybiscuit.slimefun5.core.guide.categories;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun5.implementation.items.armor.SlimefunArmorPiece;

/**
 * Heuristic type for an ADDON item, used to split an addon's items across the shared guide categories.
 * Emits only ids it can confidently detect from the item's Slimefun type or material family; anything
 * else returns null so the caller files it under the addon's Misc section. Version-safe (matches on
 * {@link Material#name()}), and never guesses an axe as a weapon (an axe is a weapon only "if designed to
 * be one", which a material heuristic can't tell - so axes are Tools).
 */
public final class ItemTypeClassifier {

    private ItemTypeClassifier() {}

    @Nullable
    public static String classify(@Nonnull SlimefunItem item) {
        if (item instanceof SlimefunArmorPiece) {
            return DefaultGuideCategories.ARMOR;
        }

        if (item instanceof EnergyNetComponent) {
            return DefaultGuideCategories.MACHINES;
        }

        Material material = null;

        try {
            if (item.getItem() != null) {
                material = item.getItem().getType();
            }
        } catch (Exception | LinkageError ignored) {
            // a broken item must not break the whole menu
        }

        return material == null ? null : classifyMaterial(material);
    }

    @Nullable
    static String classifyMaterial(@Nonnull Material material) {
        String name = material.name();

        if (name.endsWith("_SWORD") || name.equals("BOW") || name.endsWith("CROSSBOW") || name.equals("TRIDENT") || name.equals("MACE")) {
            return DefaultGuideCategories.WEAPONS;
        }

        // Axes are TOOLS: a heuristic can't tell a "designed weapon" axe from a utility axe.
        if (name.endsWith("_PICKAXE") || name.endsWith("_AXE") || name.endsWith("_SHOVEL") || name.endsWith("_HOE")
            || name.equals("SHEARS") || name.equals("FISHING_ROD") || name.equals("FLINT_AND_STEEL") || name.equals("SPYGLASS") || name.equals("BRUSH")) {
            return DefaultGuideCategories.TOOLS;
        }

        if (name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE") || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS") || name.equals("ELYTRA")) {
            return DefaultGuideCategories.ARMOR;
        }

        try {
            if (material.isEdible()) {
                return DefaultGuideCategories.FOOD;
            }
        } catch (Exception | LinkageError ignored) {
            // isEdible() can touch the registry on some versions; ignore and fall through
        }

        return null;
    }

    @Nonnull
    public static String typeSingular(@Nonnull String categoryId) {
        switch (categoryId) {
            case DefaultGuideCategories.WEAPONS:
                return "Weapon";
            case DefaultGuideCategories.TOOLS:
                return "Tool";
            case DefaultGuideCategories.ARMOR:
                return "Armor";
            case DefaultGuideCategories.MACHINES:
                return "Machine";
            case DefaultGuideCategories.FOOD:
                return "Food";
            default:
                return "Misc";
        }
    }
}
