package io.github.thebusybiscuit.slimefun4.implementation.items.geo;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class GEOScanner extends SimpleSlimefunItem<BlockUseHandler> {

    public GEOScanner(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public BlockUseHandler getItemHandler() {
        return e -> {
            e.cancel();

            Block b = e.getClickedBlock().get();
            Player p = e.getPlayer();

            if (p.hasPermission("slimefun.gps.bypass")
                || (SlimefunPlugin.getProtectionManager().hasPermission(
                p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES))
            ) {

                SlimefunPlugin.getGPSNetwork().getResourceManager().scan(p, b, 0);
            } else {
                SlimefunPlugin.getLocalization().sendMessage(p, "inventory.no-access", true);

            }
        };
    }
}
