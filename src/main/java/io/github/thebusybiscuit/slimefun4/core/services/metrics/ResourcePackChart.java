package io.github.thebusybiscuit.slimefun4.core.services.metrics;

import org.bstats.bukkit.Metrics.SimplePie;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

class ResourcePackChart extends SimplePie {

    public ResourcePackChart() {
        super("resourcepack", () -> {
            String version = SlimefunPlugin.getItemTextureService().getVersion();

            if (version != null && version.startsWith("v")) {
                return version + " (Official)";
            }
            else if (SlimefunPlugin.getItemTextureService().isActive()) {
                return "Custom / Modified";
            }
            else {
                return "None";
            }
        });
    }

}
