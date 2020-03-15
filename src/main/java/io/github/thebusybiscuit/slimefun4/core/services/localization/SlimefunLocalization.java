package io.github.thebusybiscuit.slimefun4.core.services.localization;

import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;

import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.config.Localization;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.services.LocalizationService;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;

/**
 * This is an abstract parent class of {@link LocalizationService}.
 * There is not really much more I can say besides that...
 * 
 * @author TheBusyBiscui
 * 
 * @see LocalizationService
 *
 */
public abstract class SlimefunLocalization extends Localization implements Keyed {

    public SlimefunLocalization(SlimefunPlugin plugin) {
        super(plugin);
    }

    /**
     * This method attempts to return the {@link Language} with the given
     * language code.
     * 
     * @param id
     *            The language code
     * @return A {@link Language} with the given id or null
     */
    public abstract Language getLanguage(String id);

    /**
     * This method returns the currently selected {@link Language} of a {@link Player}.
     * 
     * @param p
     *            The {@link Player} to query
     * @return The {@link Language} that was selected by the given {@link Player}
     */
    public abstract Language getLanguage(Player p);

    /**
     * This method returns the default {@link Language} of this {@link Server}
     * 
     * @return The default {@link Language}
     */
    public abstract Language getDefaultLanguage();

    protected abstract boolean hasLanguage(String id);

    /**
     * This method returns a full {@link Collection} of every {@link Language} that was
     * found.
     * 
     * @return A {@link Collection} that contains every installed {@link Language}
     */
    public abstract Collection<Language> getLanguages();

    protected abstract void addLanguage(String id, String texture);

    protected void loadEmbeddedLanguages() {
        for (EmbeddedLanguage lang : EmbeddedLanguage.values()) {
            addLanguage(lang.getId(), lang.getTexture());
        }
    }

    public String getMessage(Player p, String key) {
        Language language = getLanguage(p);
        return language.getMessages().getString(key);
    }

    public List<String> getMessages(Player p, String key) {
        Language language = getLanguage(p);
        return language.getMessages().getStringList(key);
    }

    public String getResearchName(Player p, NamespacedKey key) {
        Language language = getLanguage(p);
        if (language.getResearches() == null) return null;
        return language.getResearches().getString(key.getNamespace() + "." + key.getKey());
    }

    public String getCategoryName(Player p, NamespacedKey key) {
        Language language = getLanguage(p);
        if (language.getCategories() == null) return null;
        return language.getCategories().getString(key.getNamespace() + "." + key.getKey());
    }

    public String getResourceString(Player p, String key) {
        Language language = getLanguage(p);

        String value = language.getResources() != null ? language.getResources().getString(key) : null;

        if (value != null) {
            return value;
        }
        else {
            return getLanguage("en").getResources().getString(key);
        }
    }

    public ItemStack getRecipeTypeItem(Player p, RecipeType recipeType) {
        Language language = getLanguage(p);
        ItemStack item = recipeType.toItem();
        NamespacedKey key = recipeType.getKey();

        if (language.getRecipeTypes() == null || !language.getRecipeTypes().contains(key.getNamespace() + "." + key.getKey())) {
            language = getLanguage("en");
        }

        if (!language.getRecipeTypes().contains(key.getNamespace() + "." + key.getKey())) {
            return item;
        }

        FileConfiguration config = language.getRecipeTypes();

        return new CustomItem(item, meta -> {
            meta.setDisplayName(ChatColor.AQUA + config.getString(key.getNamespace() + "." + key.getKey() + ".name"));
            List<String> lore = config.getStringList(key.getNamespace() + "." + key.getKey() + ".lore");
            lore.replaceAll(str -> ChatColor.GRAY + str);
            meta.setLore(lore);
        });
    }

    @Override
    public void sendMessage(CommandSender sender, String key, boolean addPrefix) {
        String prefix = addPrefix ? getPrefix() : "";

        if (sender instanceof Player) {
            sender.sendMessage(ChatColors.color(prefix + getMessage((Player) sender, key)));
        }
        else {
            sender.sendMessage(ChatColor.stripColor(ChatColors.color(prefix + getMessage(key))));
        }
    }

    @Override
    public void sendMessage(CommandSender sender, String key) {
        sendMessage(sender, key, true);
    }

    public void sendMessage(CommandSender sender, String key, UnaryOperator<String> function) {
        sendMessage(sender, key, true, function);
    }

    @Override
    public void sendMessage(CommandSender sender, String key, boolean addPrefix, UnaryOperator<String> function) {
        String prefix = addPrefix ? getPrefix() : "";

        if (sender instanceof Player) {
            sender.sendMessage(ChatColors.color(prefix + function.apply(getMessage((Player) sender, key))));
        }
        else {
            sender.sendMessage(ChatColor.stripColor(ChatColors.color(prefix + function.apply(getMessage(key)))));
        }
    }

    @Override
    public void sendMessages(CommandSender sender, String key) {
        String prefix = getPrefix();

        if (sender instanceof Player) {
            for (String translation : getMessages((Player) sender, key)) {
                String message = ChatColors.color(prefix + translation);
                sender.sendMessage(message);
            }
        }
        else {
            for (String translation : getMessages(key)) {
                String message = ChatColors.color(prefix + translation);
                sender.sendMessage(ChatColor.stripColor(message));
            }
        }
    }

    @Override
    public void sendMessages(CommandSender sender, String key, boolean addPrefix, UnaryOperator<String> function) {
        String prefix = addPrefix ? getPrefix() : "";

        if (sender instanceof Player) {
            for (String translation : getMessages((Player) sender, key)) {
                String message = ChatColors.color(prefix + function.apply(translation));
                sender.sendMessage(message);
            }
        }
        else {
            for (String translation : getMessages(key)) {
                String message = ChatColors.color(prefix + function.apply(translation));
                sender.sendMessage(ChatColor.stripColor(message));
            }
        }
    }

    public void sendMessages(CommandSender sender, String key, UnaryOperator<String> function) {
        sendMessages(sender, key, true, function);
    }

}
