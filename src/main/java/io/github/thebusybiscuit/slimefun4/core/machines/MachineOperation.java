package io.github.thebusybiscuit.slimefun4.core.machines;

public interface MachineOperation {

    void addProgress(int num);

    int getProgress();

    int getTotalTicks();

    default int getRemainingTicks() {
        return getTotalTicks() - getProgress();
    }

    default boolean isFinished() {
        return getRemainingTicks() <= 0;
    }

}
