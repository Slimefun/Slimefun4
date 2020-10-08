package io.github.thebusybiscuit.slimefun4.core.services.github;

import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import me.mrCookieSlime.Slimefun.api.Slimefun;

class GitHubIssuesConnector extends GitHubConnector {

    private final IssuesCallback callback;

    @ParametersAreNonnullByDefault
    GitHubIssuesConnector(GitHubService github, String repository, IssuesCallback callback) {
        super(github, repository);
        this.callback = callback;
    }

    @Override
    public void onSuccess(@Nonnull JsonNode response) {
        if (response.isArray()) {
            JSONArray array = response.getArray();

            int issues = 0;
            int pullRequests = 0;

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                if (obj.has("pull_request")) {
                    pullRequests++;
                } else {
                    issues++;
                }
            }

            callback.accept(issues, pullRequests);
        } else {
            Slimefun.getLogger().log(Level.WARNING, "Received an unusual answer from GitHub, possibly a timeout? ({0})", response);
        }
    }

    @Override
    public String getFileName() {
        return "issues";
    }

    @Override
    public String getURLSuffix() {
        return "/issues?per_page=100";
    }

}
