package io.github.thebusybiscuit.slimefun4.core.services.github;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;

@FunctionalInterface
interface ActivityCallback {

    /**
     * This method is called when the {@link GitHubActivityConnector} finished loading.
     *
     * @param forks The amount of forks
     * @param stars The amount of stars
     * @param date  The {@link LocalDateTime} of the last activity
     */
    void accept(int forks, int stars, @Nonnull LocalDateTime date);

}
