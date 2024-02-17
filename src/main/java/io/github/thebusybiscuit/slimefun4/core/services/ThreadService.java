package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class ThreadService {

    private final ThreadGroup group;
    private final ExecutorService cachedPool;
    private final ScheduledExecutorService scheduledPool;

    public ThreadService(JavaPlugin plugin) {
        this.group = new ThreadGroup(plugin.getName());
        this.cachedPool = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(group, r, plugin.getName() + " - ThreadService");
            }
        });

        this.scheduledPool = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(group, r, plugin.getName() + " - ScheduledThreadService");
            }
        });
    }

    /**
     * Invoke a new thread from the cached thread pool with the given name.
     * This is a much better alternative to using
     * {@link BukkitScheduler#runTaskAsynchronously(org.bukkit.plugin.Plugin, Runnable)}
     * as this will show not only the plugin but a useful name.
     * By default, Bukkit will use "Craft Scheduler Thread - <x> - <plugin>" which is nice to show the plugin but
     * it's impossible to track exactly what thread that is.
     *
     * @param plugin The {@link JavaPlugin} that is creating this thread
     * @param name The name of this thread, this will be prefixed with the plugin's name
     * @param runnable The {@link Runnable} to execute
     */
    @ParametersAreNonnullByDefault
    public void newThread(JavaPlugin plugin, String name, Runnable runnable) {
        cachedPool.submit(() -> {
            // This is a bit of a hack, but it's the only way to have the thread name be as desired
            Thread.currentThread().setName(plugin.getName() + " - " + name);
            runnable.run();
        });
    }

    /**
     * Invoke a new scheduled thread from the cached thread pool with the given name.
     * This is a much better alternative to using
     * {@link BukkitScheduler#runTaskTimerAsynchronously(org.bukkit.plugin.Plugin, Runnable, long, long)}
     * as this will show not only the plugin but a useful name.
     * By default, Bukkit will use "Craft Scheduler Thread - <x> - <plugin>" which is nice to show the plugin but
     * it's impossible to track exactly what thread that is.
     *
     * @param plugin The {@link JavaPlugin} that is creating this thread
     * @param name The name of this thread, this will be prefixed with the plugin's name
     * @param runnable The {@link Runnable} to execute
     */
    @ParametersAreNonnullByDefault
    public void newScheduledThread(
        JavaPlugin plugin,
        String name,
        Runnable runnable,
        long delay,
        long period,
        TimeUnit unit
    ) {
        this.scheduledPool.scheduleWithFixedDelay(() -> {
            // This is a bit of a hack, but it's the only way to have the thread name be as desired
            Thread.currentThread().setName(plugin.getName() + " - " + name);
            runnable.run();
        }, delay, delay, unit);
    }

    /**
     * Get the caller of a given method, this should only be used for debugging purposes and is not performant.
     *
     * @return The caller of the method that called this method.
     */
    public static String getCaller() {
        // First item will be getting the call stack
        // Second item will be this call
        // Third item will be the func we care about being called
        // And finally will be the caller
        StackTraceElement element = Thread.currentThread().getStackTrace()[3];
        return element.getClassName() + "." + element.getMethodName() + ":" + element.getLineNumber();
    }
}
