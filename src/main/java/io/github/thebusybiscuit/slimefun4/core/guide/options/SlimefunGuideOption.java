package io.github.thebusybiscuit.slimefun4.core.guide.options;

import java.util.Optional;

import org.bukkit.Keyed;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;

public interface SlimefunGuideOption<T> extends Keyed {
    
    SlimefunAddon getAddon();
    
    Optional<ItemStack> getDisplayItem(Player p, ItemStack guide);

    void onClick(Player p, ItemStack guide);
    
    Optional<T> getSelectedOption(Player p, ItemStack guide);
    
    void setSelectedOption(Player p, ItemStack guide, T value);

}
