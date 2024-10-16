package io.github.thebusybiscuit.slimefun4.core.services.github;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.JsonUtils;

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
    private static final String USER_AGENT = "Slimefun4 (https://github.com/Slimefun)";
    private static final HttpClient client = HttpClient.newHttpClient();

    protected final GitHubService github;
    private final String url;
    private File file;

    /**
     * This creates a new {@link GitHubConnector} for the given repository.
     * 
     * @param github
     *            Our instance of {@link GitHubService}
     * @param repository
     *            The repository we want to connect to
     */
    GitHubConnector(@Nonnull GitHubService github, @Nonnull String repository) {
        this.github = github;
        this.url = API_URL + "repos/" + repository + getEndpoint();
    }

    /**
     * This returns the name of our cache {@link File}.
     * 
     * @return The cache {@link File} name
     */
    @Nonnull
    public abstract String getFileName();

    /**
     * This is our {@link URL} endpoint.
     * It is the suffix of the {@link URL} we want to connect to.
     * 
     * @return Our endpoint
     */
    @Nonnull
    public abstract String getEndpoint();

    /**
     * This {@link Map} contains the query parameters for our {@link URL}.
     * 
     * @return A {@link Map} with our query parameters
     */
    @Nonnull
    public abstract Map<String, Object> getParameters();

    /**
     * This method is called when the connection finished successfully.
     * 
     * @param response
     *            The response
     */
    public abstract void onSuccess(@Nonnull JsonElement response);

    /**
     * This method is called when the connection has failed.
     */
    public void onFailure() {
        // Don't do anything by default
    }

    /**
     * This method will connect to GitHub and store the received data inside a local
     * cache {@link File}.
     * Make sure to call this method asynchronously!
     */
    void download() {
        file = new File("plugins/Slimefun/cache/github/" + getFileName() + ".json");

        if (github.isLoggingEnabled()) {
            Slimefun.logger().log(Level.INFO, "Retrieving {0}.json from GitHub...", getFileName());
        }

        try {
            String params = getParameters().entrySet().stream()
                .map(p -> p.getKey() + "=" + p.getValue())
                .reduce((p1, p2) -> p1 + "&" + p2)
                .map(s -> "?" + s)
                .orElse("");
            URI uri = new URI(url + params);

            HttpResponse<String> response = client.send(
                HttpRequest.newBuilder(uri).header("User-Agent", USER_AGENT).build(),
                HttpResponse.BodyHandlers.ofString()
            );
            JsonElement element = JsonUtils.parseString(response.body());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                onSuccess(element);
                writeCacheFile(element);
            } else {
                if (github.isLoggingEnabled()) {
                    Slimefun.logger().log(Level.WARNING, "Failed to fetch {0}: {1} - {2}", new Object[] { url, response.statusCode(), element });
                }

                // It has the cached file, let's just read that then
                if (file.exists()) {
                    JsonElement cache = readCacheFile();

                    if (cache != null) {
                        onSuccess(cache);
                    }
                }
            }
        } catch (IOException | InterruptedException | JsonParseException | URISyntaxException e) {
            if (github.isLoggingEnabled()) {
                Slimefun.logger().log(Level.WARNING, "Could not connect to GitHub in time.", e);
            }

            // It has the cached file, let's just read that then
            if (file.exists()) {
                JsonElement cache = readCacheFile();

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
    private JsonElement readCacheFile() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            return JsonUtils.parseString(reader.readLine());
        } catch (IOException | JsonParseException e) {
            Slimefun.logger().log(Level.WARNING, "Failed to read Github cache file: {0} - {1}: {2}", new Object[] { file.getName(), e.getClass().getSimpleName(), e.getMessage() });
            return null;
        }
    }

    private void writeCacheFile(@Nonnull JsonElement node) {
        try (FileOutputStream output = new FileOutputStream(file)) {
            output.write(node.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            Slimefun.logger().log(Level.WARNING, "Failed to populate GitHub cache: {0} - {1}", new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        }
    }
}
