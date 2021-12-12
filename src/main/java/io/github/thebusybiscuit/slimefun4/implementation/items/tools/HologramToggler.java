package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.HologramOwner;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.WeaponUseHandler;
import io.github.thebusybiscuit.slimefun4.core.services.holograms.HologramsService;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

/**
 * The {@link HologramToggler} is a utility item that can
 * toggle the hologram created by {@link HologramOwner} implementations.
 *
 * @author Apeiros-46B
 *
 */
public class HologramToggler extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public HologramToggler(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Nonnull
    private ItemUseHandler getItemUseHandler() {
        return e -> {
            // Cancel any interactions
            e.cancel();
            e.getInteractEvent().setCancelled(true);

            Optional<SlimefunItem> optionalSfBlock = e.getSlimefunBlock();

            if (optionalSfBlock.isPresent() && e.getClickedBlock().isPresent()) {
                SlimefunItem sfBlock = optionalSfBlock.get();
                Block block = e.getClickedBlock().get();

                // Check if the Slimefun block is a HologramOwner
                if (sfBlock instanceof HologramOwner) {
                    HologramOwner holoOwner = (HologramOwner) sfBlock;
                    Location holoLoc = block.getLocation().add(holoOwner.getHologramOffset(block));
                    HologramsService service = Slimefun.getHologramsService();

                    // Toggle the hologram
                    Runnable runnable = () -> {
                        if (service.getHologram(holoLoc, false) != null) {
                            service.removeHologram(holoLoc);
                        } else {
                            service.createHologram(holoLoc, null);
                        }
                    };

                    if (Bukkit.isPrimaryThread()) {
                        runnable.run();
                    } else {
                        Slimefun.runSync(runnable);
                    }
                }
            }
        };
    }

    @Nonnull
    private ToolUseHandler getToolUseHandler() {
        // The Hologram Toggler cannot be used as a shovel
        return (e, tool, fortune, drops) -> e.setCancelled(true);
    }

    @Nonnull
    private WeaponUseHandler getWeaponUseHandler() {
        // The Hologram Toggler cannot be used as a weapon
        return (e, player, item) -> e.setCancelled(true);
    }

    @Override
    public void preRegister() {
        super.preRegister();

        // Add handlers
        addItemHandler(getItemUseHandler());
        addItemHandler(getToolUseHandler());
        addItemHandler(getWeaponUseHandler());
    }
}
