package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.papermc.lib.PaperLib;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link OutputChest} can be used to capture the output items from a {@link MultiBlockMachine}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see MultiBlockMachine
 *
 */
public class OutputChest extends SimpleSlimefunItem<BlockBreakHandler> {

    @ParametersAreNonnullByDefault
    public OutputChest(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public BlockBreakHandler getItemHandler() {
        /*
         * Explosions don't need explicit handling here.
         * The default of "destroy the chest and drop the contents" is
         * fine for our purposes already.
         */
        return new BlockBreakHandler(false, true) {

            @Override
            public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                // Fixes #2849 - Manually drop inventory contents
                Block b = e.getBlock();
                BlockState state = PaperLib.getBlockState(b, false).getState();

                if (state instanceof InventoryHolder) {
                    for (ItemStack stack : ((InventoryHolder) state).getInventory()) {
                        if (stack != null && !stack.getType().isAir()) {
                            drops.add(stack);
                        }
                    }
                }
            }
        };
    }

}
