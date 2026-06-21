package io.github.thebusybiscuit.slimefun5.core.guide.options;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.PdcCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.ChatColor;
import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun5.api.events.PlayerLanguageChangeEvent;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.core.services.localization.Language;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

class PlayerLanguageOption implements SlimefunGuideOption<String> {

    @Override
    public SlimefunAddon getAddon() {
        return Slimefun.instance();
    }

    @Override
    public NamespacedKey getKey() {
        return Slimefun.getLocalization().getKey();
    }

    @Override
    public Optional<ItemStack> getDisplayItem(Player p, ItemStack guide) {
        if (Slimefun.getLocalization().isEnabled()) {
            Language language = Slimefun.getLocalization().getLanguage(p);
            String languageName = language.isDefault() ? (Slimefun.getLocalization().getMessage(p, "languages.default") + ChatColor.DARK_GRAY + " (" + language.getName(p) + ")") : Slimefun.getLocalization().getMessage(p, "languages." + language.getId());

            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add("&e&o" + Slimefun.getLocalization().getMessage(p, "guide.work-in-progress"));
            lore.add("");
            lore.addAll(Slimefun.getLocalization().getMessages(p, "guide.languages.description", msg -> msg.replace("%contributors%", String.valueOf(Slimefun.getGitHubService().getContributors().size()))));
            lore.add("");
            lore.add("&7\u21E8 &e" + Slimefun.getLocalization().getMessage(p, "guide.languages.change"));

            ItemStack item = CustomItemStack.create(language.getItem(), "&7" + Slimefun.getLocalization().getMessage(p, "guide.languages.selected-language") + " &a" + languageName, lore.toArray(new String[0]));
            return Optional.of(item);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void onClick(Player p, ItemStack guide) {
        openLanguageSelection(p, guide);
    }

    @Override
    public Optional<String> getSelectedOption(Player p, ItemStack guide) {
        return Optional.of(Slimefun.getLocalization().getLanguage(p).getId());
    }

    @Override
    public void setSelectedOption(Player p, ItemStack guide, String value) {
        if (value == null) {
            PdcCompat.remove(p, getKey());
        } else {
            PdcCompat.setString(p, getKey(), value);
        }
    }

    private void openLanguageSelection(Player p, ItemStack guide) {
        ChestMenu menu = new ChestMenu(Slimefun.getLocalization().getMessage(p, "guide.title.languages"));

        menu.setEmptySlotsClickable(false);
        menu.addMenuOpeningHandler(SoundEffect.GUIDE_LANGUAGE_OPEN_SOUND::playFor);

        for (int i = 0; i < 9; i++) {
            if (i == 1) {
                menu.addItem(1, ChestMenuUtils.getBackButton(p, "", "&7" + Slimefun.getLocalization().getMessage(p, "guide.back.settings")), (pl, slot, item, action) -> {
                    SlimefunGuideSettings.openSettings(pl, guide);
                    return false;
                });
            } else {
                menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
            }
        }

        Language defaultLanguage = Slimefun.getLocalization().getDefaultLanguage();
        String defaultLanguageString = Slimefun.getLocalization().getMessage(p, "languages.default");

        menu.addItem(9, CustomItemStack.create(defaultLanguage.getItem(), ChatColor.GRAY + defaultLanguageString + ChatColor.DARK_GRAY + " (" + defaultLanguage.getName(p) + ")", "", "&7\u21E8 &e" + Slimefun.getLocalization().getMessage(p, "guide.languages.select-default")), (pl, i, item, action) -> {
            Slimefun.instance().getServer().getPluginManager().callEvent(new PlayerLanguageChangeEvent(pl, Slimefun.getLocalization().getLanguage(pl), defaultLanguage));
            setSelectedOption(pl, guide, null);

            Slimefun.getLocalization().sendMessage(pl, "guide.languages.updated", msg -> msg.replace("%lang%", defaultLanguageString));

            SlimefunGuideSettings.openSettings(pl, guide);
            return false;
        });

        int slot = 10;

        for (Language language : Slimefun.getLocalization().getLanguages()) {
            menu.addItem(slot, CustomItemStack.create(language.getItem(), ChatColor.GREEN + language.getName(p),
                "&7Messages: &b" + language.getTranslationProgress() + '%',
                "&7Items: &b" + itemCoveragePercent(language.getId()) + '%',
                "",
                "&7\u21E8 &e" + Slimefun.getLocalization().getMessage(p, "guide.languages.select"),
                "&7\u21E8 &eRight Click: &7Item translation breakdown"), (pl, i, item, action) -> {
                if (action.isRightClicked()) {
                    openItemCoverage(pl, guide, language);
                    return false;
                }

                Slimefun.instance().getServer().getPluginManager().callEvent(new PlayerLanguageChangeEvent(pl, Slimefun.getLocalization().getLanguage(pl), language));
                setSelectedOption(pl, guide, language.getId());

                String name = language.getName(pl);
                Slimefun.getLocalization().sendMessage(pl, "guide.languages.updated", msg -> msg.replace("%lang%", name));

                SlimefunGuideSettings.openSettings(pl, guide);
                return false;
            });

            slot++;
        }

        menu.open(p);
    }

    /** Picks one representative enabled item per addon, to use as that addon's menu icon. */
    private java.util.Map<String, SlimefunItem> representativeItems() {
        java.util.Map<String, SlimefunItem> reps = new java.util.HashMap<>();

        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            try {
                reps.putIfAbsent(item.getAddon().getName(), item);
            } catch (Exception | LinkageError ignored) {
                // A broken item should not break the menu.
            }
        }

        return reps;
    }

    /** Overall item-translation percentage for a language across all installed plugins. */
    private int itemCoveragePercent(String languageId) {
        int translated = 0;
        int total = 0;

        for (int[] counts : Slimefun.getItemTranslationService().getCoverage(languageId).values()) {
            translated += counts[0];
            total += counts[1];
        }

        return total == 0 ? 0 : (translated * 100) / total;
    }

    /** Lists Slimefun core and each addon with its item-translation coverage for the given language. */
    private void openItemCoverage(Player p, ItemStack guide, Language language) {
        ChestMenu menu = new ChestMenu(ChatColor.GREEN + language.getName(p) + ChatColor.DARK_GRAY + " - Items");

        menu.setEmptySlotsClickable(false);

        for (int i = 0; i < 9; i++) {
            if (i == 1) {
                menu.addItem(1, ChestMenuUtils.getBackButton(p, "", "&7" + Slimefun.getLocalization().getMessage(p, "guide.title.languages")), (pl, slot, item, action) -> {
                    openLanguageSelection(pl, guide);
                    return false;
                });
            } else {
                menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
            }
        }

        java.util.Map<String, SlimefunItem> icons = representativeItems();
        int slot = 9;

        for (java.util.Map.Entry<String, int[]> entry : Slimefun.getItemTranslationService().getCoverage(language.getId()).entrySet()) {
            if (slot > 53) {
                break;
            }

            int translated = entry.getValue()[0];
            int total = entry.getValue()[1];
            int percent = total == 0 ? 0 : (translated * 100) / total;

            // Use one of the addon's own items as the icon (like the addon installer menu) instead of a
            // generic pane, so the menu visually represents each addon.
            SlimefunItem rep = icons.get(entry.getKey());
            ItemStack base = rep != null ? rep.getItem() : language.getItem();

            menu.addItem(slot, CustomItemStack.create(base,
                "&a" + entry.getKey(),
                "",
                "&7Translated: &b" + translated + "&7/&b" + total,
                "&7Coverage: " + coverageColour(percent) + percent + '%'),
                ChestMenuUtils.getEmptyClickHandler());

            slot++;
        }

        menu.open(p);
    }

    private String coverageColour(int percent) {
        if (percent >= 100) {
            return ChatColor.GREEN.toString();
        } else if (percent >= 50) {
            return ChatColor.YELLOW.toString();
        } else {
            return ChatColor.RED.toString();
        }
    }

}

