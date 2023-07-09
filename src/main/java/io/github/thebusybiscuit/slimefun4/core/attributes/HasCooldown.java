package io.github.thebusybiscuit.slimefun4.core.attributes;

import javax.annotation.Nonnull;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

/**
 * Implement this interface for any {@link SlimefunItem} that may not be usable
 * for a time once used.
 *
 * @author Sefiraat
 *
 */
public interface HasCooldown extends ItemAttribute {

    NamespacedKey defaultUsageKey = new NamespacedKey(Slimefun.instance(), "cooldown");

    /**
     * Adds a time to the provided {@link ItemStack} that can be checked
     * to see if the item is on cooldown.
     *
     * @param itemStack
     *                          The {@link ItemStack} to put on cooldown
     * @param durationInSeconds
     *                          The duration in seconds to put the stack on cooldown for
     */
    default void addCooldown(@Nonnull ItemStack itemStack, int durationInSeconds) {
        addCooldown(itemStack, durationInSeconds * 1000L);
    }

    /**
     * Adds a time to the provided {@link ItemStack} that can be checked
     * to see if the item is on cooldown.
     *
     * @param itemStack
     *                      The {@link ItemStack} to put on cooldown.
     * @param durationInMs
     *                      The duration in milliseconds for the cooldown.
     */
    default void addCooldown(@Nonnull ItemStack itemStack, long durationInMs) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataAPI.setLong(itemMeta, defaultUsageKey, System.currentTimeMillis() + durationInMs);
    }

    /**
     * Checks if the item has a cooldown time and returns if it has not yet expired
     * or false if it doesn't have a matching cooldown
     *
     * @param itemStack
     *                  The {@link ItemStack} to put on cooldown
     *
     * @return True if the item is still on cooldown. False if expired or has no cooldown
     */
    public static boolean isOnCooldown(@Nonnull ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        long cooldownUntil = PersistentDataAPI.getLong(itemMeta, defaultUsageKey, 0);
        return System.currentTimeMillis() < cooldownUntil;
    }

    default int cooldownDuration() {
        return 0;
    }
}
