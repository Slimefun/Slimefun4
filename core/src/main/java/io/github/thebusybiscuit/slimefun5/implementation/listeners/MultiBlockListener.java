package io.github.thebusybiscuit.slimefun5.implementation.listeners;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.HandCompat;

import java.util.LinkedList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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

            if (compareMaterials(center, mb.getStructure(), mb.isSymmetric())) {
                multiblocks.add(mb);
            }
        }

        if (!multiblocks.isEmpty()) {
            e.setCancelled(true);

            MultiBlock mb = multiblocks.getLast();
            MultiBlockInteractEvent event = new MultiBlockInteractEvent(p, mb, b, e.getBlockFace());
            Bukkit.getPluginManager().callEvent(event);

            // Fixes #2809
            if (!event.isCancelled()) {
                mb.getSlimefunItem().callItemHandler(MultiBlockInteractionHandler.class, handler -> handler.onInteract(p, mb, b));
            }
        }
    }

    /**
     * Gives the player feedback when the block they just placed completes a {@link MultiBlock}
     * structure, so they know the machine is assembled and ready to use.
     */
    @EventHandler(ignoreCancelled = true)
    public void onMultiBlockComplete(org.bukkit.event.block.BlockPlaceEvent e) {
        Block placed = e.getBlock();

        for (MultiBlock mb : Slimefun.getRegistry().getMultiBlocks()) {
            Material[] structure = mb.getStructure();

            // Cheap pre-filter: only consider a multiblock the placed block could actually be part of.
            // This also avoids re-announcing an existing machine when placing an unrelated block beside it.
            if (!structureContains(structure, placed.getType())) {
                continue;
            }

            // The placed block can be any cell of the structure, so test every center within one block.
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (compareMaterials(placed.getRelative(dx, dy, dz), structure, mb.isSymmetric())) {
                            Player p = e.getPlayer();

                            if (io.github.thebusybiscuit.slimefun5.core.guide.options.SlimefunGuideSettings.hasMachineMessagesEnabled(p)) {
                                io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect.ANCIENT_ALTAR_FINISH_SOUND.playFor(p);
                                p.sendMessage(org.bukkit.ChatColor.GREEN + "✔ Assembled: " + mb.getSlimefunItem().getItemName());
                            }

                            return;
                        }
                    }
                }
            }
        }
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
    private boolean compareMaterials(Block b, Material[] blocks, boolean onlyTwoWay) {
        if (!compareMaterialsVertical(b, blocks[1], blocks[4], blocks[7])) {
            return false;
        }

        BlockFace[] directions = onlyTwoWay ? new BlockFace[] { BlockFace.NORTH, BlockFace.EAST } : new BlockFace[] { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

        for (BlockFace direction : directions) {
            if (compareMaterialsVertical(b.getRelative(direction), blocks[0], blocks[3], blocks[6]) && compareMaterialsVertical(b.getRelative(direction.getOppositeFace()), blocks[2], blocks[5], blocks[8])) {
                return true;
            }
        }

        return false;
    }

    private boolean compareMaterialsVertical(@Nonnull Block b, @Nullable Material top, @Nullable Material center, @Nullable Material bottom) {
        return (center == null || equals(b.getType(), center)) && (top == null || equals(b.getRelative(BlockFace.UP).getType(), top)) && (bottom == null || equals(b.getRelative(BlockFace.DOWN).getType(), bottom));
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

