package io.github.thebusybiscuit.slimefun4.core.multiblocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.inventory.InvUtils;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.MultiBlockInteractionHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.OutputChest;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

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
    protected MultiBlockMachine(Category category, SlimefunItemStack item, ItemStack[] recipe, ItemStack[] machineRecipes, BlockFace trigger) {
        super(category, item, RecipeType.MULTIBLOCK, recipe);
        this.recipes = new ArrayList<>();
        this.displayRecipes = new ArrayList<>();
        this.displayRecipes.addAll(Arrays.asList(machineRecipes));
        this.multiblock = new MultiBlock(this, convertItemStacksToMaterial(recipe), trigger);

        registerDefaultRecipes(displayRecipes);
    }

    @ParametersAreNonnullByDefault
    protected MultiBlockMachine(Category category, SlimefunItemStack item, ItemStack[] recipe, BlockFace trigger) {
        this(category, item, recipe, new ItemStack[0], trigger);
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
        SlimefunPlugin.getRegistry().getMultiBlocks().add(multiblock);
    }

    @Override
    public void load() {
        super.load();

        for (ItemStack recipeItem : displayRecipes) {
            SlimefunItem item = SlimefunItem.getByItem(recipeItem);

            if (item == null || !item.isDisabled()) {
                recipes.add(new ItemStack[] { recipeItem });
            }
        }
    }

    protected @Nonnull MultiBlockInteractionHandler getInteractionHandler() {
        return (p, mb, b) -> {
            if (mb.equals(getMultiBlock())) {
                if (canUse(p, true) && SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.INTERACT_BLOCK)) {
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
