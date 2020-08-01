package io.github.thebusybiscuit.slimefun4.core.services.github;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.slimefun4.core.services.localization.Translators;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;
import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONObject;

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
    private final ConcurrentMap<String, Contributor> contributors;

    private final Config uuidCache = new Config("plugins/Slimefun/cache/github/uuids.yml");
    private final Config texturesCache = new Config("plugins/Slimefun/cache/github/skins.yml");

    private boolean logging = false;

    private int issues = 0;
    private int pullRequests = 0;
    private int forks = 0;
    private int stars = 0;
    private LocalDateTime lastUpdate = LocalDateTime.now();

    /**
     * This creates a new {@link GitHubService} for the given repository.
     * 
     * @param repository
     *            The repository to create this {@link GitHubService} for
     */
    public GitHubService(String repository) {
        this.repository = repository;

        connectors = new HashSet<>();
        contributors = new ConcurrentHashMap<>();
        loadConnectors(false);
    }

    public void start(SlimefunPlugin plugin) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new GitHubTask(this), 80L, 60 * 60 * 20L);
    }

    private void addDefaultContributors() {
        addContributor("Fuffles_", "&dArtist");
        addContributor("IMS_Art", "&dArtist");
        addContributor("nahkd123", "&aWinner of the 2020 Addon Jam");

        new Translators(this);
    }

    private void addContributor(String name, String role) {
        Contributor contributor = new Contributor(name);
        contributor.setContribution(role, 0);
        contributor.setUniqueId(uuidCache.getUUID(name));
        contributors.put(name, contributor);
    }

    public Contributor addContributor(String minecraftName, String profile, String role, int commits) {
        String username = profile.substring(profile.lastIndexOf('/') + 1);

        Contributor contributor = contributors.computeIfAbsent(username, key -> new Contributor(minecraftName, profile));
        contributor.setContribution(role, commits);
        contributor.setUniqueId(uuidCache.getUUID(minecraftName));
        return contributor;
    }

    private void loadConnectors(boolean logging) {
        this.logging = logging;
        addDefaultContributors();

        // TheBusyBiscuit/Slimefun4 (twice because there may me multiple pages)
        connectors.add(new ContributionsConnector(this, "code", 1, repository, "developer"));
        connectors.add(new ContributionsConnector(this, "code2", 2, repository, "developer"));

        // TheBusyBiscuit/Slimefun4-Wiki
        connectors.add(new ContributionsConnector(this, "wiki", 1, "Slimefun/Slimefun-wiki", "wiki"));

        // TheBusyBiscuit/Slimefun4-Resourcepack
        connectors.add(new ContributionsConnector(this, "resourcepack", 1, "Slimefun/Resourcepack", "resourcepack"));

        // Issues and Pull Requests
        connectors.add(new GitHubIssuesTracker(this, repository, (issues, pullRequests) -> {
            this.issues = issues;
            this.pullRequests = pullRequests;
        }));

        connectors.add(new GitHubConnector(this, repository) {

            @Override
            public void onSuccess(JsonNode element) {
                JSONObject object = element.getObject();
                forks = object.getInt("forks");
                stars = object.getInt("stargazers_count");
                lastUpdate = NumberUtils.parseGitHubDate(object.getString("pushed_at"));
            }

            @Override
            public String getFileName() {
                return "repo";
            }

            @Override
            public String getURLSuffix() {
                return "";
            }
        });
    }

    protected Set<GitHubConnector> getConnectors() {
        return connectors;
    }

    protected boolean isLoggingEnabled() {
        return logging;
    }

    /**
     * This returns the {@link Contributor Contributors} to this project.
     * 
     * @return A {@link ConcurrentMap} containing all {@link Contributor Contributors}
     */
    public ConcurrentMap<String, Contributor> getContributors() {
        return contributors;
    }

    /**
     * This returns the amount of forks of our repository
     * 
     * @return The amount of forks
     */
    public int getForks() {
        return forks;
    }

    /**
     * This method returns the amount of stargazers of the repository.
     * 
     * @return The amount of people who starred the repository
     */
    public int getStars() {
        return stars;
    }

    /**
     * This returns the amount of open Issues on our repository.
     * 
     * @return The amount of open issues
     */
    public int getOpenissues() {
        return issues;
    }

    /**
     * Returns the id of Slimefun's GitHub Repository. (e.g. "TheBusyBiscuit/Slimefun4").
     * 
     * @return The id of our GitHub Repository
     */
    public String getRepository() {
        return repository;
    }

    /**
     * This method returns the amount of pending pull requests.
     * 
     * @return The amount of pending pull requests
     */
    public int getPendingPullRequests() {
        return pullRequests;
    }

    /**
     * This returns the date and time of the last commit to this repository.
     * 
     * @return A {@link LocalDateTime} object representing the date and time of the latest commit
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * This will store the {@link UUID} and texture of all {@link Contributor Contributors}
     * in memory in a {@link File} to save requests the next time we iterate over them.
     */
    protected void saveCache() {
        for (Contributor contributor : contributors.values()) {
            Optional<UUID> uuid = contributor.getUniqueId();

            uuid.ifPresent(value -> uuidCache.setValue(contributor.getName(), value));

            if (contributor.hasTexture()) {
                String texture = contributor.getTexture();

                if (!texture.equals(HeadTexture.UNKNOWN.getTexture())) {
                    texturesCache.setValue(contributor.getName(), texture);
                }
            }
        }

        uuidCache.save();
        texturesCache.save();
    }

    protected String getCachedTexture(String name) {
        return texturesCache.getString(name);
    }
}
