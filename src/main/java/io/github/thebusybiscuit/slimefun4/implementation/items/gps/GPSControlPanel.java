package io.github.thebusybiscuit.slimefun4.implementation.items.gps;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class GPSControlPanel extends SimpleSlimefunItem<BlockUseHandler> {

    public GPSControlPanel(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public BlockUseHandler getItemHandler() {
        return e -> SlimefunPlugin.getGPSNetwork().openTransmitterControlPanel(e.getPlayer());
    }
}
