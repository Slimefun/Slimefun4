package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.profile.PlayerProfile;

/**
 * Java-8 universal port: the player-profile API ({@code Bukkit#createPlayerProfile},
 * {@code PlayerProfile}, {@code PlayerTextures}, {@code SkullMeta#setOwnerProfile}) arrived in 1.18 and
 * does not exist at the 1.8.8 compile floor.
 * <p>
 * These helpers resolve it reflectively. On a modern server the real profile API is used; on a legacy
 * server {@link #createProfile(UUID, String)} returns {@code null} (callers skip the texture path) and
 * {@link #setOwnerProfile(Object, PlayerProfile)} is a no-op.
 */
public final class ProfileCompat {

    private ProfileCompat() {}

    @Nullable
    public static PlayerProfile createProfile(UUID uuid, String name) {
        try {
            Object profile = Bukkit.class.getMethod("createPlayerProfile", UUID.class, String.class).invoke(null, uuid, name);
            return (PlayerProfile) profile;
        } catch (Throwable ignored) {
            return null;
        }
    }

    public static void setOwnerProfile(Object holder, PlayerProfile profile) {
        ReflectionCompat.invoke(holder, "setOwnerProfile", profile);
    }
}
