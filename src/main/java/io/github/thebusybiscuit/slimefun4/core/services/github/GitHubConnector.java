package io.github.thebusybiscuit.slimefun4.core.services.github;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONException;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * The {@link GitHubConnector} is used to connect to the GitHub API service.
 * It can be extended by subclasses, this just serves as an abstract super class for
 * other connectors.
 * 
 * @author TheBusyBiscuit
 * @author Walshy
 */
abstract class GitHubConnector {

    private static final String API_URL = "https://api.github.com/";

    protected File file;
    protected String repository;
    protected final GitHubService github;

    @ParametersAreNonnullByDefault
    public GitHubConnector(GitHubService github, String repository) {
        this.github = github;
        this.repository = repository;
    }

    @Nonnull
    public abstract String getFileName();

    @Nonnull
    public abstract String getURLSuffix();

    /**
     * This method is called when the connection finished successfully.
     * 
     * @param response
     *            The response
     */
    public abstract void onSuccess(@Nonnull JsonNode response);

    /**
     * This method is called when the connection has failed.
     */
    public void onFailure() {
        // Don't do anything by default
    }

    public void pullFile() {
        file = new File("plugins/Slimefun/cache/github/" + getFileName() + ".json");

        if (github.isLoggingEnabled()) {
            Slimefun.getLogger().log(Level.INFO, "Retrieving {0}.json from GitHub...", getFileName());
        }

        try {
            HttpResponse<JsonNode> resp = Unirest.get(API_URL + "repos/" + repository + getURLSuffix())
                    .header("User-Agent", "Slimefun4 (https://github.com/Slimefun)")
                    .asJson();

            if (resp.isSuccess()) {
                onSuccess(resp.getBody());
                writeCacheFile(resp.getBody());
            } else {
                if (github.isLoggingEnabled()) {
                    Slimefun.getLogger().log(Level.WARNING, "Failed to fetch {0}: {1} - {2}", new Object[] { repository + getURLSuffix(), resp.getStatus(), resp.getBody() });
                }

                // It has the cached file, let's just read that then
                if (file.exists()) {
                    JsonNode cache = readCacheFile();

                    if (cache != null) {
                        onSuccess(cache);
                    }
                }
            }
        } catch (UnirestException e) {
            if (github.isLoggingEnabled()) {
                Slimefun.getLogger().log(Level.WARNING, "Could not connect to GitHub in time.", e);
            }

            // It has the cached file, let's just read that then
            if (file.exists()) {
                JsonNode cache = readCacheFile();

                if (cache != null) {
                    onSuccess(cache);
                    return;
                }
            }

            // If the request failed and it failed to read the cache then call onFailure.
            onFailure();
        }
    }

    @Nullable
    private JsonNode readCacheFile() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            return new JsonNode(reader.readLine());
        } catch (IOException | JSONException e) {
            Slimefun.getLogger().log(Level.WARNING, "Failed to read Github cache file: {0} - {1}: {2}", new Object[] { file.getName(), e.getClass().getSimpleName(), e.getMessage() });
            return null;
        }
    }

    private void writeCacheFile(@Nonnull JsonNode node) {
        try (FileOutputStream output = new FileOutputStream(file)) {
            output.write(node.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            Slimefun.getLogger().log(Level.WARNING, "Failed to populate GitHub cache: {0} - {1}", new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        }
    }
}
