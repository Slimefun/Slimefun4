package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link EnhancedFurnace} is an upgraded version of a {@link Furnace}.
 * It has a custom speed, efficiency and also a level of fortune.
 * All of these values are tweaked for every instance of this class.
 * 
 * It uses a {@link BlockTicker} to manipulate the {@link Furnace} into working faster.
 * 
 * @author TheBusyBiscuit
 *
 */
public class EnhancedFurnace extends SimpleSlimefunItem<BlockTicker> {

    private final int speed;
    private final int efficiency;
    private final int fortune;

    public EnhancedFurnace(Category category, int speed, int efficiency, int fortune, SlimefunItemStack item, ItemStack[] recipe) {
        super(category, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);

        this.speed = speed - 1;
        this.efficiency = efficiency - 1;
        this.fortune = fortune - 1;
    }

    public int getSpeed() {
        return speed;
    }

    public int getFuelEfficiency() {
        return efficiency;
    }

    public int getOutput() {
        int bonus = this.fortune;
        bonus = ThreadLocalRandom.current().nextInt(bonus + 2) - 1;
        if (bonus <= 0) bonus = 0;
        bonus++;
        return bonus;
    }

    @Override
    public BlockTicker getItemHandler() {
        return new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                if (b.getType() != Material.FURNACE) {
                    // The Furnace has been destroyed, we can clear the block data
                    BlockStorage.clearBlockInfo(b);
                }
                else {
                    Furnace furnace = (Furnace) b.getState();

                    if (furnace.getCookTime() > 0) {
                        int cookTime = furnace.getCookTime() + getSpeed() * 10;

                        furnace.setCookTime((short) Math.min(cookTime, furnace.getCookTimeTotal() - 1));
                        furnace.update(true, false);
                    }
                }
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        };
    }
}
