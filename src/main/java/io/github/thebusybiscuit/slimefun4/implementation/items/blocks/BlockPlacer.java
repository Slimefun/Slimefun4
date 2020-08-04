package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.slimefun4.api.events.BlockPlacerPlaceEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockDispenseHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link BlockPlacer} is a machine which can place {@link Block Blocks}, as the name
 * would suggest.
 * It really just is a special type of {@link Dispenser} which places items instead of
 * shooting them.
 * 
 * @author TheBusyBiscuit
 * 
 * @see BlockPlacerPlaceEvent
 *
 */
public class BlockPlacer extends SimpleSlimefunItem<BlockDispenseHandler> {

    private final ItemSetting<List<String>> blacklist = new ItemSetting<>("unplaceable-blocks", MaterialCollections.getAllUnbreakableBlocks().stream().map(Material::name).collect(Collectors.toList()));

    public BlockPlacer(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemSetting(blacklist);
    }

    @Override
    public BlockDispenseHandler getItemHandler() {
        return (e, dispenser, facedBlock, machine) -> {
            if (isShulkerBox(e.getItem().getType())) {
                // Since vanilla Dispensers can already place Shulker boxes, we
                // simply fallback to the vanilla behaviour.
                return;
            }

            e.setCancelled(true);

            if (facedBlock.isEmpty() && e.getItem().getType().isBlock() && !isBlacklisted(e.getItem().getType())) {
                SlimefunItem item = SlimefunItem.getByItem(e.getItem());

                if (item != null) {
                    // Check if this Item can even be placed down
                    if (!(item instanceof NotPlaceable)) {
                        placeSlimefunBlock(item, e.getItem(), facedBlock, dispenser);
                    }
                }
                else {
                    placeBlock(e.getItem(), facedBlock, dispenser);
                }
            }
        };
    }

    private boolean isShulkerBox(Material type) {
        return type == Material.SHULKER_BOX || type.name().endsWith("_SHULKER_BOX");
    }

    private boolean isBlacklisted(Material type) {
        for (String blockType : blacklist.getValue()) {
            if (type.toString().equals(blockType)) {
                return true;
            }
        }

        return false;
    }

    private void placeSlimefunBlock(SlimefunItem sfItem, ItemStack item, Block block, Dispenser dispenser) {
        BlockPlacerPlaceEvent e = new BlockPlacerPlaceEvent(dispenser.getBlock(), item, block);
        Bukkit.getPluginManager().callEvent(e);

        if (!e.isCancelled()) {
            boolean hasItemHandler = sfItem.callItemHandler(BlockPlaceHandler.class, handler -> {
                if (handler.isBlockPlacerAllowed()) {
                    block.setType(item.getType());
                    block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, item.getType());

                    BlockStorage.store(block, sfItem.getID());
                    handler.onBlockPlacerPlace(e);

                    if (dispenser.getInventory().containsAtLeast(item, 2)) {
                        dispenser.getInventory().removeItem(new CustomItem(item, 1));
                    }
                    else {
                        Slimefun.runSync(() -> dispenser.getInventory().removeItem(item), 2L);
                    }
                }
            });

            if (!hasItemHandler) {
                block.setType(item.getType());
                block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, item.getType());

                BlockStorage.store(block, sfItem.getID());

                if (dispenser.getInventory().containsAtLeast(item, 2)) {
                    dispenser.getInventory().removeItem(new CustomItem(item, 1));
                }
                else {
                    Slimefun.runSync(() -> dispenser.getInventory().removeItem(item), 2L);
                }
            }
        }
    }

    private void placeBlock(ItemStack item, Block facedBlock, Dispenser dispenser) {
        BlockPlacerPlaceEvent e = new BlockPlacerPlaceEvent(dispenser.getBlock(), item, facedBlock);
        Bukkit.getPluginManager().callEvent(e);

        if (!e.isCancelled()) {
            facedBlock.setType(item.getType());

            if (item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();

                if (meta.hasDisplayName()) {
                    BlockState blockState = facedBlock.getState();

                    if ((blockState instanceof Nameable)) {
                        ((Nameable) blockState).setCustomName(meta.getDisplayName());
                    }

                    // Update block state after changing name
                    blockState.update();
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
    }
}
