package me.mrCookieSlime.Slimefun.GPS;

import java.util.Set;
import java.util.UUID;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage;
import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage.HoverAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.CustomBookOverlay;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.MenuHelper;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public final class Elevator {

    private Elevator() {}

    public static void openEditor(Player p, final Block b) {
        ChestMenu menu = new ChestMenu("电梯设置");

        menu.addItem(4, new CustomItem(new ItemStack(Material.NAME_TAG), "&7楼层名 &e(单击编辑)", "", "&r" + ChatColor.translateAlternateColorCodes('&', BlockStorage.getLocationInfo(b.getLocation(), "floor"))));
        menu.addMenuClickHandler(4, (pl, slot, item, action) -> {
            pl.closeInventory();
            pl.sendMessage("");
            pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l>> &e请在聊天栏里输入你为这层楼起的名字吧!"));
            pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l>> &e支持颜色代码!"));
            pl.sendMessage("");

            MenuHelper.awaitChatInput(pl, (player, message) -> {
                BlockStorage.addBlockInfo(b, "floor", message.replace(ChatColor.COLOR_CHAR, '&'));

                player.sendMessage("");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l>> &e成功地将此层命名为:"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l>> &r" + ChatColor.translateAlternateColorCodes('&', message)));
                player.sendMessage("");

                openEditor(player, b);

                return false;
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
		tellraw.addText("&3- 选择楼层 -\n\n");
		int index = 1;
		for (int y = b.getWorld().getMaxHeight(); y > 0; y--) {
			Block block = b.getWorld().getBlockAt(b.getX(), y, b.getZ());
			if (BlockStorage.check(block, "ELEVATOR_PLATE")) {
				String floor = ChatColor.translateAlternateColorCodes('&', BlockStorage.getLocationInfo(block.getLocation(), "floor"));
				if (block.getY() == b.getY()) {
					tellraw.addText("&7> " + index + ". &r" + floor + "\n");
					tellraw.addHoverEvent(HoverAction.SHOW_TEXT, "\n&e你所在的楼层:\n&r" + floor + "\n");
				}
				else {
					tellraw.addText("&7" + index + ". &r" + floor + "\n");
					tellraw.addHoverEvent(HoverAction.SHOW_TEXT, "\n&e单击传送至楼层 \n&r" + floor + "\n");
					tellraw.addClickEvent(me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage.ClickAction.RUN_COMMAND, "/sf elevator " + block.getX() + " " + block.getY() + " " + block.getZ() + " ");
				}
				
				index++;
			}
		}
		if (index > 2) new CustomBookOverlay("电梯", "Slimefun", tellraw).open(p);
		else Messages.local.sendTranslation(p, "machines.ELEVATOR.no-destinations", true);
	}

}
