package io.github.thebusybiscuit.slimefun4.core.services.profiler.inspectors;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import io.github.thebusybiscuit.slimefun4.core.services.profiler.PerformanceInspector;

/**
 * This implementation of {@link PerformanceInspector} refers to a {@link CommandSender}
 * which is preferabbly a {@link ConsoleCommandSender}.
 * But it can theoretically be used for any type of {@link CommandSender} as it uses uncolored texts.
 * 
 * @author TheBusyBiscuit
 *
 */
public class ConsolePerformanceInspector implements PerformanceInspector {

    /**
     * Our reference to the actual underlying {@link CommandSender}.
     */
    private final CommandSender console;

    /**
     * This creates a new {@link ConsolePerformanceInspector} for the given {@link CommandSender}.
     * 
     * @param console
     *            The {@link CommandSender}, preferabbly a {@link ConsoleCommandSender}
     */
    public ConsolePerformanceInspector(@Nonnull CommandSender console) {
        Validate.notNull(console, "CommandSender cannot be null");

        this.console = console;
    }

    @Override
    public boolean isValid() {
        // The console is always "online".
        return true;
    }

    @Override
    public boolean hasFullView() {
        return false;
    }

    @Override
    public void sendMessage(@Nonnull String msg) {
        console.sendMessage(msg);
    }

}
