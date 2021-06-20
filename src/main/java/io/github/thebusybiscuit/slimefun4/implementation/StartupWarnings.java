package io.github.thebusybiscuit.slimefun4.implementation;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * This class stores some startup warnings we occasionally need to print.
 * If you setup your server the recommended way, you are never going to see
 * any of these messages.
 * 
 * @author TheBusyBiscuit
 *
 */
final class StartupWarnings {

    private static final String BORDER = "****************************************************";
    private static final String PREFIX = "* ";

    private StartupWarnings() {}

    @ParametersAreNonnullByDefault
    static void discourageCSCoreLib(Logger logger) {
        logger.log(Level.WARNING, BORDER);
        logger.log(Level.WARNING, PREFIX + "It looks like you are still using CS-CoreLib.");
        logger.log(Level.WARNING, PREFIX);
        logger.log(Level.WARNING, PREFIX + "Slimefun no longer requires CS-CoreLib to be");
        logger.log(Level.WARNING, PREFIX + "installed as of January 30th 2021. It is safe");
        logger.log(Level.WARNING, PREFIX + "to remove and we recommend you to uninstall");
        logger.log(Level.WARNING, PREFIX + "CS-CoreLib from your server immediately.");
        logger.log(Level.WARNING, BORDER);
    }

    @ParametersAreNonnullByDefault
    static void invalidMinecraftVersion(Logger logger, int majorVersion, String slimefunVersion) {
        logger.log(Level.SEVERE, BORDER);
        logger.log(Level.SEVERE, PREFIX + "Slimefun was not installed correctly!");
        logger.log(Level.SEVERE, PREFIX + "You are using the wrong version of Minecraft!");
        logger.log(Level.SEVERE, PREFIX);
        logger.log(Level.SEVERE, PREFIX + "You are using Minecraft 1.{0}.x", majorVersion);
        logger.log(Level.SEVERE, PREFIX + "but Slimefun {0} requires you to be using", slimefunVersion);
        logger.log(Level.SEVERE, PREFIX + "Minecraft {0}", String.join(" / ", SlimefunPlugin.getSupportedVersions()));
        logger.log(Level.SEVERE, BORDER);
    }

    @ParametersAreNonnullByDefault
    static void invalidServerSoftware(Logger logger) {
        logger.log(Level.SEVERE, BORDER);
        logger.log(Level.SEVERE, PREFIX + "Slimefun was not installed correctly!");
        logger.log(Level.SEVERE, PREFIX + "CraftBukkit is no longer supported!");
        logger.log(Level.SEVERE, PREFIX);
        logger.log(Level.SEVERE, PREFIX + "Slimefun requires you to use Spigot, Paper or");
        logger.log(Level.SEVERE, PREFIX + "any supported fork of Spigot or Paper.");
        logger.log(Level.SEVERE, PREFIX + "(We recommend Paper)");
        logger.log(Level.SEVERE, BORDER);
    }

}
