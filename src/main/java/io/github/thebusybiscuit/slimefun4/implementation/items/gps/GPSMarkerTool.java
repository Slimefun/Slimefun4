package io.github.thebusybiscuit.slimefun4.implementation.items.gps;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.gps.Waypoint;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link GPSMarkerTool} allows you to create a {@link Waypoint} at your current
 * {@link Location}.
 * 
 * @author TheBusyBiscuit
 *
 */
public class GPSMarkerTool extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {

    @ParametersAreNonnullByDefault
    public GPSMarkerTool(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();

            if (e.getClickedBlock().isPresent()) {
                Block b = e.getClickedBlock().get().getRelative(e.getClickedFace());
                SlimefunPlugin.getGPSNetwork().createWaypoint(e.getPlayer(), b.getLocation());
            }
        };
    }
}
