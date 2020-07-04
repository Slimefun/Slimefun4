package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

import java.util.Collection;

public final class Commands {

    private Commands() {
    }

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
        commands.add(new DebugFishCommand(plugin, cmd));
        commands.add(new ReloadCommand(plugin, cmd));
        commands.add(new TransformCommand(plugin, cmd));
        commands.add(new BackpackCommand(plugin, cmd));
    }
}
