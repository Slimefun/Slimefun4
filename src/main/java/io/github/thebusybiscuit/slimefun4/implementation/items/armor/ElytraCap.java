package io.github.thebusybiscuit.slimefun4.implementation.items.armor;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectionType;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectiveArmor;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.ElytraCrashListener;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link ElytraCap} negates damage taken when crashing into a wall using an elytra.
 *
 * @author Seggan
 *
 * @see ElytraCrashListener
 */
public class ElytraCap extends SlimefunArmorPiece implements DamageableItem, ProtectiveArmor {

    private final NamespacedKey key;

    @ParametersAreNonnullByDefault
    public ElytraCap(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe, null);

        key = new NamespacedKey(SlimefunPlugin.instance(), "elytra_armor");
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public void damageItem(Player p, ItemStack item) {
        if (p.getGameMode() != GameMode.CREATIVE) {
            DamageableItem.super.damageItem(p, item);
        }
    }

    @Nonnull
    @Override
    public ProtectionType[] getProtectionTypes() {
        return new ProtectionType[] { ProtectionType.FLYING_INTO_WALL };
    }

    @Override
    public boolean isFullSetRequired() {
        return false;
    }

    @Nonnull
    @Override
    public NamespacedKey getArmorSetId() {
        return key;
    }
}
