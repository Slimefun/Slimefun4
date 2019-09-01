package me.mrCookieSlime.Slimefun.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

@FunctionalInterface
public interface DamageableItem {
	
	boolean isDamageable();
	
	default void damageItem(Player p, ItemStack item) {
		if (item == null || item.getType() == null || item.getType() == Material.AIR) {
			return;
		}
		else if (item.getAmount() > 0 && isDamageable() && !item.getEnchantments().containsKey(Enchantment.DURABILITY) || Math.random() * 100 <= (60 + 40 / (item.getEnchantmentLevel(Enchantment.DURABILITY) + 1))) {
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
