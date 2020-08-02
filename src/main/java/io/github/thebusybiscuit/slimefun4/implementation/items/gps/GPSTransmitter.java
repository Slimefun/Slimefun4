package io.github.thebusybiscuit.slimefun4.implementation.items.gps;

import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;

public abstract class GPSTransmitter extends SimpleSlimefunItem<BlockTicker> implements EnergyNetComponent {

    public GPSTransmitter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemHandler(onPlace());
        registerBlockHandler(getID(), (p, b, stack, reason) -> {
            UUID owner = UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner"));
            SlimefunPlugin.getGPSNetwork().updateTransmitter(b.getLocation(), owner, false);
            return true;
        });
    }

    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                BlockStorage.addBlockInfo(e.getBlock(), "owner", e.getPlayer().getUniqueId().toString());
            }
        };
    }

    public abstract int getMultiplier(int y);

    public abstract int getEnergyConsumption();

    @Override
    public BlockTicker getItemHandler() {
        return new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                int charge = ChargableBlock.getCharge(b);
                UUID owner = UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner"));

                if (charge >= getEnergyConsumption()) {
                    SlimefunPlugin.getGPSNetwork().updateTransmitter(b.getLocation(), owner, true);
                    ChargableBlock.setCharge(b.getLocation(), charge - getEnergyConsumption());
                }
                else {
                    SlimefunPlugin.getGPSNetwork().updateTransmitter(b.getLocation(), owner, false);
                }
            }

            @Override
            public boolean isSynchronized() {
                return false;
            }
        };
    }

    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

}
