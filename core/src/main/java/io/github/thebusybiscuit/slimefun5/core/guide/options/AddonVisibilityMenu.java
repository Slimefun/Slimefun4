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
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        menu.addItem(49, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK),
            "&e⇦ Back"));
        menu.addMenuClickHandler(49, (pl, slot, item, action) -> {
            SlimefunGuide.openGuide(pl, guide);
            return false;
        });

        // Info header explaining the toggle + the "at least one" rule.
        menu.addItem(4, CustomItemStack.create(MaterialCompat.stack(XMaterial.BOOK),
            "&eAddon Visibility",
            "",
            "&7Click an addon to toggle whether it",
            "&7appears in your guide (browse + search).",
            "",
            "&7At least one addon must stay shown."));
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
                visible ? "&a✔ Shown" : "&c✖ Hidden",
                "",
                "&7⇨ &eClick to toggle");

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
                        pl.sendMessage(ChatColor.RED + "At least one addon must stay visible.");
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
