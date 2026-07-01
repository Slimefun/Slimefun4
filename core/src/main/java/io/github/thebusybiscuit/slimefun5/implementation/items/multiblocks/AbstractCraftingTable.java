package io.github.thebusybiscuit.slimefun5.implementation.items.multiblocks;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import io.github.bakedlibs.dough.common.ChatColors;
import io.github.bakedlibs.dough.common.CommonPatterns;
import io.github.bakedlibs.dough.items.ItemUtils;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.player.PlayerBackpack;
import io.github.thebusybiscuit.slimefun5.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun5.implementation.items.backpacks.SlimefunBackpack;
import io.github.thebusybiscuit.slimefun5.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.BlockDataCompat;
import io.papermc.lib.PaperLib;

/**
 * This abstract super class is responsible for some utility methods for machines which
 * are capable of upgrading backpacks.
 * 
 * @author TheBusyBiscuit
 * 
 * @see EnhancedCraftingTable
 * @see MagicWorkbench
 * @see ArmorForge
 *
 */
public abstract class AbstractCraftingTable extends MultiBlockMachine {

    @ParametersAreNonnullByDefault
    AbstractCraftingTable(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe, BlockFace trigger) {
        super(itemGroup, item, recipe, trigger);
    }

    /**
     * Whether the dispenser inventory matches the given recipe. Each crafting table implements this
     * with its own matching rules (lore/backpack handling); the headless {@link #autoCraft(Block)}
     * reuses it.
     */
    protected abstract boolean isCraftable(Inventory inv, ItemStack[] recipe);

    /**
     * A {@link SlimefunItems#SLOT_LOCK} placed in a dispenser slot locks it, vanilla-Crafter style:
     * the slot is treated as empty for recipe matching and is never consumed, while occupying the slot
     * makes hoppers skip it. This returns whether the given item is such a lock marker.
     */
    protected static boolean isSlotLock(@Nullable ItemStack item) {
        return item != null && SlimefunUtils.isItemSimilar(item, SlimefunItems.SLOT_LOCK.item(), false);
    }

    /** Returns null for a locked slot (so it matches an empty recipe cell), otherwise the item itself. */
    @Nullable
    protected static ItemStack ignoreLock(@Nullable ItemStack item) {
        return isSlotLock(item) ? null : item;
    }

    private static final String[] WOOD_FORMS = { "_PLANKS", "_LOG", "_WOOD" };

    /** Whether two materials are the same wood "form" (both planks, both logs, both wood/bark) so any
     *  wood variant satisfies a recipe written for one type (e.g. oak). Pre-1.13 planks/logs are a single
     *  material, so equality already covers those versions. */
    private static boolean sameWoodForm(@Nonnull Material a, @Nonnull Material b) {
        if (a == b) {
            return true;
        }
        String na = a.name();
        String nb = b.name();
        for (String form : WOOD_FORMS) {
            if (na.endsWith(form) && nb.endsWith(form)) {
                return true;
            }
        }
        return false;
    }

    /** Whether a slot's plain vanilla wood satisfies a recipe cell of a different wood variant. Slimefun
     *  items are excluded (they still match strictly by id), so only plain wood is treated loosely. */
    protected static boolean sameWoodMatch(@Nullable ItemStack slot, @Nullable ItemStack recipe) {
        if (slot == null || recipe == null || SlimefunItem.getByItem(slot) != null || SlimefunItem.getByItem(recipe) != null) {
            return false;
        }
        return sameWoodForm(slot.getType(), recipe.getType()) && slot.getAmount() >= recipe.getAmount();
    }

    /** Consumes one of each non-lock input, mirroring a successful craft. */
    protected void consumeInputs(@Nonnull Inventory inv) {
        for (int j = 0; j < 9; j++) {
            ItemStack item = inv.getContents()[j];

            if (item != null && item.getType() != Material.AIR && !isSlotLock(item)) {
                ItemUtils.consumeItem(item, true);
            }
        }
    }

    /** Drops the crafted output into open space around a block - used when no inventory has room. */
    protected void dropOutput(@Nonnull Block block, @Nonnull ItemStack output) {
        Block target = firstOpenBlock(
            block.getRelative(BlockFace.UP),
            block.getRelative(BlockFace.NORTH), block.getRelative(BlockFace.EAST),
            block.getRelative(BlockFace.SOUTH), block.getRelative(BlockFace.WEST),
            block
        );
        block.getWorld().dropItemNaturally(target.getLocation().add(0.5, 0.5, 0.5), output);
    }

    /** First candidate block that isn't solid (so a dropped item lands in open air, never trapped inside
     *  a block where it would be lost); falls back to the last candidate. */
    private static Block firstOpenBlock(Block... candidates) {
        for (Block candidate : candidates) {
            if (candidate != null && !candidate.getType().isSolid()) {
                return candidate;
            }
        }
        return candidates[candidates.length - 1];
    }

    /**
     * Performs a single craft headlessly (no {@link Player}, no permission check, no
     * {@link io.github.thebusybiscuit.slimefun5.api.events.MultiBlockCraftEvent}) directly from a
     * powered dispenser, ejecting the result as a dropped item in the dispenser's facing direction.
     * This backs the redstone auto-craft feature. Backpack recipes are skipped, since assigning a
     * backpack requires a player.
     *
     * @param dispenser
     *            The dispenser block that holds the recipe inputs
     *
     * @return Whether a craft was performed
     */
    public boolean autoCraft(@Nonnull Block dispenser) {
        BlockState state = PaperLib.getBlockState(dispenser, false).getState();

        if (!(state instanceof Dispenser)) {
            return false;
        }

        Inventory inv = ((Dispenser) state).getInventory();

        for (ItemStack[] input : RecipeType.getRecipeInputList(this)) {
            if (isCraftable(inv, input)) {
                ItemStack output = RecipeType.getRecipeOutputList(this, input).clone();

                // Backpacks need a player profile to assign an id, so they cannot be auto-crafted.
                if (SlimefunItem.getByItem(output) instanceof SlimefunBackpack) {
                    return false;
                }

                for (int j = 0; j < 9; j++) {
                    ItemStack item = inv.getContents()[j];

                    if (item != null && item.getType() != Material.AIR && !isSlotLock(item)) {
                        ItemUtils.consumeItem(item, true);
                    }
                }

                ejectOutput(dispenser, output);
                SoundEffect.ENHANCED_CRAFTING_TABLE_CRAFT_SOUND.playAt(dispenser);
                return true;
            }
        }

        return false;
    }

    /** Drops the crafted item out of the dispenser, preferring its facing direction but always landing in
     *  open air so the item is never lost (e.g. when the facing resolves to the crafting table above it). */
    private void ejectOutput(@Nonnull Block dispenser, @Nonnull ItemStack output) {
        BlockFace facing = getDispenserFacing(dispenser);
        Block target = firstOpenBlock(
            dispenser.getRelative(facing),
            dispenser.getRelative(BlockFace.NORTH), dispenser.getRelative(BlockFace.EAST),
            dispenser.getRelative(BlockFace.SOUTH), dispenser.getRelative(BlockFace.WEST),
            dispenser.getRelative(BlockFace.DOWN), dispenser.getRelative(BlockFace.UP),
            dispenser
        );
        Location location = target.getLocation().add(0.5, 0.5, 0.5);

        dispenser.getWorld().dropItem(location, output).setVelocity(new Vector(facing.getModX() * 0.2, facing.getModY() * 0.2, facing.getModZ() * 0.2));
    }

    @Nonnull
    private BlockFace getDispenserFacing(@Nonnull Block dispenser) {
        Object facing = BlockDataCompat.get(BlockDataCompat.getBlockData(dispenser), "getFacing");

        if (!(facing instanceof BlockFace)) {
            // 1.8-1.12 have no BlockData; the facing lives on the legacy MaterialData.
            org.bukkit.material.MaterialData data = PaperLib.getBlockState(dispenser, false).getState().getData();

            if (data instanceof org.bukkit.material.Directional) {
                facing = ((org.bukkit.material.Directional) data).getFacing();
            }
        }

        return facing instanceof BlockFace ? (BlockFace) facing : BlockFace.UP;
    }

    protected @Nonnull Inventory createVirtualInventory(@Nonnull Inventory inv) {
        Inventory fakeInv = Bukkit.createInventory(null, 9, "Fake Inventory");

        for (int j = 0; j < inv.getContents().length; j++) {
            ItemStack stack = inv.getContents()[j];

            /*
             * Fixes #2103 - Properly simulating the consumption
             * (which may leave behind empty buckets or glass bottles)
             */
            if (stack != null) {
                stack = stack.clone();
                ItemUtils.consumeItem(stack, true);
            }

            fakeInv.setItem(j, stack);
        }

        return fakeInv;
    }

    @ParametersAreNonnullByDefault
    protected void upgradeBackpack(Player p, Inventory inv, SlimefunBackpack backpack, ItemStack output) {
        ItemStack input = null;

        for (int j = 0; j < 9; j++) {
            if (inv.getContents()[j] != null && inv.getContents()[j].getType() != Material.AIR && SlimefunItem.getByItem(inv.getContents()[j]) instanceof SlimefunBackpack) {
                input = inv.getContents()[j];
                break;
            }
        }

        // Fixes #2574 - Carry over the Soulbound status
        if (SlimefunUtils.isSoulbound(input)) {
            SlimefunUtils.setSoulbound(output, true);
        }

        int size = backpack.getSize();
        Optional<String> id = retrieveID(input, size);

        if (id.isPresent()) {
            for (int line = 0; line < output.getItemMeta().getLore().size(); line++) {
                if (output.getItemMeta().getLore().get(line).equals(ChatColors.color("&7ID: <ID>"))) {
                    ItemMeta im = output.getItemMeta();
                    List<String> lore = im.getLore();
                    lore.set(line, lore.get(line).replace("<ID>", id.get()));
                    im.setLore(lore);
                    output.setItemMeta(im);
                    break;
                }
            }
        } else {
            for (int line = 0; line < output.getItemMeta().getLore().size(); line++) {
                if (output.getItemMeta().getLore().get(line).equals(ChatColors.color("&7ID: <ID>"))) {
                    int target = line;

                    PlayerProfile.get(p, profile -> {
                        int backpackId = profile.createBackpack(size).getId();
                        Slimefun.getBackpackListener().setBackpackId(p, output, target, backpackId);
                    });

                    break;
                }
            }
        }
    }

    private @Nonnull Optional<String> retrieveID(@Nullable ItemStack backpack, int size) {
        if (backpack != null) {
            for (String line : backpack.getItemMeta().getLore()) {
                if (line.startsWith(ChatColors.color("&7ID: ")) && line.contains("#")) {
                    String id = line.replace(ChatColors.color("&7ID: "), "");
                    String[] idSplit = CommonPatterns.HASH.split(id);

                    PlayerProfile.fromUUID(UUID.fromString(idSplit[0]), profile -> {
                        Optional<PlayerBackpack> optional = profile.getBackpack(Integer.parseInt(idSplit[1]));
                        optional.ifPresent(playerBackpack -> {
                            // Safety feature for Issue #3664
                            CompletableFuture<Void> future = playerBackpack.closeForAll();
                            future.thenRun(() -> playerBackpack.setSize(size));
                        });
                    });

                    return Optional.of(id);
                }
            }
        }

        return Optional.empty();
    }

}

