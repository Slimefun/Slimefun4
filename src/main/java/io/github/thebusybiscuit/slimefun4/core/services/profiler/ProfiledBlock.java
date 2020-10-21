package io.github.thebusybiscuit.slimefun4.core.services.profiler;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.block.Block;

import io.github.thebusybiscuit.cscorelib2.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This represents an entry in our {@link SlimefunProfiler}.
 * 
 * @author TheBusyBiscuit
 *
 */
class ProfiledBlock {

    private final BlockPosition position;
    private final SlimefunItem item;

    ProfiledBlock(@Nonnull Location l, @Nonnull SlimefunItem item) {
        this.position = new BlockPosition(l);
        this.item = item;
    }

    ProfiledBlock(@Nonnull BlockPosition position, @Nonnull SlimefunItem item) {
        this.position = position;
        this.item = item;
    }

    /**
     * This is just a <strong>dummy</strong> constructor.
     * 
     * @param b
     *            A {@link Block}
     */
    ProfiledBlock(@Nonnull Block b) {
        this.position = new BlockPosition(b);
        this.item = null;
    }

    public BlockPosition getPosition() {
        return position;
    }

    public String getId() {
        return item.getId();
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
