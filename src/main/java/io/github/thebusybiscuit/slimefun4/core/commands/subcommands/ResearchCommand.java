package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.players.PlayerList;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.Research;

class ResearchCommand extends SubCommand {

    private static final String PLACEHOLDER_PLAYER = "%player%";
    private static final String PLACEHOLDER_RESEARCH = "%research%";

    ResearchCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd);
    }

    @Override
    public String getName() {
        return "research";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    protected String getDescription() {
        return "commands.research.description";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (args.length == 3) {
            if (sender.hasPermission("slimefun.cheat.researches") || !(sender instanceof Player)) {
                Optional<Player> player = PlayerList.findByName(args[1]);

                if (player.isPresent()) {
                    Player p = player.get();

                    // Getting the PlayerProfile async
                    PlayerProfile.get(p, profile -> {
                        if (args[2].equalsIgnoreCase("all")) {
                            researchAll(sender, profile, p);
                        }
                        else if (args[2].equalsIgnoreCase("reset")) {
                            reset(profile, p);
                        }
                        else {
                            giveResearch(sender, p, args[2]);
                        }
                    });
                }
                else {
                    SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-online", true, msg -> msg.replace(PLACEHOLDER_PLAYER, args[1]));
                }
            }
            else SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
        }
        else {
            SlimefunPlugin.getLocal().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf research <Player> <all/reset/Research>"));
        }
    }

    private void giveResearch(CommandSender sender, Player p, String input) {
        Optional<Research> research = getResearchFromString(input);

        if (research.isPresent()) {
            research.get().unlock(p, true);
            SlimefunPlugin.getLocal().sendMessage(sender, "messages.give-research", true, msg -> msg.replace(PLACEHOLDER_PLAYER, p.getName()).replace(PLACEHOLDER_RESEARCH, research.get().getName(p)));
        }
        else {
            SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-valid-research", true, msg -> msg.replace(PLACEHOLDER_RESEARCH, input));
        }
    }

    private void researchAll(CommandSender sender, PlayerProfile profile, Player p) {
        for (Research res : SlimefunPlugin.getRegistry().getResearches()) {
            if (!profile.hasUnlocked(res)) {
                SlimefunPlugin.getLocal().sendMessage(sender, "messages.give-research", true, msg -> msg.replace(PLACEHOLDER_PLAYER, p.getName()).replace(PLACEHOLDER_RESEARCH, res.getName(p)));
            }

            res.unlock(p, true);
        }
    }

    private void reset(PlayerProfile profile, Player p) {
        for (Research research : SlimefunPlugin.getRegistry().getResearches()) {
            profile.setResearched(research, false);
        }

        SlimefunPlugin.getLocal().sendMessage(p, "commands.research.reset", true, msg -> msg.replace(PLACEHOLDER_PLAYER, p.getName()));
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
