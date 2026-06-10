package org.bukkit.profile;

/**
 * Compile-only stub for {@code org.bukkit.profile.PlayerProfile} (Minecraft 1.18+). Not shaded; on
 * modern servers the real interface is used at runtime. Only the texture accessors that Slimefun uses
 * are declared; signatures match the real type so calls resolve via invokeinterface.
 */
public interface PlayerProfile {

    PlayerTextures getTextures();

    void setTextures(PlayerTextures textures);
}
