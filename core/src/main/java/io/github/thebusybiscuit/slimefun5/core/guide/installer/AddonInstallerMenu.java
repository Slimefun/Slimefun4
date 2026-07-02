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
        boolean canManage = p.hasPermission(AddonCatalog.PERMISSION);
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

            menu.addItem(slot, icon(p, inst, entry, canManage));
            menu.addMenuClickHandler(slot, (pl, sl, item, action) -> {
                // Left-click opens details; right-click installs/updates straight from the grid.
                if (canManage && action.isRightClicked() && !inst.isInProgress(entry.getId())) {
                    SoundEffect.ADDON_INSTALLER_WORKING_SOUND.playFor(pl);
                    inst.installRelease(pl, entry, success -> {
                        (success ? SoundEffect.ADDON_INSTALLER_SUCCESS_SOUND : SoundEffect.ADDON_INSTALLER_FAIL_SOUND).playFor(pl);
                        open(pl, guide);
                    });
                    open(pl, guide); // refresh into the "Working…" badge
                } else {
                    AddonDetailMenu.open(pl, guide, entry);
                }
                return false;
            });
            slot++;
        }

        // Fill in latest versions for not-installed addons (persistent cache, throttled, stops on
        // rate-limit); re-render once when they arrive, only if the player is still in an installer menu.
        inst.warmLatestTagsAsync(entries, () -> {
            if (p.getOpenInventory().getType() == org.bukkit.event.inventory.InventoryType.CHEST) {
                open(p, guide);
            }
        });

        menu.open(p);
    }

    @Nonnull
    private static ItemStack icon(Player p, AddonInstaller inst, AddonCatalog.Entry entry, boolean canManage) {
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
        } else {
            // Not installed: show the latest version it would install (from the persistent cache, no live call).
            String tag = inst.getCachedLatestTag(entry.getId());

            if (!tag.isEmpty()) {
                lore.add(Slimefun.getLocalization().getMessage(p, "guide.installer.version.latest").replace("%version%", tag));
            }
        }

        lore.add("");

        // A manager gets a one-click install straight from the grid; everyone can open details.
        if (canManage && !inst.isInProgress(entry.getId())) {
            lore.add(Slimefun.getLocalization().getMessage(p, "guide.installer.quick-install"));
        }

        lore.add(Slimefun.getLocalization().getMessage(p, "guide.installer.click-details"));
        return CustomItemStack.create(MaterialCompat.stack(entry.getIcon()), "&f" + entry.getDisplayName(), lore.toArray(new String[0]));
    }
}
