package io.github.thebusybiscuit.slimefun4.core.services.profiler;

import javax.annotation.Nonnull;

import org.bukkit.Server;

/**
 * This interface is used to identify someone as a {@link PerformanceInspector}.
 * A {@link PerformanceInspector} can query the {@link SlimefunProfiler} and get the
 * results send to them as a {@link PerformanceSummary}.
 * 
 * @author TheBusyBiscuit
 *
 */
public interface PerformanceInspector {

    /**
     * This returns whether this {@link PerformanceInspector} is still valid.
     * An inspector will become invalid if they leave the {@link Server}.
     * 
     * @return Whether this inspector is still valid
     */
    boolean isValid();

    /**
     * This will send a text message to the {@link PerformanceInspector}.
     * 
     * @param msg
     *            The message to send
     */
    void sendMessage(@Nonnull String msg);

    /**
     * This determines whether the {@link PerformanceInspector} will get the full view
     * or a trimmed version which only shows the most urgent samples.
     * 
     * @return Whether to send the full {@link PerformanceSummary} or a trimmed version
     */
    boolean isVerbose();

}
