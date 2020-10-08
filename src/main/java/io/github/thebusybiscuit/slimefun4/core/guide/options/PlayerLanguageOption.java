package io.github.thebusybiscuit.slimefun4.core.guide.options;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.data.PersistentDataAPI;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerLanguageChangeEvent;
import io.github.thebusybiscuit.slimefun4.core.services.localization.Language;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

class PlayerLanguageOption implements SlimefunGuideOption<String> {

    @Override
    public SlimefunAddon getAddon() {
        return SlimefunPlugin.instance();
    }

    @Override
    public NamespacedKey getKey() {
        return SlimefunPlugin.getLocalization().getKey();
    }

    @Override
    public Optional<ItemStack> getDisplayItem(Player p, ItemStack guide) {
        if (SlimefunPlugin.getLocalization().isEnabled()) {
            Language language = SlimefunPlugin.getLocalization().getLanguage(p);
            String languageName = language.isDefault() ? (SlimefunPlugin.getLocalization().getMessage(p, "languages.default") + ChatColor.DARK_GRAY + " (" + language.getName(p) + ")") : SlimefunPlugin.getLocalization().getMessage(p, "languages." + language.getId());

            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add("&e&o" + SlimefunPlugin.getLocalization().getMessage(p, "guide.work-in-progress"));
            lore.add("");
            lore.addAll(SlimefunPlugin.getLocalization().getMessages(p, "guide.languages.description", msg -> msg.replace("%contributors%", String.valueOf(SlimefunPlugin.getGitHubService().getContributors().size()))));
            lore.add("");
            lore.add("&7\u21E8 &e" + SlimefunPlugin.getLocalization().getMessage(p, "guide.languages.change"));

            ItemStack item = new CustomItem(language.getItem(), "&7" + SlimefunPlugin.getLocalization().getMessage(p, "guide.languages.selected-language") + " &a" + languageName, lore.toArray(new String[0]));
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
        return Optional.of(SlimefunPlugin.getLocalization().getLanguage(p).getId());
    }

    @Override
    public void setSelectedOption(Player p, ItemStack guide, String value) {
        if (value == null) {
            PersistentDataAPI.remove(p, getKey());
        } else {
            PersistentDataAPI.setString(p, getKey(), value);
        }
    }

    private void openLanguageSelection(Player p, ItemStack guide) {
        ChestMenu menu = new ChestMenu(SlimefunPlugin.getLocalization().getMessage(p, "guide.title.languages"));

        menu.setEmptySlotsClickable(false);
        menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.7F, 0.7F));

        for (int i = 0; i < 9; i++) {
            if (i == 1) {
                menu.addItem(1, ChestMenuUtils.getBackButton(p, "", "&7" + SlimefunPlugin.getLocalization().getMessage(p, "guide.back.settings")), (pl, slot, item, action) -> {
                    SlimefunGuideSettings.openSettings(pl, guide);
                    return false;
                });
            } else if (i == 7) {
                menu.addItem(7, new CustomItem(SlimefunUtils.getCustomHead(HeadTexture.ADD_NEW_LANGUAGE.getTexture()), SlimefunPlugin.getLocalization().getMessage(p, "guide.languages.translations.name"), "", "&7\u21E8 &e" + SlimefunPlugin.getLocalization().getMessage(p, "guide.languages.translations.lore")), (pl, slot, item, action) -> {
                    ChatUtils.sendURL(pl, "https://github.com/Slimefun/Slimefun4/wiki/Translating-Slimefun");
                    pl.closeInventory();
                    return false;
                });
            } else {
                menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
            }
        }

        Language defaultLanguage = SlimefunPlugin.getLocalization().getDefaultLanguage();
        String defaultLanguageString = SlimefunPlugin.getLocalization().getMessage(p, "languages.default");

        menu.addItem(9, new CustomItem(defaultLanguage.getItem(), ChatColor.GRAY + defaultLanguageString + ChatColor.DARK_GRAY + " (" + defaultLanguage.getName(p) + ")", "", "&7\u21E8 &e" + SlimefunPlugin.getLocalization().getMessage(p, "guide.languages.select-default")), (pl, i, item, action) -> {
            SlimefunPlugin.instance().getServer().getPluginManager().callEvent(new PlayerLanguageChangeEvent(pl, SlimefunPlugin.getLocalization().getLanguage(pl), defaultLanguage));
            setSelectedOption(pl, guide, null);

            SlimefunPlugin.getLocalization().sendMessage(pl, "guide.languages.updated", msg -> msg.replace("%lang%", defaultLanguageString));

            SlimefunGuideSettings.openSettings(pl, guide);
            return false;
        });

        int slot = 10;

        for (Language language : SlimefunPlugin.getLocalization().getLanguages()) {
            menu.addItem(slot, new CustomItem(language.getItem(), ChatColor.GREEN + language.getName(p), "&b" + language.getTranslationProgress() + '%', "", "&7\u21E8 &e" + SlimefunPlugin.getLocalization().getMessage(p, "guide.languages.select")), (pl, i, item, action) -> {
                SlimefunPlugin.instance().getServer().getPluginManager().callEvent(new PlayerLanguageChangeEvent(pl, SlimefunPlugin.getLocalization().getLanguage(pl), language));
                setSelectedOption(pl, guide, language.getId());

                String name = language.getName(pl);
                SlimefunPlugin.getLocalization().sendMessage(pl, "guide.languages.updated", msg -> msg.replace("%lang%", name));

                SlimefunGuideSettings.openSettings(pl, guide);
                return false;
            });

            slot++;
        }

        menu.open(p);
    }

}
