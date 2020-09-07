package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

abstract class PlayerTask implements Runnable {

    protected int id;
    protected Player p;

    PlayerTask(@Nonnull Player p) {
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
     * This method checks if this {@link PlayerTask} should be continued or cancelled.
     * It will also cancel this {@link PlayerTask} if it became invalid.
     * 
     * @return Whether this {@link PlayerTask} is still valid
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
