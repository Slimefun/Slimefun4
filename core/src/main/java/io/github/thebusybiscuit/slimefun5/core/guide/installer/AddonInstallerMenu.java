package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.core.guide.options.SlimefunGuideSettings;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

import com.cryptomorin.xseries.XMaterial;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

/**
 * Screen 1 of the installer: a grid of Slimefun core + every addon with a status badge in lore.
 * Clicking an entry opens {@link AddonDetailMenu}.
 */
public final class AddonInstallerMenu {

    private static final int[] BORDER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 45, 46, 47, 48, 50, 51, 52, 53 };

    /** Shared installer state for the JVM lifetime — created lazily on first open. */
    private static AddonInstaller installer;

    private AddonInstallerMenu() {}

    @Nonnull
    public static AddonInstaller installer() {
        if (installer == null) {
            installer = new AddonInstaller(new InstallState());
        }

        return installer;
    }

    public static void open(@Nonnull Player p, @Nonnull ItemStack guide) {
        ChestMenu menu = new ChestMenu(Slimefun.getLocalization().getMessage(p, "guide.title.installer"));
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        menu.addItem(49, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK), "&e⇦ Back to Settings"));
        menu.addMenuClickHandler(49, (pl, slot, item, action) -> {
            SlimefunGuideSettings.openSettings(pl, guide);
            return false;
        });

        AddonInstaller inst = installer();
        List<AddonCatalog.Entry> entries = AddonCatalog.getEntries();

        int slot = 9;

        for (AddonCatalog.Entry entry : entries) {
            if (slot >= 45) {
                break;
            }

            menu.addItem(slot, icon(p, inst, entry));
            menu.addMenuClickHandler(slot, (pl, sl, item, action) -> {
                AddonDetailMenu.open(pl, guide, entry);
                return false;
            });
            slot++;
        }

        menu.open(p);
    }

    @Nonnull
    private static ItemStack icon(Player p, AddonInstaller inst, AddonCatalog.Entry entry) {
        String badge = StatusBadges.badge(inst, entry);
        return CustomItemStack.create(MaterialCompat.stack(entry.getIcon()),
            "&f" + entry.getDisplayName(),
            "",
            badge,
            "",
            "&7⇨ &eClick for details");
    }
}
