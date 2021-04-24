package io.github.thebusybiscuit.slimefun4.core.services.localization;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

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
 *
 * @see LocalizationService
 *
 */
public abstract class SlimefunLocalization extends Localization implements Keyed {

    protected SlimefunLocalization(@Nonnull SlimefunPlugin plugin) {
        super(plugin);
    }

    /**
     * This method attempts to return the {@link Language} with the given
     * language code.
     *
     * @param id
     *            The language code
     *
     * @return A {@link Language} with the given id or null
     */
    @Nullable
    public abstract Language getLanguage(@Nonnull String id);

    /**
     * This method returns the currently selected {@link Language} of a {@link Player}.
     *
     * @param p
     *            The {@link Player} to query
     *
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
     * @param id
     *            The {@link Language} id
     *
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
     * @param id
     *            The {@link Language} id
     * @param texture
     *            The texture of how this {@link Language} should be displayed
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
    private final FileConfiguration getFallback(@Nonnull LanguageFile file) {
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
    @Override
    public String getMessage(@Nonnull String key) {
        Validate.notNull(key, "Message key must not be null!");

        Language language = getDefaultLanguage();

        String message = language == null ? null : language.getFile(LanguageFile.MESSAGES).getString(key);

        if (message == null) {
            return getFallback(LanguageFile.MESSAGES).getString(key);
        }

        return message;
    }

    @Nonnull
    public String getMessage(@Nonnull Player p, @Nonnull String key) {
        Validate.notNull(p, "Player must not be null!");
        Validate.notNull(key, "Message key must not be null!");

        Language language = getLanguage(p);

        if (language == null) {
            return "NO LANGUAGE FOUND";
        }

        String message = language.getFile(LanguageFile.MESSAGES).getString(key);

        if (message == null) {
            return getFallback(LanguageFile.MESSAGES).getString(key);
        }

        return message;
    }

    @Nonnull
    public List<String> getMessages(@Nonnull Player p, @Nonnull String key) {
        Validate.notNull(p, "Player should not be null.");
        Validate.notNull(key, "Message key cannot be null.");

        Language language = getLanguage(p);

        if (language == null) {
            return Collections.singletonList("NO LANGUAGE FOUND");
        }

        List<String> messages = language.getFile(LanguageFile.MESSAGES).getStringList(key);

        if (messages.isEmpty()) {
            return getFallback(LanguageFile.MESSAGES).getStringList(key);
        }

        return messages;
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public List<String> getMessages(Player p, String key, UnaryOperator<String> function) {
        Validate.notNull(p, "Player cannot be null.");
        Validate.notNull(key, "Message key cannot be null.");
        Validate.notNull(function, "Function cannot be null.");

        List<String> messages = getMessages(p, key);
        messages.replaceAll(function);

        return messages;
    }

    @Nullable
    public String getResearchName(@Nonnull Player p, @Nonnull NamespacedKey key) {
        Validate.notNull(p, "Player must not be null.");
        Validate.notNull(key, "NamespacedKey cannot be null.");

        Language language = getLanguage(p);

        if (language == null || language.getFile(LanguageFile.RESEARCHES) == null) {
            return null;
        }

        return language.getFile(LanguageFile.RESEARCHES).getString(key.getNamespace() + '.' + key.getKey());
    }

    @Nullable
    public String getCategoryName(@Nonnull Player p, @Nonnull NamespacedKey key) {
        Validate.notNull(p, "Player must not be null.");
        Validate.notNull(key, "NamespacedKey cannot be null!");

        Language language = getLanguage(p);

        if (language == null || language.getFile(LanguageFile.CATEGORIES) == null) {
            return null;
        }

        return language.getFile(LanguageFile.CATEGORIES).getString(key.getNamespace() + '.' + key.getKey());
    }

    @Nullable
    public String getResourceString(@Nonnull Player p, @Nonnull String key) {
        Validate.notNull(p, "Player should not be null!");
        Validate.notNull(key, "Message key should not be null!");

        Language language = getLanguage(p);

        String value = language != null && language.getFile(LanguageFile.RESOURCES) != null ? language.getFile(LanguageFile.RESOURCES).getString(key) : null;

        if (value != null) {
            return value;
        } else {
            return getFallback(LanguageFile.RESOURCES).getString(key);
        }
    }

    @Nullable
    public ItemStack getRecipeTypeItem(@Nonnull Player p, @Nonnull RecipeType recipeType) {
        Validate.notNull(p, "Player cannot be null!");
        Validate.notNull(recipeType, "Recipe type cannot be null!");

        Language language = getLanguage(p);
        ItemStack item = recipeType.toItem();
        NamespacedKey key = recipeType.getKey();

        if (language == null || language.getFile(LanguageFile.RECIPES) == null || !language.getFile(LanguageFile.RECIPES).contains(key.getNamespace() + '.' + key.getKey())) {
            language = getLanguage("en");
        }

        if (!language.getFile(LanguageFile.RECIPES).contains(key.getNamespace() + '.' + key.getKey())) {
            return item;
        }

        FileConfiguration config = language.getFile(LanguageFile.RECIPES);

        return new CustomItem(item, meta -> {
            meta.setDisplayName(ChatColor.AQUA + config.getString(key.getNamespace() + "." + key.getKey() + ".name"));
            List<String> lore = config.getStringList(key.getNamespace() + "." + key.getKey() + ".lore");
            lore.replaceAll(line -> ChatColor.GRAY + line);
            meta.setLore(lore);

            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        });
    }

    @Override
    public void sendMessage(@Nonnull CommandSender recipient, @Nonnull String key, boolean addPrefix) {
        Validate.notNull(recipient, "Recipient cannot be null!");
        Validate.notNull(key, "Message key cannot be null!");

        String prefix = addPrefix ? getPrefix() : "";

        if (recipient instanceof Player) {
            recipient.sendMessage(ChatColors.color(prefix + getMessage((Player) recipient, key)));
        } else {
            recipient.sendMessage(ChatColor.stripColor(ChatColors.color(prefix + getMessage(key))));
        }
    }

    public void sendActionbarMessage(@Nonnull Player player, @Nonnull String key, boolean addPrefix) {
        Validate.notNull(player, "Player cannot be null!");
        Validate.notNull(key, "Message key cannot be null!");

        String prefix = addPrefix ? getPrefix() : "";
        String message = ChatColors.color(prefix + getMessage(player, key));

        BaseComponent[] components = TextComponent.fromLegacyText(message);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, components);
    }

    @Override
    public void sendMessage(@Nonnull CommandSender recipient, @Nonnull String key) {
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
    public void sendMessages(@Nonnull CommandSender recipient, @Nonnull String key) {
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
            for (String translation : getMessages((Player) recipient, key)) {
                String message = ChatColors.color(prefix + function.apply(translation));
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
}
