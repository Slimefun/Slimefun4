package io.github.thebusybiscuit.slimefun4.core.attributes;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefun4.core.machines.MachineOperation;
import io.github.thebusybiscuit.slimefun4.core.machines.MachineProcessor;

public interface ProcessHolder<T extends MachineOperation> extends ItemAttribute {

    /**
     * This method returns our {@link MachineProcessor} instance.
     * 
     * @return Our {@link MachineProcessor}
     */
    @Nonnull
    MachineProcessor<T> getMachineProcessor();

}
