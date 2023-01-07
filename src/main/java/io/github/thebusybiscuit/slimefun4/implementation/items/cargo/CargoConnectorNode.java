package io.github.thebusybiscuit.slimefun4.implementation.items.cargo;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.cargo.CargoNet;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;

/**
 * The {@link CargoConnectorNode} connects a {@link CargoNode} with a {@link CargoNet}.
 * It has no further functionality.
 * 
 * @author TheBusyBiscuit
 * 
 * @see CargoNode
 * @see CargoNet
 *
 */
public class CargoConnectorNode extends SimpleSlimefunItem<BlockUseHandler> {

    @ParametersAreNonnullByDefault
    public CargoConnectorNode(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
    }

    @Override
    public @Nonnull BlockUseHandler getItemHandler() {
        return e -> {
            if (!e.getClickedBlock().isPresent()) {
                return;
            }

            Player p = e.getPlayer();
            Block b = e.getClickedBlock().get();

            if (CargoNet.getNetworkFromLocation(b.getLocation()) != null) {
                Slimefun.getLocalization().sendActionbarMessage(p, "machines.CARGO_NODES.connected", false);
            } else {
                Slimefun.getLocalization().sendActionbarMessage(p, "machines.CARGO_NODES.not-connected", false);
            }
        };
    }
}
