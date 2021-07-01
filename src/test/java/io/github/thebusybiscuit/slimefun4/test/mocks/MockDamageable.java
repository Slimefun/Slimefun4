package io.github.thebusybiscuit.slimefun4.test.mocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class MockDamageable extends SlimefunItem implements DamageableItem {

    private final boolean itemDamageable;

    @ParametersAreNonnullByDefault
    public MockDamageable(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, boolean damageable) {
        super(category, item, recipeType, recipe);
        itemDamageable = damageable;
    }

    @Override
    public boolean isDamageable() {
        return itemDamageable;
    }

    @Override
    public void damageItem(@Nonnull Player p, @Nullable ItemStack item) {
        DamageableItem.super.damageItem(p, item);
    }
}
