package io.github.thebusybiscuit.slimefun4.api.network;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNet;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.CargoInsulator;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.EnergyInsulator;

import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * This class holds the base functionality for toggling
 * a network insulator.
 *
 * @author iTwins
 *
 * @see Network
 * @see EnergyInsulator
 * @see CargoInsulator
 */
public abstract class SignalInsulator extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public SignalInsulator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @ParametersAreNonnullByDefault
    public SignalInsulator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, @Nullable ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
    }

    /**
     * This method checks {@link BlockStorage} to see whether
     * the {@link EnergyInsulator} at the given location is enabled.
     *
     * @param location
     *      The {@link Location} to check
     * @return
     *      Whether the {@link EnergyInsulator} at the given location is enabled
     *      or false if there is no {@link EnergyInsulator}.
     */
    public static boolean isEnabled(@Nonnull Location location) {
        Preconditions.checkArgument(location != null, "The Location can not be null.");

        return Boolean.parseBoolean(BlockStorage.getLocationInfo(location, "enabled"));
    }

    /**
     * This method writes the enabled state of this {@link EnergyInsulator} to {@link BlockStorage}
     *
     * @param location
     *      The {@link Location} of the {@link SignalInsulator}
     * @param enabled
     *      The boolean value to write
     */
    public static void setEnabled(@Nonnull Location location, boolean enabled) {
        Preconditions.checkArgument(location != null, "The Location can not be null.");

        BlockStorage.getLocationInfo(location).setValue("enabled", String.valueOf(enabled));
    }

    @Override
    public void preRegister() {
        addItemHandler(onPlace(), onRightClick());
    }

    private @Nonnull BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                setEnabled(e.getBlock().getLocation(), true);
            }
        };
    }

    private @Nonnull BlockUseHandler onRightClick() {
        return e -> {
            Optional<Block> optionalBlock = e.getClickedBlock();
            if (optionalBlock.isEmpty()) {
                return;
            }

            Location location = optionalBlock.get().getLocation();
            boolean newState = !isEnabled(location);
            setEnabled(location, newState);

            if (newState) {
                e.getPlayer().sendMessage(ChatColors.color("&7Enabled: " + "&2\u2714"));
            } else {
                e.getPlayer().sendMessage(ChatColors.color("&7Enabled: " + "&4\u2718"));
            }

            for (Network network : Slimefun.getNetworkManager().getNetworksFromLocation(location, EnergyNet.class)) {
                network.markDirty(location);
            }
        };
    }

}
