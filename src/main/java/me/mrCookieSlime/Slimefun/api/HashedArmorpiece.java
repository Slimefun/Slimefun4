package me.mrCookieSlime.Slimefun.api;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunArmorPiece;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public final class HashedArmorpiece {
	
	private int hash;
	private Optional<SlimefunArmorPiece> item;
	
	protected HashedArmorpiece() {
		this.hash = 0;
		this.item = Optional.empty();
	}
	
	public void update(ItemStack stack, SlimefunItem item) {
		if (stack == null) {
			this.hash = 0;
		}
		else {
			ItemStack copy = stack.clone();
			ItemMeta meta = copy.getItemMeta();
			((Damageable) meta).setDamage(0);
			copy.setItemMeta(meta);
			this.hash = copy.hashCode();
		}
		
		if (item instanceof SlimefunArmorPiece) {
			this.item = Optional.of((SlimefunArmorPiece) item);
		}
		else {
			this.item = Optional.empty();
		}
	}
	
	public boolean hasDiverged(ItemStack stack) {
		if (stack == null) {
			return hash == 0;
		}
		else {
			ItemStack copy = stack.clone();
			ItemMeta meta = copy.getItemMeta();
			((Damageable) meta).setDamage(0);
			copy.setItemMeta(meta);
			return copy.hashCode() != hash;
		}
	}
	
	public Optional<SlimefunArmorPiece> getItem() {
		return item;
	}

}
