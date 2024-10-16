package io.github.thebusybiscuit.slimefun4.api;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.Server;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.papermc.lib.PaperLib;

/**
 * This enum holds all versions of Minecraft that we currently support.
 *
 * @author TheBusyBiscuit
 * @author Walshy
 *
 * @see Slimefun
 *
 */
public enum MinecraftVersion {

    /**
     * This constant represents Minecraft (Java Edition) Version 1.16
     * (The "Nether Update")
     */
    MINECRAFT_1_16(16, "1.16.x"),

    /**
     * This constant represents Minecraft (Java Edition) Version 1.17
     * (The "Caves and Cliffs: Part I" Update)
     */
    MINECRAFT_1_17(17, "1.17.x"),

    /**
     * This constant represents Minecraft (Java Edition) Version 1.18
     * (The "Caves and Cliffs: Part II" Update)
     */
    MINECRAFT_1_18(18, "1.18.x"),

    /**
     * This constant represents Minecraft (Java Edition) Version 1.19
     * ("The Wild Update")
     */
    MINECRAFT_1_19(19, "1.19.x"),

    /**
     * This constant represents Minecraft (Java Edition) Version 1.20
     * ("The Trails &amp; Tales Update")
     */
    MINECRAFT_1_20(20, 0, 4, "1.20.x"),

    /**
     * This constant represents Minecraft (Java Edition) Version 1.20.5
     * ("The Armored Paws Update")
     */
    MINECRAFT_1_20_5(20, 5, "1.20.5+"),

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

    private final String name;
    private final boolean virtual;
    private final int majorVersion;
    private final int minorVersion;
    private final int maxMinorVersion;

    /**
     * This constructs a new {@link MinecraftVersion} with the given name.
     * This constructor forces the {@link MinecraftVersion} to be real.
     * It must be a real version of Minecraft.
     *
     * @param majorVersion
     *            The major version of minecraft as an {@link Integer}
     * @param name
     *            The display name of this {@link MinecraftVersion}
     */
    MinecraftVersion(int majorVersion, @Nonnull String name) {
        this.name = name;
        this.majorVersion = majorVersion;
        this.minorVersion = -1;
        this.maxMinorVersion = -1;
        this.virtual = false;
    }

    /**
     * This constructs a new {@link MinecraftVersion} with the given name.
     * This constructor forces the {@link MinecraftVersion} to be real.
     * It must be a real version of Minecraft.
     *
     * @param majorVersion
     *            The major (minor in semver, major in MC land) version of minecraft as an {@link Integer}
     * @param minor
     *           The minor (patch in semver, minor in MC land) version of minecraft as an {@link Integer}
     * @param name
     *            The display name of this {@link MinecraftVersion}
     */
    MinecraftVersion(int majorVersion, int minor, @Nonnull String name) {
        this.name = name;
        this.majorVersion = majorVersion;
        this.minorVersion = minor;
        this.maxMinorVersion = -1;
        this.virtual = false;
    }

    /**
     * This constructs a new {@link MinecraftVersion} with the given name.
     * This constructor forces the {@link MinecraftVersion} to be real.
     * It must be a real version of Minecraft.
     *
     * @param majorVersion
     *            The major (minor in semver, major in MC land) version of minecraft as an {@link Integer}
     * @param minor
     *           The minor (patch in semver, minor in MC land) version of minecraft as an {@link Integer}
     * @param maxMinorVersion
     *           The maximum minor (patch) version of minecraft this version represents
     * @param name
     *            The display name of this {@link MinecraftVersion}
     */
    MinecraftVersion(int majorVersion, int minor, int maxMinorVersion, @Nonnull String name) {
        this.name = name;
        this.majorVersion = majorVersion;
        this.minorVersion = minor;
        this.maxMinorVersion = maxMinorVersion;
        this.virtual = false;
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
    MinecraftVersion(@Nonnull String name, boolean virtual) {
        this.name = name;
        this.majorVersion = 0;
        this.minorVersion = -1;
        this.maxMinorVersion = -1;
        this.virtual = virtual;
    }

    /**
     * This returns the name of this {@link MinecraftVersion} in a readable format.
     *
     * @return The name of this {@link MinecraftVersion}
     */
    public @Nonnull String getName() {
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
     * This tests if the given minecraft version number matches with this
     * {@link MinecraftVersion}.
     * <p>
     * You can obtain the version number by doing {@link PaperLib#getMinecraftVersion()}.
     * It is equivalent to the "major" version
     * <p>
     * Example: {@literal "1.13"} returns {@literal 13}
     *
     * @param minecraftVersion
     *            The {@link Integer} version to match
     *
     * @return Whether this {@link MinecraftVersion} matches the specified version id
     */
    public boolean isMinecraftVersion(int minecraftVersion) {
        return this.isMinecraftVersion(minecraftVersion, -1);
    }

    /**
     * This tests if the given minecraft version matches with this
     * {@link MinecraftVersion}.
     * <p>
     * You can obtain the version number by doing {@link PaperLib#getMinecraftVersion()}.
     * It is equivalent to the "major" version<br />
     * You can obtain the patch version by doing {@link PaperLib#getMinecraftPatchVersion()}.
     * It is equivalent to the "minor" version
     * <p>
     * Example: {@literal "1.13"} returns {@literal 13}<br />
     * Example: {@literal "1.13.2"} returns {@literal 13_2}
     *
     * @param minecraftVersion
     *            The {@link Integer} version to match
     *
     * @return Whether this {@link MinecraftVersion} matches the specified version id
     */
    public boolean isMinecraftVersion(int minecraftVersion, int patchVersion) {
        return !isVirtual()
            && this.majorVersion == minecraftVersion
            && (this.minorVersion == -1 || this.minorVersion <= patchVersion)
                && (this.maxMinorVersion == -1 || patchVersion <= this.maxMinorVersion);
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
    public boolean isAtLeast(@Nonnull MinecraftVersion version) {
        Validate.notNull(version, "A Minecraft version cannot be null!");

        if (this == UNKNOWN) {
            return false;
        }

        /**
         * Unit-Test only code.
         * Running #isAtLeast(...) should always be meaningful.
         * If the provided version equals the lowest supported version, then
         * this will essentially always return true and result in a tautology.
         * This is most definitely an oversight from us and should be fixed, therefore
         * we will trigger an exception.
         *
         * In order to not disrupt server operations, this exception is only thrown during
         * unit tests since the oversight itself will be harmless.
         */
        if (this == UNIT_TEST && version.ordinal() == 0) {
            throw new IllegalArgumentException("Version " + version + " is the lowest supported version already!");
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
    public boolean isBefore(@Nonnull MinecraftVersion version) {
        Validate.notNull(version, "A Minecraft version cannot be null!");

        if (this == UNKNOWN) {
            return true;
        }

        return version.ordinal() > this.ordinal();
    }

    /**
     * Checks whether this {@link MinecraftVersion} is older than the specified minecraft and patch versions
     * @param minecraftVersion The minecraft version
     * @param patchVersion The patch version
     * @return True if this version is before, False if this version is virtual or otherwise.
     */
    public boolean isBefore(int minecraftVersion, int patchVersion) {
        // unit tests or whatever
        if (isVirtual()) {
            return false;
        }

        // major version mismatch
        if (this.majorVersion != minecraftVersion) {
            return this.majorVersion < minecraftVersion;
        }

        return this.minorVersion == -1 ? patchVersion > 0 : this.minorVersion < patchVersion;
    }

}
