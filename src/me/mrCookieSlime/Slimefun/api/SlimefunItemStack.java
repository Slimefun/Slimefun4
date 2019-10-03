package me.mrCookieSlime.Slimefun.api;

import java.util.function.Consumer;
import java.util.logging.Level;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;

public class SlimefunItemStack extends CustomItem {
	
	private String id;

	public SlimefunItemStack(String id, Material type, String name, String... lore) {
		super(type, name, lore);
		
		this.id = id;
	}

	public SlimefunItemStack(String id, Material type, Color color, String name, String... lore) {
		super(new ItemStack(type), color, name, lore);
		
		this.id = id;
	}

	public SlimefunItemStack(String id, ItemStack item, String name, String... lore) {
		super(item, name, lore);
		
		this.id = id;
	}

	public SlimefunItemStack(String id, ItemStack item) {
		super(item);
		
		this.id = id;
	}

	public SlimefunItemStack(String id, ItemStack item, Consumer<ItemMeta> consumer) {
		super(item, consumer);
		
		this.id = id;
	}

	public SlimefunItemStack(String id, String texture, String name, String... lore) {
		super(getSkull(texture), name, lore);
		
		this.id = id;
	}
	
	private static ItemStack getSkull(String texture) {
		try {
			return CustomSkull.getItem(texture);
		} catch (Exception x) {
			Slimefun.getLogger().log(Level.SEVERE, "An Error occured while initializing the Items for Slimefun " + Slimefun.getVersion(), x);
			
			return new ItemStack(Material.PLAYER_HEAD);
		}
	}

	public String getItemID() {
		return id;
	}

}
