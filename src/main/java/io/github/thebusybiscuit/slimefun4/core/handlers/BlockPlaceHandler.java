package io.github.thebusybiscuit.slimefun4.core.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;

@FunctionalInterface
public interface BlockPlaceHandler extends ItemHandler {

    boolean onBlockPlace(Player p, BlockPlaceEvent e, ItemStack item);

    @Override
    default Class<? extends ItemHandler> getIdentifier() {
        return BlockPlaceHandler.class;
    }
}
