package io.github.thebusybiscuit.slimefun4.core.debug;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// We don't have validates in here because we want it to be quick and it's mainly for us internal devs.
public final class Debug {

    private static String testMode = null;

    private Debug() {}

    public static void log(@Nonnull String mode, @Nonnull String msg) {
        log(mode, msg, new Object[0]);
    }

    public static void log(@Nonnull String mode, @Nonnull String msg, @Nonnull Object... vars) {
        if (testMode != null && !testMode.equals(mode)) return;

        if (vars.length > 0) {
            String formatted = formatMessage(msg, vars);
            Slimefun.logger().info(formatted);
        } else {
            Slimefun.logger().info(msg);
        }
    }

    /**
     * Format the message. Replace "{}" with the supplied variable. This is quick and works great.
     *
     * <code>
     * Benchmark                    Mode  Cnt        Score       Error  Units
     * MyBenchmark.loopAllChars    thrpt    5  2336518.563 ± 24129.488  ops/s
     * MyBenchmark.whileFindChars  thrpt    5  3319022.018 ± 45663.898  ops/s
     * </code>
     *
     * @param msg  The message to send. For variables, you can pass "{}".
     * @param vars A varargs of the variables you wish to use.
     * @return The resulting String.
     */
    @Nonnull
    private static String formatMessage(@Nonnull String msg, @Nonnull Object... vars) {
        int i = 0;
        int idx = 0;
        while ((i = msg.indexOf('{', i)) != -1 && msg.charAt(i + 1) == '}') {
            msg = msg.substring(0, i) + vars[idx++] + msg.substring(i + 2);
        }

        return msg;
    }

    public static void setTestMode(@Nullable String test) {
        testMode = test;
    }
}
