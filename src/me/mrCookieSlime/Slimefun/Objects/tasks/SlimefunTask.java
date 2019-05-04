package me.mrCookieSlime.Slimefun.Objects.tasks;

import org.bukkit.Bukkit;

import java.util.UUID;

public abstract class SlimefunTask implements Runnable {
    private UUID uuid;
    private int id;

    @Override
    public void run() {
        if(cancelTask())
            return;
        executeTask();
    }

    /**
     *
     * @return True if task was cancelled.
     */
    private boolean cancelTask(){
        if(Bukkit.getPlayer(uuid) == null || Bukkit.getPlayer(uuid).isDead() || !Bukkit.getPlayer(uuid).isSneaking()) {
            Bukkit.getScheduler().cancelTask(id);
            return true;
        }
        return false;
    }

    abstract void executeTask();
}
