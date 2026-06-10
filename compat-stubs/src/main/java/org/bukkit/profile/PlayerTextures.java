package org.bukkit.profile;

import java.net.URL;

/**
 * Compile-only stub for {@code org.bukkit.profile.PlayerTextures} (Minecraft 1.18+). Not shaded; on
 * modern servers the real interface is used at runtime. Only the {@code setSkin(URL)} overload that
 * Slimefun uses is declared; signatures match the real type so calls resolve via invokeinterface.
 */
public interface PlayerTextures {

    void setSkin(URL skinUrl);
}
