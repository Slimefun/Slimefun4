package io.github.thebusybiscuit.slimefun4.core.services.github;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.github.thebusybiscuit.cscorelib2.players.MinecraftAccount;
import io.github.thebusybiscuit.cscorelib2.players.MinecraftAccount.TooManyRequestsException;

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

    private final GitHubService gitHubService;

    public GitHubTask(GitHubService github) {
        gitHubService = github;
    }

    @Override
    public void run() {
        gitHubService.getConnectors().forEach(GitHubConnector::pullFile);

        // Store all queried usernames to prevent 429 responses for pinging the
        // same URL twice in one run.
        Map<String, String> skins = new HashMap<>();

        for (Contributor contributor : gitHubService.getContributors().values()) {
            if (!contributor.hasTexture()) {
                try {
                    if (skins.containsKey(contributor.getMinecraftName())) {
                        contributor.setTexture(skins.get(contributor.getMinecraftName()));
                    }
                    else {
                        contributor.setTexture(grabTexture(skins, contributor.getMinecraftName()));
                    }
                }
                catch (IllegalArgumentException x) {
                    // There cannot be a texture found because it is not a valid MC username
                    contributor.setTexture(null);
                }
                catch (Exception x) {
                    // Too many requests
                    break;
                }
            }
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
