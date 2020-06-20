package io.github.thebusybiscuit.slimefun4.core.services.metrics;

import org.bstats.bukkit.Metrics.SimplePie;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

class CompatibilityModeChart extends SimplePie {

    CompatibilityModeChart() {
        super("compatibility_mode", () -> {
            boolean enabled = SlimefunPlugin.getRegistry().isBackwardsCompatible();
            return enabled ? "enabled" : "disabled";
        });
    }

}
