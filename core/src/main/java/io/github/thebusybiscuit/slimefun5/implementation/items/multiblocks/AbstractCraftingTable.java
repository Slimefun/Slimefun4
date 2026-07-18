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
import io.github.thebusybiscuit.slimefun5.api.researches.Research;
import io.github.thebusybiscuit.slimefun5.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun5.implementation.items.blocks.OutputChest;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun5.implementation.items.backpacks.SlimefunBackpack;
import io.github.thebusybiscuit.slimefun5.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.BlockDataCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.InventoryCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.SoundCategory;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.SoundCompat;
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
                InventoryCompat.consumeSlot(inv, j, 1, true);
            }
        }
    }

    /**
     * Consumes each input slot by the amount the matched {@code recipe} cell requires (not a hardcoded 1):
     * recipes may need more than one of an ingredient per slot (e.g. 2 planks), and {@code isCraftable}
     * already gates on {@code slot.getAmount() >= recipe.getAmount()}, so consuming only 1 left the surplus
     * behind ("consumes one too little"). Slot-lock markers and empty recipe cells are skipped.
     */
    protected void consumeInputs(@Nonnull Inventory inv, @Nonnull ItemStack[] recipe) {
        for (int j = 0; j < 9 && j < recipe.length; j++) {
            ItemStack cell = recipe[j];

            if (cell == null || cell.getType() == Material.AIR) {
                continue;
            }

            ItemStack item = inv.getContents()[j];

            if (item != null && item.getType() != Material.AIR && !isSlotLock(item)) {
                InventoryCompat.consumeSlot(inv, j, cell.getAmount(), true);
            }
        }
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

        // An unowned multiblock (nobody has interacted with it yet) never auto-crafts.
        UUID owner = Slimefun.getMultiBlockOwnership().getOwner(dispenser.getLocation());

        if (owner == null) {
            return false;
        }

        // A craft already in progress at this dispenser blocks re-triggering until it finishes: a timed
        // machine takes time to craft, and can only be powered again once the current item completes.
        Location loc = dispenser.getLocation();

        if (AUTO_CRAFTING.contains(loc)) {
            return false;
        }

        Inventory inv = ((Dispenser) state).getInventory();

        for (ItemStack[] input : RecipeType.getRecipeInputList(this)) {
            if (isCraftable(inv, input)) {
                ItemStack output = RecipeType.getRecipeOutputList(this, input).clone();

                SlimefunItem outputItem = SlimefunItem.getByItem(output);
                String outputId = outputItem != null ? outputItem.getId() : String.valueOf(output.getType());

                // Backpacks need a player profile to assign an id, so they cannot be auto-crafted.
                if (outputItem instanceof SlimefunBackpack) {
                    Slimefun.logger().info("[autocraft] " + getId() + ": recipe matched (" + outputId + ") but backpacks cannot be auto-crafted (need a player).");
                    return false;
                }

                // Gate on the owner's research/permission: only auto-craft what the owner could craft by hand.
                if (!isUnlockedForOwner(output, owner)) {
                    Slimefun.logger().info("[autocraft] " + getId() + ": recipe matched (" + outputId + ") but the owner is not allowed to use it (research not unlocked / no permission).");
                    return false;
                }

                consumeInputs(inv, input);

                int delay = getAutoCraftDelayTicks();

                if (delay <= 0) {
                    depositAutoCraftOutput(dispenser, output);
                    return true;
                }

                // Timed machine (Armor Forge / Magic Workbench): the craft takes time. Mark the dispenser
                // busy now, deposit when the craft finishes, then free it so redstone can power it again.
                AUTO_CRAFTING.add(loc);
                ItemStack finalOutput = output;
                Slimefun.runSync(() -> {
                    depositAutoCraftOutput(dispenser, finalOutput);
                    AUTO_CRAFTING.remove(loc);
                }, delay);
                return true;
            }
        }

        return false;
    }

    /** Dispenser locations with a redstone auto-craft currently in progress; blocks re-triggering. */
    private static final java.util.Set<Location> AUTO_CRAFTING = java.util.concurrent.ConcurrentHashMap.newKeySet();

    /**
     * Ticks a redstone auto-craft takes to complete before its output is ejected, mirroring the machine's
     * manual craft time. {@code 0} (default) ejects instantly (Enhanced Crafting Table); timed machines
     * (Armor Forge, Magic Workbench) override this so redstone crafting takes as long as crafting by hand,
     * and the dispenser cannot be re-powered until the current craft finishes.
     */
    protected int getAutoCraftDelayTicks() {
        return 0;
    }

    /**
     * Deposits a redstone auto-craft output: into an adjacent {@link OutputChest} if one can hold it (so
     * automated setups collect it, exactly like a manual craft), otherwise ejected out of the dispenser's
     * front. Never placed back into the dispenser, which would clog the recipe inputs and re-trigger.
     */
    private void depositAutoCraftOutput(@Nonnull Block dispenser, @Nonnull ItemStack output) {
        Optional<Inventory> chest = OutputChest.findOutputChestFor(dispenser, output);
        SlimefunItem sfItem = SlimefunItem.getByItem(output);
        String outputId = sfItem != null ? sfItem.getId() : String.valueOf(output.getType());

        if (chest.isPresent()) {
            chest.get().addItem(output);
            SoundEffect.ENHANCED_CRAFTING_TABLE_CRAFT_SOUND.playAt(dispenser);
            Slimefun.logger().info("[autocraft] " + getId() + " crafted " + outputId + " -> deposited into an adjacent output chest");
        } else {
            ejectOutput(dispenser, output);
            Slimefun.logger().info("[autocraft] " + getId() + " crafted " + outputId + " -> ejected out the dispenser front (no output chest found)");
        }
    }

    /**
     * Whether the given crafted output is allowed for the multiblock's owner at redstone time. This
     * is deliberately conservative: with no player present we only trust an already-loaded
     * {@link PlayerProfile}. If the item requires no (enabled) {@link Research} it is always allowed;
     * otherwise we require the owner's profile to be in memory AND to have the research unlocked -
     * "cannot confirm unlocked" is treated as "do not craft".
     */
    private boolean isUnlockedForOwner(@Nonnull ItemStack output, @Nonnull UUID owner) {
        SlimefunItem sfItem = SlimefunItem.getByItem(output);

        if (sfItem == null) {
            return true;
        }

        Research research = sfItem.getResearch();

        if (research == null || !research.isEnabled()) {
            // No research requirement (or researching disabled) - nothing to gate on.
            return true;
        }

        // Opt-in: let automated crafters ignore the research requirement (permission/world checks that
        // gate the machine itself still apply). Off by default, so auto-craft mirrors manual crafting.
        if (Slimefun.getCfg().getBoolean("auto-craft.bypass-research")) {
            return true;
        }

        // If the owner is online, mirror exactly what they could do by hand: canPlayerUseItem honours the
        // research AND their creative/op/permission bypass, so a redstone craft matches a manual one (a
        // player who can craft it manually can also automate it).
        Player online = Bukkit.getPlayer(owner);

        if (online != null) {
            return SlimefunUtils.canPlayerUseItem(online, output, false);
        }

        // Offline owner: only trust an already-loaded profile that has the research unlocked.
        Optional<PlayerProfile> profile = PlayerProfile.find(Bukkit.getOfflinePlayer(owner));
        return profile.isPresent() && profile.get().hasUnlocked(research);
    }

    /** Ejects the crafted item out of the dispenser's front, exactly like a vanilla dispenser: spawned
     *  at the facing face, launched in that direction, with the dispenser dispense sound. */
    protected void ejectOutput(@Nonnull Block dispenser, @Nonnull ItemStack output) {
        BlockFace facing = getDispenserFacing(dispenser);
        Location location = dispenser.getLocation().add(
            0.5 + facing.getModX() * 0.7,
            0.5 + facing.getModY() * 0.7,
            0.5 + facing.getModZ() * 0.7);

        dispenser.getWorld().dropItem(location, output)
            .setVelocity(new Vector(facing.getModX(), facing.getModY(), facing.getModZ()).multiply(0.25));
        // Played directly (not via SoundEffect/sounds.yml) so it works on servers whose sounds.yml
        // predates this sound and therefore has no configured entry for it.
        SoundCompat.playAt(dispenser.getLocation(), "BLOCK_DISPENSER_DISPENSE", SoundCategory.BLOCKS, 1F, 1F);
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
             * (which may leave behind empty buckets or glass bottles).
             * Slot locks are never consumed, so they must keep occupying the slot in the simulation too -
             * otherwise the output falsely "fits" and gets added to a still-full dispenser and lost.
             */
            if (stack != null && !isSlotLock(stack)) {
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
            // Carry the existing backpack's identity onto the upgraded output.
            PlayerBackpack.writeIdentity(output, id.get());
        } else {
            PlayerProfile.get(p, profile -> {
                int backpackId = profile.createBackpack(size).getId();
                PlayerBackpack.writeIdentity(output, p.getUniqueId() + "#" + backpackId);
            });
        }
    }

    private @Nonnull Optional<String> retrieveID(@Nullable ItemStack backpack, int size) {
        Optional<String> identity = PlayerBackpack.readIdentity(backpack);

        if (identity.isPresent()) {
            String[] idSplit = CommonPatterns.HASH.split(identity.get());

            if (idSplit.length == 2) {
                PlayerProfile.fromUUID(UUID.fromString(idSplit[0]), profile -> {
                    Optional<PlayerBackpack> optional = profile.getBackpack(Integer.parseInt(idSplit[1]));
                    optional.ifPresent(playerBackpack -> {
                        // Safety feature for Issue #3664
                        CompletableFuture<Void> future = playerBackpack.closeForAll();
                        future.thenRun(() -> playerBackpack.setSize(size));
                    });
                });
            }
        }

        return identity;
    }

}

