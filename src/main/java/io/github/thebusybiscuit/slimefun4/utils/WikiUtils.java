package io.github.thebusybiscuit.slimefun4.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * This utility class provides methods to setup the wiki pages for Slimefun addons.
 *
 * @author ybw0014
 */
public class WikiUtils {
    private WikiUtils() {}

    /**
     * This method loads the wiki pages from the wiki.json file in the plugin's resources.
     *
     * @param plugin
     *          The plugin to load the wiki pages for.
     */
    public static void setupWiki(@Nonnull Plugin plugin) {
        setupWiki(plugin, page -> page);
    }

    /**
     * This method loads the wiki pages from the wiki.json file in the plugin's resources.
     * The formatter will make changes to the wiki page name before it is added to the item.
     *
     * @param plugin
     *          The plugin to load the wiki pages for.
     * @param formatter
     *          The formatter to apply to the wiki page name.
     */
    public static void setupWiki(@Nonnull Plugin plugin, @Nonnull UnaryOperator<String> formatter) {
        Validate.notNull(plugin, "The plugin cannot be null");
        Validate.isTrue(plugin instanceof SlimefunAddon, "The plugin must be a SlimefunAddon");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(plugin.getClass().getResourceAsStream("/wiki.json"), StandardCharsets.UTF_8))) {
            JsonElement element = JsonUtils.parseString(reader.lines().collect(Collectors.joining("")));
            JsonObject json = element.getAsJsonObject();

            int count = 0;

            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                SlimefunItem item = SlimefunItem.getById(entry.getKey());

                if (item != null) {
                    String page = entry.getValue().getAsString();
                    page = formatter.apply(page);
                    item.addWikiPage(page);
                    count++;
                }
            }

            plugin.getLogger().log(Level.INFO, MessageFormat.format("Loaded {0} Wiki pages from {1}", count, plugin.getName()));
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, MessageFormat.format("Failed to load wiki.json from {0}", plugin.getName()), e);
        }
    }
}
