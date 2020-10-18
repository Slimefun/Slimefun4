package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
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
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
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
 * @author Linox
 * 
 * @see BlockPlaceHandler
 * @see BlockBreakHandler
 * @see ToolUseHandler
 *
 */
public class BlockListener implements Listener {

    public BlockListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPlaceExisting(BlockPlaceEvent e) {
        // This prevents Players from placing a block where another block already exists
        // While this can cause ghost blocks it also prevents them from replacing grass
        // or saplings etc...
        if (BlockStorage.hasBlockInfo(e.getBlock())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();
        SlimefunItem sfItem = SlimefunItem.getByItem(item);

        if (sfItem != null && !(sfItem instanceof NotPlaceable) && Slimefun.isEnabled(e.getPlayer(), sfItem, true)) {
            if (!Slimefun.hasUnlocked(e.getPlayer(), sfItem, true)) {
                e.setCancelled(true);
            } else {
                if (SlimefunPlugin.getBlockDataService().isTileEntity(e.getBlock().getType())) {
                    SlimefunPlugin.getBlockDataService().setBlockData(e.getBlock(), sfItem.getId());
                }

                BlockStorage.addBlockInfo(e.getBlock(), "id", sfItem.getId(), true);
                sfItem.callItemHandler(BlockPlaceHandler.class, handler -> handler.onPlayerPlace(e));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        if (SlimefunPlugin.getThirdPartySupportService().isEventFaked(e)) {
            // This is a "fake" event, we can ignore it.
            return;
        }

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

    @ParametersAreNonnullByDefault
    private void callToolHandler(BlockBreakEvent e, ItemStack item, int fortune, List<ItemStack> drops) {
        SlimefunItem tool = SlimefunItem.getByItem(item);

        if (tool != null) {
            if (Slimefun.hasUnlocked(e.getPlayer(), tool, true)) {
                tool.callItemHandler(ToolUseHandler.class, handler -> handler.onToolUse(e, item, fortune, drops));
            } else {
                e.setCancelled(true);
            }
        }
    }

    @ParametersAreNonnullByDefault
    private void callBlockHandler(BlockBreakEvent e, ItemStack item, int fortune, List<ItemStack> drops) {
        SlimefunItem sfItem = BlockStorage.check(e.getBlock());

        if (sfItem == null && SlimefunPlugin.getBlockDataService().isTileEntity(e.getBlock().getType())) {
            Optional<String> blockData = SlimefunPlugin.getBlockDataService().getBlockData(e.getBlock());

            if (blockData.isPresent()) {
                sfItem = SlimefunItem.getByID(blockData.get());
            }
        }

        if (sfItem != null && !sfItem.useVanillaBlockBreaking()) {
            SlimefunBlockHandler blockHandler = SlimefunPlugin.getRegistry().getBlockHandlers().get(sfItem.getId());

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
    }

    @ParametersAreNonnullByDefault
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
    @ParametersAreNonnullByDefault
    private void checkForSensitiveBlockAbove(Player p, Block b) {
        Block blockAbove = b.getRelative(BlockFace.UP);

        if (SlimefunTag.SENSITIVE_MATERIALS.isTagged(blockAbove.getType())) {
            SlimefunItem sfItem = BlockStorage.check(blockAbove);

            if (sfItem != null && !sfItem.useVanillaBlockBreaking()) {
                SlimefunBlockHandler blockHandler = SlimefunPlugin.getRegistry().getBlockHandlers().get(sfItem.getId());

                if (blockHandler != null) {
                    if (blockHandler.onBreak(p, blockAbove, sfItem, UnregisterReason.PLAYER_BREAK)) {
                        blockAbove.getWorld().dropItemNaturally(blockAbove.getLocation(), BlockStorage.retrieve(blockAbove));
                        blockAbove.setType(Material.AIR);
                    }
                } else {
                    blockAbove.getWorld().dropItemNaturally(blockAbove.getLocation(), BlockStorage.retrieve(blockAbove));
                    blockAbove.setType(Material.AIR);
                }
            }
        }
    }

    private int getBonusDropsWithFortune(@Nullable ItemStack item, @Nonnull Block b) {
        int amount = 1;

        if (item != null) {
            int fortuneLevel = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);

            if (fortuneLevel > 0 && !item.containsEnchantment(Enchantment.SILK_TOUCH)) {
                Random random = ThreadLocalRandom.current();

                amount = Math.max(1, random.nextInt(fortuneLevel + 2) - 1);
                amount = (b.getType() == Material.LAPIS_ORE ? 4 + random.nextInt(5) : 1) * (amount + 1);
            }
        }

        return amount;
    }
}
