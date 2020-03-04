package io.github.thebusybiscuit.slimefun4.core.services.github;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.plugin.Plugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.thebusybiscuit.slimefun4.core.services.localization.Translators;
import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;

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

    private boolean logging = false;

    private int issues = 0;
    private int pullRequests = 0;
    private int forks = 0;
    private int stars = 0;
    private Date lastUpdate = new Date();

    public GitHubService(String repository) {
        this.repository = repository;

        connectors = new HashSet<>();
        contributors = new ConcurrentHashMap<>();
    }

    public void start(Plugin plugin) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new GitHubTask(this), 80L, 60 * 60 * 20L);
    }

    private void addDefaultContributors() {
        Contributor fuffles = new Contributor("Fuffles_");
        fuffles.setContribution("&dSkull Texture Artist", 0);
        contributors.put(fuffles.getName(), fuffles);

        new Translators(contributors);
    }

    public void connect(boolean logging) {
        this.logging = logging;
        addDefaultContributors();

        // TheBusyBiscuit/Slimefun4 (twice because there may me multiple pages)
        connectors.add(new ContributionsConnector(this, "code", 1, repository, "developer"));
        connectors.add(new ContributionsConnector(this, "code2", 2, repository, "developer"));

        // TheBusyBiscuit/Slimefun4-Wiki
        connectors.add(new ContributionsConnector(this, "wiki", 1, "TheBusyBiscuit/Slimefun4-wiki", "wiki"));

        // TheBusyBiscuit/Slimefun4-Resourcepack
        connectors.add(new ContributionsConnector(this, "resourcepack", 1, "TheBusyBiscuit/Slimefun4-Resourcepack", "resourcepack"));

        connectors.add(new GitHubConnector(this) {

            @Override
            public void onSuccess(JsonElement element) {
                JsonObject object = element.getAsJsonObject();
                forks = object.get("forks").getAsInt();
                stars = object.get("stargazers_count").getAsInt();
                lastUpdate = NumberUtils.parseGitHubDate(object.get("pushed_at").getAsString());
            }

            @Override
            public String getRepository() {
                return repository;
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

        connectors.add(new GitHubConnector(this) {

            @Override
            public void onSuccess(JsonElement element) {
                JsonArray array = element.getAsJsonArray();

                int issueCount = 0;
                int prCount = 0;

                for (JsonElement elem : array) {
                    JsonObject obj = elem.getAsJsonObject();

                    if (obj.has("pull_request")) prCount++;
                    else issueCount++;
                }

                issues = issueCount;
                pullRequests = prCount;
            }

            @Override
            public String getRepository() {
                return repository;
            }

            @Override
            public String getFileName() {
                return "issues";
            }

            @Override
            public String getURLSuffix() {
                return "/issues";
            }
        });
    }

    public Set<GitHubConnector> getConnectors() {
        return connectors;
    }

    public ConcurrentMap<String, Contributor> getContributors() {
        return contributors;
    }

    public int getForks() {
        return forks;
    }

    public int getStars() {
        return stars;
    }

    public int getIssues() {
        return issues;
    }

    public int getPullRequests() {
        return pullRequests;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public boolean isLoggingEnabled() {
        return logging;
    }
}
