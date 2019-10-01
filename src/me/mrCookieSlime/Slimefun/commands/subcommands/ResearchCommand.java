package me.mrCookieSlime.Slimefun.commands.subcommands;

import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.players.PlayerList;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.commands.SlimefunCommand;
import me.mrCookieSlime.Slimefun.commands.SubCommand;

public class ResearchCommand extends SubCommand {

	public ResearchCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
		super(plugin, cmd);
	}

	@Override
	public String getName() {
		return "research";
	}
	
	@Override
	protected String getDescriptionPath() {
		return "commands.research.desc";
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		if (args.length == 3) {
			if (sender.hasPermission("slimefun.cheat.researches") || !(sender instanceof Player)) {
				Optional<Player> player = PlayerList.findByName(args[1]);
				
				if (player.isPresent()) {
					Player p = player.get();
					PlayerProfile profile = PlayerProfile.get(p);
					
					if (args[2].equalsIgnoreCase("all")) {
						for (Research res : Research.list()) {
							if (!profile.hasUnlocked(res)) SlimefunPlugin.getLocal().sendMessage(sender, "messages.give-research", true, msg -> msg.replace("%player%", p.getName()).replace("%research%", res.getName()));
							res.unlock(p, true);
						}
					}
					else if (args[2].equalsIgnoreCase("reset")) {
						for (Research res : Research.list()) {
							profile.setResearched(res, false);
						}
						SlimefunPlugin.getLocal().sendMessage(p, "commands.research.reset", true, msg -> msg.replace("%player%", args[1]));
					}
					else {
						Research research = null;
						for (Research res : Research.list()) {
							if (res.getName().toUpperCase().replace(' ', '_').equalsIgnoreCase(args[2])) {
								research = res;
								break;
							}
						}
						
						if (research != null) {
							research.unlock(p, true);
							final Research r = research;
							SlimefunPlugin.getLocal().sendMessage(sender, "messages.give-research", true, msg -> msg.replace("%player%", p.getName()).replace("%research%", r.getName()));
						}
						else {
							SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-valid-research", true, msg -> msg.replace("%research%", args[2]));
						}
					}
				}
				else {
					SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-online", true, msg -> msg.replace("%player%", args[1]));
				}
			}
			else SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
		}
		else {
			SlimefunPlugin.getLocal().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf research <Player> <all/reset/Research>"));
		}
	}

}
