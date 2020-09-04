package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ParachuteTask extends PlayerTask {

    public ParachuteTask(@Nonnull Player p) {
        super(p);
    }

    @Override
    protected void executeTask() {
        Vector vector = new Vector(0, 1, 0);
        vector.multiply(-0.1);
        p.setVelocity(vector);
        p.setFallDistance(0F);

        if (!p.isSneaking()) {
            Bukkit.getScheduler().cancelTask(id);
        }
    }

}
