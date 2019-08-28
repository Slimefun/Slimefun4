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
	
	public static final RecipeType MULTIBLOCK = new RecipeType(new CustomItem(Material.BRICK, "&b多方块结构", "", "&a&o按照合成表的格式用方块建造"));
	public static final RecipeType ARMOR_FORGE = new RecipeType(new CustomItem(Material.ANVIL, "&b盔甲锻造台", "", "&a&o在盔甲锻造台中制作"), "ARMOR_FORGE");
	public static final RecipeType GRIND_STONE = new RecipeType(new CustomItem(Material.DISPENSER, "&b磨石", "", "&a&o使用磨石制作"), "GRIND_STONE");
	public static final RecipeType MOB_DROP = new RecipeType(new CustomItem(Material.IRON_SWORD, "&b击杀生物掉落", "", "&a&o杀死指定的生物掉落"));
	public static final RecipeType SMELTERY = new RecipeType(new CustomItem(Material.FURNACE, "&6冶炼机", "", "&a&o使用冶炼机冶炼"), "SMELTERY");
	public static final RecipeType ORE_CRUSHER = new RecipeType(new CustomItem(Material.DISPENSER, "&b碎矿机", "", "&a&o使用碎矿机粉碎"), "ORE_CRUSHER");
	public static final RecipeType GOLD_PAN = new RecipeType(new CustomItem(Material.BOWL, "&b淘金盘", "", "&a&o使用淘金盘右键沙砾淘金"));
    public static final RecipeType COMPRESSOR = new RecipeType(new CustomItem(Material.PISTON, "&b压缩机",  "", "&a&o在压缩机里压缩它" ), "COMPRESSOR");
    public static final RecipeType PRESSURE_CHAMBER = new RecipeType(new CustomItem(Material.GLASS, "&b压力舱",  "", "&a&o在压力舱里压缩它" ), "PRESSURE_CHAMBER");
    public static final RecipeType OVEN = new RecipeType(new CustomItem(Material.FURNACE, "&b烤炉", "", "&a&oSmelt it in an Oven"), "OVEN");
    public static final RecipeType MAGIC_WORKBENCH = new RecipeType(new CustomItem(Material.BOOKSHELF, "&6魔法工作台",  "", "&a&o在魔法工作台内合成" ), "MAGIC_WORKBENCH");
    public static final RecipeType ORE_WASHER = new RecipeType(new CustomItem(Material.CAULDRON, "&6洗矿机", "", "&a&o在洗矿机中清洗"), "ORE_WASHER");
	public static final RecipeType ENHANCED_CRAFTING_TABLE = new RecipeType(new CustomItem(Material.CRAFTING_TABLE, "&e高级工作台", "", "&a&o使用高级工作台合成"), "ENHANCED_CRAFTING_TABLE");
	public static final RecipeType JUICER = new RecipeType(new CustomItem(Material.GLASS_BOTTLE, "&e榨汁机", "", "&a&o用于制作果汁"), "JUICER");
	public static final RecipeType ANCIENT_ALTAR = new RecipeType(new CustomItem(Material.ENCHANTING_TABLE, "&4古代祭坛", "", "&d你需要举行古老的仪式", "&d来合成这个物品"));
    public static final RecipeType HEATED_PRESSURE_CHAMBER = new RecipeType(new CustomItem(Material.GRAY_STAINED_GLASS, "&c加热压力舱","", "&a&o用加热压力舱合成这个物品"), "HEATED_PRESSURE_CHAMBER");

    public static final RecipeType SHAPED_RECIPE = new RecipeType(new CustomItem(Material.CRAFTING_TABLE, "&e有序合成配方", "", "&a&o原版工作台中的合成配方"));
	public static final RecipeType SHAPELESS_RECIPE = new RecipeType(new CustomItem(Material.CRAFTING_TABLE, "&e无序合成配方", "", "&a&o原版工作台中的合成配方"));
	public static final RecipeType FURNACE = new RecipeType(new CustomItem(Material.FURNACE, "&e在熔炉中冶炼", "", "&a&o把它放在熔炉里烧"));
	public static final RecipeType NULL = new RecipeType(null);
	
	private ItemStack item;
	private String machine;
	
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
		List<ItemStack> convertible = new ArrayList<ItemStack>();
		for (int i = 0; i < recipes.size(); i++) {
			if (i % 2 == 0) convertible.add(recipes.get(i)[0]);
		}
		return convertible;
	}
	
	public static List<ItemStack[]> getRecipeInputList(SlimefunItem machine) {
		if (machine == null) return new ArrayList<>();
		List<ItemStack[]> recipes = (machine instanceof SlimefunMachine ? ((SlimefunMachine) machine).getRecipes(): ((SlimefunGadget) machine).getRecipes());
		List<ItemStack[]> convertible = new ArrayList<ItemStack[]>();
		for (int i = 0; i < recipes.size(); i++) {
			if (i % 2 == 0) convertible.add(recipes.get(i));
		}
		return convertible;
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
