package io.github.thebusybiscuit.slimefun4.implementation.items.gps;

import java.util.UUID;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.gps.GPSNetwork;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.attributes.TickingBlock;
import io.github.thebusybiscuit.slimefun4.core.attributes.TickingMethod;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public abstract class GPSTransmitter extends SimpleSlimefunItem<BlockPlaceHandler> implements EnergyNetComponent, TickingBlock {

    private final int capacity;

    @ParametersAreNonnullByDefault
    public GPSTransmitter(Category category, int tier, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        this.capacity = 4 << (2 * tier);

        registerBlockHandler(getId(), (p, b, stack, reason) -> {
            UUID owner = UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner"));
            SlimefunPlugin.getGPSNetwork().updateTransmitter(b.getLocation(), owner, false);
            return true;
        });
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public BlockPlaceHandler getItemHandler() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                BlockStorage.addBlockInfo(e.getBlock(), "owner", e.getPlayer().getUniqueId().toString());
            }
        };
    }

    /**
     * This method returns the amount of complexity this type of {@link GPSTransmitter} will
     * contribute to the {@link GPSNetwork} based on the y level it was placed on.
     * 
     * @param y
     *            The height (y-level) of this transmitter.
     * 
     * @return The amount of "complexity" this transmitter contributes to the {@link GPSNetwork}.
     */
    public abstract int getMultiplier(int y);

    /**
     * This returns the amount of energy consumed per tick.
     * 
     * @return Energy consumption rate
     */
    public abstract int getEnergyConsumption();

    @Override
    public void tick(Block b) {
        int charge = getCharge(b.getLocation());
        UUID owner = UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner"));

        if (charge >= getEnergyConsumption()) {
            SlimefunPlugin.getGPSNetwork().updateTransmitter(b.getLocation(), owner, true);
            removeCharge(b.getLocation(), getEnergyConsumption());
        } else {
            SlimefunPlugin.getGPSNetwork().updateTransmitter(b.getLocation(), owner, false);
        }
    }

    @Override
    public TickingMethod getTickingMethod() {
        return TickingMethod.SEPERATE_THREAD;
    }

    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

}
