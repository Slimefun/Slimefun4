package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

abstract class AbstractPlayerTask implements Runnable {

    protected int id;
    protected Player p;

    AbstractPlayerTask(@Nonnull Player p) {
        this.p = p;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void schedule(long delay) {
        setID(Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance(), this, delay));
    }

    public void scheduleRepeating(long delay, long interval) {
        setID(Bukkit.getScheduler().scheduleSyncRepeatingTask(SlimefunPlugin.instance(), this, delay, interval));
    }

    @Override
    public void run() {
        if (isValid()) {
            executeTask();
        }
    }

    /**
     * This method checks if this {@link AbstractPlayerTask} should be continued or cancelled.
     * It will also cancel this {@link AbstractPlayerTask} if it became invalid.
     * 
     * @return Whether this {@link AbstractPlayerTask} is still valid
     */
    protected boolean isValid() {
        if (!p.isOnline() || !p.isValid() || p.isDead() || !p.isSneaking()) {
            Bukkit.getScheduler().cancelTask(id);
            return false;
        }

        return true;
    }

    protected abstract void executeTask();
}
