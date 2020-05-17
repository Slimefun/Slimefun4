package io.github.thebusybiscuit.slimefun4.core.services.metrics;

import org.bstats.bukkit.Metrics.SimplePie;

class AutoUpdaterChart extends SimplePie {

    AutoUpdaterChart() {
        super("auto_updates", () -> "disabled");
    }

}
