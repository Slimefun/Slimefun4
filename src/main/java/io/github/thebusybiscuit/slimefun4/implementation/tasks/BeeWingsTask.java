package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BeeWingsTask extends PlayerTask {

    private Location lastLocation;

    public BeeWingsTask(@Nonnull Player p) {
        super(p);
        lastLocation = p.getLocation();
    }

    @Override
    protected void executeTask() {
        if (p.hasPotionEffect(PotionEffectType.SLOW_FALLING)) {
            return;
        }

        if (p.getLocation().getY() < lastLocation.getY()) {
            Location loc = p.getLocation();
            int distanceToHighestBlock = (loc.getBlockY() - loc.getWorld().getHighestBlockYAt(loc, HeightMap.WORLD_SURFACE));

            // getDistanceToGround will only fire when playerDistanceToHighestBlock is negative (which happens when a
            // player
            // is flying under an existing structure)
            if (distanceToHighestBlock < 0) {
                int distanceToGround = getDistanceToGround(loc.getBlock(), 6);

                if (distanceToGround < 1) {
                    return;
                }

                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 40, 0));
            } else if (distanceToHighestBlock <= 6) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 40, 0));
            }
        }

        lastLocation = p.getLocation();
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
        if (!p.isOnline() || !p.isValid() || p.isDead() || !p.isGliding()) {
            Bukkit.getScheduler().cancelTask(id);
            return false;
        }

        return true;
    }

}
