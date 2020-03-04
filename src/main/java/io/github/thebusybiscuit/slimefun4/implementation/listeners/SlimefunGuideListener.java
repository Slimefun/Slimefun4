package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideSettings;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;

public class SlimefunGuideListener implements Listener {

    private final boolean giveOnFirstJoin;

    public SlimefunGuideListener(SlimefunPlugin plugin, boolean giveOnFirstJoin) {
        this.giveOnFirstJoin = giveOnFirstJoin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (giveOnFirstJoin && !e.getPlayer().hasPlayedBefore()) {
            Player p = e.getPlayer();
            if (!SlimefunPlugin.getWhitelist().getBoolean(p.getWorld().getName() + ".enabled")) return;
            if (!SlimefunPlugin.getWhitelist().getBoolean(p.getWorld().getName() + ".enabled-items.SLIMEFUN_GUIDE")) return;

            SlimefunGuideLayout type = SlimefunPlugin.getCfg().getBoolean("guide.default-view-book") ? SlimefunGuideLayout.BOOK : SlimefunGuideLayout.CHEST;
            p.getInventory().addItem(SlimefunGuide.getItem(type));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerRightClickEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();

        if (SlimefunManager.isItemSimilar(item, SlimefunGuide.getItem(SlimefunGuideLayout.BOOK), true)) {
            e.cancel();

            if (p.isSneaking()) {
                SlimefunGuideSettings.openSettings(p, item);
            }
            else {
                SlimefunGuide.openGuide(p, SlimefunGuideLayout.BOOK);
            }
        }
        else if (SlimefunManager.isItemSimilar(item, SlimefunGuide.getItem(SlimefunGuideLayout.CHEST), true)) {
            e.cancel();

            if (p.isSneaking()) {
                SlimefunGuideSettings.openSettings(p, item);
            }
            else {
                SlimefunGuide.openGuide(p, SlimefunGuideLayout.CHEST);
            }
        }
        else if (SlimefunManager.isItemSimilar(item, SlimefunGuide.getItem(SlimefunGuideLayout.CHEAT_SHEET), true)) {
            e.cancel();

            if (p.isSneaking()) {
                SlimefunGuideSettings.openSettings(p, item);
            }
            else {
                // We rather just run the command here,
                // all necessary permission checks will be handled there.
                p.chat("/sf cheat");
            }
        }
    }

}
