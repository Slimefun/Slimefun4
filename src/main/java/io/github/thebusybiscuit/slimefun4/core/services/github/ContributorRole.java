package io.github.thebusybiscuit.slimefun4.core.services.github;

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
