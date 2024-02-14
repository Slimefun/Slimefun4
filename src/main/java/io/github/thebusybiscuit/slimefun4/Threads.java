package io.github.thebusybiscuit.slimefun4;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.plugin.java.JavaPlugin;

public class Threads {

    @ParametersAreNonnullByDefault
    public static void newThread(JavaPlugin plugin, String name, Runnable runnable) {
        // TODO: Change to thread pool
        new Thread(runnable, plugin.getName() + " - " + name).start();
    }

    public static String getCaller() {
        // First item will be getting the call stack
        // Second item will be this call
        // Third item will be the func we care about being called
        // And finally will be the caller
        StackTraceElement element = Thread.currentThread().getStackTrace()[3];
        return element.getClassName() + "." + element.getMethodName() + ":" + element.getLineNumber();
    }
}
