package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.gps;

import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.GPS.NetworkStatus;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;

public abstract class GPSTransmitter extends SimpleSlimefunItem<BlockTicker> {

	public GPSTransmitter(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
		
		SlimefunItem.registerBlockHandler(getID(), new SlimefunBlockHandler() {

			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());
			}

			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				Slimefun.getGPSNetwork().updateTransmitter(b.getLocation(), UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")), NetworkStatus.OFFLINE);
				return true;
			}
		});
	}
	
	public abstract int getMultiplier(int y);
	public abstract int getEnergyConsumption();

	@Override
	public BlockTicker getItemHandler() {
		return new BlockTicker() {
			
			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				int charge = ChargableBlock.getCharge(b);
				if (charge >= getEnergyConsumption()) {
					Slimefun.getGPSNetwork().updateTransmitter(b.getLocation(), UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")), NetworkStatus.ONLINE);
					ChargableBlock.setCharge(b.getLocation(), charge - getEnergyConsumption());
				}
				else {
					Slimefun.getGPSNetwork().updateTransmitter(b.getLocation(), UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")), NetworkStatus.OFFLINE);
				}
			}
			
			@Override
			public boolean isSynchronized() {
				return false;
			}
		};
	}

}
