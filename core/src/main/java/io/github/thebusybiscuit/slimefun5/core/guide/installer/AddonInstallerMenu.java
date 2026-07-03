package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.core.balance.AddonBalanceSummary;
import io.github.thebusybiscuit.slimefun5.core.balance.BalanceService;
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

        List<String> checkUpdatesLore = new ArrayList<>();
        checkUpdatesLore.add(Slimefun.getLocalization().getMessage(p, "guide.installer.check-updates.name"));
        checkUpdatesLore.add("");
        checkUpdatesLore.addAll(Slimefun.getLocalization().getMessages(p, "guide.installer.check-updates.lore"));
        menu.addItem(48, CustomItemStack.create(MaterialCompat.stack(XMaterial.CLOCK), checkUpdatesLore));
        menu.addMenuClickHandler(48, (pl, slot, item, action) -> {
            SoundEffect.ADDON_INSTALLER_WORKING_SOUND.playFor(pl);
            List<AddonCatalog.Entry> allEntries = AddonCatalog.getEntries();
            // A forced re-check: both calls hit the network (throttle permitting); the menu reopens
            // once the version cache is refreshed so badges/versions reflect what was just fetched.
            inst.refreshUpdateStatusAsync(allEntries);
            inst.warmLatestTagsAsync(allEntries, () -> {
                SoundEffect.ADDON_INSTALLER_SUCCESS_SOUND.playFor(pl);
                open(pl, guide);
            });
            return false;
        });

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

            int entrySlot = slot;
            menu.addItem(slot, icon(p, inst, entry, canManage));
            menu.addMenuClickHandler(slot, (pl, sl, item, action) -> {
                // Left-click opens details; right-click installs/updates straight from the grid.
                if (canManage && action.isRightClicked()) {
                    if (inst.isInProgress(entry.getId())) {
                        return false; // already working — the slot shows a live progress bar
                    }

                    SoundEffect.ADDON_INSTALLER_WORKING_SOUND.playFor(pl);
                    inst.installRelease(pl, entry, success -> {
                        (success ? SoundEffect.ADDON_INSTALLER_SUCCESS_SOUND : SoundEffect.ADDON_INSTALLER_FAIL_SOUND).playFor(pl);
                        menu.replaceExistingItem(entrySlot, icon(pl, inst, entry, canManage));
                    }, () -> menu.replaceExistingItem(entrySlot, icon(pl, inst, entry, canManage)));
                    menu.replaceExistingItem(entrySlot, icon(pl, inst, entry, canManage)); // into the "Working…" badge
                } else {
                    AddonDetailMenu.open(pl, guide, entry);
                }
                return false;
            });
            slot++;
        }

        // Custom/third-party addons: any loaded addon that registered items but isn't in the catalog.
        // Managers only - it's an admin diagnostic. Reuses the same free-floating grid slots.
        if (canManage) {
            Set<String> catalogNames = new HashSet<>();
            catalogNames.add("Slimefun");

            for (AddonCatalog.Entry e : entries) {
                catalogNames.add(e.getPluginName());
            }

            for (String addonName : BalanceService.instance().detectedAddonNames(catalogNames)) {
                if (slot >= 45) {
                    break;
                }

                AddonBalanceSummary summary = BalanceService.instance().summarize(addonName);
                List<String> lore = new ArrayList<>();
                lore.add("");
                lore.add(Slimefun.getLocalization().getMessage(p, "guide.balance.detected-lore"));

                if (!summary.isEmpty()) {
                    lore.add("");
                    lore.add(Slimefun.getLocalization().getMessage(p, "guide.balance.peak")
                        .replace("%tier%", Slimefun.getLocalization().getMessage(p, "guide.balance.tier." + summary.getPeakTier().name().toLowerCase(Locale.ROOT)))
                        .replace("%score%", String.valueOf(summary.getPeak())));
                    lore.add(Slimefun.getLocalization().getMessage(p, "guide.balance.average")
                        .replace("%tier%", Slimefun.getLocalization().getMessage(p, "guide.balance.tier." + summary.getAverageTier().name().toLowerCase(Locale.ROOT)))
                        .replace("%score%", String.valueOf(summary.getAverage())));
                    lore.add(Slimefun.getLocalization().getMessage(p, "guide.balance.op-count")
                        .replace("%count%", String.valueOf(summary.getOpCount())));
                    lore.add(Slimefun.getLocalization().getMessage(p, "guide.balance.view-items"));
                }

                int detectedSlot = slot;
                String detectedName = addonName;
                menu.addItem(slot, CustomItemStack.create(MaterialCompat.stack(XMaterial.COMMAND_BLOCK),
                    Slimefun.getLocalization().getMessage(p, "guide.balance.detected") + " &f" + addonName, lore.toArray(new String[0])));
                menu.addMenuClickHandler(detectedSlot, (pl, sl, item, action) -> {
                    if (!summary.isEmpty()) {
                        AddonBalanceMenu.open(pl, guide, detectedName, detectedName, () -> open(pl, guide));
                    }
                    return false;
                });
                slot++;
            }
        }

        // Fill the version cache in the background (persisted); versions show on the next open.
        // Do NOT re-open the menu when it completes — a programmatic re-open re-fires the open sound
        // and each open spawns another warm-fetch, which cascaded into dozens of rapid page-turn sounds.
        inst.warmLatestTagsAsync(entries, null);

        menu.open(p);
    }

    @Nonnull
    private static ItemStack icon(Player p, AddonInstaller inst, AddonCatalog.Entry entry, boolean canManage) {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(StatusBadges.badge(p, inst, entry));

        Plugin plugin = inst.getLoadedPlugin(entry);

        if (plugin != null) {
            lore.add(StatusBadges.sourceLine(p, inst, entry));
            lore.add(Slimefun.getLocalization().getMessage(p, "guide.installer.version.release").replace("%version%", plugin.getDescription().getVersion()));
            List<String> authors = plugin.getDescription().getAuthors();

            if (authors != null && !authors.isEmpty()) {
                lore.add(Slimefun.getLocalization().getMessage(p, "guide.installer.info.authors").replace("%authors%", String.join(", ", authors)));
            }

            // Admin-only compact balance rating on the grid tile, so managers see it without opening the
            // detail screen. Only for loaded addons (unloaded ones have no registered items to score).
            if (canManage) {
                String addonName = entry.isCore() ? "Slimefun" : entry.getPluginName();
                AddonBalanceSummary balance = BalanceService.instance().summarize(addonName);

                if (!balance.isEmpty()) {
                    // Headline the PEAK (the strongest item) + overpowered count, not the average - a few
                    // game-breakers must not be hidden by a pile of low-power filler items dragging the mean down.
                    lore.add(Slimefun.getLocalization().getMessage(p, "guide.balance.tile")
                        .replace("%tier%", Slimefun.getLocalization().getMessage(p, "guide.balance.tier." + balance.getPeakTier().name().toLowerCase(Locale.ROOT)))
                        .replace("%score%", String.valueOf(balance.getPeak())));

                    if (balance.getOpCount() > 0) {
                        lore.add(Slimefun.getLocalization().getMessage(p, "guide.balance.op-count")
                            .replace("%count%", String.valueOf(balance.getOpCount())));
                    }
                }
            }
        } else {
            // Not installed: show the latest version it would install (from the persistent cache, no live call).
            String tag = inst.getCachedLatestTag(entry.getId());

            if (!tag.isEmpty()) {
                lore.add(Slimefun.getLocalization().getMessage(p, "guide.installer.version.latest").replace("%version%", tag));
            }
        }

        lore.add("");

        if (inst.isInProgress(entry.getId())) {
            // A live download bar for the entry being installed from this grid.
            lore.add(Slimefun.getLocalization().getMessage(p, "guide.installer.install.progress")
                .replace("%bar%", AddonInstaller.progressBar(inst.getProgress(entry.getId()))));
        } else if (canManage) {
            // A manager gets a one-click install straight from the grid; everyone can open details.
            lore.add(Slimefun.getLocalization().getMessage(p, "guide.installer.quick-install"));
        }

        lore.add(Slimefun.getLocalization().getMessage(p, "guide.installer.click-details"));
        return CustomItemStack.create(MaterialCompat.stack(entry.getIcon()), "&f" + entry.getDisplayName(), lore.toArray(new String[0]));
    }
}
