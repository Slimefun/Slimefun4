package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import io.github.bakedlibs.dough.items.ItemUtils;

/**
 * Java-8 universal port: {@code Inventory#getStorageContents()} is 1.9+. Reached reflectively, falling
 * back to {@code getContents()} on older servers (equivalent for the non-player inventories Slimefun
 * uses it on).
 */
public final class InventoryCompat {

    private InventoryCompat() {}

    /**
     * Consumes {@code amount} from the {@link ItemStack} in {@code slot} of a real (container-backed)
     * {@link Inventory} and writes the decremented — or emptied — result back through
     * {@link Inventory#setItem(int, ItemStack)}.
     * <p>
     * dough's {@link ItemUtils#consumeItem(ItemStack, int, boolean)} empties a stack with
     * {@code setAmount(0)}, but on MC 1.8-1.10 {@code CraftItemStack#setAmount(0)} only nulls the mirror's
     * handle and does <em>not</em> clear the backing NMS slot (fixed by Mojang in 1.11+). Consuming the last
     * item of a live container mirror there silently leaves it in place — an infinite duplication. Cloning the
     * slot, consuming the clone, then re-setting the slot rebuilds the NMS slot and behaves identically on
     * every version. Use only on real inventories (dispensers, chests, player inventories), never on the
     * virtual {@code DirtyChestMenu} stacks, which are not mirrors and are unaffected.
     */
    public static void consumeSlot(@Nonnull Inventory inventory, int slot, int amount, boolean replaceConsumables) {
        ItemStack item = inventory.getItem(slot);

        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        ItemStack clone = item.clone();
        ItemUtils.consumeItem(clone, amount, replaceConsumables);
        inventory.setItem(slot, isEmptied(clone) ? null : clone);
    }

    /**
     * Consumes {@code amount} from the {@link ItemStack} held in the given hand and writes the result back
     * through the hand API ({@link HandCompat}), for the same 1.8-1.10 {@code setAmount(0)} reason as
     * {@link #consumeSlot(Inventory, int, int, boolean)}. The event's item mirror is never mutated; the
     * player's actual hand stack is decremented (or replaced with a consumable leftover / cleared) and set
     * back explicitly, which rebuilds the NMS slot on every version.
     */
    public static void consumeHeldItem(@Nonnull Player player, @Nullable EquipmentSlot hand, int amount, boolean replaceConsumables) {
        PlayerInventory inventory = player.getInventory();
        boolean offHand = HandCompat.OFF_HAND != null && hand == HandCompat.OFF_HAND;
        ItemStack held = offHand ? HandCompat.getOffHand(inventory) : HandCompat.getMainHand(inventory);

        if (held == null || held.getType() == Material.AIR) {
            return;
        }

        ItemStack clone = held.clone();
        ItemUtils.consumeItem(clone, amount, replaceConsumables);
        ItemStack result = isEmptied(clone) ? null : clone;

        if (offHand) {
            HandCompat.setOffHand(inventory, result);
        } else {
            HandCompat.setMainHand(inventory, result);
        }
    }

    /** Whether a stack was fully consumed (empty type or non-positive amount) and should clear its slot. */
    private static boolean isEmptied(@Nonnull ItemStack item) {
        return item.getType() == Material.AIR || item.getAmount() <= 0;
    }

    public static ItemStack[] getStorageContents(Inventory inventory) {
        Object result = ReflectionCompat.invoke(inventory, "getStorageContents");
        return result instanceof ItemStack[] ? (ItemStack[]) result : inventory.getContents();
    }

    /**
     * {@code Inventory#isEmpty()} is 1.20+. Falls back to scanning {@code getContents()} for any non-null,
     * non-air stack on older servers.
     */
    public static boolean isEmpty(Inventory inventory) {
        Object result = ReflectionCompat.invoke(inventory, "isEmpty");

        if (result instanceof Boolean) {
            return (Boolean) result;
        }

        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() != org.bukkit.Material.AIR) {
                return false;
            }
        }

        return true;
    }
}
