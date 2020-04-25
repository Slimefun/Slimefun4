package io.github.thebusybiscuit.slimefun4.core.guide.options;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.data.PersistentDataAPI;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.skull.SkullItem;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerLanguageChangeEvent;
import io.github.thebusybiscuit.slimefun4.core.services.localization.Language;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

class PlayerLanguageOption implements SlimefunGuideOption<String> {

    @Override
    public SlimefunAddon getAddon() {
        return SlimefunPlugin.instance;
    }

    @Override
    public NamespacedKey getKey() {
        return SlimefunPlugin.getLocal().getKey();
    }

    @Override
    public Optional<ItemStack> getDisplayItem(Player p, ItemStack guide) {
        if (SlimefunPlugin.getLocal().isEnabled()) {
            Language language = SlimefunPlugin.getLocal().getLanguage(p);
            String languageName = language.isDefault() ? (SlimefunPlugin.getLocal().getMessage(p, "languages.default") + ChatColor.DARK_GRAY + " (" + language.getName(p) + ")") : SlimefunPlugin.getLocal().getMessage(p, "languages." + language.getId());

            return Optional.of(new CustomItem(language.getItem(), "&7" + SlimefunPlugin.getLocal().getMessage(p, "guide.languages.selected-language") + " &a" + languageName, "", "&7You now have the option to change", "&7the language in which Slimefun", "&7will send you messages.", "&7Note that this only translates", "&7some messages, not items.", "&7&oThis feature is still being worked on", "", "&7\u21E8 &eClick to change your language"));
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public void onClick(Player p, ItemStack guide) {
        openLanguageSelection(p, guide);
    }

    @Override
    public Optional<String> getSelectedOption(Player p, ItemStack guide) {
        return Optional.of(SlimefunPlugin.getLocal().getLanguage(p).getId());
    }

    @Override
    public void setSelectedOption(Player p, ItemStack guide, String value) {
        if (value == null) {
            PersistentDataAPI.remove(p, getKey());
        }
        else {
            PersistentDataAPI.setString(p, getKey(), value);
        }
    }

    private void openLanguageSelection(Player p, ItemStack guide) {
        ChestMenu menu = new ChestMenu(SlimefunPlugin.getLocal().getMessage(p, "guide.title.languages"));

        menu.setEmptySlotsClickable(false);
        menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.7F, 0.7F));

        for (int i = 0; i < 9; i++) {
            if (i == 1) {
                menu.addItem(1, ChestMenuUtils.getBackButton(p, "", "&7" + SlimefunPlugin.getLocal().getMessage(p, "guide.back.settings")), (pl, slot, item, action) -> {
                    SlimefunGuideSettings.openSettings(pl, guide);
                    return false;
                });
            }
            else if (i == 7) {
                menu.addItem(7, new CustomItem(SkullItem.fromHash("3edd20be93520949e6ce789dc4f43efaeb28c717ee6bfcbbe02780142f716"), SlimefunPlugin.getLocal().getMessage(p, "guide.languages.translations.name"), "", "&7\u21E8 &e" + SlimefunPlugin.getLocal().getMessage(p, "guide.languages.translations.lore")), (pl, slot, item, action) -> {
                    ChatUtils.sendURL(pl, "https://github.com/TheBusyBiscuit/Slimefun4/wiki/Translating-Slimefun");
                    pl.closeInventory();
                    return false;
                });
            }
            else {
                menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
            }
        }

        Language defaultLanguage = SlimefunPlugin.getLocal().getDefaultLanguage();
        String defaultLanguageString = SlimefunPlugin.getLocal().getMessage(p, "languages.default");

        menu.addItem(9, new CustomItem(defaultLanguage.getItem(), ChatColor.GRAY + defaultLanguageString + ChatColor.DARK_GRAY + " (" + defaultLanguage.getName(p) + ")", "", "&7\u21E8 &e" + SlimefunPlugin.getLocal().getMessage(p, "guide.languages.select-default")), (pl, i, item, action) -> {
            SlimefunPlugin.instance.getServer().getPluginManager().callEvent(new PlayerLanguageChangeEvent(pl, SlimefunPlugin.getLocal().getLanguage(pl), defaultLanguage));
            setSelectedOption(pl, guide, null);

            SlimefunPlugin.getLocal().sendMessage(pl, "guide.languages.updated", msg -> msg.replace("%lang%", defaultLanguageString));

            SlimefunGuideSettings.openSettings(pl, guide);
            return false;
        });

        int slot = 10;

        for (Language language : SlimefunPlugin.getLocal().getLanguages()) {
            menu.addItem(slot, new CustomItem(language.getItem(), ChatColor.GREEN + language.getName(p), "&b" + SlimefunPlugin.getLocal().getProgress(language) + '%', "", "&7\u21E8 &e" + SlimefunPlugin.getLocal().getMessage(p, "guide.languages.select")), (pl, i, item, action) -> {
                SlimefunPlugin.instance.getServer().getPluginManager().callEvent(new PlayerLanguageChangeEvent(pl, SlimefunPlugin.getLocal().getLanguage(pl), language));
                setSelectedOption(pl, guide, language.getId());

                String name = language.getName(pl);
                SlimefunPlugin.getLocal().sendMessage(pl, "guide.languages.updated", msg -> msg.replace("%lang%", name));

                SlimefunGuideSettings.openSettings(pl, guide);
                return false;
            });

            slot++;
        }

        menu.open(p);
    }

}
