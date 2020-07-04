package io.github.thebusybiscuit.slimefun4.core.services.metrics;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.Plugin;

/**
 * This Class represents a Metrics Service that sends data to https://bstats.org/
 * This data is used to analyse the usage of this {@link Plugin}.
 * <p>
 * You can find more info in the README file of this Project on GitHub.
 *
 * @author TheBusyBiscuit
 */
public class MetricsService {

    private final SlimefunPlugin plugin;

    /**
     * This creates a new {@link MetricsService}. The constructor does not set up
     * anything related to bStats yet, that happens in the {@link MetricsService#start()} method.
     *
     * @param plugin
     *            The instance of our {@link SlimefunPlugin}
     */
    public MetricsService(SlimefunPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * This method intializes and starts the metrics collection.
     */
    public void start() {
        Metrics metrics = new Metrics(plugin, 4574);

        metrics.addCustomChart(new ResourcePackChart());
        metrics.addCustomChart(new SlimefunVersionChart());
        metrics.addCustomChart(new ServerLanguageChart());
        metrics.addCustomChart(new PlayerLanguageChart());
        metrics.addCustomChart(new ResearchesEnabledChart());
        metrics.addCustomChart(new GuideLayoutChart());
        metrics.addCustomChart(new AddonsChart());
        metrics.addCustomChart(new CommandChart());
        metrics.addCustomChart(new ServerSizeChart());
        metrics.addCustomChart(new AutoUpdaterChart());
        metrics.addCustomChart(new CompatibilityModeChart());
    }

}
