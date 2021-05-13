package io.github.thebusybiscuit.slimefun4.core.services.localization;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.config.Localization;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.SlimefunBranch;
import io.github.thebusybiscuit.slimefun4.core.services.LocalizationService;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * This is an abstract parent class of {@link LocalizationService}.
 * There is not really much more I can say besides that...
 *
 * @author TheBusyBiscuit
 * @see LocalizationService
 */
public abstract class SlimefunLocalization extends Localization implements Keyed {

    protected SlimefunLocalization(@Nonnull SlimefunPlugin plugin) {
        super(plugin);
    }

    /**
     * This method attempts to return the {@link Language} with the given
     * language code.
     *
     * @param id The language code
     * @return A {@link Language} with the given id or null
     */
    @Nullable
    public abstract Language getLanguage(@Nonnull String id);

    /**
     * This method returns the currently selected {@link Language} of a {@link Player}.
     *
     * @param p The {@link Player} to query
     * @return The {@link Language} that was selected by the given {@link Player}
     */
    @Nullable
    public abstract Language getLanguage(@Nonnull Player p);

    /**
     * This method returns the default {@link Language} of this {@link Server}
     *
     * @return The default {@link Language}
     */
    @Nullable
    public abstract Language getDefaultLanguage();

    /**
     * This returns whether a {@link Language} with the given id exists within
     * the project resources.
     *
     * @param id The {@link Language} id
     * @return Whether the project contains a {@link Language} with that id
     */
    protected abstract boolean hasLanguage(@Nonnull String id);

    /**
     * This method returns a full {@link Collection} of every {@link Language} that was
     * found.
     *
     * @return A {@link Collection} that contains every installed {@link Language}
     */
    @Nonnull
    public abstract Collection<Language> getLanguages();

    /**
     * This method adds a new {@link Language} with the given id and texture.
     *
     * @param id      The {@link Language} id
     * @param texture The texture of how this {@link Language} should be displayed
     */
    protected abstract void addLanguage(@Nonnull String id, @Nonnull String texture);

    /**
     * This will load every {@link SupportedLanguage} into memory.
     * To be precise: It performs {@link #addLanguage(String, String)} for every
     * value of {@link SupportedLanguage}.
     */
    protected void loadEmbeddedLanguages() {
        for (SupportedLanguage lang : SupportedLanguage.values()) {
            if (lang.isReadyForRelease() || SlimefunPlugin.getUpdater().getBranch() != SlimefunBranch.STABLE) {
                addLanguage(lang.getLanguageId(), lang.getTexture());
            }
        }
    }

    @Nonnull
    @Override
    public String getMessage(@Nonnull String key) {
        return getString(getDefaultLanguage(), LanguageFile.MESSAGES, key);
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public String getMessage(Player p, String key) {
        return getString(p, LanguageFile.MESSAGES, key);
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public List<String> getMessages(Player p, String key) {
        return getStringList(p, LanguageFile.MESSAGES, key);
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public List<String> getMessages(Player p, String key, UnaryOperator<String> function) {
        return getStringList(p, LanguageFile.MESSAGES, key, function);
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public String getResearchName(Player p, NamespacedKey key) {
        return getString(p, LanguageFile.RESEARCHES, key);
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public String getCategoryName(Player p, NamespacedKey key) {
        return getString(p, LanguageFile.CATEGORIES, key);
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public String getResourceString(Player p, String key) {
        return getString(p, LanguageFile.RESOURCES, key);
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public String getRecipeString(Player p, String key) {
        return getString(p, LanguageFile.RECIPES, key);
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public ItemStack getRecipeTypeItem(Player p, RecipeType recipeType) {
        Validate.notNull(p, "Player cannot be null!");
        Validate.notNull(recipeType, "Recipe type cannot be null!");

        String keyString = namespacedKeyToString(recipeType.getKey());
        return new CustomItem(recipeType.toItem(), meta -> {
            meta.setDisplayName(ChatColor.AQUA + getRecipeString(p, keyString + ".name"));
            meta.setLore(getStringList(p, LanguageFile.RECIPES, keyString + ".lore", line -> ChatColor.GRAY + line));

            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        });
    }

    @Override
    @ParametersAreNonnullByDefault
    public void sendMessage(CommandSender recipient, String key, boolean addPrefix) {
        Validate.notNull(recipient, "Recipient cannot be null!");
        Validate.notNull(key, "Message key cannot be null!");

        String prefix = addPrefix ? getPrefix() : "";

        if (recipient instanceof Player) {
            recipient.sendMessage(ChatColors.color(prefix + getMessage((Player) recipient, key)));
        } else {
            recipient.sendMessage(ChatColor.stripColor(ChatColors.color(prefix + getMessage(key))));
        }
    }

    @ParametersAreNonnullByDefault
    public void sendActionbarMessage(Player player, String key, boolean addPrefix) {
        Validate.notNull(player, "Player cannot be null!");
        Validate.notNull(key, "Message key cannot be null!");

        String prefix = addPrefix ? getPrefix() : "";
        String message = ChatColors.color(prefix + getMessage(player, key));

        BaseComponent[] components = TextComponent.fromLegacyText(message);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, components);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void sendMessage(CommandSender recipient, String key) {
        sendMessage(recipient, key, true);
    }

    @ParametersAreNonnullByDefault
    public void sendMessage(CommandSender recipient, String key, UnaryOperator<String> function) {
        sendMessage(recipient, key, true, function);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void sendMessage(CommandSender recipient, String key, boolean addPrefix, UnaryOperator<String> function) {
        if (SlimefunPlugin.getMinecraftVersion() == MinecraftVersion.UNIT_TEST) {
            return;
        }

        String prefix = addPrefix ? getPrefix() : "";

        if (recipient instanceof Player) {
            recipient.sendMessage(ChatColors.color(prefix + function.apply(getMessage((Player) recipient, key))));
        } else {
            recipient.sendMessage(ChatColor.stripColor(ChatColors.color(prefix + function.apply(getMessage(key)))));
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void sendMessages(CommandSender recipient, String key) {
        String prefix = getPrefix();

        if (recipient instanceof Player) {
            for (String translation : getMessages((Player) recipient, key)) {
                String message = ChatColors.color(prefix + translation);
                recipient.sendMessage(message);
            }
        } else {
            for (String translation : getMessages(key)) {
                String message = ChatColors.color(prefix + translation);
                recipient.sendMessage(ChatColor.stripColor(message));
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void sendMessages(CommandSender recipient, String key, boolean addPrefix, UnaryOperator<String> function) {
        String prefix = addPrefix ? getPrefix() : "";

        if (recipient instanceof Player) {
            for (String translation : getMessages((Player) recipient, key, function)) {
                String message = ChatColors.color(prefix + translation);
                recipient.sendMessage(message);
            }
        } else {
            for (String translation : getMessages(key)) {
                String message = ChatColors.color(prefix + function.apply(translation));
                recipient.sendMessage(ChatColor.stripColor(message));
            }
        }
    }

    @ParametersAreNonnullByDefault
    public void sendMessages(CommandSender recipient, String key, UnaryOperator<String> function) {
        sendMessages(recipient, key, true, function);
    }

    @Nonnull
    protected Set<String> getTotalKeys(@Nonnull Language lang) {
        return getKeys(lang.getFiles());
    }

    @Nonnull
    protected Set<String> getKeys(@Nonnull FileConfiguration... files) {
        Set<String> keys = new HashSet<>();

        for (FileConfiguration cfg : files) {
            keys.addAll(cfg.getKeys(true));
        }

        return keys;
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    private String getString(Player p, LanguageFile languageFile, NamespacedKey key) {
        return getString(p, languageFile, namespacedKeyToString(key));
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    private String getString(Player p, LanguageFile languageFile, String key) {
        return getString(getLanguage(p), languageFile, key);
    }

    @Nonnull
    private String getString(@Nullable Language language, @Nonnull LanguageFile languageFile, @Nonnull String key) {
        Validate.notNull(languageFile, "LanguageFile must not be null!");
        Validate.notNull(key, "Message key must not be null!");

        if (language == null) {
            return "NO LANGUAGE FOUND";
        }

        String value = language.getFile(languageFile) != null
                ? language.getFile(languageFile).getString(key)
                : null;

        // TODO: 13.05.21 Nullability for the fallback?
        return value == null
                ? getFallback(languageFile).getString(key)
                : value;
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    private List<String> getStringList(Player p, LanguageFile languageFile, String key, UnaryOperator<String> function) {
        return getStringList(p, languageFile, key)
                .stream()
                .map(function)
                .collect(Collectors.toList());
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    private List<String> getStringList(Player p, LanguageFile languageFile, String key) {
        Validate.notNull(languageFile, "LanguageFile must not be null!");
        Validate.notNull(key, "Message key must not be null!");

        Language language = getLanguage(p);

        List<String> value = language != null && language.getFile(languageFile) != null
                ? language.getFile(languageFile).getStringList(key)
                : null;

        return value == null || value.isEmpty()
                ? getFallback(languageFile).getStringList(key)
                : value;
    }

    @Nonnull
    private FileConfiguration getFallback(@Nonnull LanguageFile file) {
        Language language = getLanguage(SupportedLanguage.ENGLISH.getLanguageId());

        if (language == null) {
            throw new IllegalStateException("Fallback language \"en\" is missing!");
        }

        FileConfiguration fallback = language.getFile(file);

        if (fallback != null) {
            return fallback;
        } else {
            throw new IllegalStateException("Fallback file: \"" + file.getFilePath("en") + "\" is missing!");
        }
    }

    @Nonnull
    private String namespacedKeyToString(@Nonnull NamespacedKey key) {
        return key.getNamespace() + "." + key.getKey();
    }
}
