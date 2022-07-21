package io.github.thebusybiscuit.slimefun4.core.networks.cargo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.network.Network;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

/**
 * An abstract super class of {@link CargoNet} that handles
 * interactions with ChestTerminal.
 * 
 * @author TheBusyBiscuit
 *
 */
abstract class AbstractItemNetwork extends Network {

    /**
     * This is a cache for the {@link BlockFace} a node is facing, so we don't need to
     * request the {@link BlockData} each time we visit a node
     */
    protected Map<Location, BlockFace> connectorCache = new HashMap<>();

    /**
     * This is our cache for the {@link ItemFilter} for each node.
     */
    protected Map<Location, ItemFilter> filterCache = new HashMap<>();

    protected AbstractItemNetwork(@Nonnull Location regulator) {
        super(Slimefun.getNetworkManager(), regulator);
    }

    protected Optional<Block> getAttachedBlock(@Nonnull Location l) {
        if (l.getWorld().isChunkLoaded(l.getBlockX() >> 4, l.getBlockZ() >> 4)) {
            Block block = l.getBlock();

            if (block.getType() == Material.PLAYER_WALL_HEAD) {
                BlockFace cached = connectorCache.get(l);

                if (cached != null) {
                    return Optional.of(block.getRelative(cached));
                }

                BlockFace face = ((Directional) block.getBlockData()).getFacing().getOppositeFace();
                connectorCache.put(l, face);
                return Optional.of(block.getRelative(face));
            }
        }

        return Optional.empty();
    }

    @Override
    public void markDirty(@Nonnull Location l) {
        markCargoNodeConfigurationDirty(l);
        super.markDirty(l);
    }

    /**
     * This will mark the {@link ItemFilter} of the given node dirty.
     * It will also invalidate the cached rotation.
     * 
     * @param node
     *            The {@link Location} of the cargo node
     */
    public void markCargoNodeConfigurationDirty(@Nonnull Location node) {
        ItemFilter filter = filterCache.get(node);

        if (filter != null) {
            filter.markDirty();
        }

        connectorCache.remove(node);
    }

    @ParametersAreNonnullByDefault
    private void handleWithdraw(DirtyChestMenu menu, List<ItemStackAndInteger> items, Location l) {
        for (int slot : menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.WITHDRAW, null)) {
            filter(menu.getItemInSlot(slot), items, l);
        }
    }

    @ParametersAreNonnullByDefault
    private void filter(@Nullable ItemStack stack, List<ItemStackAndInteger> items, Location node) {
        if (stack != null && CargoUtils.matchesFilter(this, node.getBlock(), stack)) {
            boolean add = true;

            for (ItemStackAndInteger item : items) {
                if (SlimefunUtils.isItemSimilar(stack, item.getItemStackWrapper(), true, false)) {
                    add = false;
                    item.add(stack.getAmount());
                }
            }

            if (add) {
                items.add(new ItemStackAndInteger(stack, stack.getAmount()));
            }
        }
    }

    protected @Nonnull ItemFilter getItemFilter(@Nonnull Block node) {
        Location loc = node.getLocation();
        ItemFilter filter = filterCache.get(loc);

        if (filter == null) {
            ItemFilter newFilter = new ItemFilter(node);
            filterCache.put(loc, newFilter);
            return newFilter;
        } else if (filter.isDirty()) {
            filter.update(node);
            return filter;
        } else {
            return filter;
        }
    }

}
