package io.github.thebusybiscuit.slimefun4.core.services.profiler;

import java.util.concurrent.ThreadFactory;

/**
 * This is our {@link ThreadFactory} for the {@link SlimefunProfiler}.
 * It holds the amount of {@link Thread Threads} we dedicate towards our {@link SlimefunProfiler}
 * and provides a naming convention for our {@link Thread Threads}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SlimefunProfiler
 *
 */
final class SlimefunThreadFactory implements ThreadFactory {

    private final int threadCount;

    SlimefunThreadFactory(int threadCount) {
        this.threadCount = threadCount;
    }

    int getThreadCount() {
        return threadCount;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return new Thread(runnable, "Slimefun Profiler");
    }

}
