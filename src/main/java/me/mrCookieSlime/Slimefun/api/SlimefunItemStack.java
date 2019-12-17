package me.mrCookieSlime.Slimefun.api;

import java.util.function.Consumer;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.item.ImmutableItemMeta;
import io.github.thebusybiscuit.cscorelib2.skull.SkullItem;
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
		super(SkullItem.fromBase64(texture), name, lore);
		
		setID(id);
	}

	private void setID(String id) {
		this.id = id;
		
		ItemMeta meta = getItemMeta();
		
		SlimefunPlugin.getItemDataService().setItemData(meta, id);
		SlimefunPlugin.getItemTextureService().setTexture(meta, id);
		
		setItemMeta(meta);
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
