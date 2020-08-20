package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.ArrayList;
import java.util.EnumSet;
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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * The {@link BlockListener} is responsible for listening to the {@link BlockPlaceEvent}
 * and {@link BlockBreakEvent}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see BlockPlaceHandler
 * @see BlockBreakHandler
 * @see ToolUseHandler
 *
 */
public class BlockListener implements Listener {

    // Materials that require a Block under it, e.g. Pressure Plates
    private final Set<Material> sensitiveMaterials = EnumSet.noneOf(Material.class);

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
    public void onBlockPlace(BlockPlaceEvent e) {
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

                sfItem.callItemHandler(BlockPlaceHandler.class, handler -> handler.onPlayerPlace(e));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        checkForSensitiveBlockAbove(e.getPlayer(), e.getBlock());

        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        int fortune = getBonusDropsWithFortune(item, e.getBlock());
        List<ItemStack> drops = new ArrayList<>();

        if (item.getType() != Material.AIR) {
            callToolHandler(e, item, fortune, drops);
        }

        if (!e.isCancelled()) {
            callBlockHandler(e, item, fortune, drops);
        }

        dropItems(e, drops);
    }

    private void callToolHandler(BlockBreakEvent e, ItemStack item, int fortune, List<ItemStack> drops) {
        SlimefunItem tool = SlimefunItem.getByItem(item);

        if (tool != null) {
            if (Slimefun.hasUnlocked(e.getPlayer(), tool, true)) {
                tool.callItemHandler(ToolUseHandler.class, handler -> handler.onToolUse(e, item, fortune, drops));
            }
            else {
                e.setCancelled(true);
            }
        }
    }

    private void callBlockHandler(BlockBreakEvent e, ItemStack item, int fortune, List<ItemStack> drops) {
        SlimefunItem sfItem = BlockStorage.check(e.getBlock());

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
            }
            else {
                sfItem.callItemHandler(BlockBreakHandler.class, handler -> handler.onBlockBreak(e, item, fortune, drops));
            }

            drops.addAll(sfItem.getDrops());
            BlockStorage.clearBlockInfo(e.getBlock());
        }
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

    /**
     * This method checks for a sensitive {@link Block}.
     * Sensitive {@link Block Blocks} are pressure plates or saplings, which should be broken
     * when the block beneath is broken as well.
     * 
     * @param p
     *            The {@link Player} who broke this {@link Block}
     * @param b
     *            The {@link Block} that was broken
     */
    private void checkForSensitiveBlockAbove(Player p, Block b) {
        Block blockAbove = b.getRelative(BlockFace.UP);

        if (sensitiveMaterials.contains(blockAbove.getType())) {
            SlimefunItem sfItem = BlockStorage.check(blockAbove);

            if (sfItem != null && !sfItem.useVanillaBlockBreaking()) {
                SlimefunBlockHandler blockHandler = SlimefunPlugin.getRegistry().getBlockHandlers().get(sfItem.getID());

                if (blockHandler != null) {
                    if (blockHandler.onBreak(p, blockAbove, sfItem, UnregisterReason.PLAYER_BREAK)) {
                        blockAbove.getWorld().dropItemNaturally(blockAbove.getLocation(), BlockStorage.retrieve(blockAbove));
                        blockAbove.setType(Material.AIR);
                    }
                }
                else {
                    blockAbove.getWorld().dropItemNaturally(blockAbove.getLocation(), BlockStorage.retrieve(blockAbove));
                    blockAbove.setType(Material.AIR);
                }
            }
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
