package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

abstract class SlimefunTask implements Runnable {

    protected int id;
    protected Player p;

    public SlimefunTask(Player p) {
        this.p = p;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void schedule(long delay) {
        setID(Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, this, delay));
    }

    public void scheduleRepeating(long delay, long interval) {
        setID(Bukkit.getScheduler().scheduleSyncRepeatingTask(SlimefunPlugin.instance, this, delay, interval));
    }

    @Override
    public void run() {
        if (isValid()) {
            executeTask();
        }
    }

    /**
     * This method checks if this {@link SlimefunTask} should be continued or cancelled.
     * It will also cancel this {@link SlimefunTask} if it became invalid.
     * 
     * @return Whether this {@link SlimefunTask} is still valid
     */
    protected boolean isValid() {
        if (!p.isOnline() || !p.isValid() || p.isDead() || !p.isSneaking()) {
            Bukkit.getScheduler().cancelTask(id);
            return false;
        }

        return true;
    }

    abstract void executeTask();
}
