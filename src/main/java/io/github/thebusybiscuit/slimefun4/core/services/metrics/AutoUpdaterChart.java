package io.github.thebusybiscuit.slimefun4.core.services.metrics;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import org.bstats.bukkit.Metrics.SimplePie;

class AutoUpdaterChart extends SimplePie {

    AutoUpdaterChart() {
        super("auto_updates", () -> {
            boolean enabled = SlimefunPlugin.getCfg().getBoolean("options.auto-update");
            return enabled ? "enabled" : "disabled";
        });
    }

}