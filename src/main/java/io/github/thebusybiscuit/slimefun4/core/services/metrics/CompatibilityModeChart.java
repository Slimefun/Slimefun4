
package io.github.thebusybiscuit.slimefun4.core.services.metrics;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import org.bstats.bukkit.Metrics.SimplePie;

class CompatibilityModeChart extends SimplePie {

    CompatibilityModeChart() {
        super("compatibility_mode", () -> {
            boolean enabled = SlimefunPlugin.getRegistry().isBackwardsCompatible();
            return enabled ? "enabled" : "disabled";
        });
    }

}