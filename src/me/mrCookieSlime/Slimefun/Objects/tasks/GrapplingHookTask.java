package me.mrCookieSlime.Slimefun.Objects.tasks;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.utils.Utilities;

public class GrapplingHookTask extends SlimefunTask {
    private Arrow arrow;

    public GrapplingHookTask(Player p, Arrow arrow) {
        super(p);
        this.arrow = arrow;
    }

    @Override
    void executeTask() {
        Utilities utilities = SlimefunPlugin.getUtilities();
        if (arrow != null && arrow.getShooter() instanceof Player && utilities.jumpState.containsKey(((Player) arrow.getShooter()).getUniqueId())) {

            final Player p = (Player) arrow.getShooter();
            if (p.getGameMode() != GameMode.CREATIVE && utilities.jumpState.get(p.getUniqueId())) arrow.getWorld().dropItem(arrow.getLocation(), SlimefunItem.getItem("GRAPPLING_HOOK"));

            for (Entity n: utilities.remove.get(p.getUniqueId())) {
                n.remove();
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> {
                utilities.jumpState.remove(p.getUniqueId());
                utilities.remove.remove(p.getUniqueId());
            }, 20L);
        }
    }
}
