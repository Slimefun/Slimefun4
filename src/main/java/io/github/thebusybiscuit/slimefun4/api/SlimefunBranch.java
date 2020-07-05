package io.github.thebusybiscuit.slimefun4.api;

import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;

/**
 * This enum represents the branch this Slimefun build is on.
 * development or stable, unofficial or even unknown.
 *
 * @author TheBusyBiscuit
 */
public enum SlimefunBranch {

    /**
     * This build stems from the official "development" branch, it is prefixed with {@code DEV - X}
     */
    DEVELOPMENT("development build", true),

    /**
     * This build stems from the official "stable" branch, it is prefixed with {@code RC - X}
     */
    STABLE("\"stable\" build", true),

    /**
     * This build stems from an unofficial branch, it contains the string {@code UNOFFICIAL}
     */
    UNOFFICIAL("Unofficial build", false),

    /**
     * This build comes from any other branch. The version does not look like anything we recognize.
     * It is definitely not an official build.
     */
    UNKNOWN("Unofficial build", false),

    /**
     * 这是汉化版自动打包编译后出现的版本
     */
    NIGHTLY("Nightly build", true);

    private final String name;
    private final boolean official;

    SlimefunBranch(String name, boolean official) {
        this.name = name;
        this.official = official;

        if (!PatternUtils.ASCII.matcher(name).matches()) {
            throw new IllegalStateException("The SlimefunBranch enum contains ILLEGAL CHARACTERS. DO NOT TRANSLATE THIS FILE.");
        }
    }

    public String getName() {
        return name;
    }

    /**
     * This method returns whether this {@link SlimefunBranch} is considered official.
     * Or whether it was unofficially modified.
     *
     * @return Whether this branch is an official one.
     */
    public boolean isOfficial() {
        return official;
    }


}
