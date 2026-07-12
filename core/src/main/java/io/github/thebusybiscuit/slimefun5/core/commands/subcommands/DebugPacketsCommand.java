package io.github.thebusybiscuit.slimefun5.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun5.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun5.core.services.localization.ItemTranslationService;
import io.github.thebusybiscuit.slimefun5.core.services.localization.Language;
import io.github.thebusybiscuit.slimefun5.core.services.localization.TranslationConfig;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.HandCompat;
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
            Player player = (Player) sender;
            Object ch = PacketReflect.channelOf(player);
            boolean injected = ch instanceof Channel && ((Channel) ch).pipeline().get("slimefun-translate") != null;
            sender.sendMessage("  channel resolved=" + (ch != null) + ", handler injected=" + injected);

            selfTest(sender, player);
        }
    }

    /**
     * Best-effort self-test of the NMS conversion chain on the item in the sender's main hand, so a single
     * in-game run reconfirms {@code asNMSCopy}/{@code asBukkitCopy} resolved the correct overload. Never
     * throws out of the command.
     */
    private void selfTest(CommandSender sender, Player player) {
        try {
            Class<?> paramType = PacketReflect.asNmsParamType();
            sender.sendMessage("  asNMSCopy param type=" + (paramType != null ? paramType.getName() : "null"));

            ItemStack hand = HandCompat.getMainHand(player.getInventory());
            if (hand == null || hand.getType() == Material.AIR) {
                sender.sendMessage("  (no item in main hand to self-test)");
                return;
            }

            Object nms = PacketReflect.asNms(hand);
            sender.sendMessage("  asNms(hand) != null: " + (nms != null));

            String id = Slimefun.getItemDataService().getItemData(hand).orElse(null);
            if (id == null) {
                sender.sendMessage("  (hand item is not a registered Slimefun item)");
                return;
            }
            sender.sendMessage("  hand item id=" + id);

            Language language = Slimefun.getLocalization().getLanguage(player);
            String languageId = language != null ? language.getId() : null;
            ItemTranslationService.RenderedDisplay display = Slimefun.getItemTranslationService()
                .renderForPacket(id, languageId, TranslationConfig.fallback(), true);
            sender.sendMessage("  renderForPacket(...).name=" + (display != null ? display.name : "null"));
        } catch (Throwable t) {
            sender.sendMessage("  self-test failed: " + t);
        }
    }
}
