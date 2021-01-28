package io.github.thebusybiscuit.slimefun4.core.attributes;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefun4.core.machines.MachineOperation;
import io.github.thebusybiscuit.slimefun4.core.machines.MachineProcessor;

public interface Processor<T extends MachineOperation> extends ItemAttribute {

    @Nonnull
    MachineProcessor<T> getMachineProcessor();

}
