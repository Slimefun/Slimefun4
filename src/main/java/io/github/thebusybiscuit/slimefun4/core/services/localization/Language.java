package io.github.thebusybiscuit.slimefun4.core.services.localization;

import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.skull.SkullItem;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.services.LocalizationService;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

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
        this.id = id;

        item = SkullItem.fromHash(hash);
        SlimefunPlugin.getItemTextureService().setTexture(item, "_UI_LANGUAGE_" + id.toUpperCase());
    }

    public String getID() {
        return id;
    }

    public FileConfiguration getMessages() {
        return messages;
    }

    public FileConfiguration getResearches() {
        return researches;
    }

    public FileConfiguration getResources() {
        return resources;
    }

    public FileConfiguration getCategories() {
        return categories;
    }

    public FileConfiguration getRecipeTypes() {
        return recipeTypes;
    }

    public void setMessages(FileConfiguration config) {
        this.messages = config;
    }

    public void setResearches(FileConfiguration config) {
        this.researches = config;
    }

    public void setResources(FileConfiguration config) {
        this.resources = config;
    }

    public void setCategories(FileConfiguration config) {
        this.categories = config;
    }

    public void setRecipeTypes(FileConfiguration config) {
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
        return SlimefunPlugin.getLocal().getMessage(p, "languages." + id);
    }

    /**
     * This method returns whether this {@link Language} is also the default
     * {@link Language} of this {@link Server}.
     * 
     * @return Whether this is the default {@link Language} of this {@link Server}
     */
    public boolean isDefault() {
        return this == SlimefunPlugin.getLocal().getDefaultLanguage();
    }

}
