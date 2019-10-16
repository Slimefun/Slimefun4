package me.mrCookieSlime.Slimefun.commands.subcommands;

import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.players.PlayerList;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.commands.SlimefunCommand;
import me.mrCookieSlime.Slimefun.commands.SubCommand;

public class GiveCommand extends SubCommand {

	public GiveCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
		super(plugin, cmd);
	}

	@Override
	public String getName() {
		return "give";
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		if (sender.hasPermission("slimefun.cheat.items") || !(sender instanceof Player)) {
	        if (args.length == 3) {
	        	Optional<Player> player = PlayerList.findByName(args[1]);
				
				if (player.isPresent()) {
					Player p = player.get();
					if (Slimefun.listIDs().contains(args[2].toUpperCase())) {
						SlimefunPlugin.getLocal().sendMessage(p, "messages.given-item", true, msg -> msg.replace("%item%", SlimefunItem.getByID(args[2].toUpperCase()).getItem().getItemMeta().getDisplayName()).replace("%amount%", "1"));
						p.getInventory().addItem(SlimefunItem.getByID(args[2].toUpperCase()).getItem());
						SlimefunPlugin.getLocal().sendMessage(sender, "messages.give-item", true, msg -> msg.replace("%player%", args[1]).replace("%item%", SlimefunItem.getByID(args[2].toUpperCase()).getItem().getItemMeta().getDisplayName()).replace("%amount%", "1"));
					}
					else SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-valid-item", true, msg -> msg.replace("%item%", args[2]));
				}
				else SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-online", true, msg -> msg.replace("%player%", args[1]));
			}
	        else if (args.length == 4) {
	        	Optional<Player> player = PlayerList.findByName(args[1]);
				
				if (player.isPresent()) {
					Player p = player.get();
                    if (Slimefun.listIDs().contains(args[2].toUpperCase())) {
                         try {
                             int amount = Integer.parseInt(args[3]);
                             
                             if (amount > 0) {
                                 SlimefunPlugin.getLocal().sendMessage(p, "messages.given-item", true, msg -> msg.replace("%item%", SlimefunItem.getByID(args[2].toUpperCase()).getItem().getItemMeta().getDisplayName()).replace("%amount%", String.valueOf(amount)));
                                 p.getInventory().addItem(new CustomItem(SlimefunItem.getByID(args[2].toUpperCase()).getItem(), amount));
                                 SlimefunPlugin.getLocal().sendMessage(sender, "messages.give-item", true, msg -> msg.replace("%player%", args[1]).replace("%item%", SlimefunItem.getByID(args[2].toUpperCase()).getItem().getItemMeta().getDisplayName()).replace("%amount%", String.valueOf(amount)));
                             }
                             else SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-valid-amount", true, msg -> msg.replace("%amount%", String.valueOf(amount)));
                        } catch (NumberFormatException e){
                            SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-valid-amount", true, msg -> msg.replace("%amount%", args[3]));
                        }
                    }
                    else SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-valid-item", true, msg -> msg.replace("%item%", args[2]));
                }
                else SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-online", true, msg -> msg.replace("%player%", args[1]));
	        }
	        else SlimefunPlugin.getLocal().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf give <Player> <Slimefun Item> [Amount]"));
		}
		else SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
	}

}
