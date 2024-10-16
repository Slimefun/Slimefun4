package io.github.thebusybiscuit.slimefun4.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * This utility class provides methods to setup the wiki pages for Slimefun addons.
 *
 * @author ybw0014
 */
public class WikiUtils {

    private static final Pattern SLIMEFUN_WIKI_PATTERN = buildPattern(Slimefun.instance().getWikiUrlTemplate());

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
        Preconditions.checkArgument(plugin != null, "The plugin cannot be null");
        Preconditions.checkArgument(formatter != null, "The formatter cannot be null");
        Preconditions.checkArgument(plugin instanceof SlimefunAddon, "The plugin must be a SlimefunAddon");

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

    /**
     * Checks if the given URL is a Slimefun official wiki URL.
     *
     * @param url
     *          The URL to check
     *
     * @return
     *          Whether the URL is a Slimefun official wiki URL
     */
    public static boolean isSlimefunOfficialWiki(@Nonnull String url) {
        Preconditions.checkArgument(url != null, "The URL cannot be null");

        return SLIMEFUN_WIKI_PATTERN.matcher(url).matches();
    }

    @Nonnull
    private static Pattern buildPattern(@Nonnull String template) {
        Preconditions.checkArgument(template != null, "The template cannot be null");

        String regexTemplate = template.replace(".", "\\.")
            .replace("/", "\\/")
            .replace("%item%", ".+");

        String regex = "^" + regexTemplate + "$";

        return Pattern.compile(regex);
    }
}
