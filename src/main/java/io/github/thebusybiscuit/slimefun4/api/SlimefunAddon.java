package io.github.thebusybiscuit.slimefun4.api;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.services.RecipeService;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

/**
 * This is a very basic interface that will be used to identify
 * the {@link Plugin} that registered a {@link SlimefunItem}.
 * 
 * It will also contain some utility methods such as {@link SlimefunAddon#getBugTrackerURL()}
 * to provide some context when bugs arise.
 * 
 * It is recommended to implement this interface if you are developing
 * an Addon.
 * 
 * @author TheBusyBiscuit
 *
 */
public interface SlimefunAddon {

    /**
     * This method returns the instance of {@link JavaPlugin} that this
     * {@link SlimefunAddon} refers to.
     * 
     * @return The instance of your {@link JavaPlugin}
     */
    @Nonnull
    JavaPlugin getJavaPlugin();

    /**
     * This method returns a link to the Bug Tracker of this {@link SlimefunAddon}
     * 
     * @return The URL for this Plugin's Bug Tracker, or null
     */
    @Nullable
    String getBugTrackerURL();

    /**
     * This method returns the name of this addon, it defaults to the name
     * of the {@link JavaPlugin} provided by {@link SlimefunAddon#getJavaPlugin()}
     * 
     * @return The Name of this {@link SlimefunAddon}
     */
    default @Nonnull String getName() {
        return getJavaPlugin().getName();
    }

    /**
     * This method returns the version of this addon, it defaults to the version
     * of the {@link JavaPlugin} provided by {@link SlimefunAddon#getJavaPlugin()}
     * 
     * @return The version of this {@link SlimefunAddon}
     */
    default @Nonnull String getPluginVersion() {
        return getJavaPlugin().getDescription().getVersion();
    }

    /**
     * This method returns the {@link Logger} of this addon, it defaults to the {@link Logger}
     * of the {@link JavaPlugin} provided by {@link SlimefunAddon#getJavaPlugin()}
     * 
     * @return The {@link Logger} of this {@link SlimefunAddon}
     */
    default @Nonnull Logger getLogger() {
        return getJavaPlugin().getLogger();
    }

    /**
     * This method checks whether the given String is the name of a dependency of this
     * {@link SlimefunAddon}.
     * It specifically checks whether the given String can be found in {@link PluginDescriptionFile#getDepend()}
     * or {@link PluginDescriptionFile#getSoftDepend()}
     * 
     * @param dependency
     *            The dependency to check for
     * 
     * @return Whether this {@link SlimefunAddon} depends on the given {@link Plugin}
     */
    default boolean hasDependency(@Nonnull String dependency) {
        Validate.notNull(dependency, "The dependency cannot be null");

        // Well... it cannot depend on itself but you get the idea.
        if (getJavaPlugin().getName().equalsIgnoreCase(dependency)) {
            return true;
        }

        PluginDescriptionFile description = getJavaPlugin().getDescription();
        return description.getDepend().contains(dependency) || description.getSoftDepend().contains(dependency);
    }

    /**
     * @return A list of all recipes in the resources folder. Addons
     * can override this to filter out certain recipes, if desired.
     */
    default Set<String> getResourceRecipeFilenames() {
        URL resourceDir = getClass().getResource("/recipes");
        if (resourceDir == null) {
            return Collections.emptySet();
        }
        URI resourceUri;
        try {
            resourceUri = resourceDir.toURI();
        } catch (URISyntaxException e) {
            return Collections.emptySet();
        }
        if (!resourceUri.getScheme().equals("jar")) {
            return Collections.emptySet();
        }
        try (FileSystem fs = FileSystems.newFileSystem(resourceUri, Collections.emptyMap())) {
            Path recipeDir = fs.getPath("/recipes");
            try (Stream<Path> files = Files.walk(recipeDir)) {
                var names = files
                    .filter(file -> file.toString().endsWith(".json"))
                    .map(file -> {
                        String filename = recipeDir.relativize(file).toString();
                        return filename.substring(0, filename.length() - 5);
                    })
                    .collect(Collectors.toSet());
                return names;
            } catch (Exception e) {
                return Collections.emptySet();
            }
        } catch (Exception e) {
            return Collections.emptySet();
        }
    }

    /**
     * Copies all recipes in the recipes folder of the jar to
     * <code>plugins/Slimefun/recipes/[subdirectory]</code>
     * This should be done on enable. If you need to add
     * any recipe overrides, those should be done before calling
     * this method.
     * @param subdirectory The subdirectory to copy files to
     */
    default void copyResourceRecipes(String subdirectory) {
        Set<String> existingRecipes = Slimefun.getRecipeService().getAllRecipeFilenames();
        Set<String> resourceNames = getResourceRecipeFilenames();
        resourceNames.removeIf(existingRecipes::contains);
        for (String name : resourceNames) {
            try (InputStream source = getClass().getResourceAsStream("/recipes/" + name + ".json")) {
                Files.copy(source, Path.of(RecipeService.SAVED_RECIPE_DIR, subdirectory, name + ".json"));
            } catch (Exception e) {
                getLogger().warning("Couldn't copy recipes in resource file '" + name + "': " + e.getLocalizedMessage());
            }
        }
    }

    /**
     * Copies all recipes in the recipes folder of the jar to
     * plugins/Slimefun/recipes. This should be done on enable.
     * If you need to add any recipe overrides, those should
     * be done before calling this method.
     */
    default void copyResourceRecipes() {
        copyResourceRecipes("");
    }

}
