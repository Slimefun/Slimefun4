package io.github.thebusybiscuit.slimefun5.core.multiblocks;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import com.cryptomorin.xseries.XMaterial;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import io.github.thebusybiscuit.slimefun5.api.events.MultiBlockInteractEvent;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.core.handlers.MultiBlockInteractionHandler;

/**
 * A {@link MultiBlock} represents a structure build in a {@link World}.
 * A {@link MultiBlock} is often linked to a {@link MultiBlockMachine} and is used
 * to recognize that machine in a {@link MultiBlockInteractEvent}.
 * 
 * @author TheBusyBiscuit
 * @author Liruxo
 * 
 * @see MultiBlockMachine
 * @see MultiBlockInteractionHandler
 * @see MultiBlockInteractEvent
 *
 */
public class MultiBlock {

    private static final Set<Tag<Material>> SUPPORTED_TAGS = new HashSet<>();

    static {
        // Allow variations of different types of wood to be used
        SUPPORTED_TAGS.add(Tag.LOGS);
        SUPPORTED_TAGS.add(Tag.WOODEN_TRAPDOORS);
        SUPPORTED_TAGS.add(Tag.WOODEN_SLABS);
        SUPPORTED_TAGS.add(Tag.WOODEN_FENCES);
        SUPPORTED_TAGS.add(Tag.FIRE);
    }

    @Nonnull
    public static Set<Tag<Material>> getSupportedTags() {
        return SUPPORTED_TAGS;
    }

    private final SlimefunItem item;
    private final Material[] blocks;
    private final BlockFace trigger;
    private final boolean isSymmetric;

    public MultiBlock(@Nonnull SlimefunItem item, Material[] build, @Nonnull BlockFace trigger) {
        Validate.notNull(item, "A MultiBlock requires a SlimefunItem!");

        if (build == null || build.length != 9) {
            throw new IllegalArgumentException("MultiBlocks must have a length of 9!");
        }

        if (trigger != BlockFace.SELF && trigger != BlockFace.UP && trigger != BlockFace.DOWN) {
            throw new IllegalArgumentException("Multiblock Blockface must be either UP, DOWN or SELF");
        }

        this.item = item;
        this.blocks = build;
        this.trigger = trigger;
        this.isSymmetric = isSymmetric(build);
    }

    @Nonnull
    public SlimefunItem getSlimefunItem() {
        return item;
    }

    private static boolean isSymmetric(@Nonnull Material[] blocks) {
        return blocks[0] == blocks[2] && blocks[3] == blocks[5] && blocks[6] == blocks[8];
    }

    @Nonnull
    public Material[] getStructure() {
        return blocks;
    }

    @Nonnull
    public BlockFace getTriggerBlock() {
        return trigger;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MultiBlock)) {
            return false;
        }

        MultiBlock mb = (MultiBlock) obj;

        if (trigger == mb.getTriggerBlock() && isSymmetric == mb.isSymmetric) {
            for (int i = 0; i < mb.getStructure().length; i++) {
                if (!compareBlocks(blocks[i], mb.getStructure()[i])) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(item.getId(), blocks, trigger, isSymmetric);
    }

    private boolean compareBlocks(Material a, @Nullable Material b) {
        if (b != null) {

            for (Tag<Material> tag : SUPPORTED_TAGS) {
                if (tag.isTagged(b)) {
                    return tag.isTagged(a);
                }
            }

            // This ensures that the Industrial Miner is still recognized while operating
            if (a == XMaterial.PISTON.parseMaterial()) {
                return b == XMaterial.PISTON.parseMaterial() || b == XMaterial.MOVING_PISTON.parseMaterial();
            } else if (b == XMaterial.PISTON.parseMaterial()) {
                return a == XMaterial.MOVING_PISTON.parseMaterial();
            }

            if (b != a) {
                return false;
            }
        }

        return true;
    }

    /**
     * This returns whether this {@link MultiBlock} is a symmetric structure or whether
     * the left and right side differ.
     * 
     * @return Whether this {@link MultiBlock} is a symmetric structure
     */
    public boolean isSymmetric() {
        return isSymmetric;
    }

    /**
     * Checks whether this {@link MultiBlock}'s structure is present, treating the given {@link Block}
     * as the structure's center (the {@code blocks[4]} cell). Shared by the interaction listener and
     * the redstone auto-craft listener.
     *
     * @param center
     *            The block to test as the structure's center
     *
     * @return Whether a valid orientation of this structure exists around {@code center}
     */
    public boolean matches(@Nonnull Block center) {
        Validate.notNull(center, "The center block cannot be null!");

        if (!matchesColumn(center, blocks[1], blocks[4], blocks[7])) {
            return false;
        }

        BlockFace[] directions = isSymmetric ? new BlockFace[] { BlockFace.NORTH, BlockFace.EAST } : new BlockFace[] { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

        for (BlockFace direction : directions) {
            if (matchesColumn(center.getRelative(direction), blocks[0], blocks[3], blocks[6]) && matchesColumn(center.getRelative(direction.getOppositeFace()), blocks[2], blocks[5], blocks[8])) {
                return true;
            }
        }

        return false;
    }

    /**
     * Whether {@code placed} occupies one of this structure's non-null cells when centred at {@code center},
     * in the orientation that matches. Used to confirm a just-placed block is genuinely part of a newly
     * completed structure, rather than an unrelated block set down beside an already-complete one (which
     * would otherwise re-announce "Assembled").
     */
    public boolean containsBlock(@Nonnull Block center, @Nonnull Block placed) {
        // Centre column: blocks[1]=up, blocks[4]=centre, blocks[7]=down.
        if (blocks[4] != null && sameBlock(center, placed)) {
            return true;
        }
        if (blocks[1] != null && sameBlock(center.getRelative(BlockFace.UP), placed)) {
            return true;
        }
        if (blocks[7] != null && sameBlock(center.getRelative(BlockFace.DOWN), placed)) {
            return true;
        }

        BlockFace[] directions = isSymmetric ? new BlockFace[] { BlockFace.NORTH, BlockFace.EAST } : new BlockFace[] { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

        for (BlockFace direction : directions) {
            if (!matchesColumn(center.getRelative(direction), blocks[0], blocks[3], blocks[6]) || !matchesColumn(center.getRelative(direction.getOppositeFace()), blocks[2], blocks[5], blocks[8])) {
                continue;
            }

            Block near = center.getRelative(direction);
            Block far = center.getRelative(direction.getOppositeFace());

            if ((blocks[3] != null && sameBlock(near, placed))
                || (blocks[0] != null && sameBlock(near.getRelative(BlockFace.UP), placed))
                || (blocks[6] != null && sameBlock(near.getRelative(BlockFace.DOWN), placed))
                || (blocks[5] != null && sameBlock(far, placed))
                || (blocks[2] != null && sameBlock(far.getRelative(BlockFace.UP), placed))
                || (blocks[8] != null && sameBlock(far.getRelative(BlockFace.DOWN), placed))) {
                return true;
            }
        }

        return false;
    }

    private static boolean sameBlock(@Nonnull Block a, @Nonnull Block b) {
        return a.getX() == b.getX() && a.getY() == b.getY() && a.getZ() == b.getZ() && a.getWorld().equals(b.getWorld());
    }

    private boolean matchesColumn(@Nonnull Block b, @Nullable Material top, @Nullable Material center, @Nullable Material bottom) {
        return (center == null || materialsMatch(b.getType(), center)) && (top == null || materialsMatch(b.getRelative(BlockFace.UP).getType(), top)) && (bottom == null || materialsMatch(b.getRelative(BlockFace.DOWN).getType(), bottom));
    }

    private boolean materialsMatch(@Nonnull Material a, @Nonnull Material b) {
        if (a == b) {
            return true;
        }

        for (Tag<Material> tag : SUPPORTED_TAGS) {
            if (tag.isTagged(a) && tag.isTagged(b)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "MultiBlock (" + item.getId() + ") {" + Arrays.toString(blocks) + "}";
    }
}

