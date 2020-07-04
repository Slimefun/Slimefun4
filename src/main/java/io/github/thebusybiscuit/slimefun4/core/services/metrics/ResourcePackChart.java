package io.github.thebusybiscuit.slimefun4.core.services.metrics;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import org.bstats.bukkit.Metrics.SimplePie;

class ResourcePackChart extends SimplePie {

    ResourcePackChart() {
        super("resourcepack", () -> {
            String version = SlimefunPlugin.getItemTextureService().getVersion();

            if (version != null && version.startsWith("v")) {
                return version + " (Official)";
            } else if (SlimefunPlugin.getItemTextureService().isActive()) {
                return "Custom / Modified";
            } else {
                return "None";
            }
        });
    }

}
