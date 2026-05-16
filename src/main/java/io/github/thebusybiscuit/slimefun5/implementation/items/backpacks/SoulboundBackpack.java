package io.github.thebusybiscuit.slimefun5.implementation.items.backpacks;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.attributes.Soulbound;

/**
 * This implementation of {@link SlimefunBackpack} is also {@link Soulbound}.
 * 
 * @author TheBusyBiscuit
 *
 */
public class SoulboundBackpack extends SlimefunBackpack implements Soulbound {

    @ParametersAreNonnullByDefault
    public SoulboundBackpack(int size, ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(size, itemGroup, item, recipeType, recipe);
    }

}

