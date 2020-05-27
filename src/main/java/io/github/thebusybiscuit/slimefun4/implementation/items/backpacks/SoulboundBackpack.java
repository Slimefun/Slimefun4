package io.github.thebusybiscuit.slimefun4.implementation.items.backpacks;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.Soulbound;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * This implementation of {@link SlimefunBackpack} is also {@link Soulbound}.
 * 
 * @author TheBusyBiscuit
 *
 */
public class SoulboundBackpack extends SlimefunBackpack implements Soulbound {

    public SoulboundBackpack(int size, Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(size, category, item, recipeType, recipe);
    }

}
