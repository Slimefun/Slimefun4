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

                    if (item != null && item.getType() != Material.AIR) {
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

    /** Drops the crafted item out of the dispenser in the direction it is facing. */
    private void ejectOutput(@Nonnull Block dispenser, @Nonnull ItemStack output) {
        BlockFace facing = getDispenserFacing(dispenser);
        Location location = dispenser.getRelative(facing).getLocation().add(0.5, 0.5, 0.5);

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

