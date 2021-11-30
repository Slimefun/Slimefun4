package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.cargo.CargoNet;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNet;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.CargoManager;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.EnergyRegulator;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.reactors.Reactor;
import io.github.thebusybiscuit.slimefun4.implementation.items.geo.GEOMiner;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

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
                Location l = b.getLocation();

                if (sfBlock instanceof EnergyRegulator) {
                    EnergyRegulator regulator = (EnergyRegulator) sfBlock;
                    EnergyNet net = EnergyNet.getNetworkFromLocationOrCreate(l);

                    net.showHologram = !net.showHologram;
                    if (!net.showHologram) {
                        regulator.removeHologram(b);
                    }
                } else if (sfBlock instanceof CargoManager) {
                    CargoManager manager = (CargoManager) sfBlock;
                    CargoNet net = CargoNet.getNetworkFromLocationOrCreate(l);

                    net.showHologram = !net.showHologram;
                    if (!net.showHologram) {
                        manager.removeHologram(b);
                    }
                } else if (sfBlock instanceof GEOMiner) {
                    GEOMiner miner = (GEOMiner) sfBlock;

                    miner.showHologram = !miner.showHologram;
                    if (!miner.showHologram) {
                        miner.removeHologram(b);
                    }
                } else if (sfBlock instanceof Reactor) {
                    Reactor reactor = (Reactor) sfBlock;

                    reactor.showHologram = !reactor.showHologram;
                    if (!reactor.showHologram) {
                        reactor.removeHologram(b);
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
