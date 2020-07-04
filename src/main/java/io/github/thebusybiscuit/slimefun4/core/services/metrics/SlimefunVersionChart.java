package io.github.thebusybiscuit.slimefun4.core.services.metrics;

import io.github.thebusybiscuit.slimefun4.api.SlimefunBranch;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import org.bstats.bukkit.Metrics.DrilldownPie;

import java.util.HashMap;
import java.util.Map;

class SlimefunVersionChart extends DrilldownPie {

    SlimefunVersionChart() {
        super("slimefun_version", () -> {
            Map<String, Map<String, Integer>> outerMap = new HashMap<>();
            Map<String, Integer> innerMap = new HashMap<>();

            innerMap.put(SlimefunPlugin.getVersion(), 1);
            outerMap.put(SlimefunBranch.UNOFFICIAL.getName(), innerMap);

            return outerMap;
        });
    }

}
