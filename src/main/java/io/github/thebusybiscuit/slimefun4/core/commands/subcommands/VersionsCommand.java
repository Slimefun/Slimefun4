package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import java.util.Collection;

import javax.annotation.Nonnull;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.papermc.lib.PaperLib;

class VersionsCommand extends SubCommand {

    VersionsCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "versions", false);
    }

    @Override
    public void onExecute(@Nonnull CommandSender sender, @Nonnull String[] args) {
        if (sender.hasPermission("slimefun.command.versions") || sender instanceof ConsoleCommandSender) {
            // After all these years... Spigot still displays as "CraftBukkit"
            // so we will just fix this inconsistency for them :)
            String serverSoftware = PaperLib.isSpigot() && !PaperLib.isPaper() ? "Spigot" : Bukkit.getName();

            ComponentBuilder builder = new ComponentBuilder();
            builder.append("This Server uses the following setup of Slimefun:\n").color(ChatColor.GRAY)
                .append(serverSoftware).color(ChatColor.GREEN).append(" " + Bukkit.getVersion() + '\n').color(ChatColor.DARK_GREEN)
                .append("Slimefun").color(ChatColor.GREEN).append(" v" + SlimefunPlugin.getVersion() + '\n').color(ChatColor.DARK_GREEN);

            if (SlimefunPlugin.getMetricsService().getVersion() != null) {
                builder.append("Metrics build: ").color(ChatColor.GREEN)
                    .append("#" + SlimefunPlugin.getMetricsService().getVersion() + '\n').color(ChatColor.DARK_GREEN);
            }

            addJavaVersion(builder);

            if (SlimefunPlugin.getRegistry().isBackwardsCompatible()) {
                builder.append("Backwards compatibility enabled!\n").color(ChatColor.RED);
            }

            builder.append("\n");

            addPluginVersions(builder);

            System.out.println(ComponentSerializer.toString(builder.create()));
            sender.spigot().sendMessage(builder.create());
        } else {
            SlimefunPlugin.getLocalization().sendMessage(sender, "messages.no-permission", true);
        }
    }

    private void addJavaVersion(@Nonnull ComponentBuilder builder) {
        String javaVer = System.getProperty("java.version");
        if (javaVer.startsWith("1.")) {
            javaVer = javaVer.substring(2);
        }
        if (javaVer.indexOf('.') != -1) { // If it's like 11.0.1.3 or 8.0_275
            javaVer = javaVer.substring(0, javaVer.indexOf('.'));
        }
        int ver = Integer.parseInt(javaVer);

        if (ver < 11) {
            builder.append("Java " + ver).color(ChatColor.RED)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                    "You should be using at least Java 11! Paper will be dropping support for before Java 11 starting at MC 1.17"
                )))
                .append("\n")
                .event((HoverEvent) null);
        } else {
            builder.append("Java " + ver + "\n").color(ChatColor.GREEN);
        }
    }

    private void addPluginVersions(@Nonnull ComponentBuilder builder) {
        Collection<Plugin> addons = SlimefunPlugin.getInstalledAddons();
        builder.append("Installed Addons: ").color(ChatColor.GRAY)
            .append("(" + addons.size() + ")").color(ChatColor.DARK_GRAY);

        for (Plugin plugin : addons) {
            String version = plugin.getDescription().getVersion();

            if (Bukkit.getPluginManager().isPluginEnabled(plugin)) {
                builder.append("\n  " + plugin.getName()).color(ChatColor.GREEN)
                    .append(" v" + version).color(ChatColor.DARK_GREEN);
            } else {
                HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new Text("Plugin is disabled. Check the console for an error and report on their issue tracker."));

                // We need to reset the hover event or it's added to all components
                builder.append("\n  " + plugin.getName()).color(ChatColor.RED).event(hover)
                    .append(" v" + version).color(ChatColor.DARK_RED)
                    .append("").event((HoverEvent) null);
            }
        }
    }
}
