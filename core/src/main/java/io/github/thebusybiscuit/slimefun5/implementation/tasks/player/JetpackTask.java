package io.github.thebusybiscuit.slimefun5.implementation.tasks.player;

import javax.annotation.Nonnull;

import org.bukkit.Effect;
import org.bukkit.Material;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun5.implementation.items.electric.gadgets.Jetpack;

public class JetpackTask extends AbstractPlayerTask {

    private static final float COST = 0.08F;

    private final Jetpack jetpack;

    public JetpackTask(@Nonnull Player p, @Nonnull Jetpack jetpack) {
        super(p);
        this.jetpack = jetpack;
    }

    @Override
    protected void executeTask() {
        if (p.getInventory().getChestplate() == null || p.getInventory().getChestplate().getType() == Material.AIR) {
            return;
        }

        // Only thrust while airborne, so simply crouching on the ground doesn't fire the jetpack (and
        // doesn't waste charge). Jump first, then hold crouch to fly.
        if (p.isOnGround()) {
            return;
        }

        if (jetpack.removeItemCharge(p.getInventory().getChestplate(), COST)) {
            SoundEffect.JETPACK_THRUST_SOUND.playAt(p.getLocation(), SoundCategory.PLAYERS);
            p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 1, 1);
            p.setFallDistance(0F);
            Vector vector = new Vector(0, 1, 0);
            vector.multiply(jetpack.getThrust());
            vector.add(p.getEyeLocation().getDirection().multiply(0.2F));

            p.setVelocity(vector);
        } else {
            cancel();
        }
    }
}

