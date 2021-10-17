package io.github.thebusybiscuit.slimefun4.api.gps;

import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;

public class Whitelist {

    private final PlayerProfile profile;
    private final String id;
    private final String permittedUUID;

    @ParametersAreNonnullByDefault
    public Whitelist(PlayerProfile profile, String id, String loc, String permittedUUID) {

        Validate.notNull(profile, "Profile must never be null!");
        Validate.notNull(id, "permittedUUID must never be null!");
        Validate.notNull(loc, "permittedUUID must never be null!");
        Validate.notNull(permittedUUID, "permittedUUID must never be null!");

        this.profile = profile;
        this.id = id;
        this.permittedUUID = permittedUUID;
    }
    /**
     * This returns the owner of the {@link io.github.thebusybiscuit.slimefun4.api.gps.Waypoint}.
     *
     * @return The corresponding {@link PlayerProfile}
     */
    @Nonnull
    public PlayerProfile getOwner() {
        return profile;
    }

    /**
     * This method returns the unique identifier for this {@link io.github.thebusybiscuit.slimefun4.api.gps.Waypoint}.
     *
     * @return The {@link io.github.thebusybiscuit.slimefun4.api.gps.Waypoint} id
     */
    @Nonnull
    public String getId() {
        return id;
    }

    /**
     * This returns the name of this {@link io.github.thebusybiscuit.slimefun4.api.gps.Waypoint}.
     *
     * @return The name of this {@link io.github.thebusybiscuit.slimefun4.api.gps.Waypoint}
     */
    @Nonnull
    public String getName() {
        return permittedUUID;
    }

    /**
     * This returns the {@link Location} of this {@link io.github.thebusybiscuit.slimefun4.api.gps.Waypoint}
     *
     * @return The {@link io.github.thebusybiscuit.slimefun4.api.gps.Waypoint} {@link Location}
     */
    @Nonnull
    public ItemStack getIcon() {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        assert meta != null;
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(permittedUUID)));
        skull.setItemMeta(meta);
        return skull;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(profile.getUUID(), id, permittedUUID, permittedUUID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof io.github.thebusybiscuit.slimefun4.api.gps.Whitelist)) {
            return false;
        }

        io.github.thebusybiscuit.slimefun4.api.gps.Whitelist whitelist = (io.github.thebusybiscuit.slimefun4.api.gps.Whitelist) obj;
        return profile.getUUID().equals(whitelist.getOwner().getUUID()) && id.equals(whitelist.getId()) && permittedUUID.equals(whitelist.getName());
    }
}
