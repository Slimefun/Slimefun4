package io.github.thebusybiscuit.slimefun4.core.attributes;

import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNet;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This Interface, when attached to a class that inherits from {@link SlimefunItem}, marks
 * the Item as an electric Block.
 * This will make this Block interact with an {@link EnergyNet}.
 * <p>
 * You can specify the Type of Block via {@link EnergyNetComponent#getEnergyComponentType()}.
 * You can also specify a capacity for this Block via {@link EnergyNetComponent#getCapacity()}.
 *
 * @author TheBusyBiscuit
 * @see EnergyNetComponentType
 * @see EnergyNet
 */
public interface EnergyNetComponent extends ItemAttribute {

    /**
     * This method returns the Type of {@link EnergyNetComponentType} this {@link SlimefunItem} represents.
     * It describes how this Block will interact with an {@link EnergyNet}.
     *
     * @return The {@link EnergyNetComponentType} this {@link SlimefunItem} represents.
     */
    EnergyNetComponentType getEnergyComponentType();

    /**
     * This method returns the max amount of electricity this Block can hold.
     * If the capacity is zero, then this Block cannot hold any electricity.
     *
     * @return The max amount of electricity this Block can store.
     */
    int getCapacity();

    /**
     * This method is used for internal purposes to register the component.
     * You do not have to call this method yourself.
     *
     * @param id The id of the {@link SlimefunItem} this refers to
     */
    default void registerComponent(String id) {
        switch (getEnergyComponentType()) {
            case CONSUMER:
                SlimefunPlugin.getRegistry().getEnergyConsumers().add(id);
                break;
            case CAPACITOR:
                SlimefunPlugin.getRegistry().getEnergyCapacitors().add(id);
                break;
            case GENERATOR:
                SlimefunPlugin.getRegistry().getEnergyGenerators().add(id);
                break;
            default:
                break;
        }

        int capacity = getCapacity();

        if (capacity > 0) {
            SlimefunPlugin.getRegistry().getEnergyCapacities().put(id, capacity);
        }
    }

}
