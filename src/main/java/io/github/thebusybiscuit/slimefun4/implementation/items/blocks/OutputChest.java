package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.inventory.InvUtils;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.handlers.VanillaInventoryDropHandler;
import io.papermc.lib.PaperLib;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link OutputChest} can be used to capture the output items from a {@link MultiBlockMachine}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see MultiBlockMachine
 *
 */
public class OutputChest extends SlimefunItem {

    // @formatter:off
    private static final BlockFace[] possibleFaces = {
        BlockFace.UP,
        BlockFace.NORTH,
        BlockFace.EAST,
        BlockFace.SOUTH,
        BlockFace.WEST
    };
    // @formatter:on

    @ParametersAreNonnullByDefault
    public OutputChest(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemHandler(new VanillaInventoryDropHandler<>(Chest.class));
    }

    @Nonnull
    public static Optional<Inventory> findOutputChestFor(@Nonnull Block b, @Nonnull ItemStack item) {
        for (BlockFace face : possibleFaces) {
            Block potentialOutput = b.getRelative(face);

            // Check if the target block is a Chest
            if (potentialOutput.getType() == Material.CHEST) {
                SlimefunItem slimefunItem = BlockStorage.check(potentialOutput);

                // Fixes #3012 - Check if the OutputChest is not disabled here.
                if (slimefunItem instanceof OutputChest && !slimefunItem.isDisabledIn(b.getWorld())) {
                    // Found the output chest! Now, let's check if we can fit the product in it.
                    BlockState state = PaperLib.getBlockState(potentialOutput, false).getState();

                    if (state instanceof Chest) {
                        Inventory inv = ((Chest) state).getInventory();

                        // Check if the Item fits into that inventory.
                        if (InvUtils.fits(inv, item)) {
                            return Optional.of(inv);
                        }
                    }
                }
            }
        }

        return Optional.empty();
    }

}
