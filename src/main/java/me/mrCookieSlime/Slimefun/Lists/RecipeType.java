package me.mrCookieSlime.Slimefun.Lists;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.recipes.MinecraftRecipe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunGadget;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunMachine;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.SlimefunRecipes;

public class RecipeType {
	
	public static final RecipeType MULTIBLOCK = new RecipeType(new CustomItem(Material.BRICKS, "&bMultiBlock", "", "&a&oBuild it in the World"));
	public static final RecipeType ARMOR_FORGE = new RecipeType((SlimefunItemStack) SlimefunItems.ARMOR_FORGE, "", "&a&oCraft it in an Armor Forge");
	public static final RecipeType GRIND_STONE = new RecipeType((SlimefunItemStack) SlimefunItems.GRIND_STONE, "", "&a&oGrind it using the Grind Stone");
	public static final RecipeType MOB_DROP = new RecipeType(new CustomItem(Material.IRON_SWORD, "&bMob Drop", "", "&a&oKill the specified Mob to obtain this Item"));
	public static final RecipeType SMELTERY = new RecipeType((SlimefunItemStack) SlimefunItems.SMELTERY, "", "&a&oSmelt it using a Smeltery");
	public static final RecipeType ORE_CRUSHER = new RecipeType((SlimefunItemStack) SlimefunItems.ORE_CRUSHER, "", "&a&oCrush it using the Ore Crusher");
	public static final RecipeType GOLD_PAN = new RecipeType((SlimefunItemStack) SlimefunItems.GOLD_PAN, "", "&a&oUse a Gold Pan on Gravel to obtain this Item");
	public static final RecipeType COMPRESSOR = new RecipeType((SlimefunItemStack) SlimefunItems.COMPRESSOR, "", "&a&oCompress it using the Compressor");
	public static final RecipeType PRESSURE_CHAMBER = new RecipeType((SlimefunItemStack) SlimefunItems.PRESSURE_CHAMBER, "", "&a&oCompress it using the Pressure Chamber");
	public static final RecipeType MAGIC_WORKBENCH = new RecipeType((SlimefunItemStack) SlimefunItems.MAGIC_WORKBENCH, "", "&a&oCraft it in a Magic Workbench");
	public static final RecipeType ORE_WASHER = new RecipeType((SlimefunItemStack) SlimefunItems.ORE_WASHER, "", "&a&oWash it in an Ore Washer");
	public static final RecipeType ENHANCED_CRAFTING_TABLE = new RecipeType((SlimefunItemStack) SlimefunItems.ENHANCED_CRAFTING_TABLE, "", "&a&oA regular Crafting Table cannot", "&a&ohold this massive Amount of Power...");
	public static final RecipeType JUICER = new RecipeType((SlimefunItemStack) SlimefunItems.JUICER, "", "&a&oUsed for Juice Creation");
	public static final RecipeType ANCIENT_ALTAR = new RecipeType((SlimefunItemStack) SlimefunItems.ANCIENT_ALTAR, "", "&dYou will need to craft this Item", "&dby performing an Ancient Altar Ritual");
	public static final RecipeType HEATED_PRESSURE_CHAMBER = new RecipeType((SlimefunItemStack) SlimefunItems.HEATED_PRESSURE_CHAMBER, "", "&a&oCraft this Item in a", "&a&oHeated Pressure Chamber");
	
	public static final RecipeType SHAPED_RECIPE = new RecipeType(new CustomItem(Material.CRAFTING_TABLE, "&eShaped Recipe", "", "&a&oJust a standard Recipe in the Workbench..."));
	public static final RecipeType SHAPELESS_RECIPE = new RecipeType(new CustomItem(Material.CRAFTING_TABLE, "&eShapeless Recipe", "", "&a&oJust a standard Recipe in the Workbench..."));
	public static final RecipeType FURNACE = new RecipeType(new CustomItem(Material.FURNACE, "&eFurnace Recipe", "", "&a&oJust smelt it in a regular Furnace"));
	public static final RecipeType NULL = new RecipeType((ItemStack) null);
	
	private final ItemStack item;
	private final String machine;
	
	public RecipeType(ItemStack item, String machine) {
		this.item = item;
		this.machine = machine;
	}
	
	public RecipeType(SlimefunItemStack slimefunItem, String... lore) {
		this.item = new CustomItem(slimefunItem, null, lore);
		this.machine = slimefunItem.getItemID();
	}
	
	public RecipeType(ItemStack item) {
		this(item, "");
	}
	
	public RecipeType(MinecraftRecipe<?> recipe) {
		this(new ItemStack(recipe.getMachine()));
	}
	
	public RecipeType(String machine, int seconds, ItemStack[] input, ItemStack[] output) {
		this.machine = machine;
		this.item = getMachine().getItem();
		
		SlimefunRecipes.registerMachineRecipe(machine, seconds, input, output);
	}
	
	public ItemStack toItem() {
		return this.item;
	}
	
	public SlimefunItem getMachine() {
		return SlimefunItem.getByID(machine);
	}
	
	public static List<ItemStack> getRecipeInputs(SlimefunItem machine) {
		if (machine == null) return new ArrayList<>();
		List<ItemStack[]> recipes = (machine instanceof SlimefunMachine ? ((SlimefunMachine) machine).getRecipes(): ((SlimefunGadget) machine).getRecipes());
		List<ItemStack> convertible = new ArrayList<>();
		for (int i = 0; i < recipes.size(); i++) {
			if (i % 2 == 0) convertible.add(recipes.get(i)[0]);
		}
		return convertible;
	}
	
	public static List<ItemStack[]> getRecipeInputList(SlimefunItem machine) {
		if (machine == null) return new ArrayList<>();
		List<ItemStack[]> recipes = (machine instanceof SlimefunMachine ? ((SlimefunMachine) machine).getRecipes(): ((SlimefunGadget) machine).getRecipes());
		List<ItemStack[]> convertible = new ArrayList<>();
		for (int i = 0; i < recipes.size(); i++) {
			if (i % 2 == 0) convertible.add(recipes.get(i));
		}
		return convertible;
	}
	
	public static ItemStack getRecipeOutput(SlimefunItem machine, ItemStack input) {
		List<ItemStack[]> recipes = (machine instanceof SlimefunMachine ? ((SlimefunMachine) machine).getRecipes(): ((SlimefunGadget) machine).getRecipes());
		return recipes.get(((getRecipeInputs(machine).indexOf(input) * 2) + 1))[0].clone();
	}
	
	public static ItemStack getRecipeOutputList(SlimefunItem machine, ItemStack[] input) {
		List<ItemStack[]> recipes = (machine instanceof SlimefunMachine ? ((SlimefunMachine) machine).getRecipes(): ((SlimefunGadget) machine).getRecipes());
		return recipes.get(((getRecipeInputList(machine).indexOf(input) * 2) + 1))[0];
	}
}
