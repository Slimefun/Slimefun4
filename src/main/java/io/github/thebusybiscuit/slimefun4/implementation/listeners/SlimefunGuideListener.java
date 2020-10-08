package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.SlimefunGuideOpenEvent;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import io.github.thebusybiscuit.slimefun4.core.guide.options.SlimefunGuideSettings;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

public class SlimefunGuideListener implements Listener {

    private final boolean giveOnFirstJoin;

    public SlimefunGuideListener(@Nonnull SlimefunPlugin plugin, boolean giveOnFirstJoin) {
        this.giveOnFirstJoin = giveOnFirstJoin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (giveOnFirstJoin && !e.getPlayer().hasPlayedBefore()) {
            Player p = e.getPlayer();

            if (!SlimefunPlugin.getWorldSettingsService().isWorldEnabled(p.getWorld())) {
                return;
            }

            SlimefunGuideLayout type = SlimefunGuide.getDefaultLayout();
            p.getInventory().addItem(SlimefunGuide.getItem(type).clone());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerRightClickEvent e) {
        Player p = e.getPlayer();

        if (tryOpenGuide(p, e, SlimefunGuideLayout.BOOK) == Result.ALLOW) {
            if (p.isSneaking()) {
                SlimefunGuideSettings.openSettings(p, e.getItem());
            } else {
                openGuide(p, e, SlimefunGuideLayout.BOOK);
            }
        } else if (tryOpenGuide(p, e, SlimefunGuideLayout.CHEST) == Result.ALLOW) {
            if (p.isSneaking()) {
                SlimefunGuideSettings.openSettings(p, e.getItem());
            } else {
                openGuide(p, e, SlimefunGuideLayout.CHEST);
            }
        } else if (tryOpenGuide(p, e, SlimefunGuideLayout.CHEAT_SHEET) == Result.ALLOW) {
            if (p.isSneaking()) {
                SlimefunGuideSettings.openSettings(p, e.getItem());
            } else {
                // We rather just run the command here,
                // all necessary permission checks will be handled there.
                p.chat("/sf cheat");
            }
        }
    }

    @ParametersAreNonnullByDefault
    private void openGuide(Player p, PlayerRightClickEvent e, SlimefunGuideLayout layout) {
        SlimefunGuideOpenEvent event = new SlimefunGuideOpenEvent(p, e.getItem(), layout);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            e.cancel();
            SlimefunGuide.openGuide(p, event.getGuideLayout());
        }
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    private Result tryOpenGuide(Player p, PlayerRightClickEvent e, SlimefunGuideLayout layout) {
        ItemStack item = e.getItem();
        if (SlimefunUtils.isItemSimilar(item, SlimefunGuide.getItem(layout), true, false)) {

            if (!SlimefunPlugin.getWorldSettingsService().isWorldEnabled(p.getWorld())) {
                SlimefunPlugin.getLocalization().sendMessage(p, "messages.disabled-item", true);
                return Result.DENY;
            }

            return Result.ALLOW;
        }

        return Result.DEFAULT;
    }

}
