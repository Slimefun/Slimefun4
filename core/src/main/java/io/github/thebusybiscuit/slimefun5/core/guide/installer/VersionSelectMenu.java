package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

import com.cryptomorin.xseries.XMaterial;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

/**
 * A paginated list of all published releases of an addon. Releases are fetched async, then rendered on the
 * main thread. Selecting one installs that exact version - which is how a player downgrades to (or
 * reinstalls) an older release rather than only being able to jump to the latest.
 */
public final class VersionSelectMenu {

    private VersionSelectMenu() {}

    public static void open(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull AddonCatalog.Entry entry, int page) {
        Slimefun.instance().getServer().getScheduler().runTaskAsynchronously(Slimefun.instance(), () -> {
            List<AddonReleaseService.ReleaseInfo> releases = new AddonReleaseService().fetchReleases(entry);
            Slimefun.instance().getServer().getScheduler().runTask(Slimefun.instance(), () -> render(p, guide, entry, releases, page));
        });
    }

    private static void render(Player p, ItemStack guide, AddonCatalog.Entry entry, List<AddonReleaseService.ReleaseInfo> releases, int page) {
        ChestMenu menu = new ChestMenu(Slimefun.getLocalization().getMessage(p, "guide.title.installer"));
        menu.addMenuOpeningHandler(SoundEffect.GUIDE_BUTTON_CLICK_SOUND::playFor);
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, 0, 2, 3, 4, 5, 6, 7, 8, 45, 47, 48, 49, 50, 51, 53);

        menu.addItem(1, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK), Slimefun.getLocalization().getMessage(p, "guide.installer.back")));
        menu.addMenuClickHandler(1, (pl, slot, item, action) -> {
            AddonDetailMenu.open(pl, guide, entry);
            return false;
        });

        if (releases.isEmpty()) {
            menu.addItem(22, CustomItemStack.create(MaterialCompat.stack(XMaterial.BARRIER),
                Slimefun.getLocalization().getMessage(p, "guide.installer.versions.none"), "",
                Slimefun.getLocalization().getMessage(p, "guide.installer.versions.none-lore")));
            menu.addMenuClickHandler(22, ChestMenuUtils.getEmptyClickHandler());
            menu.open(p);
            return;
        }

        String installed = inst().getCachedLatestTag(entry.getId());

        for (int i = page * 36; i < releases.size() && i < (page + 1) * 36; i++) {
            AddonReleaseService.ReleaseInfo release = releases.get(i);
            boolean current = release.getTag().equals(installed);
            String title = (current ? "&a✔ " : "&e") + release.getTag();
            menu.addItem(i - page * 36 + 9, CustomItemStack.create(MaterialCompat.stack(XMaterial.PAPER), title, "",
                Slimefun.getLocalization().getMessage(p, "guide.installer.versions.click")));
            menu.addMenuClickHandler(i - page * 36 + 9, (pl, slot, item, action) -> {
                SoundEffect.ADDON_INSTALLER_WORKING_SOUND.playFor(pl);
                inst().installRelease(pl, entry, success -> {
                    (success ? SoundEffect.ADDON_INSTALLER_SUCCESS_SOUND : SoundEffect.ADDON_INSTALLER_FAIL_SOUND).playFor(pl);
                    AddonDetailMenu.open(pl, guide, entry);
                }, null, release);
                AddonDetailMenu.open(pl, guide, entry); // re-render into the "Installing…" state
                return false;
            });
        }

        int pages = (releases.size() - 1) / 36 + 1;

        menu.addItem(46, ChestMenuUtils.getPreviousButton(p, page + 1, pages));
        menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
            if (page > 0) {
                render(pl, guide, entry, releases, page - 1);
            }

            return false;
        });

        menu.addItem(52, ChestMenuUtils.getNextButton(p, page + 1, pages));
        menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
            if (page + 1 < pages) {
                render(pl, guide, entry, releases, page + 1);
            }

            return false;
        });

        menu.open(p);
    }

    private static AddonInstaller inst() {
        return AddonInstallerMenu.installer();
    }
}
