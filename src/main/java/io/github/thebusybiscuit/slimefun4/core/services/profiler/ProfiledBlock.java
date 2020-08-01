package io.github.thebusybiscuit.slimefun4.core.services.profiler;

import org.bukkit.Location;
import org.bukkit.block.Block;

import io.github.thebusybiscuit.cscorelib2.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

class ProfiledBlock {

    private final BlockPosition position;
    private final SlimefunItem item;

    ProfiledBlock(Location l, SlimefunItem item) {
        this.position = new BlockPosition(l);
        this.item = item;
    }

    ProfiledBlock(BlockPosition position, SlimefunItem item) {
        this.position = position;
        this.item = item;
    }

    ProfiledBlock(Block b) {
        this(new BlockPosition(b), null);
    }

    public BlockPosition getPosition() {
        return position;
    }

    public String getId() {
        return item.getID();
    }

    public SlimefunAddon getAddon() {
        return item.getAddon();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProfiledBlock) {
            return position.equals(((ProfiledBlock) obj).position);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }

}
