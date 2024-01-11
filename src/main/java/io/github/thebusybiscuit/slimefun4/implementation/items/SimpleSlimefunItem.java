package io.github.thebusybiscuit.slimefun4.implementation.items;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemHandler;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCategory;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeStructure;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;

/**
 * This is basically a quickstart class for your very first {@link SlimefunItem}.
 * This class easily allows you to add one {@link ItemHandler} to your {@link SlimefunItem}.
 * 
 * You could use an {@link ItemUseHandler} for example to give your {@link SlimefunItem}
 * very basic right-click functionalities.
 * 
 * @author TheBusyBiscuit
 * 
 * @see ItemHandler
 * @see ItemUseHandler
 * @see SlimefunItem
 *
 * @param <T>
 *            The Type of {@link ItemHandler} to add to this {@link SlimefunItem}
 */
public abstract class SimpleSlimefunItem<T extends ItemHandler> extends SlimefunItem {

    @Deprecated
    @ParametersAreNonnullByDefault
    protected SimpleSlimefunItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Deprecated
    @ParametersAreNonnullByDefault
    protected SimpleSlimefunItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, @Nullable ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
    }

    @ParametersAreNonnullByDefault
    protected SimpleSlimefunItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeCategory recipeCategory, ItemStack[] recipe) {
        super(itemGroup, item, recipeCategory, recipe);
    }

    @ParametersAreNonnullByDefault
    protected SimpleSlimefunItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeCategory recipeCategory, ItemStack[] recipe, @Nullable ItemStack recipeOutput) {
        super(itemGroup, item, recipeCategory, recipe, recipeOutput);
    }

    @ParametersAreNonnullByDefault
    protected SimpleSlimefunItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeCategory recipeCategory, ItemStack[] recipe, RecipeStructure structure) {
        super(itemGroup, item, recipeCategory, recipe, structure);
    }

    @ParametersAreNonnullByDefault
    protected SimpleSlimefunItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeCategory recipeCategory, ItemStack[] recipe, @Nullable ItemStack recipeOutput, RecipeStructure structure) {
        super(itemGroup, item, recipeCategory, recipe, recipeOutput);
    }

    @Override
    public void preRegister() {
        addItemHandler(getItemHandler());
    }

    /**
     * This returns the {@link ItemHandler} that will be added to this {@link SlimefunItem}.
     * 
     * @return The {@link ItemHandler} that should be added to this {@link SlimefunItem}
     */
    public abstract @Nonnull T getItemHandler();

}
