package io.github.thebusybiscuit.slimefun4.implementation.items.gps;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.NotPlaceable;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class GPSMarkerTool extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {

    public GPSMarkerTool(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();

            if (e.getClickedBlock().isPresent()) {
                Block b = e.getClickedBlock().get().getRelative(e.getClickedFace());
                SlimefunPlugin.getGPSNetwork().addWaypoint(e.getPlayer(), b.getLocation());
            }
        };
    }
}
