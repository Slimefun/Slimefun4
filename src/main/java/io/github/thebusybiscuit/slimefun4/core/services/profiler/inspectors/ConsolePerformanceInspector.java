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
     * Whether a summary will be verbose or trimmed of.
     */
    private final boolean verbose;

    /**
     * This creates a new {@link ConsolePerformanceInspector} for the given {@link CommandSender}.
     * 
     * @param console
     *            The {@link CommandSender}, preferabbly a {@link ConsoleCommandSender}
     * @param verbose
     *            Whether the summary will be verbose or not
     */
    public ConsolePerformanceInspector(@Nonnull CommandSender console, boolean verbose) {
        Validate.notNull(console, "CommandSender cannot be null");

        this.console = console;
        this.verbose = verbose;
    }

    @Override
    public boolean isValid() {
        // The console is always "online".
        return true;
    }

    @Override
    public boolean isVerbose() {
        return verbose;
    }

    @Override
    public void sendMessage(@Nonnull String msg) {
        console.sendMessage(msg);
    }

}
