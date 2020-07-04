package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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
                } else {
                    sfItem.callItemHandler(BlockPlaceHandler.class, handler -> handler.onBlockPlace(e.getPlayer(), e, item));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockUnregister(BlockBreakEvent e) {
        if (hasSensitiveBlockAbove(e.getPlayer(), e.getBlock())) {
            e.setCancelled(true);
            return;
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

        if (sfItem != null && !sfItem.useVanillaBlockBreaking()) {
            SlimefunBlockHandler blockHandler = SlimefunPlugin.getRegistry().getBlockHandlers().get(sfItem.getID());

            if (blockHandler != null) {
                if (!blockHandler.onBreak(e.getPlayer(), e.getBlock(), sfItem, UnregisterReason.PLAYER_BREAK)) {
                    e.setCancelled(true);
                    return;
                }
            } else {
                sfItem.callItemHandler(BlockBreakHandler.class, handler -> handler.onBlockBreak(e, item, fortune, drops));
            }

            drops.addAll(sfItem.getDrops());
            BlockStorage.clearBlockInfo(e.getBlock());
        }

        if (item.getType() != Material.AIR) {
            for (ItemHandler handler : SlimefunItem.getPublicItemHandlers(BlockBreakHandler.class)) {
                if (((BlockBreakHandler) handler).onBlockBreak(e, item, fortune, drops)) {
                    break;
                }
            }
        }

        dropItems(e, drops);
    }

    private void dropItems(BlockBreakEvent e, List<ItemStack> drops) {
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

    private boolean hasSensitiveBlockAbove(Player p, Block b) {
        Block blockAbove = b.getRelative(BlockFace.UP);

        if (sensitiveMaterials.contains(blockAbove.getType())) {
            SlimefunItem sfItem = BlockStorage.check(blockAbove);

            if (sfItem != null && !sfItem.useVanillaBlockBreaking()) {
                SlimefunBlockHandler blockHandler = SlimefunPlugin.getRegistry().getBlockHandlers().get(sfItem.getID());

                if (blockHandler != null) {
                    if (blockHandler.onBreak(p, blockAbove, sfItem, UnregisterReason.PLAYER_BREAK)) {
                        blockAbove.getWorld().dropItemNaturally(blockAbove.getLocation(), BlockStorage.retrieve(blockAbove));
                        blockAbove.setType(Material.AIR);
                    } else {
                        return true;
                    }
                }
            }
        }

        return false;
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