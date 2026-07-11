package io.github.thebusybiscuit.slimefun5.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun5.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun5.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun5.core.services.localization.TranslationConfig;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.packet.PacketItemDescriptor;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.packet.PacketReflect;
import io.netty.channel.Channel;

/**
 * {@code /sf debugpackets} reports whether the packet-based item translation layer is wired up on this
 * server: whether it is enabled, how many {@link PacketItemDescriptor}s resolved for this Minecraft
 * version, and (for a player sender) whether their Netty pipeline carries the {@code slimefun-translate}
 * handler. Purely diagnostic - it changes nothing.
 * Admin-only ({@code slimefun.command.debugpackets}, default op).
 */
class DebugPacketsCommand extends SubCommand {

    @ParametersAreNonnullByDefault
    DebugPacketsCommand(Slimefun plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "debugpackets", true);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onExecute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("slimefun.command.debugpackets")) {
            Slimefun.getLocalization().sendMessage(sender, "messages.no-permission", true);
            return;
        }

        boolean enabled = TranslationConfig.packetsEnabled();
        int descriptors = PacketItemDescriptor.resolveAll().size();
        sender.sendMessage("Slimefun packet translation: enabled=" + enabled + ", descriptors=" + descriptors);

        if (sender instanceof Player) {
            Object ch = PacketReflect.channelOf((Player) sender);
            boolean injected = ch instanceof Channel && ((Channel) ch).pipeline().get("slimefun-translate") != null;
            sender.sendMessage("  channel resolved=" + (ch != null) + ", handler injected=" + injected);
        }
    }
}
