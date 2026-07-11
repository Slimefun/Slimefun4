package io.github.thebusybiscuit.slimefun5.core.services.localization;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.packet.PacketItemDescriptor;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.packet.PacketReflect;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * Rewrites outbound inventory packets so each player sees Slimefun items in their own language, without
 * mutating the stored item. A no-op if translation.packets is disabled or no packet descriptor resolves
 * on this server version (both leave items at their raw-id canonical = the fallback).
 */
public class PacketTranslationService implements Listener {

    private static final String HANDLER_NAME = "slimefun-translate";

    private final List<PacketItemDescriptor> descriptors;

    public PacketTranslationService(@Nonnull Slimefun plugin) {
        this.descriptors = PacketItemDescriptor.resolveAll();

        if (!TranslationConfig.packetsEnabled() || descriptors.isEmpty()) {
            Slimefun.logger().info("Packet item translation disabled or unsupported on this version; "
                + "items use their configured fallback.");
            return;
        }

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        injectAll();
    }

    public void injectAll() {
        for (Player p : Slimefun.instance().getServer().getOnlinePlayers()) {
            inject(p);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        inject(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        // Channel is torn down by the server; nothing to clean up explicitly.
    }

    private void inject(Player player) {
        Object channelObj = PacketReflect.channelOf(player);
        if (!(channelObj instanceof io.netty.channel.Channel)) {
            return;
        }
        io.netty.channel.Channel channel = (io.netty.channel.Channel) channelObj;

        try {
            if (channel.pipeline().get(HANDLER_NAME) != null) {
                return; // already injected
            }
            channel.pipeline().addBefore("packet_handler", HANDLER_NAME, new ChannelDuplexHandler() {
                @Override
                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                    Object out = translate(player, msg);
                    super.write(ctx, out, promise);
                }
            });
        } catch (Throwable t) {
            // "packet_handler" absent or pipeline locked → skip; player sees canonical id (fallback).
        }
    }

    /** Returns msg (possibly a rewritten packet). Never throws — on any failure returns msg unchanged. */
    private Object translate(Player player, Object msg) {
        try {
            for (PacketItemDescriptor descriptor : descriptors) {
                if (descriptor.matches(msg)) {
                    String language = languageOf(player);
                    TranslationConfig.FallbackMode fallback = TranslationConfig.fallback();
                    return descriptor.rewrite(msg, nmsItem -> rewriteItem(nmsItem, language, fallback));
                }
            }
        } catch (Throwable ignored) {
            // fall through
        }
        return msg;
    }

    private Object rewriteItem(Object nmsItem, String language, TranslationConfig.FallbackMode fallback) {
        ItemStack bukkit = PacketReflect.asBukkit(nmsItem);
        if (bukkit == null || !bukkit.hasItemMeta()) {
            return nmsItem;
        }
        String id = Slimefun.getItemDataService().getItemData(bukkit).orElse(null);
        if (id == null) {
            return nmsItem;
        }
        ItemTranslationService.RenderedDisplay display =
            Slimefun.getItemTranslationService().renderForPacket(id, language, fallback);
        if (display == null) {
            return nmsItem;
        }
        ItemMeta meta = bukkit.getItemMeta();
        if (meta == null) {
            return nmsItem;
        }
        meta.setDisplayName(display.name);
        meta.setLore(display.lore.isEmpty() ? null : display.lore);
        bukkit.setItemMeta(meta);
        Object rewritten = PacketReflect.asNms(bukkit);
        return rewritten != null ? rewritten : nmsItem;
    }

    /** Player's Slimefun language, else their MC client locale, else null (renderForPacket handles fallback). */
    private String languageOf(Player player) {
        try {
            Object lang = Slimefun.getLocalization().getLanguage(player);
            if (lang != null) {
                return (String) lang.getClass().getMethod("getId").invoke(lang);
            }
        } catch (Throwable ignored) {
            // fall through to client locale
        }
        try {
            // Reflective: Player.getLocale() was added after the 1.8.8 Bukkit API this module compiles
            // against (mirrors the getId() reflection above), so it cannot be called directly here.
            String locale = (String) player.getClass().getMethod("getLocale").invoke(player);
            if (locale != null && locale.length() >= 2) {
                return locale.substring(0, 2);
            }
        } catch (Throwable ignored) {
            // fall through
        }
        return null;
    }
}
