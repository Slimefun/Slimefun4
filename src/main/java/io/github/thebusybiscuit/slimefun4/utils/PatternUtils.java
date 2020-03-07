package io.github.thebusybiscuit.slimefun4.utils;

import java.util.regex.Pattern;

/**
 * This class is created for common-use patterns used in things such as {@link String#split(String)}. <br />
 * Every time something like {@link String#split(String)} is called it will compile a {@link Pattern},
 * for code that is called often this can be a massive performance loss.
 * This class solves that, one compile but many uses!
 * 
 * @author Walshy
 * 
 */
public final class PatternUtils {

    private PatternUtils() {}

    public static final Pattern SEMICOLON = Pattern.compile(";");
    public static final Pattern HASH = Pattern.compile("#");
    public static final Pattern COMMA = Pattern.compile(",");
    public static final Pattern SLASH_SEPARATOR = Pattern.compile(" / ");
    public static final Pattern DASH = Pattern.compile("-");
}
