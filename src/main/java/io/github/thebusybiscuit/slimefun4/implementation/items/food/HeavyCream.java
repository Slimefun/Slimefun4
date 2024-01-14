package io.github.thebusybiscuit.slimefun4.implementation.items.food;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCategory;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;

/**
 * This {@link SlimefunItem} can be obtained by crafting, it's
 * used for various foods and recipes.
 *
 * @author TheSilentPro
 * 
 */
public class HeavyCream extends SimpleSlimefunItem<ItemUseHandler> {

    @Deprecated
    @ParametersAreNonnullByDefault
    public HeavyCream(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        this(itemGroup, item, recipeType.asRecipeCategory(), recipe, recipeOutput);
    }
    
    @ParametersAreNonnullByDefault
    public HeavyCream(ItemGroup itemGroup, SlimefunItemStack item, RecipeCategory recipeCategory, ItemStack[] recipe, ItemStack recipeOutput) {
        super(itemGroup, item, recipeCategory, recipe, recipeOutput);
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Optional<Block> block = e.getClickedBlock();

            if (!block.isPresent() || !block.get().getType().isInteractable()) {
                e.cancel();
            }
        };
    }

}