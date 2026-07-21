package io.github.thebusybiscuit.slimefun5.core.guide.themes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun5.implementation.items.armor.SlimefunArmorPiece;

/**
 * Classifies a single {@link SlimefunItem} into a {@link GuideTheme} for the categorized guide, so an
 * addon's items are split across the shared themes (its weapons under Weapons, its tools under Tools, ...)
 * rather than all landing in one bucket.
 *
 * <p>Only untagged (addon) items are classified this way; Slimefun's own groups declare their theme
 * explicitly. Signals are cheap and version-safe: the item's Slimefun type first (armor, energy machine),
 * then its material family (matched on {@link Material#name()} so it works on every supported version).
 * Anything that matches nothing specific falls back to {@link GuideTheme#MISC}.
 */
public final class ItemThemeClassifier {

    private ItemThemeClassifier() {}

    @Nonnull
    public static GuideTheme classify(@Nonnull SlimefunItem item) {
        if (item instanceof SlimefunArmorPiece) {
            return GuideTheme.ARMOR;
        }

        // Almost every powered Slimefun machine, generator or capacitor is an EnergyNetComponent.
        if (item instanceof EnergyNetComponent) {
            return GuideTheme.ENERGY_TECH;
        }

        Material material = null;

        try {
            if (item.getItem() != null) {
                material = item.getItem().getType();
            }
        } catch (Exception | LinkageError ignored) {
            // A broken item must not break the whole categorized menu.
        }

        GuideTheme byMaterial = material == null ? null : classifyMaterial(material);
        return byMaterial != null ? byMaterial : GuideTheme.MISC;
    }

    /**
     * The theme implied purely by an item's {@link Material} family, or {@code null} when the material
     * maps to no specific theme (blocks, ingots, dusts, machine heads, ...). Separated out so the
     * heuristic can be unit-tested without booting the whole item catalogue.
     */
    @Nullable
    static GuideTheme classifyMaterial(@Nonnull Material material) {
        String name = material.name();

        if (name.endsWith("_SWORD") || name.equals("BOW") || name.endsWith("CROSSBOW") || name.equals("TRIDENT") || name.equals("MACE")) {
            return GuideTheme.WEAPONS;
        }

        if (name.endsWith("_PICKAXE") || name.endsWith("_AXE") || name.endsWith("_SHOVEL") || name.endsWith("_HOE")
            || name.equals("SHEARS") || name.equals("FISHING_ROD") || name.equals("FLINT_AND_STEEL") || name.equals("SPYGLASS") || name.equals("BRUSH")) {
            return GuideTheme.TOOLS;
        }

        if (name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE") || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS")
            || name.equals("ELYTRA")) {
            return GuideTheme.ARMOR;
        }

        try {
            if (material.isEdible()) {
                return GuideTheme.FOOD;
            }
        } catch (Exception | LinkageError ignored) {
            // isEdible() can touch the item registry on some versions; ignore and fall through.
        }

        return null;
    }
}
