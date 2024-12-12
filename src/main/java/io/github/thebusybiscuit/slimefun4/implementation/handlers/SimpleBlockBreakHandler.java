package io.github.thebusybiscuit.slimefun4.implementation.handlers;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.github.thebusybiscuit.slimefun4.core.machines.MachineOperation;
import io.github.thebusybiscuit.slimefun4.core.machines.MachineProcessor;
import io.github.thebusybiscuit.slimefun4.implementation.operations.CraftingOperation;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.AndroidMineEvent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.androids.MinerAndroid;

/**
 * This simple implementation of {@link BlockBreakHandler} will execute the same code
 * for when the {@link Block} is broken by a {@link Player}, by a {@link MinerAndroid} or an explosion.
 * By default, both androids and explosions are allowed.
 * 
 * @author TheBusyBiscuit
 * @author Vaan1310/Intybyte
 * 
 * @see BlockBreakHandler
 *
 */
public abstract class SimpleBlockBreakHandler extends BlockBreakHandler {

    /**
     * This constructs a new {@link SimpleBlockBreakHandler}.
     */
    protected SimpleBlockBreakHandler() {
        super(true, true);
    }

    /**
     * This method is called when a {@link Block} of this type is broken by a {@link Player},
     * by a {@link MinerAndroid} or through an explosion.
     * 
     * @param b
     *            The broken {@link Block}
     */
    public abstract void onBlockBreak(@Nonnull Block b);

    @Override
    public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
        onBlockBreak(e.getBlock());
    }

    @Override
    public void onAndroidBreak(AndroidMineEvent e) {
        onBlockBreak(e.getBlock());
    }

    @Override
    public void onExplode(Block b, List<ItemStack> drops) {
        onBlockBreak(b);
    }

    /**
     * Creates a {@link SimpleBlockBreakHandler} given the processor and the slots to drop of a machine.
     *
     * @param processor the machine's {@link MachineProcessor}, used to drop the items that didn't finish processing for crafting recipes
     * @param slots which slots of the inventory to drop on block break
     */
    public static SimpleBlockBreakHandler of(@Nullable MachineProcessor<? extends MachineOperation> processor, int[]... slots) {
        return new SimpleBlockBreakHandler() {
            @Override
            public void onBlockBreak(@Nonnull Block b) {
                BlockMenu inv = BlockStorage.getInventory(b);
                Location loc = b.getLocation();
                if (inv != null) {
                    for (int[] slotArray : slots) {
                        inv.dropItems(loc, slotArray);
                    }
                }

                if (processor == null) {
                    return;
                }

                var operation = processor.getOperation(b);
                if (operation == null) {
                    return;
                }

                if (operation instanceof CraftingOperation craftingOperation) {
                    var ingredients = craftingOperation.getIngredients();
                    World world = loc.getWorld();

                    if (world == null) {
                        processor.endOperation(b);
                        return;
                    }

                    for (ItemStack ingredient : ingredients) {
                        world.dropItemNaturally(loc, ingredient);
                    }
                }

                processor.endOperation(b);
            }
        };
    }
}
