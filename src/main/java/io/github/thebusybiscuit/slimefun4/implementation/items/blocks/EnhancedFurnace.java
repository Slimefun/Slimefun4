package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.TickingBlock;
import io.github.thebusybiscuit.slimefun4.core.attributes.TickingMethod;
import io.papermc.lib.PaperLib;
import io.papermc.lib.features.blockstatesnapshot.BlockStateSnapshotResult;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link EnhancedFurnace} is an upgraded version of a {@link Furnace}.
 * It has a custom speed, efficiency and also a level of fortune.
 * All of these values are tweaked for every instance of this class.
 * 
 * It uses {@link TickingBlock} to manipulate the {@link Furnace} into working faster.
 * 
 * @author TheBusyBiscuit
 *
 */
public class EnhancedFurnace extends SlimefunItem implements TickingBlock {

    private final int speed;
    private final int efficiency;
    private final int fortuneLevel;

    @ParametersAreNonnullByDefault
    public EnhancedFurnace(Category category, int speed, int efficiency, int fortune, SlimefunItemStack item, ItemStack[] recipe) {
        super(category, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);

        this.speed = speed - 1;
        this.efficiency = efficiency - 1;
        this.fortuneLevel = fortune - 1;
    }

    /**
     * This returns the processing speed of this {@link EnhancedFurnace}.
     * 
     * @return The processing speed
     */
    public int getProcessingSpeed() {
        return speed;
    }

    /**
     * This returns the fuel efficiency of this {@link EnhancedFurnace}.
     * The fuel efficiency is a multiplier that is applied to any fuel burnt in this {@link EnhancedFurnace}.
     * 
     * @return The fuel multiplier
     */
    public int getFuelEfficiency() {
        return efficiency;
    }

    public int getRandomOutputAmount() {
        int bonus = ThreadLocalRandom.current().nextInt(fortuneLevel + 2);
        return 1 + bonus;
    }

    @Override
    public TickingMethod getTickingMethod() {
        return TickingMethod.MAIN_THREAD;
    }

    @Override
    public void tick(@Nonnull Block b) {
        if (b.getType() != Material.FURNACE) {
            // The Furnace has been destroyed, we can clear the block data
            BlockStorage.clearBlockInfo(b);
        } else {
            BlockStateSnapshotResult result = PaperLib.getBlockState(b, false);
            BlockState state = result.getState();

            // Check if the BlockState is a Furnace and cooking something
            if (state instanceof Furnace && ((Furnace) state).getCookTime() > 0) {
                setProgress((Furnace) state);

                // Only update if necessary
                if (result.isSnapshot()) {
                    state.update(true, false);
                }
            }
        }
    }

    private void setProgress(@Nonnull Furnace furnace) {
        // Update the cooktime
        int cookTime = furnace.getCookTime() + getProcessingSpeed() * 10;
        furnace.setCookTime((short) Math.min(cookTime, furnace.getCookTimeTotal() - 1));
    }
}
