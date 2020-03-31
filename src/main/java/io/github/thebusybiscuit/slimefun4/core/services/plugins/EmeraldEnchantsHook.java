package io.github.thebusybiscuit.slimefun4.core.services.plugins;

import me.mrCookieSlime.CSCoreLibPlugin.PlayerRunnable;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.EmeraldEnchants.EnchantmentGuide;
import me.mrCookieSlime.Slimefun.api.GuideHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

class EmeraldEnchantsHook implements GuideHandler {

    private PlayerRunnable runnable;

    public EmeraldEnchantsHook() {
        runnable = new PlayerRunnable(-1) {

            @Override
            public void run(Player p) {
                EnchantmentGuide.open(p);
            }
        };
    }

    @Override
    public int next(Player p, int index, ChestMenu menu) {
        menu.addItem(index, new CustomItem(Material.ENCHANTED_BOOK, "&2EmeraldEnchants &a(Enchantment Guide)", "", "&a> Click to view a List of all custom Enchantments"));
        menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
            EnchantmentGuide.open(p);
            return false;
        });

        return index + 1;
    }

    @Override
    public void addEntry(List<String> text, List<String> tooltip) {
        text.add(ChatColor.translateAlternateColorCodes('&', "&2Enchantment Guide"));
        tooltip.add(ChatColor.translateAlternateColorCodes('&', "&aClick to open\n&aEmeraldEnchants' Enchantment Guide"));
    }

    @Override
    public int getTier() {
        return 1;
    }

    @Override
    public PlayerRunnable getRunnable() {
        return runnable;
    }

    @Override
    public boolean trackHistory() {
        return false;
    }

}