package me.mrCookieSlime.Slimefun.Lists;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunGadget;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunMachine;
import me.mrCookieSlime.Slimefun.api.SlimefunRecipes;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RecipeType {
	
	public static final RecipeType MULTIBLOCK = new RecipeType(new CustomItem(Material.BRICK, "&b多方块结构", 0, new String[] {"", "&a&o依照配方表在世界中造出来"}));
	public static final RecipeType ARMOR_FORGE = new RecipeType(new CustomItem(Material.ANVIL, "&b盔甲锻造台", 0, new String[] {"", "&a&o在盔甲锻造台里合成"}), "ARMOR_FORGE");
	public static final RecipeType GRIND_STONE = new RecipeType(new CustomItem(Material.DISPENSER, "&b磨石", 0, new String[] {"", "&a&o用磨石磨物品"}), "GRIND_STONE");
	public static final RecipeType MOB_DROP = new RecipeType(new CustomItem(Material.IRON_SWORD, "&b生物掉落", 0, new String[] {"", "&a&o击杀指定的生物获得物品"}));
	public static final RecipeType SMELTERY = new RecipeType(new CustomItem(Material.FURNACE, "&6冶炼炉", 0, new String[] {"", "&a&o在冶炼炉中冶炼"}), "SMELTERY");
	public static final RecipeType ORE_CRUSHER = new RecipeType(new CustomItem(Material.DISPENSER, "&b矿石粉碎机", 0, new String[] {"", "&a&o在矿石粉碎机中打碎"}), "ORE_CRUSHER");
	public static final RecipeType GOLD_PAN = new RecipeType(new CustomItem(Material.BOWL, "&b淘金盘", 0, new String[] {"", "&a&o用淘金盘右键沙砾获得"}));
	public static final RecipeType COMPRESSOR = new RecipeType(new CustomItem(Material.PISTON, "&b压缩机", 0, new String[] {"", "&a&o在压缩机中压缩"}), "COMPRESSOR");
	public static final RecipeType PRESSURE_CHAMBER = new RecipeType(new CustomItem(Material.GLASS, "&b压力机", 0, new String[] {"", "&a&o在压力机中压缩"}), "PRESSURE_CHAMBER");
	public static final RecipeType OVEN = new RecipeType(new CustomItem(Material.FURNACE, "&b烤炉", 0, new String[] {"", "&a&o用烤炉熔炼它"}), "OVEN");
	public static final RecipeType MAGIC_WORKBENCH = new RecipeType(new CustomItem(Material.BOOKSHELF, "&6魔法工作台", 0, new String[] {"", "&a&o在魔法工作台中合成"}), "MAGIC_WORKBENCH");
	public static final RecipeType ORE_WASHER = new RecipeType(new CustomItem(Material.CAULDRON, "&6洗矿机", 0, new String[] {"", "&a&o在洗矿机中清洗"}), "ORE_WASHER");
	public static final RecipeType ENHANCED_CRAFTING_TABLE = new RecipeType(new CustomItem(Material.CRAFTING_TABLE, "&e进阶工作台", 0, new String[] {"", "&a&o一个普通的工作台", "&a&o无法承受这巨大的能量..."}), "ENHANCED_CRAFTING_TABLE");
	public static final RecipeType JUICER = new RecipeType(new CustomItem(Material.GLASS_BOTTLE, "&e榨汁机", 0, new String[] {"", "&a&o用于制作果汁"}), "JUICER");
	public static final RecipeType ANCIENT_ALTAR = new RecipeType(new CustomItem(Material.ENCHANTING_TABLE, "&4古代祭坛", 0, new String[] {"", "&d你需要举行古老的仪式", "&d来合成这个物品"}));
	public static final RecipeType HEATED_PRESSURE_CHAMBER = new RecipeType(new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS), "&c加热压力舱", "", "&a&o在加热压力舱中制作"), "HEATED_PRESSURE_CHAMBER");
	
	public static final RecipeType SHAPED_RECIPE = new RecipeType(new CustomItem(Material.CRAFTING_TABLE, "&e原版合成表", 0, new String[] {"", "&a&o原版的工作台合成表"}));
	public static final RecipeType SHAPELESS_RECIPE = new RecipeType(new CustomItem(Material.CRAFTING_TABLE, "&e原版合成表", 0, new String[] {"", "&a&o原版的工作台合成表"}));
	public static final RecipeType FURNACE = new RecipeType(new CustomItem(Material.FURNACE, "&e熔炉", 0, new String[] {"", "&a&o在原版的熔炉中熔炼"}));
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
		return SlimefunItem.getByID(machine);
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
