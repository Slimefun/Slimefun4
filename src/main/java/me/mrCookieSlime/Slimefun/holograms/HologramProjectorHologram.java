package me.mrCookieSlime.Slimefun.holograms;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.chat.ChatInput;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.math.DoubleHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public final class HologramProjectorHologram {
	
	private HologramProjectorHologram() {}
	
	public static ArmorStand getArmorStand(Block projector, boolean createIfNoneExists) {
		String nametag = BlockStorage.getLocationInfo(projector.getLocation(), "text");
		double offset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), "offset"));
		Location l = new Location(projector.getWorld(), projector.getX() + 0.5, projector.getY() + offset, projector.getZ() + 0.5);
		
		for (Entity n : l.getChunk().getEntities()) {
			if (n instanceof ArmorStand && n.getCustomName() != null && n.getCustomName().equals(nametag) && l.distanceSquared(n.getLocation()) < 0.4D) {
				return (ArmorStand) n;
			}
		}
		
		if (!createIfNoneExists) {
			return null;
		}
		
		ArmorStand hologram = SimpleHologram.create(l);
		hologram.setCustomName(nametag);
		return hologram;
	}
	
	public static void remove(Block b) {
		ArmorStand hologram = getArmorStand(b, false);
		if (hologram != null) hologram.remove();
	}

	public static void openEditor(Player p, final Block projector) {
		ChestMenu menu = new ChestMenu("Hologram Settings");
		
		menu.addItem(0, new CustomItem(new ItemStack(Material.NAME_TAG), "&7Text &e(Click to edit)", "", "&r" + ChatColor.translateAlternateColorCodes('&', BlockStorage.getLocationInfo(projector.getLocation(), "text"))));
		menu.addMenuClickHandler(0, (pl, slot, item, action) -> {
			pl.closeInventory();
			SlimefunPlugin.getLocal().sendMessage(pl, "machines.HOLOGRAM_PROJECTOR.enter-text", true);
			ChatInput.waitForPlayer(SlimefunPlugin.instance, pl, message -> {
				ArmorStand hologram = getArmorStand(projector, true);
				hologram.setCustomName(ChatColor.translateAlternateColorCodes('&', message));
				BlockStorage.addBlockInfo(projector, "text", hologram.getCustomName());
				openEditor(pl, projector);
			});
			return false;
		});
		
		menu.addItem(1, new CustomItem(new ItemStack(Material.CLOCK), "&7Offset: &e" + DoubleHandler.fixDouble(Double.valueOf(BlockStorage.getLocationInfo(projector.getLocation(), "offset")) + 1.0D), "", "&rLeft Click: &7+0.1", "&rRight Click: &7-0.1"));
		menu.addMenuClickHandler(1, (pl, slot, item, action) -> {
			double offset = DoubleHandler.fixDouble(Double.valueOf(BlockStorage.getLocationInfo(projector.getLocation(), "offset")) + (action.isRightClicked() ? -0.1F : 0.1F));
			ArmorStand hologram = getArmorStand(projector, true);
			Location l = new Location(projector.getWorld(), projector.getX() + 0.5, projector.getY() + offset, projector.getZ() + 0.5);
			hologram.teleport(l);
			BlockStorage.addBlockInfo(projector, "offset", String.valueOf(offset));
			openEditor(pl, projector);
			return false;
		});
		
		menu.open(p);
	}

}
