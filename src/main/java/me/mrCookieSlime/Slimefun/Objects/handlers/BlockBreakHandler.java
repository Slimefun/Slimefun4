package me.mrCookieSlime.Slimefun.Objects.handlers;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@FunctionalInterface
public interface BlockBreakHandler extends ItemHandler {

    boolean onBlockBreak(BlockBreakEvent e, ItemStack item, int fortune, List<ItemStack> drops);

    @Override
    default Class<? extends ItemHandler> getIdentifier() {
        return BlockBreakHandler.class;
    }
}
