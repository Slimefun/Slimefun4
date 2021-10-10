package io.github.thebusybiscuit.slimefun4.core.debug;

import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

/**
 * This class is responsible for debug logging.
 * Server owners can enable testing specific cases and have debug logs for those cases.
 *
 * <b>Note:</b> We don't have validates in here because we want it to be quick and it's mainly for us internal devs.
 *
 * @author WalshyDev
 */
public final class Debug {

    private static String testCase = null;

    private Debug() {}

    /**
     * Log a message if the {@link TestCase} is currently enabled.
     *
     * @param testCase
     *            The {@link TestCase} to use
     * @param msg
     *            The message to log
     */
    public static void log(@Nonnull TestCase testCase, @Nonnull String msg) {
        log(testCase.toString(), msg, new Object[0]);
    }

    /**
     * Log a variable message if the {@link TestCase} is currently enabled.
     *
     * @param testCase
     *            The {@link TestCase} to use
     * @param msg
     *            The message to log
     * @param vars
     *            The variables to replace, use "{}" in the message and have it replaced with a specified thing
     */
    public static void log(@Nonnull TestCase testCase, @Nonnull String msg, @Nonnull Object... vars) {
        log(testCase.toString(), msg, vars);
    }

    /**
     * Log a message if the test case is currently enabled.
     *
     * @param test
     *            The test case to use
     * @param msg
     *            The message to log
     */
    public static void log(@Nonnull String test, @Nonnull String msg) {
        log(test, msg, new Object[0]);
    }

    /**
     * Log a message if the test case is currently enabled.
     *
     * @param test
     *            The test case to use
     * @param msg
     *            The message to log
     * @param vars
     *            The variables to replace, use "{}" in the message and have it replaced with a specified thing
     */
    public static void log(@Nonnull String test, @Nonnull String msg, @Nonnull Object... vars) {
        if (testCase == null || !testCase.equals(test)) {
            return;
        }

        if (vars.length > 0) {
            String formatted = formatMessage(msg, vars);
            Slimefun.logger().log(Level.INFO, "[DEBUG {0}] {1}", new Object[] { test, formatted });
        } else {
            Slimefun.logger().log(Level.INFO, "[DEBUG {0}] {1}", new Object[] { test, msg });
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
     * @param msg
     *            The message to send. For variables, you can pass "{}"
     * @param vars
     *            A varargs of the variables you wish to use
     * 
     * @return The resulting String
     */
    private static @Nonnull String formatMessage(@Nonnull String msg, @Nonnull Object... vars) {
        int i = 0;
        int idx = 0;

        // Find an opening curly brace `{` and validate the next char is a closing one `}`
        while ((i = msg.indexOf('{', i)) != -1 && msg.charAt(i + 1) == '}') {
            // Substring up to the opening brace `{`, add the variable for this and add the rest of the message
            msg = msg.substring(0, i) + vars[idx] + msg.substring(i + 2);
            idx++;
        }

        return msg;
    }

    /**
     * Set the current test case for this server.
     * This will enable debug logging for this specific case which can be helpful by Slimefun or addon developers.
     *
     * @param test
     *            The test case to enable or null to disable it
     */
    public static void setTestCase(@Nullable String test) {
        testCase = test;
    }

    /**
     * Get the current test case for this server or null if disabled
     *
     * @return The current test case to enable or null if disabled
     */
    public static @Nullable String getTestCase() {
        return testCase;
    }
}
