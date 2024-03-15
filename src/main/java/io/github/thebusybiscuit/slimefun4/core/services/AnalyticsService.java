package io.github.thebusybiscuit.slimefun4.core.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.github.thebusybiscuit.slimefun4.core.debug.Debug;
import io.github.thebusybiscuit.slimefun4.core.debug.TestCase;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

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
    private final HttpClient client = HttpClient.newHttpClient();

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
        indexes.add(id);
        object.add("indexes", indexes);

        // Up to 20 doubles (including the version)
        JsonArray doublesArray = new JsonArray();
        doublesArray.add(VERSION);
        if (doubles != null) {
            for (double d : doubles) {
                doublesArray.add(d);
            }
        }
        object.add("doubles", doublesArray);

        // Up to 20 blobs (including the version)
        JsonArray blobsArray = new JsonArray();
        blobsArray.add(Slimefun.getVersion());
        if (blobs != null) {
            for (String s : blobs) {
                blobsArray.add(s);
            }
        }
        object.add("blobs", blobsArray);

        Debug.log(TestCase.ANALYTICS, "Sending analytics data for " + id);
        Debug.log(TestCase.ANALYTICS, object.toString());

        // Send async, we do not care about the result. If it fails, that's fine.
        client.sendAsync(HttpRequest.newBuilder()
            .uri(URI.create(API_URL))
            .header("User-Agent", "Mozilla/5.0 Slimefun4 AnalyticsService")
            .POST(HttpRequest.BodyPublishers.ofString(object.toString()))
            .build(),
            HttpResponse.BodyHandlers.discarding()
        ).thenAcceptAsync((res) -> {
            if (res.statusCode() == 200) {
                Debug.log(TestCase.ANALYTICS, "Analytics data for " + id + " sent successfully");
            } else {
                Debug.log(TestCase.ANALYTICS, "Analytics data for " + id + " failed to send - " + res.statusCode());
            }
        });
    }
}
