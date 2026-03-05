package io.github.thebusybiscuit.slimefun4.core.services.github;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

class GitHubIssuesConnector extends GitHubConnector {

    private final IssuesCallback callback;

    @ParametersAreNonnullByDefault
    GitHubIssuesConnector(GitHubService github, String repository, IssuesCallback callback) {
        super(github, repository);
        this.callback = callback;
    }

    @Override
    public void onSuccess(@Nonnull JsonElement response) {
        if (response.isJsonArray()) {
            JsonArray array = response.getAsJsonArray();

            int issues = 0;
            int pullRequests = 0;

            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();

                if (obj.has("pull_request")) {
                    pullRequests++;
                } else {
                    issues++;
                }
            }

            callback.accept(issues, pullRequests);
        } else {
            Slimefun.logger().log(Level.WARNING, "Received an unusual answer from GitHub, possibly a timeout? ({0})", response);
        }
    }

    @Override
    public String getFileName() {
        return "issues";
    }

    @Override
    public String getEndpoint() {
        return "/issues";
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("per_page", 100);
        return parameters;
    }

}
