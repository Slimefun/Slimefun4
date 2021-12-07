package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.HologramOwner;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.cargo.CargoNet;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNet;
import io.github.thebusybiscuit.slimefun4.core.services.holograms.HologramsService;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.CargoManager;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.EnergyRegulator;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.reactors.Reactor;
import io.github.thebusybiscuit.slimefun4.implementation.items.geo.GEOMiner;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

/**
 * The {@link HologramController} is a utility that can
 * toggle the hologram above various machines.
 *
 * @author Apeiros-46B
 *
 */
public class HologramController extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public HologramController(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Nonnull
    private ItemUseHandler getItemUseHandler() {
        return e -> {
            e.cancel();
            e.getInteractEvent().setCancelled(true);

            Optional<SlimefunItem> optionalSfBlock = e.getSlimefunBlock();

            if (optionalSfBlock.isPresent() && e.getClickedBlock().isPresent()) {
                SlimefunItem sfBlock = optionalSfBlock.get();
                Block b = e.getClickedBlock().get();
                Location loc = b.getLocation();

                if (sfBlock instanceof HologramOwner) {
                    HologramOwner holoOwner = (HologramOwner) sfBlock;
                    loc = loc.add(holoOwner.getHologramOffset(b));
                    HologramsService service = Slimefun.getHologramsService();

                    Location finalLoc = loc;
                    Runnable runnable = () -> {
                        if (service.getHologram(finalLoc, false) != null) {
                            service.removeHologram(finalLoc);
                        } else {
                            service.setHologramLabel(finalLoc, "");
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
        return (e, tool, fortune, drops) -> {
            // The Hologram Controller cannot be used as a shovel
            e.setCancelled(true);
        };
    }

    @Override
    public void preRegister() {
        super.preRegister();

        addItemHandler(getItemUseHandler());
        addItemHandler(getToolUseHandler());
    }
}
