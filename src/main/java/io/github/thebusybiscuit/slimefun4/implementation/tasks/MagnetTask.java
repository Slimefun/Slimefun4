package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class MagnetTask extends SlimefunTask {

    public MagnetTask(Player p) {
        super(p);
    }

    @Override
    public void executeTask() {
        for (Entity item : p.getNearbyEntities(6D, 6D, 6D)) {
            if (item instanceof Item && !item.hasMetadata("no_pickup") && ((Item) item).getPickupDelay() <= 0) {
                item.teleport(p.getEyeLocation());
                p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 2F);
            }
        }
    }

    @Override
    protected boolean isValid() {
        return super.isValid() && p.getGameMode() != GameMode.SPECTATOR;
    }

}
