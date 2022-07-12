package io.github.thebusybiscuit.slimefun4.core.attributes;

import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.machines.MachineProcessor;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets.Multimeter;

import javax.annotation.Nonnull;

/**
 * This Interface, when attached to a class that inherits from {@link SlimefunItem}, marks
 * the Item as one that will display additional information when hit with a {@link Multimeter}
 * 
 * @author Sefiraat
 * 
 * @see Multimeter
 *
 */
public interface MultimeterInformational extends ItemAttribute {

    /**
     * This method returns the {@link String}s that will be messaged to
     * the {@link Player} when the MultiMeter is used on the block.
     *
     * @return Our {@link MachineProcessor}
     */
    @Nonnull
    String[] getAdditionalInfo();

}
