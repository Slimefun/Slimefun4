package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.HandledBlock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.NotPlaceable;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockPlaceHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class BlockListener implements Listener {

    // Materials that require a Block under it, e.g. Pressure Plates
    private final Set<Material> sensitiveMaterials = new HashSet<>();

    public BlockListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        sensitiveMaterials.add(Material.CAKE);
        sensitiveMaterials.add(Material.STONE_PRESSURE_PLATE);
        sensitiveMaterials.add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
        sensitiveMaterials.add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
        sensitiveMaterials.addAll(Tag.SAPLINGS.getValues());
        sensitiveMaterials.addAll(Tag.WOODEN_PRESSURE_PLATES.getValues());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockRegister(BlockPlaceEvent e) {
        if (BlockStorage.hasBlockInfo(e.getBlock())) {
            e.setCancelled(true);
            return;
        }

        ItemStack item = e.getItemInHand();

        SlimefunItem sfItem = SlimefunItem.getByItem(item);
        if (sfItem != null && Slimefun.isEnabled(e.getPlayer(), sfItem, true) && !(sfItem instanceof NotPlaceable)) {
            if (!Slimefun.hasUnlocked(e.getPlayer(), sfItem, true)) {
                e.setCancelled(true);
            }
            else {
                if (SlimefunPlugin.getBlockDataService().isTileEntity(e.getBlock().getType())) {
                    SlimefunPlugin.getBlockDataService().setBlockData(e.getBlock(), sfItem.getID());
                }

                BlockStorage.addBlockInfo(e.getBlock(), "id", sfItem.getID(), true);

                SlimefunBlockHandler blockHandler = SlimefunPlugin.getRegistry().getBlockHandlers().get(sfItem.getID());
                if (blockHandler != null) {
                    blockHandler.onPlace(e.getPlayer(), e.getBlock(), sfItem);
                }
                else {
                    sfItem.callItemHandler(BlockPlaceHandler.class, handler -> handler.onBlockPlace(e, item));
                }
            }
        }
        else {
            for (ItemHandler handler : SlimefunItem.getPublicItemHandlers(BlockPlaceHandler.class)) {
                if (((BlockPlaceHandler) handler).onBlockPlace(e, item)) {
                    break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockUnregister(BlockBreakEvent e) {
        Block blockAbove = e.getBlock().getRelative(BlockFace.UP);

        if (sensitiveMaterials.contains(blockAbove.getType())) {
            SlimefunItem sfItem = BlockStorage.check(blockAbove);

            if (sfItem != null && !(sfItem instanceof HandledBlock)) {
                SlimefunBlockHandler blockHandler = SlimefunPlugin.getRegistry().getBlockHandlers().get(sfItem.getID());

                if (blockHandler != null) {
                    if (blockHandler.onBreak(e.getPlayer(), blockAbove, sfItem, UnregisterReason.PLAYER_BREAK)) {
                        blockAbove.getWorld().dropItemNaturally(blockAbove.getLocation(), BlockStorage.retrieve(blockAbove));
                        blockAbove.setType(Material.AIR);
                    }
                    else {
                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }

        SlimefunItem sfItem = BlockStorage.check(e.getBlock());
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        int fortune = getBonusDropsWithFortune(item, e.getBlock());
        List<ItemStack> drops = new ArrayList<>();

        if (sfItem == null && SlimefunPlugin.getBlockDataService().isTileEntity(e.getBlock().getType())) {
            Optional<String> blockData = SlimefunPlugin.getBlockDataService().getBlockData(e.getBlock());

            if (blockData.isPresent()) {
                sfItem = SlimefunItem.getByID(blockData.get());
            }
        }

        if (sfItem != null && !(sfItem instanceof HandledBlock)) {
            SlimefunBlockHandler blockHandler = SlimefunPlugin.getRegistry().getBlockHandlers().get(sfItem.getID());

            if (blockHandler != null) {
                if (blockHandler.onBreak(e.getPlayer(), e.getBlock(), sfItem, UnregisterReason.PLAYER_BREAK)) {
                    drops.addAll(sfItem.getDrops());
                    BlockStorage.clearBlockInfo(e.getBlock());
                }
                else {
                    e.setCancelled(true);
                    return;
                }
            }
            else {
                sfItem.callItemHandler(BlockBreakHandler.class, handler -> handler.onBlockBreak(e, item, fortune, drops));
            }
        }

        if (item.getType() != Material.AIR) {
            for (ItemHandler handler : SlimefunItem.getPublicItemHandlers(BlockBreakHandler.class)) {
                if (((BlockBreakHandler) handler).onBlockBreak(e, item, fortune, drops)) {
                    break;
                }
            }
        }

        if (!drops.isEmpty()) {
            e.getBlock().setType(Material.AIR);

            if (e.isDropItems()) {
                for (ItemStack drop : drops) {
                    if (drop != null && drop.getType() != Material.AIR) {
                        e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), drop);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();

        if (SlimefunUtils.isItemSimilar(item, SlimefunItems.ADVANCED_CIRCUIT_BOARD, true)) e.setCancelled(true);
        else if (SlimefunUtils.isItemSimilar(item, SlimefunItems.CARBON, true)) e.setCancelled(true);
        else if (SlimefunUtils.isItemSimilar(item, SlimefunItems.COMPRESSED_CARBON, true)) e.setCancelled(true);
        else if (SlimefunUtils.isItemSimilar(item, SlimefunItems.CARBON_CHUNK, true)) e.setCancelled(true);
        else if (SlimefunUtils.isItemSimilar(item, SlimefunItems.ANDROID_MEMORY_CORE, true)) e.setCancelled(true);
        else if (SlimefunUtils.isItemSimilar(item, SlimefunItems.LAVA_CRYSTAL, true)) e.setCancelled(true);
        else if (SlimefunUtils.isItemSimilar(item, SlimefunItems.TINY_URANIUM, true)) e.setCancelled(true);
        else if (SlimefunUtils.isItemSimilar(item, SlimefunItems.SMALL_URANIUM, true)) e.setCancelled(true);
        else if (SlimefunUtils.isItemSimilar(item, SlimefunItems.BROKEN_SPAWNER, false)) e.setCancelled(true);
        else if (e.getBlock().getY() != e.getBlockAgainst().getY() && (SlimefunUtils.isItemSimilar(item, SlimefunItems.CARGO_INPUT, false) || SlimefunUtils.isItemSimilar(item, SlimefunItems.CARGO_OUTPUT, false) || SlimefunUtils.isItemSimilar(item, SlimefunItems.CARGO_OUTPUT_ADVANCED, false))) {
            SlimefunPlugin.getLocal().sendMessage(e.getPlayer(), "machines.CARGO_NODES.must-be-placed", true);
            e.setCancelled(true);
        }
    }

    private int getBonusDropsWithFortune(ItemStack item, Block b) {
        int fortune = 1;

        if (item != null && item.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS) && !item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
            Random random = ThreadLocalRandom.current();
            int fortuneLevel = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);

            fortune = Math.max(1, random.nextInt(fortuneLevel + 2) - 1);
            fortune = (b.getType() == Material.LAPIS_ORE ? 4 + random.nextInt(5) : 1) * (fortune + 1);
        }

        return fortune;
    }
}
