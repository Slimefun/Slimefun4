package io.github.bakedlibs.dough.versions;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Server;

/**
 * Patched override of the shaded dough {@code MinecraftVersion}.
 *
 * The original class calls {@code SemanticVersion.parse()} on the first
 * dash-delimited component of {@code Server.getBukkitVersion()}.  For
 * Minecraft 26.1.2 that component is {@code "26.1.2.build.12"}, which
 * contains a non-numeric fourth segment and therefore throws
 * {@link IllegalArgumentException} inside {@code SemanticVersion.parse()},
 * ultimately crashing the static initialiser of {@code ItemUtils}.
 *
 * This replacement normalises the string to {@code "major.minor.patch"}
 * before parsing so {@code "26.1.2.build.12"} becomes {@code "26.1.2"}.
 */
public class MinecraftVersion extends SemanticVersion {

    public MinecraftVersion(int major, int minor, int patch) {
        super(major, minor, patch);
    }

    private MinecraftVersion(@Nonnull SemanticVersion version) {
        this(version.getMajorVersion(), version.getMinorVersion(), version.getPatchVersion());
    }

    @Nonnull
    public static MinecraftVersion of(@Nonnull Server server) throws UnknownServerVersionException {
        if (server == null) {
            throw new UnknownServerVersionException("Server should not be null!", null);
        }

        String rawBukkitVersion = server.getBukkitVersion();

        try {
            // Take only the part before the first '-' (e.g. "26.1.2.build.12")
            String withoutSuffix = rawBukkitVersion.split("-")[0];

            // Split into dot-separated components and take only the leading numeric ones.
            String[] parts = withoutSuffix.split("\\.");
            int major = 1, minor = 0, patch = 0;
            if (parts.length >= 1) {
                major = Integer.parseInt(parts[0]);
            }
            // Legacy "1.x.y" format — skip leading "1" and treat parts[1] as major
            if (major == 1 && parts.length >= 2) {
                minor = Integer.parseInt(parts[1]);
                if (parts.length >= 3) {
                    // parts[2] might be "1" in "1.21.1" or numeric prefix of "2something"
                    try {
                        patch = Integer.parseInt(parts[2]);
                    } catch (NumberFormatException ignored) {
                        patch = 0;
                    }
                }
                return new MinecraftVersion(major, minor, patch);
            }

            // Modern "year.drop.hotfix[.extra…]" format — parts[0] is year/major
            if (parts.length >= 2) {
                minor = Integer.parseInt(parts[1]);
            }
            if (parts.length >= 3) {
                try {
                    patch = Integer.parseInt(parts[2]);
                } catch (NumberFormatException ignored) {
                    patch = 0;
                }
            }
            return new MinecraftVersion(major, minor, patch);
        } catch (Exception x) {
            throw new UnknownServerVersionException("Could not recognize version string: " + rawBukkitVersion, x);
        }
    }

    @Nonnull
    public static MinecraftVersion get() throws UnknownServerVersionException {
        return of(Bukkit.getServer());
    }

    public static boolean isMocked(@Nonnull Server server) {
        Class<?> clazz = server.getClass();
        while (clazz != null) {
            if (clazz.getName().endsWith("mockbukkit.ServerMock")) {
                return true;
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    public static boolean isMocked() {
        return isMocked(Bukkit.getServer());
    }

    @Override
    public String getAsString() {
        return "Minecraft " + super.getAsString();
    }
}
