package me.mrCookieSlime.Slimefun.api;

import java.util.function.Consumer;
import java.util.logging.Level;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.item.ImmutableItemMeta;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class SlimefunItemStack extends CustomItem {
	
	private String id;
	private ImmutableItemMeta immutableMeta;

	public SlimefunItemStack(String id, Material type, String name, String... lore) {
		super(type, name, lore);

		setID(id);
	}

	public SlimefunItemStack(String id, Material type, Color color, String name, String... lore) {
		super(new ItemStack(type), color, name, lore);

		setID(id);
	}

	public SlimefunItemStack(String id, ItemStack item, String name, String... lore) {
		super(item, name, lore);

		setID(id);
	}

	public SlimefunItemStack(String id, ItemStack item) {
		super(item);

		setID(id);
	}

	public SlimefunItemStack(String id, ItemStack item, Consumer<ItemMeta> consumer) {
		super(item, consumer);

		setID(id);
	}

	public SlimefunItemStack(String id, String texture, String name, String... lore) {
		super(getSkull(texture), name, lore);
		
		setID(id);
	}

	private void setID(String id) {
		this.id = id;
		
		ItemMeta meta = getItemMeta();
		
		SlimefunPlugin.getItemDataService().setItemData(meta, id);
		SlimefunPlugin.getItemTextureService().setTexture(meta, id);
		
		setItemMeta(meta);
	}
	
	private static ItemStack getSkull(String texture) {
		try {
			return CustomSkull.getItem(texture);
		} catch (Exception x) {
			Slimefun.getLogger().log(Level.SEVERE, "An Error occurred while initializing the Items for Slimefun " + Slimefun.getVersion(), x);
			
			return new ItemStack(Material.PLAYER_HEAD);
		}
	}

	public String getItemID() {
		return id;
	}
	
	public ImmutableItemMeta getImmutableMeta() {
		return immutableMeta;
	}
	
	@Override
	public boolean setItemMeta(ItemMeta meta) {
		immutableMeta = new ImmutableItemMeta(meta);
		
		return super.setItemMeta(meta);
	}
	
	@Override
	public ItemStack clone() {
		SlimefunItemStack item = (SlimefunItemStack) super.clone();
		item.id = getItemID();
		return item;
	}

}
