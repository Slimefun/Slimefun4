package io.github.thebusybiscuit.slimefun4.core.services.localization;

import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.services.LocalizationService;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

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
    public Language(String id, String hash) {
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
    public String getId() {
        return id;
    }

    FileConfiguration getMessagesFile() {
        return messages;
    }

    FileConfiguration getResearchesFile() {
        return researches;
    }

    FileConfiguration getResourcesFile() {
        return resources;
    }

    FileConfiguration getCategoriesFile() {
        return categories;
    }

    FileConfiguration getRecipeTypesFile() {
        return recipeTypes;
    }

    public void setMessagesFile(FileConfiguration config) {
        Validate.notNull(config);

        this.messages = config;
    }

    public void setResearchesFile(FileConfiguration config) {
        Validate.notNull(config);

        this.researches = config;
    }

    public void setResourcesFile(FileConfiguration config) {
        Validate.notNull(config);

        this.resources = config;
    }

    public void setCategoriesFile(FileConfiguration config) {
        Validate.notNull(config);

        this.categories = config;
    }

    public void setRecipeTypesFile(FileConfiguration config) {
        Validate.notNull(config);

        this.recipeTypes = config;
    }

    /**
     * This method returns the {@link ItemStack} that is used to display this {@link Language}
     * in the {@link SlimefunGuide}.
     *
     * @return The {@link ItemStack} used to display this {@link Language}
     */
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
    public String getName(Player p) {
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
        return "Language [ id= " + id + " | default=" + isDefault() + " ]";
    }

    public FileConfiguration[] getFiles() {
        return new FileConfiguration[]{getMessagesFile(), getCategoriesFile(), getResearchesFile(), getResourcesFile()};
    }

}