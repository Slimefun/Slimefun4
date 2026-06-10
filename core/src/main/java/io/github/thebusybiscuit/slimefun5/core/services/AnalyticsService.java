package io.github.thebusybiscuit.slimefun5.core.services;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import io.github.thebusybiscuit.slimefun5.core.debug.Debug;
import io.github.thebusybiscuit.slimefun5.core.debug.TestCase;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * This class represents an analytics service that sends data.
 * This data is used to analyse performance of this {@link Plugin}.
 * <p>
 * You can find more info in the README file of this Project on GitHub.
 *
 * @author WalshyDev
 */
public class AnalyticsService {

    private static final int VERSION = 1;
    private static final String API_URL = "https://analytics.slimefun.dev/ingest";

    private final JavaPlugin plugin;

    private boolean enabled;

    public AnalyticsService(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        this.enabled = Slimefun.getCfg().getBoolean("metrics.analytics");

        if (enabled) {
            plugin.getLogger().info("Enabled Analytics Service");

            // Send the timings data every minute
            Slimefun.getThreadService().newScheduledThread(
                plugin,
                "AnalyticsService - Timings",
                sendTimingsAnalytics(),
                1,
                1,
                TimeUnit.MINUTES
            );
        }
    }

    // We'll send some timing data every minute.
    // To date, we collect the tick interval, the avg timing per tick and avg timing per machine
    @Nonnull
    private Runnable sendTimingsAnalytics() {
        return () -> {
            double tickInterval = Slimefun.getTickerTask().getTickRate();
            // This is currently used by bStats in a ranged way, we'll move this
            double totalTimings = Slimefun.getProfiler().getAndResetAverageNanosecondTimings();
            double avgPerMachine = Slimefun.getProfiler().getAverageTimingsPerMachine();

            if (totalTimings == 0 || avgPerMachine == 0) {
                Debug.log(TestCase.ANALYTICS, "Ignoring analytics data for server_timings as no data was found"
                    + " - total: " + totalTimings + ", avg: " + avgPerMachine);
                // Ignore if no data
                return;
            }

            send("server_timings", new double[]{
                // double1 is schema version
                tickInterval, // double2
                totalTimings, // double3
                avgPerMachine // double4
            }, null);
        };
    }

    public void recordPlayerProfileDataTime(@Nonnull String backend, boolean load, long nanoseconds) {
        send(
            "player_profile_data_load_time",
            new double[]{
                // double1 is schema version
                nanoseconds, // double2
                load ? 1 : 0 // double3 - 1 if load, 0 if save
            },
            new String[]{
                // blob1 is version
                backend // blob2
            }
        );
    }

    // Important: Keep the order of these doubles and blobs the same unless you increment the version number
    // If a value is no longer used, just send null or replace it with a new value - don't shift the order
    @ParametersAreNonnullByDefault
    private void send(String id, double[] doubles, String[] blobs) {
        // If not enabled or not official build (e.g. local build) or a unit test, just ignore.
        if (
            !enabled
            || !Slimefun.getUpdater().getBranch().isOfficial()
            || Slimefun.instance().isUnitTest()
        ) return;

        JsonObject object = new JsonObject();
        // Up to 1 index
        JsonArray indexes = new JsonArray();
        indexes.add(new JsonPrimitive(id));
        object.add("indexes", indexes);

        // Up to 20 doubles (including the version)
        JsonArray doublesArray = new JsonArray();
        doublesArray.add(new JsonPrimitive(VERSION));
        if (doubles != null) {
            for (double d : doubles) {
                doublesArray.add(new JsonPrimitive(d));
            }
        }
        object.add("doubles", doublesArray);

        // Up to 20 blobs (including the version)
        JsonArray blobsArray = new JsonArray();
        blobsArray.add(new JsonPrimitive(Slimefun.getVersion()));
        if (blobs != null) {
            for (String s : blobs) {
                blobsArray.add(new JsonPrimitive(s));
            }
        }
        object.add("blobs", blobsArray);

        Debug.log(TestCase.ANALYTICS, "Sending analytics data for " + id);
        Debug.log(TestCase.ANALYTICS, object.toString());

        // Java 8: dispatch the POST on an async task so we don't block the calling thread.
        // We do not care about the result. If it fails, that's fine.
        String payload = object.toString();
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> postAnalytics(id, payload));
    }

    // Performs the actual HTTP POST using Java 8's HttpURLConnection (the universal jar targets
    // Java 8, so the Java 11 HttpClient is unavailable). Must be called off the main thread.
    private void postAnalytics(@Nonnull String id, @Nonnull String payload) {
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) new URL(API_URL).openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(10_000);
            connection.setReadTimeout(10_000);
            connection.setDoOutput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 Slimefun5 AnalyticsService");
            connection.setRequestProperty("Content-Type", "application/json");

            try (OutputStream output = connection.getOutputStream()) {
                output.write(payload.getBytes(StandardCharsets.UTF_8));
            }

            int statusCode = connection.getResponseCode();

            if (statusCode == 200) {
                Debug.log(TestCase.ANALYTICS, "Analytics data for " + id + " sent successfully");
            } else {
                Debug.log(TestCase.ANALYTICS, "Analytics data for " + id + " failed to send - " + statusCode);
            }
        } catch (IOException e) {
            Debug.log(TestCase.ANALYTICS, "Analytics data for " + id + " failed to send - " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}

