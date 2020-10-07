package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets.Jetpack;

public class JetpackTask extends PlayerTask {

    private static final float COST = 0.08F;

    private final Jetpack jetpack;

    public JetpackTask(@Nonnull Player p, @Nonnull Jetpack jetpack) {
        super(p);
        this.jetpack = jetpack;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }

    @Override
    protected void executeTask() {
        if (p.getInventory().getChestplate() == null || p.getInventory().getChestplate().getType() == Material.AIR) {
            return;
        }

        if (jetpack.removeItemCharge(p.getInventory().getChestplate(), COST)) {
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, (float) 0.25, 1);
            p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 1, 1);
            p.setFallDistance(0F);
            Vector vector = new Vector(0, 1, 0);
            vector.multiply(jetpack.getThrust());
            vector.add(p.getEyeLocation().getDirection().multiply(0.2F));

            p.setVelocity(vector);
        } else {
            Bukkit.getScheduler().cancelTask(id);
        }
    }
}
