package io.github.thebusybiscuit.slimefun4.core.services.profiler;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Holds the different types of ordering for summaries.
 *
 * @author Walshy
 */
public enum SummaryOrderType {

    /**
     * Sort by highest to the lowest total timings
     */
    HIGHEST,
    /**
     * Sort by lowest to the highest total timings
     */
    LOWEST,
    /**
     * Sort by average timings (highest to lowest)
     */
    AVERAGE;

    @ParametersAreNonnullByDefault
    List<Map.Entry<String, Long>> sort(SlimefunProfiler profiler, Set<Map.Entry<String, Long>> entrySet) {
        switch(this) {
            case HIGHEST:
                return entrySet.stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            case LOWEST:
                return entrySet.stream()
                        .sorted(Comparator.comparingLong(Map.Entry::getValue))
                        .collect(Collectors.toList());
            default:
                final Map<String, Long> map = new HashMap<>();
                for (Map.Entry<String, Long> entry : entrySet) {
                    int count = profiler.getBlocksOfId(entry.getKey());
                    long avg = count > 0 ? entry.getValue() / count : entry.getValue();

                    map.put(entry.getKey(), avg);
                }
                return map.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .collect(Collectors.toList());
        }
    }
}
