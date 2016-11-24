package me.mrCookieSlime.Slimefun.Lists;

import java.util.ArrayList;
import java.util.List;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunGadget;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunMachine;
import me.mrCookieSlime.Slimefun.api.SlimefunRecipes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class RecipeType {
	
	public static final RecipeType MULTIBLOCK = new RecipeType(new CustomItem(Material.BRICK, "&bMultiBlock", 0, new String[] {"", "&a&oBuild it in the World"}));
	public static final RecipeType ARMOR_FORGE = new RecipeType(new CustomItem(Material.ANVIL, "&bArmor Forge", 0, new String[] {"", "&a&oCraft it in an Armor Forge"}), "ARMOR_FORGE");
	public static final RecipeType GRIND_STONE = new RecipeType(new CustomItem(Material.DISPENSER, "&bGrind Stone", 0, new String[] {"", "&a&oGrind it using the Grind Stone"}), "GRIND_STONE");
	public static final RecipeType MOB_DROP = new RecipeType(new CustomItem(Material.MONSTER_EGG, "&bMob Drop", 0, new String[] {"", "&a&oKill the specified Mob to obtain this Item"}));
	public static final RecipeType SMELTERY = new RecipeType(new CustomItem(Material.FURNACE, "&6Smeltery", 0, new String[] {"", "&a&oSmelt it using a Smeltery"}), "SMELTERY");
	public static final RecipeType ORE_CRUSHER = new RecipeType(new CustomItem(Material.DISPENSER, "&bOre Crusher", 0, new String[] {"", "&a&oCrush it using the Ore Crusher"}), "ORE_CRUSHER");
	public static final RecipeType GOLD_PAN = new RecipeType(new CustomItem(Material.BOWL, "&bGold Pan", 0, new String[] {"", "&a&oUse a Gold Pan on Gravel to obtain this Item"}));
	public static final RecipeType COMPRESSOR = new RecipeType(new CustomItem(Material.PISTON_BASE, "&bCompressor", 0, new String[] {"", "&a&oCompress it using the Compressor"}), "COMPRESSOR");
	public static final RecipeType PRESSURE_CHAMBER = new RecipeType(new CustomItem(Material.GLASS, "&bPressure Chamber", 0, new String[] {"", "&a&oCompress it using the Pressure Chamber"}), "PRESSURE_CHAMBER");
	public static final RecipeType OVEN = new RecipeType(new CustomItem(Material.FURNACE, "&bOven", 0, new String[] {"", "&a&oSmelt it in an Oven"}), "OVEN");
	public static final RecipeType MAGIC_WORKBENCH = new RecipeType(new CustomItem(Material.BOOKSHELF, "&6Magic Workbench", 0, new String[] {"", "&a&oCraft it in a Magic Workbench"}), "MAGIC_WORKBENCH");
	public static final RecipeType ORE_WASHER = new RecipeType(new CustomItem(Material.CAULDRON_ITEM, "&6Ore Washer", 0, new String[] {"", "&a&oWash it in an Ore Washer"}), "ORE_WASHER");
	public static final RecipeType ENHANCED_CRAFTING_TABLE = new RecipeType(new CustomItem(Material.WORKBENCH, "&eEnhanced Crafting Table", 0, new String[] {"", "&a&oA regular Crafting Table cannot", "&a&ohold this massive Amount of Power..."}), "ENHANCED_CRAFTING_TABLE");
	public static final RecipeType JUICER = new RecipeType(new CustomItem(Material.GLASS_BOTTLE, "&eJuicer", 0, new String[] {"", "&a&oUsed for Juice Creation"}), "JUICER");
	public static final RecipeType ANCIENT_ALTAR = new RecipeType(new CustomItem(Material.ENCHANTMENT_TABLE, "&4Ancient Altar", 0, new String[] {"", "&dYou will need to craft this Item", "&dby performing an Ancient Altar Ritual"}));
	public static final RecipeType HEATED_PRESSURE_CHAMBER = new RecipeType(new CustomItem(Material.STAINED_GLASS, "&cHeated Pressure Chamber", 8, new String[] {"", "&a&oCraft this Item in a", "&a&oHeated Pressure Chamber"}), "HEATED_PRESSURE_CHAMBER");
	
	public static final RecipeType SHAPED_RECIPE = new RecipeType(new CustomItem(Material.WORKBENCH, "&eShaped Recipe", 0, new String[] {"", "&a&oJust a standard Recipe in the Workbench..."}));
	public static final RecipeType SHAPELESS_RECIPE = new RecipeType(new CustomItem(Material.WORKBENCH, "&eShapeless Recipe", 0, new String[] {"", "&a&oJust a standard Recipe in the Workbench..."}));
	public static final RecipeType FURNACE = new RecipeType(new CustomItem(Material.FURNACE, "&eFurnace Recipe", 0, new String[] {"", "&a&oJust smelt it in a regular Furnace"}));
	public static final RecipeType NULL = new RecipeType(null);
	
	ItemStack item;
	String machine;
	
	public RecipeType(ItemStack item) {
		this.item = item;
		this.machine = "";
	}
	
	public RecipeType(ItemStack item, String machine) {
		this.item = item;
		this.machine = machine;
	}
	
	public RecipeType(String machine, int seconds, ItemStack[] input, ItemStack[] output) {
		this.machine = machine;
		SlimefunItem item = getMachine();
		this.item = item.getItem();
		
		SlimefunRecipes.registerMachineRecipe(machine, seconds, input, output);
	}
	
	public ItemStack toItem() {
		return this.item;
	}
	
	public SlimefunItem getMachine() {
		return SlimefunItem.getByName(machine);
	}
	
	public static List<ItemStack> getRecipeInputs(SlimefunItem machine) {
		if (machine == null) return new ArrayList<ItemStack>();
		List<ItemStack[]> recipes = (machine instanceof SlimefunMachine ? ((SlimefunMachine) machine).getRecipes(): ((SlimefunGadget) machine).getRecipes());
		List<ItemStack> convertable = new ArrayList<ItemStack>();
		for (int i = 0; i < recipes.size(); i++) {
			if (i % 2 == 0) convertable.add(recipes.get(i)[0]);
		}
		return convertable;
	}
	
	public static List<ItemStack[]> getRecipeInputList(SlimefunItem machine) {
		if (machine == null) return new ArrayList<ItemStack[]>();
		List<ItemStack[]> recipes = (machine instanceof SlimefunMachine ? ((SlimefunMachine) machine).getRecipes(): ((SlimefunGadget) machine).getRecipes());
		List<ItemStack[]> convertable = new ArrayList<ItemStack[]>();
		for (int i = 0; i < recipes.size(); i++) {
			if (i % 2 == 0) convertable.add(recipes.get(i));
		}
		return convertable;
	}
	
	public static ItemStack getRecipeOutput(SlimefunItem machine, ItemStack input) {
		List<ItemStack[]> recipes = (machine instanceof SlimefunMachine ? ((SlimefunMachine) machine).getRecipes(): ((SlimefunGadget) machine).getRecipes());
		return recipes.get(((getRecipeInputs(machine).indexOf(input) * 2) + 1))[0];
	}
	
	public static ItemStack getRecipeOutputList(SlimefunItem machine, ItemStack[] input) {
		List<ItemStack[]> recipes = (machine instanceof SlimefunMachine ? ((SlimefunMachine) machine).getRecipes(): ((SlimefunGadget) machine).getRecipes());
		return recipes.get(((getRecipeInputList(machine).indexOf(input) * 2) + 1))[0];
	}
}
