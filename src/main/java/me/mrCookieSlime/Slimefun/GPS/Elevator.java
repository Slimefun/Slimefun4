package me.mrCookieSlime.Slimefun.GPS;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage;
import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage.HoverAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.CustomBookOverlay;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.MenuHelper;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.MenuHelper.ChatHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class Elevator {
	
	public static List<UUID> ignored = new ArrayList<UUID>();

	public static void openEditor(Player p, final Block b) {
		ChestMenu menu = new ChestMenu("Elevator Settings");
		
		menu.addItem(4, new CustomItem(new MaterialData(Material.NAME_TAG), "�7Floor Name �e(Click to edit)", "", "�r" + ChatColor.translateAlternateColorCodes('&', BlockStorage.getBlockInfo(b, "floor"))));
		menu.addMenuClickHandler(4, new MenuClickHandler() {
			
			@Override
			public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction arg3) {
				p.closeInventory();
				p.sendMessage("");
				p.sendMessage("�4�l>> �ePlease enter a Name for this Floor in your Chat!");
				p.sendMessage("�4�l>> �e(Chat Colors are supported!");
				p.sendMessage("");
				
				MenuHelper.awaitChatInput(p, new ChatHandler() {
					
					@Override
					public boolean onChat(Player p, String message) {
						BlockStorage.addBlockInfo(b, "floor", message.replaceAll("�", "&"));
						
						p.sendMessage("");
						p.sendMessage("�4�l>> �eSuccessfully named this Floor:");
						p.sendMessage("�4�l>> �r" + ChatColor.translateAlternateColorCodes('&', message));
						p.sendMessage("");
						
						openEditor(p, b);
						
						return false;
					}
				});
				return false;
			}
		});
		
		menu.open(p);
	}

	public static void openDialogue(Player p, Block b) {
		if (ignored.contains(p.getUniqueId())) {
			ignored.remove(p.getUniqueId());
			return;
		}
		TellRawMessage tellraw = new TellRawMessage();
		tellraw.addText("�3- Pick a Destination -\n\n");
		int index = 1;
		for (int y = b.getWorld().getMaxHeight(); y > 0; y--) {
			Block block = b.getWorld().getBlockAt(b.getX(), y, b.getZ());
			if (BlockStorage.check(block, "ELEVATOR_PLATE")) {
				String floor = ChatColor.translateAlternateColorCodes('&', BlockStorage.getBlockInfo(block, "floor"));
				if (block.getY() == b.getY()) {
					tellraw.addText("�7> " + index + ". �r" + floor + "\n");
					tellraw.addHoverEvent(HoverAction.SHOW_TEXT, "\n�eThis is the Floor you are currently on:\n�r" + floor + "\n");
				}
				else {
					tellraw.addText("�7" + index + ". �r" + floor + "\n");
					tellraw.addHoverEvent(HoverAction.SHOW_TEXT, "\n�eClick to teleport to this Floor\n�r" + floor + "\n");
					tellraw.addClickEvent(me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage.ClickAction.RUN_COMMAND, "/sf elevator " + block.getX() + " " + block.getY() + " " + block.getZ() + " ");
				}
				
				index++;
			}
		}
		if (index > 2) new CustomBookOverlay("Elevator", "Slimefun", tellraw).open(p);
		else Messages.local.sendTranslation(p, "machines.ELEVATOR.no-destinations", true);
	}

}
