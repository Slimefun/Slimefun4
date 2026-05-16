package io.github.thebusybiscuit.slimefun5.implementation.items.blocks;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.handlers.RainbowTickHandler;
import io.github.thebusybiscuit.slimefun5.implementation.items.SimpleSlimefunItem;

/**
 * A {@link RainbowBlock} cycles through different colored {@link Material Materials}.
 * 
 * @author TheBusyBiscuit
 *
 */
public class RainbowBlock extends SimpleSlimefunItem<RainbowTickHandler> {

    private final RainbowTickHandler ticker;

    @ParametersAreNonnullByDefault
    public RainbowBlock(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput, RainbowTickHandler ticker) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);

        this.ticker = ticker;
    }

    @Override
    public RainbowTickHandler getItemHandler() {
        return ticker;
    }

}

