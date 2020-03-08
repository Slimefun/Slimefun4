package io.github.thebusybiscuit.slimefun4.implementation.items.gps;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.gps.GPSNetwork;
import io.github.thebusybiscuit.slimefun4.api.gps.TeleportationManager;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The Teleporter is a {@link SlimefunItem} that can be placed down and allows a {@link Player}
 * to display to any of his waypoints he set via his {@link GPSNetwork}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see GPSNetwork
 * @see TeleportationManager
 *
 */
public class Teleporter extends SlimefunItem {

    public Teleporter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        SlimefunItem.registerBlockHandler(getID(), new SlimefunBlockHandler() {

            @Override
            public void onPlace(Player p, Block b, SlimefunItem item) {
                BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());
            }

            @Override
            public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
                return true;
            }
        });
    }

}
