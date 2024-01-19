package io.github.thebusybiscuit.slimefun4.core.services.github;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;

import io.github.bakedlibs.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;

/**
 * This Service is responsible for grabbing every {@link Contributor} to this project
 * from GitHub and holding data associated to the project repository, such
 * as open issues or pending pull requests.
 * 
 * @author TheBusyBiscuit
 *
 */
public class GitHubService {

    private final String repository;
    private final Set<GitHubConnector> connectors;

    private boolean logging = false;

    private LocalDateTime lastUpdate = LocalDateTime.now();

    private int openIssues = 0;
    private int pendingPullRequests = 0;
    private int publicForks = 0;
    private int stargazers = 0;

    /**
     * This creates a new {@link GitHubService} for the given repository.
     * 
     * @param repository
     *            The repository to create this {@link GitHubService} for
     */
    public GitHubService(@Nonnull String repository) {
        this.repository = repository;

        connectors = new HashSet<>();
    }

    /**
     * This will start the {@link GitHubService} and run the asynchronous {@link GitHubTask}
     * every so often to update its data.
     * 
     * @param plugin
     *            Our instance of {@link Slimefun}
     */
    public void start(@Nonnull Slimefun plugin) {
        loadConnectors(false);

        long period = TimeUnit.HOURS.toMillis(1);
        GitHubTask task = new GitHubTask(this);

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, task, 30 * 20L, period);
    }

    private void loadConnectors(boolean logging) {
        this.logging = logging;

        // Issues and Pull Requests
        connectors.add(new GitHubIssuesConnector(this, repository, (issues, pullRequests) -> {
            this.openIssues = issues;
            this.pendingPullRequests = pullRequests;
        }));

        // Forks, star count and last commit date
        connectors.add(new GitHubActivityConnector(this, repository, (forks, stars, date) -> {
            this.publicForks = forks;
            this.stargazers = stars;
            this.lastUpdate = date;
        }));
    }

    protected @Nonnull Set<GitHubConnector> getConnectors() {
        return connectors;
    }

    protected boolean isLoggingEnabled() {
        return logging;
    }


    /**
     * This returns the amount of forks of our repository
     * 
     * @return The amount of forks
     */
    public int getForks() {
        return publicForks;
    }

    /**
     * This method returns the amount of stargazers of the repository.
     * 
     * @return The amount of people who starred the repository
     */
    public int getStars() {
        return stargazers;
    }

    /**
     * This returns the amount of open Issues on our repository.
     * 
     * @return The amount of open issues
     */
    public int getOpenIssues() {
        return openIssues;
    }

    /**
     * Returns the id of Slimefun's GitHub Repository. (e.g. "Slimefun/Slimefun4").
     * 
     * @return The id of our GitHub Repository
     */
    public @Nonnull String getRepository() {
        return repository;
    }

    /**
     * This method returns the amount of pending pull requests.
     * 
     * @return The amount of pending pull requests
     */
    public int getPendingPullRequests() {
        return pendingPullRequests;
    }

    /**
     * This returns the date and time of the last commit to this repository.
     * 
     * @return A {@link LocalDateTime} object representing the date and time of the latest commit
     */
    public @Nonnull LocalDateTime getLastUpdate() {
        return lastUpdate;
    }


}
