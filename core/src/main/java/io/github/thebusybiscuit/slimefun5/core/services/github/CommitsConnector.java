package io.github.thebusybiscuit.slimefun5.core.services.github;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * This {@link GitHubConnector} aggregates the commit authors of a specific branch via the
 * {@code /commits} endpoint, rather than the repository's default branch like
 * {@link ContributionsConnector} does through {@code /contributors}.
 * <p>
 * GitHub's {@code /contributors} endpoint only reports authors of the <em>default</em> branch, so
 * anyone whose work lives on a feature/development branch (a very common situation for forks) is
 * never credited. This connector walks the commit history of a configured development branch so
 * that <em>everyone</em> who contributes to this fork is shown in the credits, not just the
 * original upstream contributors.
 *
 * @author intisy
 *
 * @see ContributionsConnector
 * @see GitHubService
 */
class CommitsConnector extends GitHubConnector {

    /**
     * GitHub Bots and invalid accounts that should not be counted as contributors.
     */
    private final List<String> ignoredAccounts = new ArrayList<>();

    private final String prefix;
    private final String role;
    private final String branch;
    private final int page;

    private boolean finished = false;

    @ParametersAreNonnullByDefault
    CommitsConnector(GitHubService github, String prefix, int page, String repository, String branch, ContributorRole role) {
        super(github, repository);

        this.prefix = prefix;
        this.page = page;
        this.branch = branch;
        this.role = role.getId();

        ignoredAccounts.add("invalid-email-address");
        ignoredAccounts.add("renovate");
        ignoredAccounts.add("renovate-bot");
        ignoredAccounts.add("renovate[bot]");
        ignoredAccounts.add("TheBusyBot");
        ignoredAccounts.add("ImgBotApp");
        ignoredAccounts.add("imgbot");
        ignoredAccounts.add("imgbot[bot]");
        ignoredAccounts.add("github-actions[bot]");
        ignoredAccounts.add("gitlocalize-app");
        ignoredAccounts.add("gitlocalize-app[bot]");
        ignoredAccounts.add("mt-gitlocalize");
    }

    /**
     * This returns whether this {@link CommitsConnector} has finished its task.
     *
     * @return Whether it is finished
     */
    public boolean hasFinished() {
        return finished;
    }

    @Override
    public void onSuccess(@Nonnull JsonElement response) {
        finished = true;

        if (response.isJsonArray()) {
            computeContributors(response.getAsJsonArray());
        } else {
            Slimefun.logger().log(Level.WARNING, "Received an unusual answer from GitHub, possibly a timeout? ({0})", response);
        }
    }

    @Override
    public void onFailure() {
        finished = true;
    }

    @Override
    public String getFileName() {
        return prefix + "_contributors";
    }

    @Override
    public String getEndpoint() {
        return "/commits";
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("sha", branch);
        parameters.put("per_page", 100);
        parameters.put("page", page);
        return parameters;
    }

    private void computeContributors(@Nonnull JsonArray array) {
        // The /commits endpoint returns one entry per commit, so we aggregate the commit count per
        // author within this page before crediting them. Each page sets the count exactly once,
        // which keeps the hourly refresh idempotent (no double-counting across runs).
        Map<String, Integer> commitsByAuthor = new LinkedHashMap<>();
        Map<String, String> profileByAuthor = new HashMap<>();

        for (JsonElement element : array) {
            JsonObject commit = element.getAsJsonObject();
            JsonElement authorElement = commit.get("author");

            // "author" is null when the commit's email is not linked to a GitHub account.
            if (authorElement == null || authorElement.isJsonNull()) {
                continue;
            }

            JsonObject author = authorElement.getAsJsonObject();
            String name = author.get("login").getAsString();

            if (ignoredAccounts.contains(name)) {
                continue;
            }

            commitsByAuthor.merge(name, 1, Integer::sum);
            profileByAuthor.putIfAbsent(name, author.get("html_url").getAsString());
        }

        for (Map.Entry<String, Integer> entry : commitsByAuthor.entrySet()) {
            String name = entry.getKey();

            // FORK_DEVELOPER is a distinct role, so this adds to the total instead of overwriting the
            // contributor's authoritative /contributors count.
            github.addContributor(name, profileByAuthor.get(name), role, entry.getValue());
        }
    }
}
