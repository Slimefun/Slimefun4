package io.github.thebusybiscuit.slimefun4.api.events;

import java.util.UUID;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;

/**
 * This {@link Event} is called when the {@link PlayerProfile} of a {@link Player}
 * is loaded into memory.
 * The {@link AsyncProfileLoadEvent} is called asynchronously and can be used to "inject"
 * a custom {@link PlayerProfile} if necessary.
 *
 * @author TheBusyBiscuit
 *
 * @see PlayerProfile
 *
 */
public class AsyncProfileLoadEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final UUID uniqueId;
    private PlayerProfile profile;

    public AsyncProfileLoadEvent(@Nonnull PlayerProfile profile) {
        super(true);

        Validate.notNull(profile, "The Profile cannot be null");

        this.uniqueId = profile.getUUID();
        this.profile = profile;
    }

    @Nonnull
    public UUID getPlayerUUID() {
        return uniqueId;
    }

    @Nonnull
    public PlayerProfile getProfile() {
        return profile;
    }

    /**
     * This method can be used to inject your custom {@link PlayerProfile} implementations.
     * However, the passed {@link PlayerProfile} must have the same {@link UUID} as the original one!
     * 
     * @param profile
     *            The {@link PlayerProfile}
     */
    public void setProfile(@Nonnull PlayerProfile profile) {
        Validate.notNull(profile, "The PlayerProfile cannot be null!");
        Validate.isTrue(profile.getUUID().equals(uniqueId), "Cannot inject a PlayerProfile with a different UUID");

        this.profile = profile;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }
}
