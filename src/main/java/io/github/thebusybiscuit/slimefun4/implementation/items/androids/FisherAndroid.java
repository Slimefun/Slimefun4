package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.collections.RandomizedSet;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public abstract class FisherAndroid extends ProgrammableAndroid {
	
	private final RandomizedSet<ItemStack> fishingLoot = new RandomizedSet<>();

	public FisherAndroid(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
		
		for (Material fish : MaterialCollections.getAllFishItems()) {
			fishingLoot.add(new ItemStack(fish), 20);
		}

		fishingLoot.add(new ItemStack(Material.BONE), 10);
		fishingLoot.add(new ItemStack(Material.STRING), 10);
		fishingLoot.add(new ItemStack(Material.KELP), 6);
		fishingLoot.add(new ItemStack(Material.STICK), 5);
		fishingLoot.add(new ItemStack(Material.INK_SAC), 4);
		fishingLoot.add(new ItemStack(Material.ROTTEN_FLESH), 3);
		fishingLoot.add(new ItemStack(Material.LEATHER), 2);
	}
	
	@Override
	public AndroidType getAndroidType() {
		return AndroidType.FISHERMAN;
	}
	
	@Override
	protected void fish(Block b, BlockMenu menu) {
		Block water = b.getRelative(BlockFace.DOWN);

		if (water.getType() == Material.WATER) {
			water.getWorld().playSound(water.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1F, 1F);

			if (ThreadLocalRandom.current().nextInt(100) < 10 * getTier()) {
				ItemStack drop = fishingLoot.getRandom();

				if (menu.fits(drop, getOutputSlots())) {
					menu.pushItem(drop, getOutputSlots());
				}
			}

		}
	}

}
