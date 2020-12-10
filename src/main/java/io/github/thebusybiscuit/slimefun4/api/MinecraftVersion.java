package io.github.thebusybiscuit.slimefun4.api;

import org.apache.commons.lang.Validate;
import org.bukkit.Server;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

/**
 * This enum holds all versions of Minecraft that we currently support.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SlimefunPlugin
 *
 */
public enum MinecraftVersion {

    /**
     * This constant represents Minecraft (Java Edition) Version 1.14
     * (The "Village &amp; Pillage" Update)
     */
    MINECRAFT_1_14("1.14.x"),

    /**
     * This constant represents Minecraft (Java Edition) Version 1.15
     * (The "Buzzy Bees" Update)
     */
    MINECRAFT_1_15("1.15.x"),

    /**
     * This constant represents Minecraft (Java Edition) Version 1.16
     * (The "Nether Update")
     */
    MINECRAFT_1_16("1.16.x"),

    /**
     * This constant represents an exceptional state in which we were unable
     * to identify the Minecraft Version we are using
     */
    UNKNOWN("Unknown", true),

    /**
     * This is a very special state that represents the environment being a Unit
     * Test and not an actual running Minecraft Server.
     */
    UNIT_TEST("Unit Test Environment", true);

    public static final MinecraftVersion[] valuesCache = values();

    private final String name;
    private final boolean virtual;
    private final String prefix;

    /**
     * This constructs a new {@link MinecraftVersion} with the given name.
     * This constructor forces the {@link MinecraftVersion} to be real.
     * It must be a real version of Minecraft.
     * 
     * @param name
     *            The display name of this {@link MinecraftVersion}
     */
    MinecraftVersion(String name) {
        this(name, false);
    }

    /**
     * This constructs a new {@link MinecraftVersion} with the given name.
     * A virtual {@link MinecraftVersion} (unknown or unit test) is not an actual
     * version of Minecraft but rather a state of the {@link Server} software.
     * 
     * @param name
     *            The display name of this {@link MinecraftVersion}
     * @param virtual
     *            Whether this {@link MinecraftVersion} is virtual
     */
    MinecraftVersion(String name, boolean virtual) {
        this.name = name;
        this.virtual = virtual;
        this.prefix = name().replace("MINECRAFT_", "v") + '_';
    }

    /**
     * This returns the name of this {@link MinecraftVersion} in a readable format.
     * 
     * @return The name of this {@link MinecraftVersion}
     */
    public String getName() {
        return name;
    }

    /**
     * This returns whether this {@link MinecraftVersion} is virtual or not.
     * A virtual {@link MinecraftVersion} does not actually exist but is rather
     * a state of the {@link Server} software used.
     * Virtual {@link MinecraftVersion MinecraftVersions} include "UNKNOWN" and
     * "UNIT TEST".
     * 
     * @return Whether this {@link MinecraftVersion} is virtual or not
     */
    public boolean isVirtual() {
        return virtual;
    }

    /**
     * This method checks whether the given version matches with this
     * {@link MinecraftVersion}.
     * 
     * @param version
     *            The version to compare
     * 
     * @return Whether the version matches with this one
     */
    public boolean matches(String version) {
        Validate.notNull(version, "The input version must not be null!");
        return version.startsWith(prefix);
    }

    /**
     * This method checks whether this {@link MinecraftVersion} is newer or equal to
     * the given {@link MinecraftVersion},
     * 
     * An unknown version will default to {@literal false}.
     * 
     * @param version
     *            The {@link MinecraftVersion} to compare
     * 
     * @return Whether this {@link MinecraftVersion} is newer or equal to the given {@link MinecraftVersion}
     */
    public boolean isAtLeast(MinecraftVersion version) {
        Validate.notNull(version, "A Minecraft version cannot be null!");

        if (this == UNKNOWN) {
            return false;
        }

        return this.ordinal() >= version.ordinal();
    }

    /**
     * This checks whether this {@link MinecraftVersion} is older than the specified {@link MinecraftVersion}.
     * 
     * An unknown version will default to {@literal true}.
     * 
     * @param version
     *            The {@link MinecraftVersion} to compare
     * 
     * @return Whether this {@link MinecraftVersion} is older than the given one
     */
    public boolean isBefore(MinecraftVersion version) {
        Validate.notNull(version, "A Minecraft version cannot be null!");

        if (this == UNKNOWN) {
            return true;
        }

        return version.ordinal() > this.ordinal();
    }

}
