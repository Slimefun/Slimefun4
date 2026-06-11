package io.github.thebusybiscuit.slimefun5.core.services.github;

import javax.annotation.Nonnull;

/**
 * This enum holds the different roles a {@link Contributor} can have.
 * This is only used to store various {@link String} constants for these roles.
 * The actual {@link ContributorRole} type itself is not used anywhere.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Contributor
 * @see GitHubService
 *
 */
enum ContributorRole {

    DEVELOPER("developer"),
    /**
     * Credit for commits made on this fork's development branch (see {@link CommitsConnector}).
     * The id starts with '&' so it is rendered as a literal label instead of being looked up as a
     * translation key, which keeps fork-specific crediting self-contained (no language files needed).
     */
    FORK_DEVELOPER("&aFork Developer"),
    RESOURCEPACK_ARTIST("resourcepack"),
    TRANSLATOR("translator"),
    WIKI_EDITOR("wiki");

    private final String id;

    ContributorRole(@Nonnull String id) {
        this.id = id;
    }

    @Nonnull
    String getId() {
        return id;
    }

}

