package io.github.thebusybiscuit.slimefun4.api;

import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

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
	 * @return	The instance of your {@link JavaPlugin}
	 */
	JavaPlugin getJavaPlugin();
	
	/**
	 * This method returns a link to the Bug Tracker of this {@link SlimefunAddon}
	 * 
	 * @return	The URL for this Plugin's Bug Tracker, or null
	 */
	String getBugTrackerURL();
	
	/**
	 * This method returns the name of this addon, it defaults to the name
	 * of the {@link JavaPlugin} provided by {@link SlimefunAddon#getJavaPlugin()}
	 * 
	 * @return	The Name of this {@link SlimefunAddon}
	 */
	default String getName() {
		return getJavaPlugin().getName();
	}
	
	/**
	 * This method returns the version of this addon, it defaults to the version
	 * of the {@link JavaPlugin} provided by {@link SlimefunAddon#getJavaPlugin()}
	 * 
	 * @return	The version of this {@link SlimefunAddon}
	 */
	default String getPluginVersion() {
		return getJavaPlugin().getDescription().getVersion();
	}

	/**
	 * This method returns the {@link Logger} of this addon, it defaults to the {@link Logger}
	 * of the {@link JavaPlugin} provided by {@link SlimefunAddon#getJavaPlugin()}
	 * 
	 * @return	The {@link Logger} of this {@link SlimefunAddon}
	 */
	default Logger getLogger() {
		return getJavaPlugin().getLogger();
	}

}
