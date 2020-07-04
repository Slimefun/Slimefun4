package io.github.thebusybiscuit.slimefun4.core.services.github;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

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

    public abstract void onSuccess(JsonElement element);

    public void onFailure() {
        // Don't do anything by default
    }

    public void pullFile() {
        file = new File("plugins/Slimefun/cache/github/" + getFileName() + ".json");

        if (github.isLoggingEnabled()) {
            Slimefun.getLogger().log(Level.INFO, "Retrieving {0}.json from GitHub...", getFileName());
        }

        try {
            URL website = new URL("https://api.github.com/repos/" + repository + getURLSuffix());

            URLConnection connection = website.openConnection();
            connection.setConnectTimeout(8000);
            connection.addRequestProperty("Accept-Charset", "UTF-8");
            connection.addRequestProperty("User-Agent", "Slimefun 4 GitHub Agent (by TheBusyBiscuit)");
            connection.setDoOutput(true);

            try (ReadableByteChannel channel = Channels.newChannel(connection.getInputStream())) {
                try (FileOutputStream stream = new FileOutputStream(file)) {
                    stream.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
                    parseData();
                }
            }
        } catch (IOException e) {
            if (github.isLoggingEnabled()) {
                Slimefun.getLogger().log(Level.WARNING, "Could not connect to GitHub in time.");
            }

            if (hasData()) {
                parseData();
            } else {
                onFailure();
            }
        }
    }

    public boolean hasData() {
        return getFile().exists();
    }

    public File getFile() {
        return file;
    }

    public void parseData() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(getFile()), StandardCharsets.UTF_8))) {
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            JsonElement element = new JsonParser().parse(builder.toString());
            onSuccess(element);
        } catch (IOException x) {
            Slimefun.getLogger().log(Level.SEVERE, x, () -> "An Error occured while parsing GitHub-Data for Slimefun " + SlimefunPlugin.getVersion());
            onFailure();
        }
    }
}