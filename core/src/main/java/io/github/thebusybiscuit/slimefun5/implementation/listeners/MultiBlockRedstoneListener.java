package io.github.thebusybiscuit.slimefun5.implementation.listeners;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.core.multiblocks.MultiBlock;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.items.multiblocks.AbstractCraftingTable;

import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * Lets a dispenser that forms part of a crafting-table {@link MultiBlock} (Enhanced Crafting Table,
 * Magic Workbench, Armor Forge) auto-craft when powered by redstone: the vanilla dispense is
 * cancelled and the machine instead crafts headlessly, ejecting the result in the dispenser's facing
 * direction. The dispenser is a vanilla block (no {@link BlockStorage} entry), which is how this is
 * distinguished from Slimefun's own dispenser machines.
 *
 * @author TheBusyBiscuit
 *
 * @see AbstractCraftingTable
 */
public class MultiBlockRedstoneListener implements Listener {

    // Dispensers that already auto-crafted within the current debounce window. A single redstone
    // activation can fire BlockDispenseEvent more than once (multi-tick pulses, comparator/observer
    // signals), which would otherwise craft multiple times - one signal must craft at most once.
    private final Set<Location> recentlyCrafted = new HashSet<>();

    public MultiBlockRedstoneListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDispense(BlockDispenseEvent e) {
        Block dispenser = e.getBlock();

        // Only vanilla dispensers that are not themselves a Slimefun machine (those are handled
        // by DispenserListener); a multiblock's dispenser has no BlockStorage entry.
        if (dispenser.getType() != Material.DISPENSER || BlockStorage.check(dispenser) != null) {
            return;
        }

        AbstractCraftingTable machine = findCraftingTable(dispenser);

        if (machine != null) {
            // Cancel the vanilla dispense so recipe ingredients are never spat out; craft instead.
            e.setCancelled(true);

            Location loc = dispenser.getLocation();

            // Debounce: ignore further dispense events for this dispenser until the window clears,
            // so one redstone activation crafts exactly once even if it pulses several times.
            if (!recentlyCrafted.add(loc)) {
                return;
            }

            Slimefun.runSync(() -> recentlyCrafted.remove(loc), 2L);

            // Vanilla splits one item off the selected slot BEFORE firing this event, and only restores it
            // (because we cancelled) after this handler returns. Crafting now would read the dispenser one
            // ingredient short, so a recipe with exactly one of that ingredient would falsely not match
            // ("needs enough to craft twice"). Defer one tick so autoCraft sees the full, restored inventory.
            Slimefun.runSync(() -> {
                try {
                    machine.autoCraft(dispenser);
                } catch (Exception | LinkageError x) {
                    Slimefun.logger().warning("Failed to redstone auto-craft at " + loc + ": " + x.getMessage());
                }
            }, 1L);
        }
    }

    /**
     * Finds the crafting-table multiblock this dispenser belongs to by testing every block within one
     * cell as the structure's center. Returns null if the dispenser is not part of such a machine.
     */
    @Nullable
    private AbstractCraftingTable findCraftingTable(@Nonnull Block dispenser) {
        for (MultiBlock mb : Slimefun.getRegistry().getMultiBlocks()) {
            SlimefunItem item = mb.getSlimefunItem();

            if (!(item instanceof AbstractCraftingTable) || item.isDisabledIn(dispenser.getWorld())) {
                continue;
            }

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (mb.matches(dispenser.getRelative(dx, dy, dz))) {
                            return (AbstractCraftingTable) item;
                        }
                    }
                }
            }
        }

        return null;
    }
}
