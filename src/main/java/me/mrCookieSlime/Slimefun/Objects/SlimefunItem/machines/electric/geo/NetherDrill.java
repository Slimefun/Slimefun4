package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.geo;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.GEO.OreGenResource;
import me.mrCookieSlime.Slimefun.GEO.OreGenSystem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.ADrill;

@Deprecated
public abstract class NetherDrill extends ADrill {

	public NetherDrill(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
	}

	@Override
	public OreGenResource getOreGenResource() {
		return OreGenSystem.getResource("Nether Ice");
	}

	@Override
	public ItemStack[] getOutputItems() {
		return new ItemStack[] {SlimefunItems.NETHER_ICE};
	}

	@Override
	public int getProcessingTime() {
		return 24;
	}

	@Override
	public String getInventoryTitle() {
		return "&4Nether Drill";
	}

	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.DIAMOND_PICKAXE);
	}

	@Override
	public String getMachineIdentifier() {
		return "NETHER_DRILL";
	}
	
}
