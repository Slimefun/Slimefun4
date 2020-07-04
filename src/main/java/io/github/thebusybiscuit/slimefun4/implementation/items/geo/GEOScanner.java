package io.github.thebusybiscuit.slimefun4.implementation.items.geo;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockUseHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class GEOScanner extends SimpleSlimefunItem<BlockUseHandler> {

    public GEOScanner(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public BlockUseHandler getItemHandler() {
        return e -> {
            Block b = e.getClickedBlock().get();

            e.cancel();
            SlimefunPlugin.getGPSNetwork().getResourceManager().scan(e.getPlayer(), b, 0);
        };
    }
}
