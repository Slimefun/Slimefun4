package io.github.thebusybiscuit.slimefun4.api.items;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class SlimefunItem implements Keyed {
	
	protected Plugin plugin;
	protected NamespacedKey key;
	protected SlimefunItemStack itemStack;
	
	public SlimefunItem(Plugin plugin, SlimefunItemStack item) {
		this.plugin = plugin;
		this.itemStack = item;
		
		this.key = new NamespacedKey(plugin, getID().toLowerCase());
	}
	
	public ItemStack asItemStackCopy() {
		return itemStack.clone();
	}
	
	public String getID() {
		return itemStack.getItemID();
	}
	
	@Override
	public NamespacedKey getKey() {
		return key;
	}
	
	public void preRegister() {
		// Override this as necessary
	}
	
	public void register() {
		// Registering logic
	}
	
	public void postRegister() {
		// Override this as necessary
	}

}
