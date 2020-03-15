package io.github.thebusybiscuit.slimefun4.core.attributes;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This interface, when attached to a {@link SlimefunItem}, provides an easy method for damaging
 * an {@link ItemStack}, see {@link #damageItem(Player, ItemStack)}.
 * 
 * It also provides a simple {@link #isDamageable()} method, in case you wanna add a config
 * option that decides whether or not this {@link SlimefunItem} shall be damageable.
 * 
 * @author TheBusyBiscuit
 *
 */
@FunctionalInterface
public interface DamageableItem extends ItemAttribute {

    boolean isDamageable();

    default void damageItem(Player p, ItemStack item) {
        if (isDamageable() && item != null && item.getType() != Material.AIR && item.getAmount() > 0) {
            if (item.getEnchantments().containsKey(Enchantment.DURABILITY) && Math.random() * 100 <= (60 + Math.floorDiv(40, (item.getEnchantmentLevel(Enchantment.DURABILITY) + 1)))) {
                return;
            }

            ItemMeta meta = item.getItemMeta();
            Damageable damageable = (Damageable) meta;

            if (damageable.getDamage() >= item.getType().getMaxDurability()) {
                item.setAmount(0);
            }
            else {
                damageable.setDamage(damageable.getDamage() + 1);
                item.setItemMeta(meta);
            }
        }
    }

}
