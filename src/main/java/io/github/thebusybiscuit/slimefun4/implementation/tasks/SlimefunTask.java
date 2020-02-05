package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class SlimefunTask implements Runnable {

    protected int id;
    protected Player p;

    public SlimefunTask(Player p) {
        this.p = p;
    }

    public void setID(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        if (!isInvalid()) executeTask();
    }

    /**
     *
     * @return True if task was cancelled.
     */
    protected boolean isInvalid() {
        if (!p.isOnline() || !p.isValid() || p.isDead() || !p.isSneaking()) {
            Bukkit.getScheduler().cancelTask(id);
            return true;
        }

        return false;
    }

    abstract void executeTask();
}