package io.github.thebusybiscuit.slimefun5.core.multiblocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;

import org.apache.commons.lang.Validate;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import io.github.bakedlibs.dough.inventory.InvUtils;
import io.github.bakedlibs.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun5.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.ItemSpawnReason;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.api.researches.Research;
import io.github.thebusybiscuit.slimefun5.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun5.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun5.core.handlers.MultiBlockInteractionHandler;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.items.blocks.OutputChest;
import io.github.thebusybiscuit.slimefun5.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.BlockDataCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.SoundCategory;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.SoundCompat;
import io.papermc.lib.PaperLib;

/**
 * A {@link MultiBlockMachine} is a {@link SlimefunItem} that is built in the {@link World}.
 * It holds recipes and a {@link MultiBlock} object which represents its structure.
 * 
 * @author TheBusyBiscuit
 * 
 * @see MultiBlock
 *
 */
public abstract class MultiBlockMachine extends SlimefunItem implements NotPlaceable, RecipeDisplayItem {

    protected final List<ItemStack[]> recipes;
    protected final List<ItemStack> displayRecipes;
    protected final MultiBlock multiblock;

    @ParametersAreNonnullByDefault
    protected MultiBlockMachine(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe, ItemStack[] machineRecipes, BlockFace trigger) {
        super(itemGroup, item, RecipeType.MULTIBLOCK, recipe);
        this.recipes = new ArrayList<>();
        this.displayRecipes = new ArrayList<>();
        this.displayRecipes.addAll(Arrays.asList(machineRecipes));
        this.multiblock = new MultiBlock(this, convertItemStacksToMaterial(recipe), trigger);

        registerDefaultRecipes(displayRecipes);
    }

    @ParametersAreNonnullByDefault
    protected MultiBlockMachine(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe, BlockFace trigger) {
        this(itemGroup, item, recipe, new ItemStack[0], trigger);
    }

    protected void registerDefaultRecipes(@Nonnull List<ItemStack> recipes) {
        // Override this method to register some default recipes
    }

    public @Nonnull List<ItemStack[]> getRecipes() {
        return recipes;
    }

    @Override
    public @Nonnull List<ItemStack> getDisplayRecipes() {
        return displayRecipes;
    }

    public @Nonnull MultiBlock getMultiBlock() {
        return multiblock;
    }

    public void addRecipe(ItemStack[] input, ItemStack output) {
        Validate.notNull(output, "Recipes must have an Output!");

        recipes.add(input);
        recipes.add(new ItemStack[] { output });
    }

    @Override
    public void register(@Nonnull SlimefunAddon addon) {
        addItemHandler(getInteractionHandler());
        super.register(addon);
    }

    @Override
    public void postRegister() {
        Slimefun.getRegistry().getMultiBlocks().add(multiblock);
    }

    @Override
    public void load() {
        super.load();

        Preconditions.checkArgument(displayRecipes.size() % 2 == 0, "This MultiBlockMachine's display recipes were illegally modified!");

        for (int i = 0; i < displayRecipes.size(); i += 2) {
            ItemStack inputStack = displayRecipes.get(i);
            ItemStack outputStack = null;
            if (displayRecipes.size() >= i + 2) {
                outputStack = displayRecipes.get(i + 1);
            }

            SlimefunItem inputItem = SlimefunItem.getByItem(inputStack);
            SlimefunItem outputItem = SlimefunItem.getByItem(outputStack);
            // If the input/output is not a Slimefun item or it's not disabled then it's valid.
            if ((inputItem == null || !inputItem.isDisabled()) && (outputItem == null || !outputItem.isDisabled())) {
                recipes.add(new ItemStack[] { inputStack });
                recipes.add(new ItemStack[] { outputStack });
            }
        }
    }

    protected @Nonnull MultiBlockInteractionHandler getInteractionHandler() {
        return (p, mb, b) -> {
            if (mb.equals(getMultiBlock())) {
                if (canUse(p, true) && Slimefun.getProtectionManager().hasPermission(p, b.getLocation(), Interaction.INTERACT_BLOCK)) {
                    onInteract(p, b);
                }

                return true;
            } else {
                return false;
            }
        };
    }

    public abstract void onInteract(Player p, Block b);

    /**
     * Overloaded method for finding a potential output chest.
     * Fallbacks to the old system of putting the adding back into the dispenser.
     * Optional last argument Inventory placeCheckerInv is for a {@link MultiBlockMachine} that create
     * a dummy inventory to check if there's a space for the adding, i.e. Enhanced crafting table
     * 
     * @param adding
     *            The {@link ItemStack} that should be added
     * @param dispBlock
     *            The {@link Block} of our {@link Dispenser}
     * @param dispInv
     *            The {@link Inventory} of our {@link Dispenser}
     * 
     * @return The target {@link Inventory}
     */

    @ParametersAreNonnullByDefault
    protected @Nullable Inventory findOutputInventory(ItemStack adding, Block dispBlock, Inventory dispInv) {
        return findOutputInventory(adding, dispBlock, dispInv, dispInv);
    }

    @ParametersAreNonnullByDefault
    protected @Nullable Inventory findOutputInventory(ItemStack product, Block dispBlock, Inventory dispInv, Inventory placeCheckerInv) {
        Optional<Inventory> outputChest = OutputChest.findOutputChestFor(dispBlock, product);

        /*
         * This if-clause will trigger if no suitable output chest was found.
         * It's functionally the same as the old fit check for the dispenser,
         * only refactored.
         */
        if (!outputChest.isPresent() && InvUtils.fits(placeCheckerInv, product)) {
            return dispInv;
        } else {
            return outputChest.orElse(null);
        }
    }

    /**
     * This method handles an output {@link ItemStack} from the {@link MultiBlockMachine} which has a crafting delay
     *
     * @param outputItem
     *            A crafted {@link ItemStack} from {@link MultiBlockMachine}
     * @param block
     *            Main {@link Block} of our {@link Container} from {@link MultiBlockMachine}
     * @param blockInv
     *            The {@link Inventory} of our {@link Container}
     *
     */
    @ParametersAreNonnullByDefault
    protected void handleCraftedItem(ItemStack outputItem, Block block, Inventory blockInv) {
        Inventory outputInv = findOutputInventory(outputItem, block, blockInv);

        if (outputInv != null) {
            outputInv.addItem(outputItem);
        } else {
            ItemStack rest = blockInv.addItem(outputItem).get(0);

            // fallback: drop item
            if (rest != null) {
                SlimefunUtils.spawnItem(block.getLocation(), rest, ItemSpawnReason.MULTIBLOCK_MACHINE_OVERFLOW, true);
            }
        }
    }

    /**
     * Performs a single craft headlessly (no {@link Player}, no permission prompt, no
     * {@link io.github.thebusybiscuit.slimefun5.api.events.MultiBlockCraftEvent}) directly from a powered
     * dispenser, ejecting the result out of the dispenser's front (or into an adjacent {@link OutputChest}).
     * This backs the redstone auto-craft feature for every {@link MultiBlockMachine} whose structure
     * contains a dispenser holding the recipe inputs.
     * <p>
     * Matching is shapeless - the dispenser must contain every ingredient of some recipe in sufficient
     * quantity - which covers both single-input machines (Ore Crusher, Compressor, ...) and multi-input
     * ones (Smeltery). {@link AbstractCraftingTable} overrides this with its own shaped 3x3 matcher.
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

        Inventory inv = ((Dispenser) state).getInventory();

        for (ItemStack[] input : RecipeType.getRecipeInputList(this)) {
            if (!dispenserContainsAll(inv, input)) {
                continue;
            }

            ItemStack output = RecipeType.getRecipeOutputList(this, input);

            if (output == null) {
                continue;
            }

            output = output.clone();

            // Gate on the owner's research/permission: only auto-craft what the owner could craft by hand.
            if (!isAutoCraftUnlockedForOwner(output, owner)) {
                return false;
            }

            // Consume each ingredient by the amount the recipe requires.
            for (ItemStack removing : input) {
                if (removing != null) {
                    InvUtils.removeItem(inv, removing.getAmount(), true, stack -> SlimefunUtils.isItemSimilar(stack, removing, true));
                }
            }

            depositAutoCraftOutput(dispenser, output);
            SoundCompat.playAt(dispenser.getLocation(), "BLOCK_DISPENSER_DISPENSE", SoundCategory.BLOCKS, 0.5F, 1F);
            return true;
        }

        return false;
    }

    /**
     * Whether the dispenser holds every non-null ingredient of the recipe in sufficient quantity. Works on a
     * deducting copy of the contents so an ingredient that appears twice in one recipe needs twice the count.
     */
    private boolean dispenserContainsAll(@Nonnull Inventory inv, @Nonnull ItemStack[] recipe) {
        List<ItemStack> pool = new ArrayList<>();

        for (ItemStack content : inv.getContents()) {
            if (content != null && content.getType() != Material.AIR) {
                pool.add(content.clone());
            }
        }

        for (ItemStack expected : recipe) {
            if (expected == null) {
                continue;
            }

            int needed = expected.getAmount();

            for (ItemStack available : pool) {
                if (needed <= 0) {
                    break;
                }

                if (available.getAmount() > 0 && SlimefunUtils.isItemSimilar(available, expected, true)) {
                    int taken = Math.min(needed, available.getAmount());
                    available.setAmount(available.getAmount() - taken);
                    needed -= taken;
                }
            }

            if (needed > 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Deposits an auto-craft output: into an adjacent {@link OutputChest} if one can hold it, otherwise
     * ejected out of the dispenser's front - never back into the dispenser, which would clog the inputs.
     */
    private void depositAutoCraftOutput(@Nonnull Block dispenser, @Nonnull ItemStack output) {
        Optional<Inventory> chest = OutputChest.findOutputChestFor(dispenser, output);

        if (chest.isPresent()) {
            chest.get().addItem(output);
        } else {
            BlockFace facing = getDispenserFacing(dispenser);
            Location location = dispenser.getLocation().add(
                0.5 + facing.getModX() * 0.7,
                0.5 + facing.getModY() * 0.7,
                0.5 + facing.getModZ() * 0.7);

            dispenser.getWorld().dropItem(location, output)
                .setVelocity(new Vector(facing.getModX(), facing.getModY(), facing.getModZ()).multiply(0.25));
        }
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

    /**
     * Whether the given crafted output is allowed for the multiblock's owner at redstone time. Conservative:
     * with no player present, an item needing an enabled {@link Research} is only allowed if the owner is
     * online and could craft it, or an already-loaded profile has the research unlocked ("cannot confirm
     * unlocked" is treated as "do not craft"). The {@code auto-craft.bypass-research} config opts out.
     */
    private boolean isAutoCraftUnlockedForOwner(@Nonnull ItemStack output, @Nonnull UUID owner) {
        SlimefunItem sfItem = SlimefunItem.getByItem(output);

        if (sfItem == null) {
            return true;
        }

        Research research = sfItem.getResearch();

        if (research == null || !research.isEnabled() || Slimefun.getCfg().getBoolean("auto-craft.bypass-research")) {
            return true;
        }

        Player online = Bukkit.getPlayer(owner);

        if (online != null) {
            return SlimefunUtils.canPlayerUseItem(online, output, false);
        }

        Optional<PlayerProfile> profile = PlayerProfile.find(Bukkit.getOfflinePlayer(owner));
        return profile.isPresent() && profile.get().hasUnlocked(research);
    }

    private static @Nonnull Material[] convertItemStacksToMaterial(@Nonnull ItemStack[] items) {
        List<Material> materials = new ArrayList<>();

        for (ItemStack item : items) {
            if (item == null) {
                materials.add(null);
            } else if (item.getType() == Material.FLINT_AND_STEEL) {
                materials.add(Material.FIRE);
            } else {
                materials.add(item.getType());
            }
        }

        return materials.toArray(new Material[0]);
    }

}

