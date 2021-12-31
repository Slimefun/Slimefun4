package io.github.thebusybiscuit.slimefun4.api.setup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

/**
 * This class used to setup the wiki pages.
 *
 * @author ybw0014
 *
 * @see SlimefunItem
 */
public final class WikiSetup {

    private WikiSetup() {}

    /**
     * This method will load wiki pages from "/wiki.json" file.
     *
     * @param addon The {@link SlimefunAddon} instance that is setting up wiki pages
     */
    public static void fromJson(JavaPlugin addon) {
        if (!(addon instanceof SlimefunAddon)) {
            throw new IllegalArgumentException("This is not a valid slimefun addon");
        }

        addon.getLogger().log(Level.INFO, "Loading Wiki pages...");
        JsonParser parser = new JsonParser();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(addon.getClass().getResourceAsStream("/wiki.json"), StandardCharsets.UTF_8))) {
            JsonElement element = parser.parse(reader.lines().collect(Collectors.joining("")));
            JsonObject json = element.getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                SlimefunItem item = SlimefunItem.getById(entry.getKey());

                if (item != null) {
                    item.addWikipage(entry.getValue().getAsString());
                }
            }
        } catch (IOException e) {
            addon.getLogger().log(Level.SEVERE, "Failed to load wiki.json file", e);
        }

    }
}
