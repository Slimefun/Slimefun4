package io.github.thebusybiscuit.slimefun5.core.services;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Delivers in-game bug reports. A report is posted to a Discord webhook AND opened as a GitHub
 * issue on the target repository (whichever of the two is configured). Everything that touches the
 * network runs off the main thread; the player only gets a single success/failure message back.
 *
 * <p>Reports are posted by the server's configured service token/webhook and credit the reporting
 * player by name - we never ask a player to connect their own GitHub account.
 *
 * @author Slimefun5
 */
public final class BugReportService {

    private static final String USER_AGENT = "Slimefun5 (https://github.com/Slimefun5)";
    private static final String DEFAULT_RELAY = "https://slimefun5-bot.finn-089.workers.dev/report";
    private static final int TIMEOUT = 10_000;

    private BugReportService() {}

    public static boolean isEnabled() {
        return Slimefun.getCfg().getBoolean("bug-reports.enabled");
    }

    /** Per-player anti-spam: timestamp of each player's last accepted report. */
    private static final Map<UUID, Long> lastReport = new ConcurrentHashMap<>();

    private static long cooldownSeconds() {
        return Slimefun.getCfg().contains("bug-reports.cooldown-seconds")
            ? Math.max(0, Slimefun.getCfg().getInt("bug-reports.cooldown-seconds"))
            : 60;
    }

    /** True when this player submitted a report within the configured cooldown window (read-only). */
    public static boolean isOnCooldown(@Nonnull Player p) {
        long cooldownMs = cooldownSeconds() * 1000L;

        if (cooldownMs <= 0) {
            return false;
        }

        Long last = lastReport.get(p.getUniqueId());
        return last != null && System.currentTimeMillis() - last < cooldownMs;
    }

    /**
     * Submits a bug report asynchronously and reports the outcome to the player.
     *
     * @param p           the reporting {@link Player}
     * @param title       the report title
     * @param description the report body
     * @param plugins     the affected plugin/addon display names (may be several)
     */
    public static void submit(@Nonnull Player p, @Nonnull String title, @Nonnull String description, @Nonnull List<String> plugins) {
        if (isOnCooldown(p)) {
            Slimefun.getLocalization().sendMessage(p, "guide.report.cooldown", true);
            return;
        }

        lastReport.put(p.getUniqueId(), System.currentTimeMillis());

        String player = p.getName();
        String mcVersion = Bukkit.getBukkitVersion();
        String sfVersion = Slimefun.getVersion();
        String pluginList = plugins.isEmpty() ? "(unspecified)" : String.join(", ", plugins);

        Bukkit.getScheduler().runTaskAsynchronously(Slimefun.instance(), () -> {
            boolean relayed = postRelay(title, description, plugins, player, mcVersion, sfVersion);
            boolean discord = !relayed && postDiscord(title, description, pluginList, player, mcVersion, sfVersion);
            boolean github = postGitHubIssue(title, description, pluginList, player, mcVersion, sfVersion);
            boolean delivered = relayed || discord || github;

            Slimefun.runSync(() -> Slimefun.getLocalization().sendMessage(p, delivered ? "guide.report.success" : "guide.report.failed", true));
        });
    }

    private static boolean postRelay(String title, String description, List<String> plugins, String player, String mc, String sf) {
        // Absent key (older config that predates this feature) falls back to the built-in relay;
        // an explicitly emptied key disables relaying.
        String relay = Slimefun.getCfg().contains("bug-reports.relay-url")
            ? Slimefun.getCfg().getString("bug-reports.relay-url")
            : DEFAULT_RELAY;

        if (relay == null || relay.trim().isEmpty()) {
            return false;
        }

        JsonArray pluginsJson = new JsonArray();
        for (String plugin : plugins) {
            pluginsJson.add(new JsonPrimitive(plugin));
        }

        JsonObject body = new JsonObject();
        body.addProperty("title", title);
        body.addProperty("description", description);
        body.add("plugins", pluginsJson);
        body.addProperty("player", player);
        body.addProperty("mcVersion", mc);
        body.addProperty("sfVersion", sf);

        return post(relay.trim(), body.toString(), null, null);
    }

    private static boolean postDiscord(String title, String description, String plugins, String player, String mc, String sf) {
        String webhook = Slimefun.getCfg().getString("bug-reports.discord.webhook-url");

        if (webhook == null || webhook.trim().isEmpty()) {
            return false;
        }

        JsonObject body = new JsonObject();
        body.addProperty("content", "**Bug Report: " + title + "**\n**Plugins:** " + plugins + "\n**Player:** " + player + "\n**Slimefun:** " + sf + "  **MC:** " + mc + "\n\n" + description);

        return post(webhook, body.toString(), null, null);
    }

    private static boolean postGitHubIssue(String title, String description, String plugins, String player, String mc, String sf) {
        String token = Slimefun.getCfg().getString("bug-reports.github.token");

        if (token == null || token.trim().isEmpty()) {
            return false;
        }

        String owner = Slimefun.getCfg().getString("bug-reports.github.owner");
        if (owner == null || owner.trim().isEmpty()) {
            owner = "Slimefun5";
        }

        JsonObject body = new JsonObject();
        body.addProperty("title", title);
        body.addProperty("body", "Reported in-game by **" + player + "**\n\n- Affected: " + plugins + "\n- Slimefun: `" + sf + "`\n- Minecraft: `" + mc + "`\n\n---\n\n" + description);

        JsonArray labels = new JsonArray();
        labels.add(new JsonPrimitive("in-game-report"));
        body.add("labels", labels);

        return post("https://api.github.com/repos/" + owner + "/Slimefun5/issues", body.toString(), "token " + token.trim(), "application/vnd.github+json");
    }

    private static boolean post(@Nonnull String endpoint, @Nonnull String json, @Nullable String authorization, @Nullable String accept) {
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) new URI(endpoint).toURL().openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            connection.setDoOutput(true);
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Content-Type", "application/json");

            if (accept != null) {
                connection.setRequestProperty("Accept", accept);
            }
            if (authorization != null) {
                connection.setRequestProperty("Authorization", authorization);
            }

            try (OutputStream out = connection.getOutputStream()) {
                out.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int status = connection.getResponseCode();
            if (status >= 200 && status < 300) {
                return true;
            }
            Slimefun.logger().log(Level.WARNING, "Bug report POST to {0} returned HTTP {1}", new Object[] { hostOf(endpoint), status });
            return false;
        } catch (Exception e) {
            Slimefun.logger().log(Level.WARNING, "Bug report POST to {0} failed: {1}", new Object[] { hostOf(endpoint), e.getClass().getSimpleName() + ": " + e.getMessage() });
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String hostOf(String endpoint) {
        try {
            return URI.create(endpoint).getHost();
        } catch (Exception e) {
            return endpoint;
        }
    }
}
