package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import java.util.Collection;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public final class CommandSetup {

    private CommandSetup() {}

    public static void addCommands(SlimefunCommand cmd, Collection<SubCommand> commands) {
        SlimefunPlugin plugin = cmd.getPlugin();

        commands.add(new HelpCommand(plugin, cmd));
        commands.add(new VersionsCommand(plugin, cmd));
        commands.add(new CheatCommand(plugin, cmd));
        commands.add(new GuideCommand(plugin, cmd));
        commands.add(new GiveCommand(plugin, cmd));
        commands.add(new ResearchCommand(plugin, cmd));
        commands.add(new StatsCommand(plugin, cmd));
        commands.add(new TimingsCommand(plugin, cmd));
        commands.add(new TeleporterCommand(plugin, cmd));
        commands.add(new OpenGuideCommand(plugin, cmd));
        commands.add(new SearchCommand(plugin, cmd));
    }
}
