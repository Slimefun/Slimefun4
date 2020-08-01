package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockDispenseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

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

            if ((facedBlock.getType() == null || facedBlock.getType() == Material.AIR) && e.getItem().getType().isBlock() && !isBlacklisted(e.getItem().getType())) {
                SlimefunItem item = SlimefunItem.getByItem(e.getItem());

                if (item != null) {
                    // Check if this Item can even be placed down
                    if (!(item instanceof NotPlaceable) && !SlimefunPlugin.getRegistry().getBlockHandlers().containsKey(item.getID())) {
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
        block.setType(item.getType());
        BlockStorage.store(block, sfItem.getID());
        block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, item.getType());

        if (item.getType() == Material.SPAWNER && sfItem instanceof RepairedSpawner) {
            Optional<EntityType> entity = ((RepairedSpawner) sfItem).getEntityType(item);

            if (entity.isPresent()) {
                CreatureSpawner spawner = (CreatureSpawner) block.getState();
                spawner.setSpawnedType(entity.get());
                spawner.update(true, false);
            }
        }

        if (dispenser.getInventory().containsAtLeast(item, 2)) {
            dispenser.getInventory().removeItem(new CustomItem(item, 1));
        }
        else {
            Slimefun.runSync(() -> dispenser.getInventory().removeItem(item), 2L);
        }
    }

    private void placeBlock(ItemStack item, Block facedBlock, Dispenser dispenser) {
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
