package me.mrCookieSlime.Slimefun.api;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

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

	public SlimefunItemStack(String id, Color color, PotionEffect effect, String name, String... lore) {
		super(Material.POTION, im -> {
			if (name != null) {
				im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
			}
			
			if (lore.length > 0) {
				List<String> lines = new ArrayList<>();
				
				for (String line : lore) {
					lines.add(ChatColor.translateAlternateColorCodes('&', line));
				}
				
				im.setLore(lines);
			}
			
			if (im instanceof PotionMeta) {
				((PotionMeta) im).setColor(color);
				((PotionMeta) im).addCustomEffect(effect, true);
			}
		});
		
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
		super(getSkull(texture), name, lore);
		this.texture = texture;
		
		setID(id);
	}

	public SlimefunItemStack(String id, String texture, String name, Consumer<ItemMeta> consumer) {
		super(getSkull(texture), meta -> {
			if (name != null) {
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
			}
			
			consumer.accept(meta);
		});
		
		this.texture = texture;
		
		setID(id);
	}

	public SlimefunItemStack(String id, String texture, Consumer<ItemMeta> consumer) {
		super(getSkull(texture), consumer);
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
	
	private static ItemStack getSkull(String texture) {
		String base64 = texture;
		
		// At this point we can be sure it's not a base64 encoded texture
		if (!texture.startsWith("ey")) {
			base64 = Base64.getEncoder().encodeToString(("{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/" + texture + "\"}}}").getBytes());
		}
		
		return SkullItem.fromBase64(base64);
	}

}
