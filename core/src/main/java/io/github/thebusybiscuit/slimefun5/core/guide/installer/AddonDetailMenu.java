package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

import com.cryptomorin.xseries.XMaterial;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

/**
 * Screen 2: one entry's status and actions. The "Install/Update" action (Mode A) is always
 * available; "Build from branch" (Mode B) appears only when the dev environment supports it.
 * The button label switches between "Install" and "Update" based on whether the plugin is loaded;
 * the actual latest-release lookup happens when the player clicks (inside AddonInstaller, async),
 * so opening the menu makes no network call.
 */
public final class AddonDetailMenu {

    private static final int[] BORDER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };

    private AddonDetailMenu() {}

    public static void open(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull AddonCatalog.Entry entry) {
        ChestMenu menu = new ChestMenu(Slimefun.getLocalization().getMessage(p, "guide.title.installer"));
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        AddonInstaller inst = AddonInstallerMenu.installer();

        menu.addItem(0, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK), "&e⇦ Back"));
        menu.addMenuClickHandler(0, (pl, slot, item, action) -> {
            AddonInstallerMenu.open(pl, guide);
            return false;
        });

        List<String> deps = new ArrayList<>();

        for (AddonCatalog.Entry dep : AddonCatalog.resolveDependencies(entry)) {
            if (!inst.isLoaded(dep)) {
                deps.add(dep.getDisplayName());
            }
        }

        // Header (the entry itself).
        List<String> headerLore = new ArrayList<>();
        headerLore.add("");
        headerLore.add(StatusBadges.badge(inst, entry));

        if (!deps.isEmpty()) {
            headerLore.add("&7Will also install: &f" + String.join(", ", deps));
        }

        menu.addItem(13, CustomItemStack.create(MaterialCompat.stack(entry.getIcon()), "&f" + entry.getDisplayName(), headerLore.toArray(new String[0])));
        menu.addMenuClickHandler(13, ChestMenuUtils.getEmptyClickHandler());

        // Action: install/update from latest release (always available).
        String label = inst.isLoaded(entry) ? "&aUpdate to latest release" : "&aInstall latest release";
        menu.addItem(29, CustomItemStack.create(MaterialCompat.stack(XMaterial.LIME_DYE), label, "", "&7Downloads the latest GitHub release", "&7and stages it for the next restart.", "", "&7⇨ &eClick"));
        menu.addMenuClickHandler(29, (pl, slot, item, action) -> {
            inst.installRelease(pl, entry);
            pl.closeInventory();
            return false;
        });

        // Action: build from branch (Mode B, dev only).
        if (EnvironmentDetector.canBuildFromSource()) {
            menu.addItem(33, CustomItemStack.create(MaterialCompat.stack(XMaterial.WHEAT_SEEDS), "&bBuild from a branch", "", "&7Clone & compile a chosen branch", "&7(developer environments only).", "", "&7⇨ &eClick to pick a branch"));
            menu.addMenuClickHandler(33, (pl, slot, item, action) -> {
                BranchSelectMenu.open(pl, guide, entry, 0);
                return false;
            });
        }

        menu.open(p);
    }
}
