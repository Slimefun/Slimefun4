package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.api.events.BlockPlacerPlaceEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.settings.MaterialTagSetting;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockDispenseHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import io.papermc.lib.PaperLib;
import io.papermc.lib.features.blockstatesnapshot.BlockStateSnapshotResult;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
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
public class BlockPlacer extends SlimefunItem {

    private final ItemSetting<List<String>> blacklist = new MaterialTagSetting("unplaceable-blocks", SlimefunTag.UNBREAKABLE_MATERIALS);

    public BlockPlacer(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemSetting(blacklist);
        addItemHandler(onPlace(), onBlockDispense());
    }

    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                Player p = e.getPlayer();
                Block b = e.getBlock();

                BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());
            }
        };
    }

    private BlockDispenseHandler onBlockDispense() {
        return (e, dispenser, facedBlock, machine) -> {
            if (!hasPermission(dispenser, facedBlock)) {
                e.setCancelled(true);
                return;
            }

            Material material = e.getItem().getType();

            if (SlimefunTag.SHULKER_BOXES.isTagged(material)) {
                // Since vanilla Dispensers can already place Shulker boxes, we
                // simply fallback to the vanilla behaviour.
                return;
            }

            e.setCancelled(true);

            if (!material.isBlock() || SlimefunTag.BLOCK_PLACER_IGNORED_MATERIALS.isTagged(material)) {
                // Some materials cannot be reliably placed, like beds, it would look
                // kinda wonky, so we just ignore these altogether.
                // The event has already been cancelled too, so they won't drop.
                return;
            }

            if (facedBlock.isEmpty() && !isBlacklisted(material)) {
                SlimefunItem item = SlimefunItem.getByItem(e.getItem());

                if (item != null) {
                    // Check if this Item can even be placed down
                    if (!(item instanceof NotPlaceable)) {
                        placeSlimefunBlock(item, e.getItem(), facedBlock, dispenser);
                    }
                } else {
                    placeBlock(e.getItem(), facedBlock, dispenser);
                }
            }
        };
    }

    /**
     * This checks whether the {@link Player} who placed down this {@link BlockPlacer} has
     * building permissions at that {@link Location}.
     * 
     * @param dispenser
     *            The {@link Dispenser} who represents our {@link BlockPlacer}
     * @param target
     *            The {@link Block} where it should be placed
     * 
     * @return Whether this action is permitted or not
     */
    private boolean hasPermission(Dispenser dispenser, Block target) {
        String owner = BlockStorage.getLocationInfo(dispenser.getLocation(), "owner");

        if (owner == null) {
            // If no owner was set, then we will fallback to the previous behaviour:
            // Allowing block placers to bypass protection, newly placed Block placers
            // will respect protection plugins.
            return true;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(owner));
        return SlimefunPlugin.getProtectionManager().hasPermission(player, target, ProtectableAction.PLACE_BLOCK);
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

                    BlockStorage.store(block, sfItem.getId());
                    handler.onBlockPlacerPlace(e);

                    if (dispenser.getInventory().containsAtLeast(item, 2)) {
                        dispenser.getInventory().removeItem(new CustomItem(item, 1));
                    } else {
                        SlimefunPlugin.runSync(() -> dispenser.getInventory().removeItem(item), 2L);
                    }
                }
            });

            if (!hasItemHandler) {
                block.setType(item.getType());
                block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, item.getType());

                BlockStorage.store(block, sfItem.getId());

                if (dispenser.getInventory().containsAtLeast(item, 2)) {
                    dispenser.getInventory().removeItem(new CustomItem(item, 1));
                } else {
                    SlimefunPlugin.runSync(() -> dispenser.getInventory().removeItem(item), 2L);
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
                    BlockStateSnapshotResult blockState = PaperLib.getBlockState(facedBlock, false);

                    if ((blockState.getState() instanceof Nameable)) {
                        Nameable nameable = ((Nameable) blockState.getState());
                        nameable.setCustomName(meta.getDisplayName());

                        if (blockState.isSnapshot()) {
                            // Update block state after changing name
                            blockState.getState().update(true, false);
                        }
                    }
                }

            }

            facedBlock.getWorld().playEffect(facedBlock.getLocation(), Effect.STEP_SOUND, item.getType());

            if (dispenser.getInventory().containsAtLeast(item, 2)) {
                dispenser.getInventory().removeItem(new CustomItem(item, 1));
            } else {
                SlimefunPlugin.runSync(() -> dispenser.getInventory().removeItem(item), 2L);
            }
        }
    }
}
