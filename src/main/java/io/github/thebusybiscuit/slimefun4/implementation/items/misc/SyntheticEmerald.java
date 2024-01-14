package io.github.thebusybiscuit.slimefun4.implementation.items.misc;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCategory;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.VillagerTradingListener;

/**
 * The {@link SyntheticEmerald} is an almost normal emerald.
 * It can even be used to trade with {@link Villager Villagers}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see VillagerTradingListener
 *
 */
public class SyntheticEmerald extends SlimefunItem {

    @Deprecated
    @ParametersAreNonnullByDefault
    public SyntheticEmerald(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        this(itemGroup, item, recipeType.asRecipeCategory(), recipe);
    }
    
    @ParametersAreNonnullByDefault
    public SyntheticEmerald(ItemGroup itemGroup, SlimefunItemStack item, RecipeCategory recipeCategory, ItemStack[] recipe) {
        super(itemGroup, item, recipeCategory, recipe);

        setUseableInWorkbench(true);
    }

}
