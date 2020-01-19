package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.skull.SkullBlock;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;

public class DebugFishListener implements Listener {
	
	public DebugFishListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler
	public void onDebug(PlayerInteractEvent e) {
		if (e.getAction() == Action.PHYSICAL || e.getHand() != EquipmentSlot.HAND) return;

		Player p = e.getPlayer();

		if (SlimefunManager.isItemSimilar(e.getItem(), SlimefunItems.DEBUG_FISH, true)) {
			e.setCancelled(true);
			
			if (p.isOp()) {
				switch (e.getAction()) {
				case LEFT_CLICK_BLOCK:
					if (p.isSneaking()) {
						if (BlockStorage.hasBlockInfo(e.getClickedBlock())) {
							BlockStorage.clearBlockInfo(e.getClickedBlock());
						}
					}
					else e.setCancelled(false);
					break;
				case RIGHT_CLICK_BLOCK:
					if (p.isSneaking()) {
						Block b = e.getClickedBlock().getRelative(e.getBlockFace());
						b.setType(Material.PLAYER_HEAD);
						SkullBlock.setFromBase64(b, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTllYjlkYTI2Y2YyZDMzNDEzOTdhN2Y0OTEzYmEzZDM3ZDFhZDEwZWFlMzBhYjI1ZmEzOWNlYjg0YmMifX19");
					}
					else if (BlockStorage.hasBlockInfo(e.getClickedBlock())) {
						p.sendMessage(" ");
						p.sendMessage(ChatColors.color("&d" + e.getClickedBlock().getType() + " &e@ X: " + e.getClickedBlock().getX() + " Y: " + e.getClickedBlock().getY() + " Z: " + e.getClickedBlock().getZ()));
						p.sendMessage(ChatColors.color("&dID: " + "&e" + BlockStorage.checkID(e.getClickedBlock())));
						
						if (e.getClickedBlock().getState() instanceof Skull) {
							p.sendMessage(ChatColors.color("&dSkull: " + "&2\u2714"));
							p.sendMessage(ChatColors.color("  &dRotation: &e" + ((Rotatable) e.getClickedBlock().getBlockData()).getRotation().toString()));
						}
						
						if (BlockStorage.getStorage(e.getClickedBlock().getWorld()).hasInventory(e.getClickedBlock().getLocation())) {
							p.sendMessage(ChatColors.color("&dInventory: " + "&2\u2714"));
						}
						
						else {
							p.sendMessage(ChatColors.color("&dInventory: " + "&4\u2718"));
						}
						
						if (BlockStorage.check(e.getClickedBlock()).isTicking()) {
							p.sendMessage(ChatColors.color("&dTicking: " + "&2\u2714"));
							p.sendMessage(ChatColors.color("  &dAsync: &e" + (BlockStorage.check(e.getClickedBlock()).getBlockTicker().isSynchronized() ? "&4\u2718": "&2\u2714")));
							p.sendMessage(ChatColors.color("  &dTimings: &e" + SlimefunPlugin.getTicker().getTimings(e.getClickedBlock()) + "ms"));
							p.sendMessage(ChatColors.color("  &dTotal Timings: &e" + SlimefunPlugin.getTicker().getTimings(BlockStorage.checkID(e.getClickedBlock())) + "ms"));
							p.sendMessage(ChatColors.color("  &dChunk Timings: &e" + SlimefunPlugin.getTicker().getTimings(e.getClickedBlock().getChunk()) + "ms"));
						}
						else if (BlockStorage.check(e.getClickedBlock()).getEnergyTicker() != null) {
							p.sendMessage(ChatColors.color("&dTicking: " + "&b~ &3(Indirect)"));
							p.sendMessage(ChatColors.color("  &dTimings: &e" + SlimefunPlugin.getTicker().getTimings(e.getClickedBlock()) + "ms"));
							p.sendMessage(ChatColors.color("  &dChunk Timings: &e" + SlimefunPlugin.getTicker().getTimings(e.getClickedBlock().getChunk()) + "ms"));
						}
						else {
							p.sendMessage(ChatColors.color("&dTicking: " + "&4\u2718"));
							p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&dTicking: " + "&4\u2718"));
						}
						
						if (ChargableBlock.isChargable(e.getClickedBlock())) {
							p.sendMessage(ChatColors.color("&dChargable: " + "&2\u2714"));
							p.sendMessage(ChatColors.color("  &dEnergy: &e" + ChargableBlock.getCharge(e.getClickedBlock()) + " / " + ChargableBlock.getMaxCharge(e.getClickedBlock())));
						}
						else {
							p.sendMessage(ChatColors.color("&dChargable: " + "&4\u2718"));
						}
						
						p.sendMessage(ChatColors.color("&6" + BlockStorage.getBlockInfoAsJson(e.getClickedBlock())));
						p.sendMessage(" ");
					}
					break;
				default:
					break;

				}
			}
		}
	}
}
