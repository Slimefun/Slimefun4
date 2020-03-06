package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockDispenseHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class BlockPlacer extends SimpleSlimefunItem<BlockDispenseHandler> {

    private String[] blacklist;

    public BlockPlacer(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String[] keys, Object[] values) {
        super(category, item, recipeType, recipe, keys, values);
    }

    @Override
    public BlockDispenseHandler getItemHandler() {
        return (e, dispenser, facedBlock, machine) -> {
            e.setCancelled(true);

            if ((facedBlock.getType() == null || facedBlock.getType() == Material.AIR) && e.getItem().getType().isBlock() && !isBlacklisted(e.getItem().getType())) {
                SlimefunItem sfItem = SlimefunItem.getByItem(e.getItem());

                if (sfItem != null) {
                    if (!SlimefunPlugin.getRegistry().getBlockHandlers().containsKey(sfItem.getID())) {
                        placeSlimefunBlock(sfItem, e.getItem(), facedBlock, dispenser);
                    }
                }
                else {
                    placeBlock(e.getItem(), facedBlock, dispenser);
                }
            }
        };
    }

    private boolean isBlacklisted(Material type) {
        for (String blockType : blacklist) {
            if (type.toString().equals(blockType)) {
                return true;
            }
        }

        return false;
    }

    private void placeSlimefunBlock(SlimefunItem sfItem, ItemStack item, Block facedBlock, Dispenser dispenser) {
        facedBlock.setType(item.getType());
        BlockStorage.store(facedBlock, sfItem.getID());
        facedBlock.getWorld().playEffect(facedBlock.getLocation(), Effect.STEP_SOUND, item.getType());

        if (dispenser.getInventory().containsAtLeast(item, 2)) {
            dispenser.getInventory().removeItem(new CustomItem(item, 1));
        }
        else {
            Slimefun.runSync(() -> dispenser.getInventory().removeItem(item), 2L);
        }
    }

    private void placeBlock(ItemStack item, Block facedBlock, Dispenser dispenser) {
        facedBlock.setType(item.getType());

        if (item.hasItemMeta() && item.getItemMeta() instanceof BlockStateMeta) {
            BlockState itemBlockState = ((BlockStateMeta) item.getItemMeta()).getBlockState();
            BlockState blockState = facedBlock.getState();

            if ((blockState instanceof Nameable) && item.getItemMeta().hasDisplayName()) {
                ((Nameable) blockState).setCustomName(item.getItemMeta().getDisplayName());
            }

            // Update block state after changing name
            blockState.update();

            // Changing the inventory of the block based on the inventory of the block's itemstack (Currently only
            // applies to shulker boxes)
            // Inventory has to be changed after blockState.update() as updating it will create a different Inventory
            // for the object
            if (facedBlock.getState() instanceof BlockInventoryHolder) {
                ((BlockInventoryHolder) facedBlock.getState()).getInventory().setContents(((BlockInventoryHolder) itemBlockState).getInventory().getContents());
            }

        }

        facedBlock.getWorld().playEffect(facedBlock.getLocation(), Effect.STEP_SOUND, item.getType());

        if (dispenser.getInventory().containsAtLeast(item, 2)) {
            dispenser.getInventory().removeItem(new CustomItem(item, 1));
        }
        else {
            Slimefun.runSync(() -> dispenser.getInventory().removeItem(item), 2L);
        }
    }

    @Override
    public void postRegister() {
        List<?> list = (List<?>) Slimefun.getItemValue(getID(), "unplaceable-blocks");
        blacklist = list.toArray(new String[0]);
    }
}
