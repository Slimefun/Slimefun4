package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.core.guide.options.SlimefunGuideSettings;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
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

    /** Shared installer state for the JVM lifetime. Eager so two simultaneous opens can't create two instances. */
    private static final AddonInstaller INSTALLER = new AddonInstaller(new InstallState());

    private AddonInstallerMenu() {}

    @Nonnull
    public static AddonInstaller installer() {
        return INSTALLER;
    }

    public static void open(@Nonnull Player p, @Nonnull ItemStack guide) {
        ChestMenu menu = new ChestMenu(Slimefun.getLocalization().getMessage(p, "guide.title.installer"));
        menu.addMenuOpeningHandler(SoundEffect.GUIDE_BUTTON_CLICK_SOUND::playFor);
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        menu.addItem(49, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK), Slimefun.getLocalization().getMessage(p, "guide.installer.back-settings")));
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

            // Libraries auto-install with their dependents; they aren't user-facing addons.
            if (entry.isLibrary()) {
                continue;
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
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(StatusBadges.badge(p, inst, entry));

        Plugin plugin = inst.getLoadedPlugin(entry);

        if (plugin != null) {
            lore.add(Slimefun.getLocalization().getMessage(p, "guide.installer.version.release").replace("%version%", plugin.getDescription().getVersion()));
            List<String> authors = plugin.getDescription().getAuthors();

            if (authors != null && !authors.isEmpty()) {
                lore.add(Slimefun.getLocalization().getMessage(p, "guide.installer.info.authors").replace("%authors%", String.join(", ", authors)));
            }
        }

        lore.add("");
        lore.add(Slimefun.getLocalization().getMessage(p, "guide.installer.click-details"));
        return CustomItemStack.create(MaterialCompat.stack(entry.getIcon()), "&f" + entry.getDisplayName(), lore.toArray(new String[0]));
    }
}
