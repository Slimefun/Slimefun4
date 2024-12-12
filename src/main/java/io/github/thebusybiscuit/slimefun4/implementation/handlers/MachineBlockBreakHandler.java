package io.github.thebusybiscuit.slimefun4.implementation.handlers;

import io.github.thebusybiscuit.slimefun4.core.machines.MachineOperation;
import io.github.thebusybiscuit.slimefun4.core.machines.MachineProcessor;
import io.github.thebusybiscuit.slimefun4.implementation.operations.CraftingOperation;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * A {@link SimpleBlockBreakHandler} given the processor and the slots to drop of a machine.
 *  Uses the machine's {@link MachineProcessor} to drop the items that didn't finish processing for crafting recipes
 *  The slots field is used to drop items in those slots of block's inventory.
 *
 * @author Vaan1310/Intybyte
 *
 */
public class MachineBlockBreakHandler extends SimpleBlockBreakHandler {
    private final MachineProcessor<? extends MachineOperation> processor;
    private final int[][] slots;

    public MachineBlockBreakHandler(MachineProcessor<? extends MachineOperation> processor, int[]... slots) {
        this.processor = processor;
        this.slots = slots;
    }

    public MachineBlockBreakHandler(int[]... slots) {
        this(null, slots);
    }

    protected void beforeBreak(@Nonnull Block b) {

    }

    @Override
    public final void onBlockBreak(@Nonnull Block b) {
        beforeBreak(b);
        BlockMenu inv = BlockStorage.getInventory(b);
        Location loc = b.getLocation();

        // Check if the world where we might want to drop stuff is valid
        World world = loc.getWorld();
        if (world == null) {
            if (processor != null) {
                processor.endOperation(b);
            }
            afterBreak(b);
            return;
        }

        if (inv != null) {
            for (int[] slotArray : slots) {
                inv.dropItems(loc, slotArray);
            }
        }

        if (processor == null) {
            afterBreak(b);
            return;
        }

        var operation = processor.getOperation(b);
        if (operation == null) {
            afterBreak(b);
            return;
        }

        // if the processed operation is a crafting recipe give back recipe ingredients, Fixes #4268
        if (!(operation instanceof CraftingOperation craftingOperation)) {
            processor.endOperation(b);
            afterBreak(b);
            return;
        }

        ItemStack[] ingredients = craftingOperation.getIngredients();
        for (ItemStack ingredient : ingredients) {
            world.dropItemNaturally(loc, ingredient);
        }

        processor.endOperation(b);
        afterBreak(b);
    }

    protected void afterBreak(@Nonnull Block b) {

    }
}
