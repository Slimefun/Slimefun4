package io.github.thebusybiscuit.slimefun4.core.machines;

public interface MachineOperation {

    boolean isFinished();

    void addProgress(int num);

}
