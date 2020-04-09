package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockDispenseHandler;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BlockPlacer extends SimpleSlimefunItem<BlockDispenseHandler> {

    private ItemSetting<List<String>> blacklist = new ItemSetting<>("unplaceable-blocks", MaterialCollections.getAllUnbreakableBlocks().stream().map(Material::name).collect(Collectors.toList()));

    public BlockPlacer(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemSetting(blacklist);
    }

    @Override
    public BlockDispenseHandler getItemHandler() {
        return (e, dispenser, facedBlock, machine) -> {
            e.setCancelled(true);

            if (facedBlock.getType() == Material.AIR && e.getItem().getType().isBlock() && !isBlacklisted(e.getItem().getType())) {
                SlimefunItem sfItem = SlimefunItem.getByItem(e.getItem());

                if (sfItem != null) {
                    if (!SlimefunPlugin.getRegistry().getBlockHandlers().containsKey(sfItem.getID())) {
                        placeSlimefunBlock(sfItem, e.getItem(), facedBlock, dispenser);
                    }
                } else {
                    placeBlock(e.getItem(), facedBlock, dispenser);
                }
            }
        };
    }

    private boolean isBlacklisted(Material type) {
        for (String blockType : blacklist.getValue()) {
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
            if (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_14) && blockState instanceof BlockInventoryHolder) {
                ((BlockInventoryHolder) blockState).getInventory().setContents(((BlockInventoryHolder) itemBlockState).getInventory().getContents());
            }

        }

        facedBlock.getWorld().playEffect(facedBlock.getLocation(), Effect.STEP_SOUND, item.getType());

        if (dispenser.getInventory().containsAtLeast(item, 2)) {
            dispenser.getInventory().removeItem(new CustomItem(item, 1));
        } else {
            Slimefun.runSync(() -> removeItems(dispenser, item));
        }
    }

    private void removeItems(Dispenser dispenser, ItemStack itemStack) {
        Map<Integer, ItemStack> unRemovedItems = dispenser.getInventory().removeItem(itemStack);
        Inventory inv = dispenser.getInventory();

        if (!unRemovedItems.isEmpty()) {
            unRemovedItems.forEach((k, v) -> {
                int size = itemStack.getAmount();
                for (int i = 0; i < inv.getSize(); i++) {
                    if (v.getType() == itemStack.getType()) {
                        if (size > 0) {
                            inv.setItem(i, null);
                            size--;
                        } else {
                            break;
                        }
                    }
                }
            });
        }
    }
}