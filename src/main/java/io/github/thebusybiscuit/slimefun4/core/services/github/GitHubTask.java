package io.github.thebusybiscuit.slimefun4.core.services.github;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import io.github.thebusybiscuit.cscorelib2.players.MinecraftAccount;
import io.github.thebusybiscuit.cscorelib2.players.MinecraftAccount.TooManyRequestsException;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * This {@link GitHubTask} represents a {@link Runnable} that is run every X minutes.
 * It retrieves every {@link Contributor} of this project from GitHub.
 * 
 * @author TheBusyBiscuit
 * 
 * @see GitHubService
 * @see Contributor
 *
 */
class GitHubTask implements Runnable {

    private static final int MAX_REQUESTS_PER_MINUTE = 12;

    private final GitHubService gitHubService;

    GitHubTask(GitHubService github) {
        gitHubService = github;
    }

    @Override
    public void run() {
        gitHubService.getConnectors().forEach(GitHubConnector::pullFile);

        grabTextures();
    }

    private void grabTextures() {
        // Store all queried usernames to prevent 429 responses for pinging the
        // same URL twice in one run.
        Map<String, String> skins = new HashMap<>();
        int count = 0;

        for (Contributor contributor : gitHubService.getContributors().values()) {
            if (!contributor.hasTexture()) {
                try {
                    if (skins.containsKey(contributor.getMinecraftName())) {
                        contributor.setTexture(skins.get(contributor.getMinecraftName()));
                    }
                    else {
                        contributor.setTexture(grabTexture(skins, contributor.getMinecraftName()));
                        count++;

                        if (count >= MAX_REQUESTS_PER_MINUTE) {
                            break;
                        }
                    }
                }
                catch (IllegalArgumentException x) {
                    // There cannot be a texture found because it is not a valid MC username
                    contributor.setTexture(null);
                }
                catch (IOException x) {
                    // Too many requests
                    Slimefun.getLogger().log(Level.WARNING, "Attempted to connect to mojang.com, got this response: {0}: {1}", new Object[] { x.getClass().getSimpleName(), x.getMessage() });
                    Slimefun.getLogger().log(Level.WARNING, "This usually means mojang.com is down or started to rate-limit this connection, this is not an error message!");

                    // Retry after 2 minutes if it was rate-limiting
                    if (x.getMessage().contains("429")) {
                        Bukkit.getScheduler().runTaskLaterAsynchronously(SlimefunPlugin.instance, this::grabTextures, 2 * 60 * 20L);
                    }
                    return;
                }
                catch (TooManyRequestsException x) {
                    Slimefun.getLogger().log(Level.WARNING, "Received a rate-limit from mojang.com, retrying in 2 minutes");
                    Bukkit.getScheduler().runTaskLaterAsynchronously(SlimefunPlugin.instance, this::grabTextures, 2 * 60 * 20L);
                    return;
                }
            }
        }

        if (count >= MAX_REQUESTS_PER_MINUTE) {
            // Slow down API requests and wait a minute after more than x requests were made
            Bukkit.getScheduler().runTaskLaterAsynchronously(SlimefunPlugin.instance, this::grabTextures, 60 * 20L);
        }
    }

    private String grabTexture(Map<String, String> skins, String username) throws TooManyRequestsException, IOException {
        Optional<UUID> uuid = MinecraftAccount.getUUID(username);

        if (uuid.isPresent()) {
            Optional<String> skin = MinecraftAccount.getSkin(uuid.get());
            skins.put(username, skin.orElse(""));
            return skin.orElse(null);
        }
        else {
            return null;
        }
    }

}
