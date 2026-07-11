package io.github.thebusybiscuit.slimefun5.core.services.localization;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun5.api.events.PlayerLanguageChangeEvent;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.PdcCompat;
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

    /** Sentinel stored in {@link #languageCache} for "no explicit language / no known locale" (ConcurrentHashMap forbids null values). */
    private static final String NO_LANGUAGE = "";

    private final List<PacketItemDescriptor> descriptors;

    /**
     * Effective language id per player, refreshed on the main thread (join / language change) and read
     * verbatim by the Netty write handler. This is the ONLY thing the Netty thread may touch - it must
     * never call into {@link LocalizationService#getLanguage(Player)} or entity PDC directly, both of
     * which are main-thread-only (and on 1.8-1.13 the legacy-pdc.yml fallback path is a shared,
     * synchronized-guarded map that a Netty thread would read outside that guard, racing main-thread writes).
     */
    private final Map<UUID, String> languageCache = new ConcurrentHashMap<>();

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
            refreshLanguage(p);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        inject(e.getPlayer());
        refreshLanguage(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        // Channel is torn down by the server; nothing to clean up explicitly.
        languageCache.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onLanguageChange(PlayerLanguageChangeEvent e) {
        // Mirrors ItemTranslationListener: the new language is only applied to the player after this
        // event resolves, so re-resolve one tick later - still on the main thread.
        Player p = e.getPlayer();
        Slimefun.runSync(() -> refreshLanguage(p), 1L);
    }

    /**
     * Computes the effective packet-translation language for a player and stores it in the thread-safe
     * cache. Must run on the main thread: it reads the player's explicitly-chosen Slimefun language (PDC)
     * and, failing that, their MC client locale.
     */
    private void refreshLanguage(@Nonnull Player p) {
        String explicit = PdcCompat.getString(p, Slimefun.getLocalization().getKey());
        if (explicit != null) {
            languageCache.put(p.getUniqueId(), explicit);
            return;
        }

        try {
            // Reflective: Player.getLocale() was added after the 1.8.8 Bukkit API this module compiles against.
            String locale = (String) p.getClass().getMethod("getLocale").invoke(p);
            if (locale != null && locale.length() >= 2) {
                languageCache.put(p.getUniqueId(), locale.substring(0, 2).toLowerCase(java.util.Locale.ROOT));
                return;
            }
        } catch (Throwable ignored) {
            // fall through
        }

        languageCache.put(p.getUniqueId(), NO_LANGUAGE);
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
            // Logged (once per join, not spammy) so an operator on a fork with a differently-named
            // pipeline stage has a signal instead of silently losing packet translation.
            Slimefun.logger().info("Could not inject packet translation handler for " + player.getName()
                + " (pipeline stage \"packet_handler\" missing or locked); items will use their configured fallback.");
        }
    }

    /** Returns msg (possibly a rewritten packet). Never throws — on any failure returns msg unchanged. */
    private Object translate(Player player, Object msg) {
        try {
            for (PacketItemDescriptor descriptor : descriptors) {
                if (descriptor.matches(msg)) {
                    // Pure cache read - no getLanguage()/entity PDC access on the Netty thread.
                    String cached = languageCache.get(player.getUniqueId());
                    final String language = NO_LANGUAGE.equals(cached) ? null : cached;
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

}
