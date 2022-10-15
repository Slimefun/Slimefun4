package io.github.thebusybiscuit.slimefun4.core.guide.options;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.bukkit.Keyed;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;

/**
 * This interface represents an option in the {@link SlimefunGuide}.
 * 
 * @author TheBusyBiscuit
 *
 * @param <T>
 *            The type of value for this option
 */
public interface SlimefunGuideOption<T> extends Keyed {

    /**
     * This returns the {@link SlimefunAddon} which added this {@link SlimefunGuideOption}.
     * 
     * @return The registering {@link SlimefunAddon}
     */
    @Nonnull
    SlimefunAddon getAddon();

    Optional<ItemStack> getDisplayItem(Player p, ItemStack guide);

    void onClick(Player p, ItemStack guide);

    Optional<T> getSelectedOption(Player p, ItemStack guide);

    void setSelectedOption(Player p, ItemStack guide, T value);

}
