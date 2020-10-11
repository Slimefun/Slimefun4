package io.github.thebusybiscuit.slimefun4.core.services.plugins;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import com.sk89q.worldedit.world.block.BlockStateHolder;

import me.mrCookieSlime.Slimefun.api.BlockStorage;

class WorldEditHook {

    WorldEditHook() {
        WorldEdit.getInstance().getEventBus().register(this);
    }

    @Subscribe
    public void wrapForLogging(EditSessionEvent event) {
        event.setExtent(new AbstractDelegateExtent(event.getExtent()) {

            @Override
            public <T extends BlockStateHolder<T>> boolean setBlock(BlockVector3 pos, T block) throws WorldEditException {
                if (block.getBlockType().getMaterial().isAir()) {
                    World world = Bukkit.getWorld(event.getWorld().getName());

                    if (world != null) {
                        Location l = new Location(world, pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());

                        if (BlockStorage.hasBlockInfo(l)) {
                            BlockStorage.clearBlockInfo(l);
                        }
                    }
                }

                return getExtent().setBlock(pos, block);
            }

        });
    }

}
