package me.mrCookieSlime.Slimefun.GPS;

import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.chat.ChatInput;
import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage;
import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage.HoverAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.CustomBookOverlay;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public final class Elevator {

	private Elevator() {}
	
	public static void openEditor(Player p, final Block b) {
		ChestMenu menu = new ChestMenu("Elevator Settings");
		
		menu.addItem(4, new CustomItem(new ItemStack(Material.NAME_TAG), "&7Floor Name &e(Click to edit)", "", "&r" + ChatColor.translateAlternateColorCodes('&', BlockStorage.getLocationInfo(b.getLocation(), "floor"))));
		menu.addMenuClickHandler(4, (pl, slot, item, action) -> {
			pl.closeInventory();
			pl.sendMessage("");
			pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l>> &ePlease enter a Name for this Floor in your Chat!"));
			pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l>> &e(Chat Colors are supported!)"));
			pl.sendMessage("");
			
			ChatInput.waitForPlayer(SlimefunPlugin.instance, pl, message -> {
				BlockStorage.addBlockInfo(b, "floor", message.replace(ChatColor.COLOR_CHAR, '&'));
				
				pl.sendMessage("");
				pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l>> &eSuccessfully named this Floor:"));
				pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l>> &r" + ChatColor.translateAlternateColorCodes('&', message)));
				pl.sendMessage("");
				
				openEditor(pl, b);
			});
			
			return false;
		});
		
		menu.open(p);
	}

	public static void openDialogue(Player p, Block b) {
		Set<UUID> elevatorUsers = SlimefunPlugin.getUtilities().elevatorUsers;
		
		if (elevatorUsers.contains(p.getUniqueId())) {
			elevatorUsers.remove(p.getUniqueId());
			return;
		}
		
		TellRawMessage tellraw = new TellRawMessage();
		tellraw.addText("&3- Pick a Destination -\n\n");
		int index = 1;
		for (int y = b.getWorld().getMaxHeight(); y > 0; y--) {
			Block block = b.getWorld().getBlockAt(b.getX(), y, b.getZ());
			if (BlockStorage.check(block, "ELEVATOR_PLATE")) {
				String floor = ChatColor.translateAlternateColorCodes('&', BlockStorage.getLocationInfo(block.getLocation(), "floor"));
				if (block.getY() == b.getY()) {
					tellraw.addText("&7> " + index + ". &r" + floor + "\n");
					tellraw.addHoverEvent(HoverAction.SHOW_TEXT, "\n&eThis is the Floor you are currently on:\n&r" + floor + "\n");
				}
				else {
					tellraw.addText("&7" + index + ". &r" + floor + "\n");
					tellraw.addHoverEvent(HoverAction.SHOW_TEXT, "\n&eClick to teleport to this Floor\n&r" + floor + "\n");
					tellraw.addClickEvent(me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage.ClickAction.RUN_COMMAND, "/sf elevator " + block.getX() + ' ' + block.getY() + ' ' + block.getZ() + " ");
				}
				
				index++;
			}
		}
		if (index > 2) new CustomBookOverlay("Elevator", "Slimefun", tellraw).open(p);
		else SlimefunPlugin.getLocal().sendMessage(p, "machines.ELEVATOR.no-destinations", true);
	}

}
