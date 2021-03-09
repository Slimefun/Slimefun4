package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dropper;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.Smeltery;
import io.papermc.lib.PaperLib;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link IgnitionChamber} is used to re-ignite a {@link Smeltery}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Smeltery
 *
 */
public class IgnitionChamber extends SimpleSlimefunItem<BlockBreakHandler> {

    @ParametersAreNonnullByDefault
    public IgnitionChamber(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public BlockBreakHandler getItemHandler() {
        /*
         * Explosions don't need explicit handling here.
         * The default of "destroy the dispenser and drop the contents" is
         * fine for our purposes already.
         */
        return new BlockBreakHandler(false, true) {

            @Override
            public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                // Fixes #2856 - Manually drop inventory contents
                Block b = e.getBlock();
                BlockState state = PaperLib.getBlockState(b, false).getState();

                if (state instanceof Dropper) {
                    for (ItemStack stack : ((Dropper) state).getInventory()) {
                        if (stack != null && !stack.getType().isAir()) {
                            drops.add(stack);
                        }
                    }
                }
            }
        };
    }

}
