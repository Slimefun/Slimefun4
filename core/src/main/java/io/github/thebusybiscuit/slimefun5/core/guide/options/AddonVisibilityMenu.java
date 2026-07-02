package io.github.thebusybiscuit.slimefun5.core.guide.options;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.cryptomorin.xseries.XMaterial;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.core.guide.AddonVisibility;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

/**
 * A per-player menu listing every installed addon with an on/off toggle controlling whether that addon's
 * categories and items appear in the player's guide (browse + search). Backed by {@link AddonVisibility}.
 */
public final class AddonVisibilityMenu {

    private static final int[] BORDER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 45, 46, 47, 48, 50, 51, 52, 53 };

    private AddonVisibilityMenu() {}

    public static void open(@Nonnull Player p, @Nonnull ItemStack guide) {
        ChestMenu menu = new ChestMenu(Slimefun.getLocalization().getMessage(p, "guide.title.addon-visibility"));
        menu.addMenuOpeningHandler(SoundEffect.GUIDE_BUTTON_CLICK_SOUND::playFor);
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        menu.addItem(49, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK),
            Slimefun.getLocalization().getMessage(p, "guide.addon-visibility.back")));
        menu.addMenuClickHandler(49, (pl, slot, item, action) -> {
            SlimefunGuide.openGuide(pl, guide);
            return false;
        });

        // Info header explaining the toggle + the "at least one" rule.
        java.util.List<String> header = new java.util.ArrayList<>();
        header.add(Slimefun.getLocalization().getMessage(p, "guide.addon-visibility.menu-title"));
        header.add("");
        header.addAll(Slimefun.getLocalization().getMessages(p, "guide.addon-visibility.menu-lore"));
        menu.addItem(4, CustomItemStack.create(MaterialCompat.stack(XMaterial.BOOK), header));
        menu.addMenuClickHandler(4, ChestMenuUtils.getEmptyClickHandler());

        // Map of addon id (the category NamespacedKey namespace, lowercased) -> display name. A map also
        // dedupes in case Slimefun lists itself among the installed addons.
        Map<String, String> addons = new LinkedHashMap<>();
        addons.put("slimefun", "Slimefun");
        for (Plugin addon : Slimefun.getInstalledAddons()) {
            addons.put(addon.getName().toLowerCase(), addon.getName());
        }

        int slot = 9;
        for (Map.Entry<String, String> entry : addons.entrySet()) {
            if (slot >= 45) {
                Slimefun.logger().warning("[Guide] Addon Visibility menu is full; "
                    + (addons.size() - (slot - 9)) + " addon(s) beyond the first " + (slot - 9) + " are not listed.");
                break;
            }

            String addonId = entry.getKey();
            boolean visible = !AddonVisibility.isHidden(p, addonId);
            ItemStack icon = CustomItemStack.create(
                MaterialCompat.stack(visible ? XMaterial.LIME_STAINED_GLASS_PANE : XMaterial.GRAY_STAINED_GLASS_PANE),
                (visible ? "&a" : "&7") + entry.getValue(),
                "",
                visible ? Slimefun.getLocalization().getMessage(p, "guide.addon-visibility.shown") : Slimefun.getLocalization().getMessage(p, "guide.addon-visibility.hidden"),
                "",
                Slimefun.getLocalization().getMessage(p, "guide.addon-visibility.toggle"));

            menu.addItem(slot, icon);
            menu.addMenuClickHandler(slot, (pl, sl, item, action) -> {
                // Enforce at least one shown: refuse to hide the last visible addon.
                if (visible) {
                    int shown = 0;
                    for (String id : addons.keySet()) {
                        if (!AddonVisibility.isHidden(pl, id)) {
                            shown++;
                        }
                    }
                    if (shown <= 1) {
                        pl.sendMessage(ChatColor.translateAlternateColorCodes('&', Slimefun.getLocalization().getMessage(pl, "guide.addon-visibility.must-stay")));
                        return false;
                    }
                }

                AddonVisibility.setHidden(pl, addonId, visible);
                open(pl, guide);
                return false;
            });
            slot++;
        }

        menu.open(p);
    }
}
