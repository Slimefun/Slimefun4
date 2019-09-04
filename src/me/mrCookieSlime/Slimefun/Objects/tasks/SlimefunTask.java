package me.mrCookieSlime.Slimefun.Objects.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class SlimefunTask implements Runnable {
	
    protected UUID uuid;
    protected int id;
    protected Player p;

    public SlimefunTask(Player p) {
    	this.p = p;
        this.uuid = p.getUniqueId();
    }

    public void setID(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        if(cancelTask()) return;
        executeTask();
    }

    /**
     *
     * @return True if task was cancelled.
     */
    protected boolean cancelTask(){
        if (Bukkit.getPlayer(uuid) == null || Bukkit.getPlayer(uuid).isDead() || !Bukkit.getPlayer(uuid).isSneaking()) {
            Bukkit.getScheduler().cancelTask(id);
            return true;
        }
        
        return false;
    }

    abstract void executeTask();
}
