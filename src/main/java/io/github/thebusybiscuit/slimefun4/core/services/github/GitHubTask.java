package io.github.thebusybiscuit.slimefun4.core.services.github;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;

import io.github.bakedlibs.dough.skins.PlayerSkin;
import io.github.bakedlibs.dough.skins.UUIDLookup;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

/**
 * This {@link GitHubTask} represents a {@link Runnable} that is run every X minutes.
 * It retrieves every {@link Contributor} of this project from GitHub.
 * 
 * @author TheBusyBiscuit
 * 
 * @see GitHubService
 *
 */
class GitHubTask implements Runnable {

    private static final int MAX_REQUESTS_PER_MINUTE = 16;
    private final GitHubService gitHubService;

    GitHubTask(@Nonnull GitHubService github) {
        gitHubService = github;
    }

    @Override
    public void run() {

        if (Bukkit.isPrimaryThread()) {
            Slimefun.logger().log(Level.SEVERE, "The contributors task may never run on the main Thread!");
            return;
        }

        connectAndCache();
    }

    private void connectAndCache() {
        gitHubService.getConnectors().forEach(GitHubConnector::download);
    }

}
