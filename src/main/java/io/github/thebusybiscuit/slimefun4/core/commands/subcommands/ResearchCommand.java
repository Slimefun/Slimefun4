package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import java.util.Optional;
import java.util.function.UnaryOperator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.players.PlayerList;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

class ResearchCommand extends SubCommand {

    private static final String PLACEHOLDER_PLAYER = "%player%";
    private static final String PLACEHOLDER_RESEARCH = "%research%";

    ResearchCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "research", false);
    }

    @Override
    protected String getDescription() {
        return "commands.research.description";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (args.length == 3) {
            if (!(sender instanceof Player) || sender.hasPermission("slimefun.cheat.researches")) {
                Optional<Player> player = PlayerList.findByName(args[1]);

                if (player.isPresent()) {
                    Player p = player.get();

                    // Getting the PlayerProfile async
                    PlayerProfile.get(p, profile -> {
                        if (args[2].equalsIgnoreCase("all")) {
                            researchAll(sender, profile, p);
                        } else if (args[2].equalsIgnoreCase("reset")) {
                            reset(profile, p);
                        } else {
                            giveResearch(sender, p, args[2]);
                        }
                    });
                } else {
                    SlimefunPlugin.getLocalization().sendMessage(sender, "messages.not-online", true, msg -> msg.replace(PLACEHOLDER_PLAYER, args[1]));
                }
            } else {
                SlimefunPlugin.getLocalization().sendMessage(sender, "messages.no-permission", true);
            }
        } else {
            SlimefunPlugin.getLocalization().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf research <Player> <all/reset/Research>"));
        }
    }

    private void giveResearch(CommandSender sender, Player p, String input) {
        Optional<Research> research = getResearchFromString(input);

        if (research.isPresent()) {
            research.get().unlock(p, true, player -> {
                UnaryOperator<String> variables = msg -> msg.replace(PLACEHOLDER_PLAYER, player.getName()).replace(PLACEHOLDER_RESEARCH, research.get().getName(player));
                SlimefunPlugin.getLocalization().sendMessage(player, "messages.give-research", true, variables);
            });
        } else {
            SlimefunPlugin.getLocalization().sendMessage(sender, "messages.not-valid-research", true, msg -> msg.replace(PLACEHOLDER_RESEARCH, input));
        }
    }

    private void researchAll(CommandSender sender, PlayerProfile profile, Player p) {
        for (Research res : SlimefunPlugin.getRegistry().getResearches()) {
            if (!profile.hasUnlocked(res)) {
                SlimefunPlugin.getLocalization().sendMessage(sender, "messages.give-research", true, msg -> msg.replace(PLACEHOLDER_PLAYER, p.getName()).replace(PLACEHOLDER_RESEARCH, res.getName(p)));
            }

            res.unlock(p, true);
        }
    }

    private void reset(PlayerProfile profile, Player p) {
        for (Research research : SlimefunPlugin.getRegistry().getResearches()) {
            profile.setResearched(research, false);
        }

        SlimefunPlugin.getLocalization().sendMessage(p, "commands.research.reset", true, msg -> msg.replace(PLACEHOLDER_PLAYER, p.getName()));
    }

    private Optional<Research> getResearchFromString(String input) {
        if (!input.contains(":")) {
            return Optional.empty();
        }

        for (Research research : SlimefunPlugin.getRegistry().getResearches()) {
            if (research.getKey().toString().equalsIgnoreCase(input)) {
                return Optional.of(research);
            }
        }

        return Optional.empty();
    }

}
