package io.github.thebusybiscuit.slimefun5.core.balance;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun5.api.SlimefunAddon;

/**
 * Optional, opt-in per-addon score overrides read from a bundled {@code balance.yml} at the root of
 * the addon jar. Absent file => empty overrides (the heuristic scores every item). Authors are never
 * required to ship this. Format:
 *
 * <pre>
 * scores:
 *   MY_ITEM_ID: 85
 * trivial:
 *   - SOME_INGREDIENT_ID
 * </pre>
 */
public final class BalanceOverrides {

    private static final BalanceOverrides EMPTY = new BalanceOverrides(Collections.<String, Integer>emptyMap(), Collections.<String>emptySet());

    private final Map<String, Integer> scores;
    private final Set<String> trivialIds;

    private BalanceOverrides(Map<String, Integer> scores, Set<String> trivialIds) {
        this.scores = scores;
        this.trivialIds = trivialIds;
    }

    @Nullable
    public Integer getScore(@Nonnull String itemId) {
        return scores.get(itemId);
    }

    public boolean isTrivial(@Nonnull String itemId) {
        return trivialIds.contains(itemId);
    }

    public boolean isEmpty() {
        return scores.isEmpty() && trivialIds.isEmpty();
    }

    /** Reads {@code balance.yml} from the addon's jar. Never throws; returns EMPTY on any problem. */
    @Nonnull
    public static BalanceOverrides load(@Nonnull SlimefunAddon addon) {
        JavaPlugin plugin = addon.getJavaPlugin();

        try (InputStream in = plugin.getResource("balance.yml")) {
            if (in == null) {
                return EMPTY;
            }

            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(new InputStreamReader(in, StandardCharsets.UTF_8));

            Map<String, Integer> scores = new HashMap<>();
            ConfigurationSection section = yaml.getConfigurationSection("scores");

            if (section != null) {
                for (String key : section.getKeys(false)) {
                    scores.put(key, section.getInt(key));
                }
            }

            Set<String> trivial = new HashSet<>(yaml.getStringList("trivial"));

            if (scores.isEmpty() && trivial.isEmpty()) {
                return EMPTY;
            }

            return new BalanceOverrides(scores, trivial);
        } catch (Exception e) {
            return EMPTY;
        }
    }
}
