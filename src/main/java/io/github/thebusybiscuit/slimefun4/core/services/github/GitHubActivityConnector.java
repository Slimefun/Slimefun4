package io.github.thebusybiscuit.slimefun4.core.services.github;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;

class GitHubActivityConnector extends GitHubConnector {

    private final ActivityCallback callback;

    @ParametersAreNonnullByDefault
    GitHubActivityConnector(GitHubService github, String repository, ActivityCallback callback) {
        super(github, repository);
        this.callback = callback;
    }

    @Override
    public void onSuccess(@Nonnull JsonElement response) {
        JsonObject object = response.getAsJsonObject();
        int forks = object.get("forks").getAsInt();
        int stars = object.get("stargazers_count").getAsInt();
        LocalDateTime lastPush = NumberUtils.parseGitHubDate(object.get("pushed_at").getAsString());

        callback.accept(forks, stars, lastPush);
    }

    @Override
    public String getFileName() {
        return "repo";
    }

    @Override
    public String getEndpoint() {
        return "";
    }

    @Override
    public Map<String, Object> getParameters() {
        return new HashMap<>();
    }

}
