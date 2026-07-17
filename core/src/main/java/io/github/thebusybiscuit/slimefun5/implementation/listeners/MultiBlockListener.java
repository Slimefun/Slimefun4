package io.github.thebusybiscuit.slimefun5.implementation.listeners;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.HandCompat;

import java.util.LinkedList;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import io.github.thebusybiscuit.slimefun5.api.events.MultiBlockInteractEvent;
import io.github.thebusybiscuit.slimefun5.core.handlers.MultiBlockInteractionHandler;
import io.github.thebusybiscuit.slimefun5.core.multiblocks.MultiBlock;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * This {@link Listener} is responsible for listening to a {@link PlayerInteractEvent} and
 * triggering any {@link MultiBlockInteractionHandler}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see MultiBlock
 * @see MultiBlockInteractionHandler
 * @see MultiBlockInteractEvent
 *
 */
public class MultiBlockListener implements Listener {

    public MultiBlockListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || HandCompat.getHand(e) != EquipmentSlot.HAND) {
            return;
        }

        Player p = e.getPlayer();
        Block b = e.getClickedBlock();
        LinkedList<MultiBlock> multiblocks = new LinkedList<>();

        for (MultiBlock mb : Slimefun.getRegistry().getMultiBlocks()) {
            Block center = b.getRelative(mb.getTriggerBlock());

            if (mb.matches(center)) {
                multiblocks.add(mb);
            }
        }

        if (!multiblocks.isEmpty()) {
            e.setCancelled(true);

            // Several multiblocks can match the same clicked structure when legacy (1.8-1.12) material-collapse
            // makes distinct modern variants identical (e.g. ARMOR_FORGE vs EXP_DISPENSER both resolve to
            // ANVIL over DISPENSER). Dispatch to every match, most-recently-registered first, instead of only
            // getLast(): each machine's onInteract self-guards on its own recipe/input, so the intended one
            // acts and the rest no-op - rather than the wrong (last-registered) one silently swallowing it.
            java.util.Iterator<MultiBlock> it = multiblocks.descendingIterator();

            while (it.hasNext()) {
                MultiBlock mb = it.next();
                MultiBlockInteractEvent event = new MultiBlockInteractEvent(p, mb, b, e.getBlockFace());
                Bukkit.getPluginManager().callEvent(event);

                // Fixes #2809
                if (!event.isCancelled()) {
                    mb.getSlimefunItem().callItemHandler(MultiBlockInteractionHandler.class, handler -> handler.onInteract(p, mb, b));
                }
            }
        }
    }

    /**
     * Gives the player feedback when the block they just placed completes a {@link MultiBlock}
     * structure, so they know the machine is assembled and ready to use.
     */
    @EventHandler(ignoreCancelled = true)
    public void onMultiBlockComplete(org.bukkit.event.block.BlockPlaceEvent e) {
        // canBuild() is false when the placement is blocked (e.g. the player is standing in the target
        // cell), in which case the block reverts - never announce a machine that was never actually built.
        if (!e.canBuild()) {
            return;
        }

        Block placed = e.getBlock();
        Player p = e.getPlayer();
        Material placedType = placed.getType();

        // The placed block's world state only settles (or reverts) after the event returns, so defer the
        // structure match one tick and re-verify the placed block is still there before announcing.
        Slimefun.runSync(() -> {
            if (placed.getType() != placedType) {
                return;
            }

            for (MultiBlock mb : Slimefun.getRegistry().getMultiBlocks()) {
                // Cheap pre-filter: only consider a multiblock the placed block could actually be part of.
                // This also avoids re-announcing an existing machine when placing an unrelated block beside it.
                if (!structureContains(mb.getStructure(), placedType)) {
                    continue;
                }

                // The placed block can be any cell of the structure, so test every center within one block.
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dz = -1; dz <= 1; dz++) {
                            if (mb.matches(placed.getRelative(dx, dy, dz))) {
                                if (io.github.thebusybiscuit.slimefun5.core.guide.options.SlimefunGuideSettings.hasMachineMessagesEnabled(p)) {
                                    io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect.ANCIENT_ALTAR_FINISH_SOUND.playFor(p);
                                    p.sendMessage(org.bukkit.ChatColor.GREEN + "✔ Assembled: " + Slimefun.getItemTranslationService().getName(p, mb.getSlimefunItem()));
                                }

                                return;
                            }
                        }
                    }
                }
            }
        }, 1L);
    }

    private boolean structureContains(@Nonnull Material[] structure, @Nonnull Material placed) {
        for (Material cell : structure) {
            if (cell != null && equals(placed, cell)) {
                return true;
            }
        }

        return false;
    }

    @ParametersAreNonnullByDefault
    private boolean equals(Material a, Material b) {
        if (a == b) {
            return true;
        }

        for (Tag<Material> tag : MultiBlock.getSupportedTags()) {
            if (tag.isTagged(a) && tag.isTagged(b)) {
                return true;
            }
        }

        return false;
    }
}

