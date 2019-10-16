package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.generators;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;

public abstract class BioGenerator extends AGenerator {

	public BioGenerator(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
	}
	
	@Override
	public void registerDefaultRecipes() {
		registerFuel(new MachineFuel(2, new ItemStack(Material.ROTTEN_FLESH)));
		registerFuel(new MachineFuel(2, new ItemStack(Material.SPIDER_EYE)));
		registerFuel(new MachineFuel(2, new ItemStack(Material.BONE)));
		registerFuel(new MachineFuel(3, new ItemStack(Material.APPLE)));
		registerFuel(new MachineFuel(3, new ItemStack(Material.MELON)));
		registerFuel(new MachineFuel(27, new ItemStack(Material.MELON)));
		registerFuel(new MachineFuel(3, new ItemStack(Material.PUMPKIN)));
		registerFuel(new MachineFuel(3, new ItemStack(Material.PUMPKIN_SEEDS)));
		registerFuel(new MachineFuel(3, new ItemStack(Material.MELON_SEEDS)));
		registerFuel(new MachineFuel(3, new ItemStack(Material.WHEAT)));
		registerFuel(new MachineFuel(3, new ItemStack(Material.WHEAT_SEEDS)));
		registerFuel(new MachineFuel(3, new ItemStack(Material.CARROT)));
		registerFuel(new MachineFuel(3, new ItemStack(Material.POTATO)));
		registerFuel(new MachineFuel(3, new ItemStack(Material.SUGAR_CANE)));
		registerFuel(new MachineFuel(3, new ItemStack(Material.NETHER_WART)));
		registerFuel(new MachineFuel(2, new ItemStack(Material.RED_MUSHROOM)));
		registerFuel(new MachineFuel(2, new ItemStack(Material.BROWN_MUSHROOM)));
		registerFuel(new MachineFuel(2, new ItemStack(Material.VINE)));
		registerFuel(new MachineFuel(2, new ItemStack(Material.CACTUS)));
		registerFuel(new MachineFuel(2, new ItemStack(Material.LILY_PAD)));
		registerFuel(new MachineFuel(8, new ItemStack(Material.CHORUS_FRUIT)));
		registerFuel(new MachineFuel(1, new ItemStack(Material.BAMBOO)));
		registerFuel(new MachineFuel(1, new ItemStack(Material.KELP)));
		registerFuel(new MachineFuel(2, new ItemStack(Material.DRIED_KELP)));
		registerFuel(new MachineFuel(20, new ItemStack(Material.DRIED_KELP_BLOCK)));
		registerFuel(new MachineFuel(1, new ItemStack(Material.SEAGRASS)));
		registerFuel(new MachineFuel(2, new ItemStack(Material.SEA_PICKLE)));

		// Leaves
		for(Material m: Tag.LEAVES.getValues()) {
			registerFuel(new MachineFuel(1, new ItemStack(m)));
		}

		// Saplings
		for (Material m: Tag.SAPLINGS.getValues()) {
			registerFuel(new MachineFuel(1, new ItemStack(m)));
		}
		
		// Small Flowers (formally just dandelions and poppies.
		for (Material m: Tag.SMALL_FLOWERS.getValues()) {
			registerFuel(new MachineFuel(1, new ItemStack(m)));
		}
	}

	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.GOLDEN_HOE);
	}

	@Override
	public String getInventoryTitle() {
		return "&2Bio Reactor";
	}

}
