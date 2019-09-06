package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class InfusedHopper extends SimpleSlimefunItem<BlockTicker> {

	public InfusedHopper(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
	}
	
	@Override
	public BlockTicker getItemHandler() {
		return new BlockTicker() {

			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				if (b.getType() != Material.HOPPER) {
					// we're no longer a hopper, we were probably destroyed. skipping this tick.
					BlockStorage.clearBlockInfo(b);
					return;
				}
				
				Location l = b.getLocation().add(0.5, 1.2, 0.5);
				boolean sound = false;
				
				for (Entity n: b.getWorld().getNearbyEntities(l, 3.5D, 3.5D, 3.5D, n -> n instanceof Item && n.isValid() && !n.hasMetadata("no_pickup") && n.getLocation().distanceSquared(l) > 0.1)) {
					n.setVelocity(new Vector(0, 0.1, 0));
					n.teleport(l);
					sound = true;
				}
				
				if (sound) {
					b.getWorld().playSound(b.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 5F, 2F);
				}
			}

			@Override
			public boolean isSynchronized() {
				return true;
			}
		};
	}
}
