package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.holograms.InfusedHopperHologram;

public class InfusedHopper extends SlimefunItem {

	public InfusedHopper(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
		
		SlimefunItem.registerBlockHandler(getID(), new SlimefunBlockHandler() {

			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				InfusedHopperHologram.getArmorStand(b, true);
			}

			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				InfusedHopperHologram.remove(b);
				return true;
			}
		});
	}

	@Override
	public void register(boolean slimefun) {
		addItemHandler(new BlockTicker() {

			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				if (b.getType() != Material.HOPPER) {
					// we're no longer a hopper, we were probably destroyed. skipping this tick.
					BlockStorage.clearBlockInfo(b);
					return;
				}
				
				ArmorStand hologram = InfusedHopperHologram.getArmorStand(b, true);
				boolean sound = false;
				
				for (Entity n: hologram.getNearbyEntities(3.5D, 3.5D, 3.5D)) {
					if (n instanceof Item && !n.hasMetadata("no_pickup") && n.getLocation().distance(hologram.getLocation()) > 0.4D) {
						n.setVelocity(new Vector(0, 0.1, 0));
						n.teleport(hologram);
						sound = true;
					}
				}
				
				if (sound) b.getWorld().playSound(b.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 5F, 2F);
			}

			@Override
			public boolean isSynchronized() {
				return true;
			}
		});
		
		super.register(slimefun);
	}
}
