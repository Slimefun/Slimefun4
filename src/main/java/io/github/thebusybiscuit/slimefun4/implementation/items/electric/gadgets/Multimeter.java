package io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets;

import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNet;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link Multimeter} is used to measure charge and capacity of any {@link EnergyNetComponent}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see EnergyNet
 * @see EnergyNetComponent
 *
 */
public class Multimeter extends SimpleSlimefunItem<ItemUseHandler> {

    @ParametersAreNonnullByDefault
    public Multimeter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Optional<SlimefunItem> block = e.getSlimefunBlock();

            if (e.getClickedBlock().isPresent() && block.isPresent()) {
                SlimefunItem item = block.get();

                if (item instanceof EnergyNetComponent) {
                    EnergyNetComponent component = (EnergyNetComponent) item;

                    if (component.isChargeable()) {
                        e.cancel();

                        Location l = e.getClickedBlock().get().getLocation();
                        String stored = NumberUtils.getCompactDouble(component.getCharge(l)) + " J";
                        String capacity = NumberUtils.getCompactDouble(component.getCapacity()) + " J";

                        Player p = e.getPlayer();
                        p.sendMessage("");
                        SlimefunPlugin.getLocalization().sendMessage(p, "messages.multimeter", false, str -> str.replace("%stored%", stored).replace("%capacity%", capacity));
                        p.sendMessage("");
                    }
                }
            }
        };
    }
}
