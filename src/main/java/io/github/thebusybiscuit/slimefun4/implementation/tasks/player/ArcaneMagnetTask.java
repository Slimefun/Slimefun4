package io.github.thebusybiscuit.slimefun4.implementation.tasks.player;

import javax.annotation.Nonnull;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.ArcaneMagnet;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

/**
 * This {@link AbstractPlayerTask} is run when a {@link Player} carries an {@link ArcaneMagnet}.
 * It manages the automatic pickup of nearby {@link Item items} and {@link ExperienceOrb experience}.
 *
 * @author TheBusyBiscuit
 * @author JustAHuman
 *
 * @see ArcaneMagnet
 * @see InfusedMagnetTask
 */
public class ArcaneMagnetTask extends AbstractPlayerTask {

    /**
     * The radius in which an {@link Item} is picked up.
     */
    private final double itemRadius;
    /**
     * The radius in which an {@link ExperienceOrb} is picked up.
     */
    private final double experienceRadius;

    /**
     * This creates a new {@link ArcaneMagnetTask} for the given {@link Player} with the given
     * pickup radius.
     *
     * @param p
     *            The {@link Player} who {@link Item items} should be teleported to
     * @param itemRadius
     *            The radius in which {@link Item items} should be picked up
     * @param experienceRadius
     *            The radius in which {@link ExperienceOrb experience} should be picked up
     */
    public ArcaneMagnetTask(@Nonnull Player p, double itemRadius, double experienceRadius) {
        super(p);

        this.itemRadius = itemRadius;
        this.experienceRadius = experienceRadius;
    }

    @Override
    protected void executeTask() {
        boolean playSound = false;
        double maxRadius = Math.max(itemRadius, experienceRadius);

        for (Entity entity : p.getNearbyEntities(maxRadius, maxRadius, maxRadius)) {
            if (!(entity instanceof Item) && !(entity instanceof ExperienceOrb)) {
                continue;
            }

            double distance = p.getLocation().distanceSquared(entity.getLocation());
            if (distance <= 0.3) {
                continue;
            }

            if (entity instanceof Item item && !SlimefunUtils.hasNoPickupFlag(item) && item.getPickupDelay() <= 0 && distance <= Math.pow(itemRadius, 2)) {
                item.teleport(p.getLocation());
                playSound = true;
            } else if (entity instanceof ExperienceOrb orb && distance <= Math.pow(experienceRadius, 2)) {
                orb.teleport(p.getLocation());
                playSound = true;
            }
        }

        // Only play a sound if an entity was found
        if (playSound) {
            SoundEffect.ARCANE_MAGNET_TELEPORT_SOUND.playFor(p);
        }
    }

    @Override
    protected boolean isValid() {
        return super.isValid() && p.getGameMode() != GameMode.SPECTATOR;
    }
}

