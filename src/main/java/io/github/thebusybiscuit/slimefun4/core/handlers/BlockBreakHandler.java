package io.github.thebusybiscuit.slimefun4.core.handlers;

import java.util.List;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;

@FunctionalInterface
public interface BlockBreakHandler extends ItemHandler {

    boolean onBlockBreak(BlockBreakEvent e, ItemStack item, int fortune, List<ItemStack> drops);

    @Override
    default Class<? extends ItemHandler> getIdentifier() {
        return BlockBreakHandler.class;
    }
}
