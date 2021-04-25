package io.github.thebusybiscuit.slimefun4.core.services.localization;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;

/**
 * This enum holds the different types of files each {@link Language} holds.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Language
 * @see SlimefunLocalization
 *
 */
public enum LanguageFile {

    MESSAGES("messages.yml"),
    CATEGORIES("categories.yml"),
    RECIPES("recipes.yml"),
    RESOURCES("resources.yml"),
    RESEARCHES("researches.yml");

    protected static final LanguageFile[] valuesCached = values();

    private final String fileName;

    LanguageFile(@Nonnull String fileName) {
        this.fileName = fileName;
    }

    @Nonnull
    public String getFilePath(@Nonnull Language language) {
        return getFilePath(language.getId());
    }

    @Nonnull
    public String getFilePath(@Nonnull String languageId) {
        Validate.notNull(languageId, "Language id must not be null!");
        return "/languages/" + languageId + '/' + fileName;
    }

}
