package io.github.thebusybiscuit.slimefun4.core.services.metrics;

import org.bstats.bukkit.Metrics.SimplePie;
import org.bukkit.Bukkit;

class ServerSizeChart extends SimplePie {

    ServerSizeChart() {
        super("server_size", () -> {
            int players = Bukkit.getOnlinePlayers().size();

            if (players < 10) {
                return "0-10";
            }

            if (players < 25) {
                return "10-25";
            }

            if (players < 50) {
                return "25-50";
            }

            if (players < 100) {
                return "50-100";
            }

            if (players < 200) {
                return "100-200";
            }

            if (players < 300) {
                return "200-300";
            }

            return "300+";
        });
    }

}
