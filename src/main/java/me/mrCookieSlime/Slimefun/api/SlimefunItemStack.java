package me.mrCookieSlime.Slimefun.api;

import java.util.Optional;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
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
	
	private final String texture;

	public SlimefunItemStack(String id, Material type, String name, String... lore) {
		super(type, name, lore);
		texture = null;
		
		setID(id);
	}

	public SlimefunItemStack(String id, Material type, Color color, String name, String... lore) {
		super(new ItemStack(type), color, name, lore);
		texture = null;

		setID(id);
	}

	public SlimefunItemStack(String id, ItemStack item, String name, String... lore) {
		super(item, name, lore);
		texture = null;

		setID(id);
	}

	public SlimefunItemStack(String id, ItemStack item) {
		super(item);
		texture = null;

		setID(id);
	}

	public SlimefunItemStack(String id, ItemStack item, Consumer<ItemMeta> consumer) {
		super(item, consumer);
		texture = null;

		setID(id);
	}

	public SlimefunItemStack(String id, Material type, String name, Consumer<ItemMeta> consumer) {
		super(type, meta -> {
			if (name != null) {
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
			}
			
			consumer.accept(meta);
		});

		texture = null;
		setID(id);
	}

	public SlimefunItemStack(String id, String texture, String name, String... lore) {
		super(SkullItem.fromBase64(texture), name, lore);
		this.texture = texture;
		
		setID(id);
	}

	public SlimefunItemStack(String id, String texture, String name, Consumer<ItemMeta> consumer) {
		super(SkullItem.fromBase64(texture), meta -> {
			if (name != null) {
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
			}
			
			consumer.accept(meta);
		});
		
		this.texture = texture;
		
		setID(id);
	}

	public SlimefunItemStack(String id, String texture, Consumer<ItemMeta> consumer) {
		super(SkullItem.fromBase64(texture), consumer);
		this.texture = texture;
		
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

	public Optional<String> getBase64Texture() {
		return Optional.ofNullable(texture);
	}

}
