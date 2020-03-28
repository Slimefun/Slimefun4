package io.github.thebusybiscuit.slimefun4.core.services.metrics;

import java.util.HashMap;
import java.util.Map;

import org.bstats.bukkit.Metrics.DrilldownPie;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

class SlimefunVersionChart extends DrilldownPie {

    SlimefunVersionChart() {
        super("slimefun_version", () -> {
            Map<String, Map<String, Integer>> outerMap = new HashMap<>();
            Map<String, Integer> innerMap = new HashMap<>();
            
            innerMap.put(SlimefunPlugin.getVersion(), 1);
            outerMap.put(SlimefunPlugin.getUpdater().getBranch().getName(), innerMap);
            
            return outerMap;
        });
    }

}
