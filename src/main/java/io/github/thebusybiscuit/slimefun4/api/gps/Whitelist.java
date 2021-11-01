package io.github.thebusybiscuit.slimefun4.api.gps;

import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;

/**
 * A {@link Whitelist} represents a {@link Player} that was created by a {@link Player}.
 *
 * @author Toast732
*/
public class Whitelist {

    private final PlayerProfile profile;
    private final UUID id;
    private final String user;

    /**
     * This constructs a new {@link Whitelist} object.
     *
     *  @param profile
     *      The owning {@link PlayerProfile}
     *  @param id
     *      The unique id for this {@link Player}
     *  @param user
     *      The username of this {@link Player}
     */
    @ParametersAreNonnullByDefault
    public Whitelist(PlayerProfile profile, String user, UUID id) {

        Validate.notNull(profile, "Profile must never be null!");
        Validate.notNull(id, "id must never be null!");
        Validate.notNull(user, "user must never be null!");

        this.profile = profile;
        this.id = id;
        this.user = user;
    }

    /**
     * This returns the owner of the teleporter plate.
     *
     * @return The name of the owner of the teleporter plate
     */
    @Nonnull
    public PlayerProfile getOwner() {
        return profile;
    }

    /**
     * This returns the uuid of the whitelisted player.
     *
     * @return The uuid of the player
     */
    @Nonnull
    public UUID getId() {
        return id;
    }

    /**
     * This returns the name of the whitelisted player.
     *
     * @return The name of the player
     */
    @Nonnull
    public String getName() {
        return user;
    }

    /**
     * This returns the head of the player
     *
     * @return The head of the player
     */
    @Nonnull
    public ItemStack getIcon() {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        assert meta != null;
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(id));
        skull.setItemMeta(meta);
        return skull;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(profile.getUUID(), id, user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Whitelist)) {
            return false;
        }

        Whitelist whitelist = (Whitelist) obj;
        return profile.getUUID().equals(whitelist.getOwner().getUUID()) && id.equals(whitelist.getId()) && user.equals(whitelist.getName());
    }
}
