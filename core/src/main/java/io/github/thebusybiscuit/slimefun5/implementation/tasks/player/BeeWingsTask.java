package io.github.thebusybiscuit.slimefun5.implementation.tasks.player;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.EntityCompat;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedPotionEffectType;
import io.github.thebusybiscuit.slimefun5.implementation.items.magical.BeeWings;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.BeeWingsListener;

/**
 * This task is responsible for the repeating checks for our {@link BeeWings}.
 * 
 * @author beSnow
 * @author TheBusyBiscuit
 * 
 * @see BeeWings
 * @see BeeWingsListener
 *
 */
public class BeeWingsTask extends AbstractPlayerTask {

    private static final int MIN_ALTITUDE = 4;

    private Location lastLocation;

    public BeeWingsTask(@Nonnull Player p) {
        super(p);
        lastLocation = p.getLocation();
    }

    @Override
    protected void executeTask() {
        if (p.getLocation().getY() < lastLocation.getY()) {
            Location loc = p.getLocation();
            // HeightMap overload is 1.15+; the single-arg getHighestBlockYAt(Location) exists on 1.8 and
            // resolves to the world surface, so it is equivalent here.
            int distanceToHighestBlock = (loc.getBlockY() - loc.getWorld().getHighestBlockYAt(loc));

            /*
             * getDistanceToGround will only fire when distanceToHighestBlock is negative
             * (which happens when a player flies beneath an existing structure)
             */
            if (distanceToHighestBlock < 0) {
                int distanceToGround = getDistanceToGround(loc.getBlock(), 6);

                if (distanceToGround < 1) {
                    return;
                }

                slowDown();
            } else if (distanceToHighestBlock <= MIN_ALTITUDE) {
                slowDown();
            }
        }

        lastLocation = p.getLocation();
    }

    private void slowDown() {
        Slimefun.getLocalization().sendMessage(p, "messages.bee-suit-slow-fall");

        p.setFallDistance(0);

        // SLOW_FALLING is 1.13+ (null on older servers, where the slow-fall effect simply cannot apply).
        if (VersionedPotionEffectType.SLOW_FALLING != null) {
            p.addPotionEffect(new PotionEffect(VersionedPotionEffectType.SLOW_FALLING, 60, 0));
        }
    }

    /**
     * Calculates the distance of the given {@link Block} from the ground.
     *
     * @param b
     *            The {@link Block} to calculate from.
     * @param limit
     *            The limit of {@link Block blocks} to check under the given {@link Block b}.
     *
     */
    private int getDistanceToGround(@Nonnull Block b, int limit) {
        for (int i = 1; i <= limit; i++) {
            Block relative = b.getRelative(0, -i, 0);

            if (relative.getType().isSolid()) {
                return i;
            }
        }

        return 0;
    }

    @Override
    protected boolean isValid() {
        // The task is only valid as long as the Player is alive and gliding
        if (!p.isOnline() || !p.isValid() || p.isDead() || !EntityCompat.isGliding(p)
                || (VersionedPotionEffectType.SLOW_FALLING != null && p.hasPotionEffect(VersionedPotionEffectType.SLOW_FALLING))) {
            cancel();
            return false;
        }

        return true;
    }

}

