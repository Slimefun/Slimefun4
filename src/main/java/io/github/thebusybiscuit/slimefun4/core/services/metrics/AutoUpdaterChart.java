package io.github.thebusybiscuit.slimefun4.core.services.metrics;

import org.bstats.bukkit.Metrics.SimplePie;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

class AutoUpdaterChart extends SimplePie {

    AutoUpdaterChart() {
        super("auto_updates", () -> {
            boolean enabled = SlimefunPlugin.getUpdater().isEnabled();
            return enabled ? "enabled" : "disabled";
        });
    }

}
