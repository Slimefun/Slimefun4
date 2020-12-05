package io.github.thebusybiscuit.slimefun4.core.handlers;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.AndroidMineEvent;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;

public abstract class BlockBreakHandler implements ItemHandler {

    private final boolean allowAndroids;
    private final boolean allowExplosions;

    public BlockBreakHandler(boolean allowAndroids, boolean allowExplosions) {
        this.allowAndroids = allowAndroids;
        this.allowExplosions = allowExplosions;
    }

    @ParametersAreNonnullByDefault
    public abstract void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops);

    @ParametersAreNonnullByDefault
    public void onExplode(Block b, List<ItemStack> drops) {
        // This can be overridden, if necessary
    }

    @ParametersAreNonnullByDefault
    public void onAndroidBreak(AndroidMineEvent e) {
        // This can be overridden, if necessary
    }

    public boolean isExplosionAllowed() {
        return allowExplosions;
    }

    public boolean isAndroidAllowed() {
        return allowAndroids;
    }

    @Override
    public Class<? extends ItemHandler> getIdentifier() {
        return BlockBreakHandler.class;
    }
}
