package io.github.thebusybiscuit.slimefun4.core.services.profiler.inspectors;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.core.services.profiler.SummaryOrderType;
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
     * The order type of the timings.
     */
    private final SummaryOrderType orderType;

    /**
     * This creates a new {@link ConsolePerformanceInspector} for the given {@link CommandSender}.
     * 
     * @param console
     *            The {@link CommandSender}, preferably a {@link ConsoleCommandSender}
     * @param verbose
     *            Whether the summary will be verbose or not
     * @param orderType
     *            The {@link SummaryOrderType} of the timings
     */
    @ParametersAreNonnullByDefault
    public ConsolePerformanceInspector(CommandSender console, boolean verbose, SummaryOrderType orderType) {
        Validate.notNull(console, "CommandSender cannot be null");
        Validate.notNull(orderType, "SummaryOrderType cannot be null");

        this.console = console;
        this.verbose = verbose;
        this.orderType = orderType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        // The console is always "online".
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull SummaryOrderType getOrderType() {
        return orderType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(@Nonnull String msg) {
        console.sendMessage(msg);
    }
}
