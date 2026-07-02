package io.github.thebusybiscuit.slimefun5.core.balance;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.core.attributes.DamageableItem;
import io.github.thebusybiscuit.slimefun5.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun5.core.attributes.Radioactive;
import io.github.thebusybiscuit.slimefun5.implementation.items.armor.SlimefunArmorPiece;

/**
 * Derives a starting balance score (0-100) for an item from signals it already exposes - no manual
 * authoring required. This is what makes balance data OPTIONAL for addon authors: an addon that
 * declares nothing still gets a defensible score. Authored overrides ({@link BalanceOverrides}) refine
 * outliers. Pure: no I/O, no world state - depends only on the item's own metadata.
 */
public final class ItemBalanceHeuristic {

    /** A trivial item has no custom mechanic and no combat/automation role: a crafting resource. */
    public boolean isTrivial(SlimefunItem item) {
        if (item instanceof SlimefunArmorPiece || item instanceof DamageableItem || item instanceof EnergyNetComponent) {
            return false;
        }

        // No item handlers => no custom behaviour => a plain resource/ingredient (dust, ingot, gem).
        return item.getHandlers().isEmpty();
    }

    /** A 0-100 estimate. STANDARD baseline, adjusted by robust signals, clamped. */
    public int estimate(SlimefunItem item) {
        int score = 18; // low-STANDARD baseline

        score += recipeCost(item); // 0..12

        if (item instanceof EnergyNetComponent) {
            score += 12; // machines/generators automate manual vanilla work
        }

        if (item instanceof SlimefunArmorPiece) {
            score += 14; // effect armour outclasses vanilla armour
        }

        if (item instanceof DamageableItem) {
            score += 14; // custom weapons
        }

        if (item instanceof Radioactive) {
            score += 8; // hazardous / late-game materials
        }

        if (!item.getHandlers().isEmpty()) {
            score += Math.min(10, item.getHandlers().size() * 3); // more behaviours => more capability
        }

        return Math.max(0, Math.min(100, score));
    }

    /** 0..12 from the number of non-air ingredients in the (up to 9-slot) recipe. */
    private int recipeCost(SlimefunItem item) {
        ItemStack[] recipe = item.getRecipe();

        if (recipe == null) {
            return 0;
        }

        int ingredients = 0;

        for (ItemStack stack : recipe) {
            if (stack != null && stack.getType() != Material.AIR) {
                ingredients++;
            }
        }

        return Math.min(12, ingredients + (ingredients >= 8 ? 4 : 0));
    }
}
