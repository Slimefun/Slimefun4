package io.github.thebusybiscuit.slimefun4.core.services.metrics;

import org.bstats.bukkit.Metrics.SimplePie;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

class ResearchesEnabledChart extends SimplePie {

    ResearchesEnabledChart() {
        super("servers_with_researches_enabled", () -> {
            boolean enabled = SlimefunPlugin.getRegistry().isFreeCreativeResearchingEnabled();
            return enabled ? "enabled" : "disabled";
        });
    }

}
