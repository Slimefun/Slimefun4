package me.mrCookieSlime.Slimefun.holograms;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.MenuHelper;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.MenuHelper.ChatHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class Projector {
	
	public static ArmorStand getArmorStand(Block projector) {
		String nametag = ChatColor.translateAlternateColorCodes('&', BlockStorage.getBlockInfo(projector, "text"));
		double offset = Double.valueOf(BlockStorage.getBlockInfo(projector, "offset"));
		Location l = new Location(projector.getWorld(), projector.getX() + 0.5, projector.getY() + offset, projector.getZ() + 0.5);
		
		for (Entity n: l.getChunk().getEntities()) {
			if (n instanceof ArmorStand) {
				if (n.getCustomName() != null && n.getCustomName().equals(nametag) && l.distanceSquared(n.getLocation()) < 0.4D) return (ArmorStand) n;
			}
		}
		
		ArmorStand hologram = ArmorStandFactory.createHidden(l);
		hologram.setCustomName(nametag);
		return hologram;
	}

	public static void openEditor(Player p, final Block projector) {
		ChestMenu menu = new ChestMenu("Hologram Settings");
		
		menu.addItem(0, new CustomItem(new MaterialData(Material.NAME_TAG), "�7Text �e(Click to edit)", "", "�r" + ChatColor.translateAlternateColorCodes('&', BlockStorage.getBlockInfo(projector, "text"))));
		menu.addMenuClickHandler(0, new MenuClickHandler() {
			
			@Override
			public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction arg3) {
				p.closeInventory();
				Messages.local.sendTranslation(p, "machines.HOLOGRAM_PROJECTOR.enter-text", true);
				MenuHelper.awaitChatInput(p, new ChatHandler() {
					
					@Override
					public boolean onChat(Player p, String message) {
						ArmorStand hologram = getArmorStand(projector);
						hologram.setCustomName(ChatColor.translateAlternateColorCodes('&', message));
						BlockStorage.addBlockInfo(projector, "text", message);
						openEditor(p, projector);
						return false;
					}
				});
				return false;
			}
		});
		
		menu.addItem(1, new CustomItem(new MaterialData(Material.WATCH), "�7Offset: �e" + DoubleHandler.fixDouble(Double.valueOf(BlockStorage.getBlockInfo(projector, "offset")) + 1.0D), "", "�rLeft Click: �7+0.1", "�rRight Click: �7-0.1"));
		menu.addMenuClickHandler(1, new MenuClickHandler() {
			
			@Override
			public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction arg3) {
				double offset = DoubleHandler.fixDouble(Double.valueOf(BlockStorage.getBlockInfo(projector, "offset")) + (arg3.isRightClicked() ? -0.1F: 0.1F));
				ArmorStand hologram = getArmorStand(projector);
				Location l = new Location(projector.getWorld(), projector.getX() + 0.5, projector.getY() + offset, projector.getZ() + 0.5);
				hologram.teleport(l);
				BlockStorage.addBlockInfo(projector, "offset", String.valueOf(offset));
				openEditor(p, projector);
				return false;
			}
		});
		
		menu.open(p);
	}

}
