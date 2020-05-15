package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.EnhancedFurnace;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * This {@link Listener} is responsible for enforcing the "fuel efficiency" and "fortune" policies
 * of an {@link EnhancedFurnace}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see EnhancedFurnace
 *
 */
public class EnhancedFurnaceListener implements Listener {

    // This will throttle the spam a bit, enough to irritate Server Owners so they report it
    // but low enough to not cause them to rage quit
    private long lastWarning = 0;

    public EnhancedFurnaceListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFuelBurn(FurnaceBurnEvent e) {
        SlimefunItem furnace = BlockStorage.check(e.getBlock());

        if (furnace instanceof EnhancedFurnace && ((EnhancedFurnace) furnace).getFuelEfficiency() > 0) {
            int burnTime = e.getBurnTime();
            int newBurnTime = ((EnhancedFurnace) furnace).getFuelEfficiency() * burnTime;
            e.setBurnTime(newBurnTime);

            if (e.getBurnTime() < burnTime && lastWarning + TimeUnit.MINUTES.toMillis(10) < System.currentTimeMillis()) {
                lastWarning = System.currentTimeMillis();
                throw new IllegalStateException("Enhanced Furnace tried to increase burn time but actually decreased it: " + burnTime + " > " + e.getBurnTime() + " (supposed to be " + newBurnTime + ")");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemSmelt(FurnaceSmeltEvent e) {
        SlimefunItem sfItem = BlockStorage.check(e.getBlock());

        if (sfItem instanceof EnhancedFurnace) {
            Furnace furnace = (Furnace) e.getBlock().getState();
            int amount = furnace.getInventory().getSmelting().getType().toString().endsWith("_ORE") ? ((EnhancedFurnace) sfItem).getOutput() : 1;
            Optional<ItemStack> result = Optional.ofNullable(furnace.getInventory().getResult());

            if (!result.isPresent()) {
                result = SlimefunPlugin.getMinecraftRecipes().getFurnaceOutput(furnace.getInventory().getSmelting());
            }

            if (result.isPresent()) {
                ItemStack item = result.get();
                furnace.getInventory().setResult(new CustomItem(item, Math.min(item.getAmount() + amount, item.getMaxStackSize())));
            }
        }
    }

}
