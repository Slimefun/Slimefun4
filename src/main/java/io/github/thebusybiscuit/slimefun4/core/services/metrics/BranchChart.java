package io.github.thebusybiscuit.slimefun4.core.services.metrics;

import org.bstats.bukkit.Metrics.SimplePie;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

class BranchChart extends SimplePie {

    public BranchChart() {
        super("branch", SlimefunPlugin.getUpdater().getBranch()::getName);
    }

}
