package io.github.thebusybiscuit.slimefun4.core.services.metrics;

import org.bstats.bukkit.Metrics.SimplePie;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

class GuideLayoutChart extends SimplePie {

    GuideLayoutChart() {
        super("guide_layout", () -> {
            boolean book = SlimefunPlugin.getCfg().getBoolean("guide.default-view-book");

            return book ? "Book" : "Chest GUI";
        });
    }

}
