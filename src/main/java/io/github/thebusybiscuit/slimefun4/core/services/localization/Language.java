package io.github.thebusybiscuit.slimefun4.core.services.localization;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.services.LocalizationService;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

/**
 * This Class represents a {@link Language} that Slimefun can recognize and use.
 * 
 * @author TheBusyBiscuit
 * 
 * @see LocalizationService
 *
 */
public final class Language {

    private final String id;
    private final ItemStack item;
    private double progress = -1;

    private FileConfiguration messages;
    private FileConfiguration researches;
    private FileConfiguration resources;
    private FileConfiguration categories;
    private FileConfiguration recipeTypes;

    /**
     * This instantiates a new {@link Language} with the given language code
     * and skull texture.
     * 
     * @param id
     *            The language code of this {@link Language}
     * @param hash
     *            The hash of the skull texture to use
     */
    public Language(@Nonnull String id, @Nonnull String hash) {
        Validate.notNull(id, "A Language must have an id that is not null!");
        Validate.notNull(hash, "A Language must have a texture that is not null!");

        this.id = id;
        this.item = SlimefunUtils.getCustomHead(hash);

        SlimefunPlugin.getItemTextureService().setTexture(item, "_UI_LANGUAGE_" + id.toUpperCase(Locale.ROOT));
    }

    /**
     * This returns the identifier of this {@link Language}.
     * 
     * @return The identifier of this {@link Language}
     */
    @Nonnull
    public String getId() {
        return id;
    }

    /**
     * This method returns the progress of translation for this {@link Language}.
     * The progress is determined by the amount of translated strings divided by the amount
     * of strings in the english {@link Language} file and multiplied by 100.0
     * 
     * @return A percentage {@code (0.0 - 100.0)} for the progress of translation of this {@link Language}
     */
    public double getTranslationProgress() {
        if (id.equals("en")) {
            return 100.0;
        } else {
            if (progress < 0) {
                progress = SlimefunPlugin.getLocalization().calculateProgress(this);
            }

            return progress;
        }
    }

    @Nullable
    FileConfiguration getMessagesFile() {
        return messages;
    }

    @Nullable
    FileConfiguration getResearchesFile() {
        return researches;
    }

    @Nullable
    FileConfiguration getResourcesFile() {
        return resources;
    }

    @Nullable
    FileConfiguration getCategoriesFile() {
        return categories;
    }

    @Nullable
    FileConfiguration getRecipeTypesFile() {
        return recipeTypes;
    }

    public void setMessagesFile(@Nonnull FileConfiguration config) {
        Validate.notNull(config);

        this.messages = config;
    }

    public void setResearchesFile(@Nonnull FileConfiguration config) {
        Validate.notNull(config);

        this.researches = config;
    }

    public void setResourcesFile(@Nonnull FileConfiguration config) {
        Validate.notNull(config);

        this.resources = config;
    }

    public void setCategoriesFile(@Nonnull FileConfiguration config) {
        Validate.notNull(config);

        this.categories = config;
    }

    public void setRecipeTypesFile(@Nonnull FileConfiguration config) {
        Validate.notNull(config);

        this.recipeTypes = config;
    }

    /**
     * This method returns the {@link ItemStack} that is used to display this {@link Language}
     * in the {@link SlimefunGuide}.
     * 
     * @return The {@link ItemStack} used to display this {@link Language}
     */
    @Nonnull
    public ItemStack getItem() {
        return item;
    }

    /**
     * This method localizes the name of this {@link Language} in the selected {@link Language}
     * of the given {@link Player}.
     * 
     * @param p
     *            The {@link Player} to localize the name for
     * @return The localized name of this {@link Language}
     */
    @Nonnull
    public String getName(@Nonnull Player p) {
        String name = SlimefunPlugin.getLocalization().getMessage(p, "languages." + id);
        return name != null ? name : toString();
    }

    /**
     * This method returns whether this {@link Language} is also the default
     * {@link Language} of this {@link Server}.
     * 
     * @return Whether this is the default {@link Language} of this {@link Server}
     */
    public boolean isDefault() {
        return this == SlimefunPlugin.getLocalization().getDefaultLanguage();
    }

    @Override
    public String toString() {
        return "Language {id= " + id + ", default=" + isDefault() + " }";
    }

    @Nonnull
    public FileConfiguration[] getFiles() {
        return new FileConfiguration[] { getMessagesFile(), getCategoriesFile(), getResearchesFile(), getResourcesFile() };
    }

}
