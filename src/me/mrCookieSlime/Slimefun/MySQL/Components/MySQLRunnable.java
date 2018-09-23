package me.mrCookieSlime.Slimefun.MySQL.Components;

import org.bukkit.scheduler.BukkitRunnable;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class MySQLRunnable extends BukkitRunnable {

    public String trace;
    public String plugin;
    public String time;
    public MySQLRunnable(String Plugin, String Trace)
    {
        this.trace = Trace;
        this.plugin = Plugin;
        Date date = new Date(System.currentTimeMillis());
        Format format = new SimpleDateFormat("HH:mm:ss");
        this.time = format.format(date);
    }
    @Override
    public abstract void run();
}
