package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.core.balance.AddonBalanceSummary;
import io.github.thebusybiscuit.slimefun5.core.balance.BalanceService;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
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

    // Frame the top and bottom rows only; the icon (13) and action buttons float in the airy middle,
    // matching the clean wiki-style layout rather than flooding every slot with glass panes.
    private static final int[] BORDER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 45, 46, 47, 48, 49, 50, 51, 52, 53 };

    private AddonDetailMenu() {}

    public static void open(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull AddonCatalog.Entry entry) {
        ChestMenu menu = new ChestMenu(Slimefun.getLocalization().getMessage(p, "guide.title.installer"));
        menu.setEmptySlotsClickable(false);
        menu.addMenuOpeningHandler(SoundEffect.GUIDE_BUTTON_CLICK_SOUND::playFor);
        ChestMenuUtils.drawBackground(menu, BORDER);

        AddonInstaller inst = AddonInstallerMenu.installer();
        boolean canManage = p.hasPermission(AddonCatalog.PERMISSION);

        // async + throttled; updates are announced at startup/join, not from here
        inst.refreshUpdateStatusAsync(java.util.Collections.singletonList(entry));

        // Warm the version cache so the install button can show the version (from the grid's warm this
        // is usually already cached). No re-open on completion — that re-fires the open sound and can
        // cascade (see AddonInstallerMenu).
        inst.fetchLatestTagAsync(entry, null);

        menu.addItem(0, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK), Slimefun.getLocalization().getMessage(p, "guide.installer.back")));
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

        List<String> headerLore = new ArrayList<>();
        headerLore.add("");
        headerLore.add(StatusBadges.badge(p, inst, entry));

        String versionLine = versionLine(p, inst, entry);

        if (versionLine != null) {
            headerLore.add(versionLine);
        }

        InstallState.Record record = inst.getState().get(entry.getId());
        boolean restartPending = record != null && record.isRestartPending();

        if (!restartPending && inst.isUpdateAvailable(entry.getId())) {
            headerLore.add(Slimefun.getLocalization().getMessage(p, "guide.installer.update.available")
                .replace("%version%", inst.getLatestVersionLabel(entry.getId())));
        }

        if (!deps.isEmpty()) {
            headerLore.add(Slimefun.getLocalization().getMessage(p, "guide.installer.will-install").replace("%deps%", String.join(", ", deps)));
        }

        Plugin loaded = inst.getLoadedPlugin(entry);

        if (loaded != null) {
            String description = loaded.getDescription().getDescription();

            if (description != null && !description.trim().isEmpty()) {
                headerLore.add("");
                headerLore.add("&7" + description.trim());
            }

            List<String> authors = loaded.getDescription().getAuthors();

            if (authors != null && !authors.isEmpty()) {
                headerLore.add(Slimefun.getLocalization().getMessage(p, "guide.installer.info.authors").replace("%authors%", String.join(", ", authors)));
            }
        }

        // Balance rating (admin-only). Computed live from the loaded addon's registered items; a
        // not-yet-installed addon has no items to score, so this only appears once the addon is loaded.
        AddonBalanceSummary balance = AddonBalanceSummary.EMPTY;

        if (canManage && inst.isLoaded(entry)) {
            String addonName = entry.isCore() ? "Slimefun" : entry.getPluginName();
            balance = BalanceService.instance().summarize(addonName);

            if (!balance.isEmpty()) {
                headerLore.add("");
                headerLore.add(Slimefun.getLocalization().getMessage(p, "guide.balance.header"));
                headerLore.add(Slimefun.getLocalization().getMessage(p, "guide.balance.average")
                    .replace("%tier%", tierName(p, balance.getAverageTier()))
                    .replace("%score%", String.valueOf(balance.getAverage())));
                headerLore.add(Slimefun.getLocalization().getMessage(p, "guide.balance.peak")
                    .replace("%tier%", tierName(p, balance.getPeakTier()))
                    .replace("%score%", String.valueOf(balance.getPeak())));
                headerLore.add(Slimefun.getLocalization().getMessage(p, "guide.balance.op-count")
                    .replace("%count%", String.valueOf(balance.getOpCount())));
                headerLore.add(Slimefun.getLocalization().getMessage(p, "guide.balance.view-items"));
            }
        }

        menu.addItem(13, CustomItemStack.create(MaterialCompat.stack(entry.getIcon()), "&f" + entry.getDisplayName(), headerLore.toArray(new String[0])));

        if (canManage && !balance.isEmpty()) {
            String addonName = entry.isCore() ? "Slimefun" : entry.getPluginName();
            menu.addMenuClickHandler(13, (pl, slot, item, action) -> {
                AddonBalanceMenu.open(pl, guide, addonName, entry.getDisplayName(), () -> open(pl, guide, entry));
                return false;
            });
        } else {
            menu.addMenuClickHandler(13, ChestMenuUtils.getEmptyClickHandler());
        }

        boolean showInstall = canManage;
        boolean showDelete = canManage && !entry.isCore() && !entry.isLibrary() && inst.isLoaded(entry);
        boolean showBuild = canManage && EnvironmentDetector.canBuildFromSource();

        boolean showGithub = io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuide.showExternalLinks();
        int[] slots = centeredActionSlots((showInstall ? 1 : 0) + (showDelete ? 1 : 0) + (showBuild ? 1 : 0) + (showGithub ? 1 : 0));
        int idx = 0;

        if (showInstall) {
            int s = slots[idx++];

            if (inst.isInProgress(entry.getId())) {
                // Installing: a clear in-GUI "working" state with a live progress bar, so the chat
                // line isn't the only signal.
                menu.addItem(s, workingButton(p, inst, entry));
                menu.addMenuClickHandler(s, ChestMenuUtils.getEmptyClickHandler());
            } else {
                String tag = inst.getCachedLatestTag(entry.getId());
                String title = Slimefun.getLocalization().getMessage(p, inst.isLoaded(entry) ? "guide.installer.install.update" : "guide.installer.install.install");

                if (!tag.isEmpty()) {
                    title = title + " &7(" + tag + ")";
                }

                List<String> installLore = new ArrayList<>();
                installLore.add(title);
                installLore.add("");
                installLore.addAll(Slimefun.getLocalization().getMessages(p, "guide.installer.install.lore"));
                menu.addItem(s, CustomItemStack.create(MaterialCompat.stack(XMaterial.LIME_DYE), installLore));
                int buttonSlot = s;
                menu.addMenuClickHandler(s, (pl, slot, item, action) -> {
                    SoundEffect.ADDON_INSTALLER_WORKING_SOUND.playFor(pl);
                    inst.installRelease(pl, entry, success -> {
                        (success ? SoundEffect.ADDON_INSTALLER_SUCCESS_SOUND : SoundEffect.ADDON_INSTALLER_FAIL_SOUND).playFor(pl);
                        open(pl, guide, entry);
                    }, () -> menu.replaceExistingItem(buttonSlot, workingButton(pl, inst, entry)));
                    open(pl, guide, entry); // immediately re-render into the "Installing…" state
                    return false;
                });
            }
        }

        if (showDelete) {
            int s = slots[idx++];
            List<String> deleteLore = new ArrayList<>();
            deleteLore.add(Slimefun.getLocalization().getMessage(p, "guide.installer.delete.name"));
            deleteLore.add("");
            deleteLore.addAll(Slimefun.getLocalization().getMessages(p, "guide.installer.delete.lore"));
            menu.addItem(s, CustomItemStack.create(MaterialCompat.stack(XMaterial.RED_DYE), deleteLore));
            menu.addMenuClickHandler(s, (pl, slot, item, action) -> {
                inst.deleteAddon(pl, entry);
                open(pl, guide, entry);
                return false;
            });
        }

        if (showBuild) {
            int s = slots[idx++];
            List<String> buildLore = new ArrayList<>();
            buildLore.add(Slimefun.getLocalization().getMessage(p, "guide.installer.build.name"));
            buildLore.add("");
            buildLore.addAll(Slimefun.getLocalization().getMessages(p, "guide.installer.build.lore"));
            menu.addItem(s, CustomItemStack.create(MaterialCompat.stack(XMaterial.WHEAT_SEEDS), buildLore));
            menu.addMenuClickHandler(s, (pl, slot, item, action) -> {
                BranchSelectMenu.open(pl, guide, entry, 0);
                return false;
            });
        }

        if (showGithub) {
            int githubSlot = slots[idx];
            List<String> githubLore = new ArrayList<>();
            githubLore.add(Slimefun.getLocalization().getMessage(p, "guide.installer.github.name"));
            githubLore.add("");
            githubLore.addAll(Slimefun.getLocalization().getMessages(p, "guide.installer.github.lore"));
            menu.addItem(githubSlot, CustomItemStack.create(MaterialCompat.stack(XMaterial.BOOK), githubLore));
            menu.addMenuClickHandler(githubSlot, (pl, slot, item, action) -> {
                pl.closeInventory();
                Slimefun.getLocalization().sendMessage(pl, "guide.installer.github.chat", true);
                pl.sendMessage("https://github.com/" + entry.getSlug());
                return false;
            });
        }

        menu.open(p);
    }

    /** The "Installing…" button with a live progress bar, shared by the initial render and progress updates. */
    @Nonnull
    private static ItemStack workingButton(@Nonnull Player p, @Nonnull AddonInstaller inst, @Nonnull AddonCatalog.Entry entry) {
        List<String> workingLore = new ArrayList<>();
        workingLore.add(Slimefun.getLocalization().getMessage(p, "guide.installer.install.working"));
        workingLore.add(AddonInstaller.progressBar(inst.getProgress(entry.getId())));
        return CustomItemStack.create(MaterialCompat.stack(XMaterial.CLOCK), workingLore);
    }

    /** Action-button slots in the bottom row, centered on slot 31 with a gap between each. */
    private static int[] centeredActionSlots(int count) {
        int[] slots = new int[count];
        int start = 31 - (count - 1);

        for (int i = 0; i < count; i++) {
            slots[i] = start + 2 * i;
        }

        return slots;
    }

    /**
     * Builds the version line for the header: the installed release tag, the built branch + commit for
     * a from-source build, or the loaded plugin version for a jar the installer did not stage. Returns
     * null when the entry is not loaded (the status badge already says so).
     */
    @javax.annotation.Nullable
    private static String versionLine(@Nonnull Player p, @Nonnull AddonInstaller inst, @Nonnull AddonCatalog.Entry entry) {
        Plugin plugin = inst.getLoadedPlugin(entry);

        if (plugin == null) {
            return null;
        }

        InstallState.Record record = inst.getState().get(entry.getId());
        String pluginVersion = plugin.getDescription().getVersion();

        if (record != null && record.getMethod() == InstallState.Method.BRANCH) {
            String commit = record.getCommit().isEmpty() ? pluginVersion : record.getCommit();
            return Slimefun.getLocalization().getMessage(p, "guide.installer.version.branch")
                .replace("%branch%", record.getVersion())
                .replace("%commit%", commit);
        }

        if (record != null && record.getMethod() == InstallState.Method.RELEASE) {
            return Slimefun.getLocalization().getMessage(p, "guide.installer.version.release")
                .replace("%version%", record.getVersion());
        }

        // Loaded but not staged by the installer: a custom/local build. Show its plugin version.
        return Slimefun.getLocalization().getMessage(p, "guide.installer.version.custom")
            .replace("%version%", pluginVersion);
    }

    @Nonnull
    private static String tierName(@Nonnull Player p, @Nonnull io.github.thebusybiscuit.slimefun5.core.balance.BalanceTier tier) {
        return Slimefun.getLocalization().getMessage(p, "guide.balance.tier." + tier.name().toLowerCase(Locale.ROOT));
    }
}
