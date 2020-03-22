package io.github.thebusybiscuit.slimefun4.core.attributes;

public enum MachineType {

    CAPACITOR("电容"),
    GENERATOR("发电机"),
    MACHINE("机器");

    private final String suffix;

    MachineType(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public String toString() {
        return suffix;
    }

}
