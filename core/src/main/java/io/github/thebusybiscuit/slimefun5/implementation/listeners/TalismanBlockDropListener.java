package io.github.thebusybiscuit.slimefun5.implementation.listeners;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun5.implementation.items.magical.talismans.Talisman;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.HandCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedEnchantment;
import io.github.thebusybiscuit.slimefun5.utils.tags.SlimefunTag;

/**
 * This {@link Listener} handles the double-drop {@link Talisman Talismans} (Miner and Farmer)
 * that react to {@link BlockDropItemEvent}. That event only exists on Minecraft 1.13 and later,
 * so it lives in its own {@link Listener} that is registered solely on supported versions — keeping
 * the rest of {@link TalismanListener} loadable on legacy servers (the universal Java-8 jar).
 *
 * @author TheBusyBiscuit
 *
 * @see TalismanListener
 * @see Talisman
 */
public class TalismanBlockDropListener implements Listener {

    public TalismanBlockDropListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockDropItems(BlockDropItemEvent e) {
        ItemStack item = HandCompat.getMainHand(e.getPlayer().getInventory());

        // We are going to ignore Silk Touch here
        if (item.getType() != Material.AIR && item.getAmount() > 0) {
            ItemMeta meta = item.getItemMeta();

            // Ignore Silk Touch Enchantment
            if (meta.hasEnchant(Enchantment.SILK_TOUCH)) {
                return;
            }

            Material type = e.getBlockState().getType();

            // Handle double drops for Miner Talisman
            doubleTalismanDrops(e, SlimefunItems.TALISMAN_MINER, SlimefunTag.MINER_TALISMAN_TRIGGERS, type, meta);

            // Handle double drops for Farmer Talisman
            doubleTalismanDrops(e, SlimefunItems.TALISMAN_FARMER, SlimefunTag.FARMER_TALISMAN_TRIGGERS, type, meta);
        }
    }

    @ParametersAreNonnullByDefault
    private void doubleTalismanDrops(BlockDropItemEvent e, SlimefunItemStack talismanItemStack, SlimefunTag tag, Material type, ItemMeta meta) {
        if (tag.isTagged(type)) {
            Collection<Item> drops = e.getItems();

            if (Talisman.trigger(e, talismanItemStack, false)) {
                int dropAmount = getAmountWithFortune(type, meta.getEnchantLevel(VersionedEnchantment.FORTUNE));

                // Keep track of whether we actually doubled the drops or not
                boolean doubledDrops = false;

                // Loop through all dropped items
                for (Item drop : drops) {
                    ItemStack droppedItem = drop.getItemStack();

                    // We do not want to dupe blocks
                    if (!droppedItem.getType().isBlock()) {
                        int amount = Math.max(1, (dropAmount * 2) - droppedItem.getAmount());
                        e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), CustomItemStack.create(droppedItem, amount));
                        doubledDrops = true;
                    }
                }

                // Fixes #2077
                if (doubledDrops) {
                    Talisman talisman = talismanItemStack.getItem(Talisman.class);

                    // Fixes #2818
                    if (talisman != null) {
                        talisman.sendMessage(e.getPlayer());
                    }
                }
            }
        }
    }

    private int getAmountWithFortune(@Nonnull Material type, int fortuneLevel) {
        if (fortuneLevel > 0) {
            Random random = ThreadLocalRandom.current();
            int amount = random.nextInt(fortuneLevel + 2) - 1;
            amount = Math.max(amount, 1);
            amount = (type == Material.LAPIS_ORE ? 4 + random.nextInt(5) : 1) * (amount + 1);
            return amount;
        } else {
            return 1;
        }
    }
}
