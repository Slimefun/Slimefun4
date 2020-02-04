package io.github.thebusybiscuit.slimefun4.api;

public enum SlimefunBranch {

    DEVELOPMENT("master"),
    STABLE("stable"),
    UNOFFICIAL("Unknown"),
    UNKNOWN("Unknown");

    private final String name;

    private SlimefunBranch(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}