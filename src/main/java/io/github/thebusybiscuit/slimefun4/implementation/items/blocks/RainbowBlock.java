package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.RainbowTicker;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.inventory.ItemStack;

public class RainbowBlock extends SimpleSlimefunItem<RainbowTicker> {

    private final RainbowTicker ticker;

    public RainbowBlock(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput, RainbowTicker ticker) {
        super(category, item, recipeType, recipe, recipeOutput);

        this.ticker = ticker;
    }

    @Override
    public RainbowTicker getItemHandler() {
        return ticker;
    }

}