package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.handlers.RainbowTickHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class RainbowBlock extends SimpleSlimefunItem<RainbowTickHandler> {

    private final RainbowTickHandler ticker;

    public RainbowBlock(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput, RainbowTickHandler ticker) {
        super(category, item, recipeType, recipe, recipeOutput);

        this.ticker = ticker;
    }

    @Override
    public RainbowTickHandler getItemHandler() {
        return ticker;
    }

}
