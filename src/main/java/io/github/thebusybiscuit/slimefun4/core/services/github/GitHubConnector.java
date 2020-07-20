package io.github.thebusybiscuit.slimefun4.core.services.github;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import me.mrCookieSlime.Slimefun.api.Slimefun;

abstract class GitHubConnector {

    protected File file;
    protected String repository;
    protected final GitHubService github;

    public GitHubConnector(GitHubService github, String repository) {
        this.github = github;
        this.repository = repository;
    }

    public abstract String getFileName();

    public abstract String getURLSuffix();

    public abstract void onSuccess(JsonNode element);

    public void onFailure() {
        // Don't do anything by default
    }

    public void pullFile() {
        file = new File("plugins/Slimefun/cache/github/" + getFileName() + ".json");

        if (github.isLoggingEnabled()) {
            Slimefun.getLogger().log(Level.INFO, "Retrieving {0}.json from GitHub...", getFileName());
        }

        try {
            HttpResponse<JsonNode> resp = Unirest
                .get("https://api.github.com/repos/" + repository + getURLSuffix())
                .header("User-Agent", "Slimefun4 (https://github.com/Slimefun)")
                .asJson();

            if (resp.isSuccess()) {
                onSuccess(resp.getBody());
                writeCacheFile(resp.getBody());
            } else
                Slimefun.getLogger().log(Level.WARNING, "Failed to fetch {0}",
                    repository + getURLSuffix());
        } catch (UnirestException e) {
            if (github.isLoggingEnabled()) {
                Slimefun.getLogger().log(Level.WARNING, "Could not connect to GitHub in time.");
            }
            onFailure();
        }
    }

    private void writeCacheFile(JsonNode node) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(node.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            Slimefun.getLogger().log(Level.WARNING, "Failed to populate GitHub cache");
        }
    }
}
