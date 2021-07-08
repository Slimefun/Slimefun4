package io.github.thebusybiscuit.slimefun4.api.items;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.SlimefunItemSpawnEvent;
import io.github.thebusybiscuit.slimefun4.core.networks.cargo.CargoNet;
import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AncientPedestal;
import io.github.thebusybiscuit.slimefun4.implementation.items.seasonal.ChristmasPresent;
import io.github.thebusybiscuit.slimefun4.implementation.items.seasonal.EasterEgg;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.GoldPan;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.PickaxeOfContainment;

/**
 * This enum holds the different reasons as to why we may need to spawn an item.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SlimefunItemSpawnEvent
 *
 */
public enum ItemSpawnReason {

    /**
     * The item is spawned on top of an {@link AncientPedestal}.
     */
    ANCIENT_PEDESTAL_PLACE_ITEM,

    /**
     * This {@link ItemStack} is dropped as a result of the {@link PickaxeOfContainment}
     * breaking a monster spawner.
     */
    BROKEN_SPAWNER_DROP,

    /**
     * The {@link ItemStack} is dropped as the result of a {@link CargoNet}
     * overflowing.
     */
    CARGO_OVERFLOW,

    /**
     * THe {@link ItemStack} is dropped as the result of an opened {@link ChristmasPresent}.
     */
    CHRISTMAS_PRESENT_OPENED,

    /**
     * THe {@link ItemStack} is dropped as the result of an opened {@link EasterEgg}.
     */
    EASTER_EGG_OPENED,

    /**
     * The {@link ItemStack} is dropped as the result of a {@link GoldPan} being used
     * on a {@link Block} which yields drops.
     */
    GOLD_PAN_USE,

    /**
     * Other reasons we did not account for.
     */
    MISC;

}
