package io.github.thebusybiscuit.slimefun4.api;

/**
 * This enum represents the branch this Slimefun build is on.
 * development or stable, unofficial or even unknown.
 * 
 * @author TheBusyBiscuit
 *
 */
public enum SlimefunBranch {

    /**
     * This build stems from the official "development" branch, it is prefixed with {@code DEV - X}
     */
    DEVELOPMENT("master", true),

    /**
     * This build stems from the official "stable" branch, it is prefixed with {@code RC - X}
     */
    STABLE("stable", true),

    /**
     * This build stems from an unofficial branch, it contains the string {@code UNOFFICIAL}
     */
    UNOFFICIAL("Unknown", false),

    /**
     * This build comes from any other branch. The version does not look like anything we recognize.
     * It is definitely not an official build.
     */
    UNKNOWN("Unknown", false);

    private final String name;
    private final boolean official;

    private SlimefunBranch(String name, boolean official) {
        this.name = name;
        this.official = official;
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
