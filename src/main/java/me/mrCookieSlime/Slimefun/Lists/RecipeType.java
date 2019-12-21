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
	
	public static final RecipeType MULTIBLOCK = new RecipeType(new CustomItem(Material.BRICKS, "&b多方塊機器", "", "&a&o照著說明直接建造在世界中"));
	public static final RecipeType ARMOR_FORGE = new RecipeType((SlimefunItemStack) SlimefunItems.ARMOR_FORGE, "", "&a&o使用鍛造台鍛造得到");
	public static final RecipeType GRIND_STONE = new RecipeType((SlimefunItemStack) SlimefunItems.GRIND_STONE, "", "&a&o使用磨石來打磨");
	public static final RecipeType MOB_DROP = new RecipeType(new CustomItem(Material.IRON_SWORD, "&b生物掉落物", "", "&a&o殺死特定生物得到"));
	public static final RecipeType SMELTERY = new RecipeType((SlimefunItemStack) SlimefunItems.SMELTERY, "", "&a&o使用冶煉爐來燒製物品");
	public static final RecipeType ORE_CRUSHER = new RecipeType((SlimefunItemStack) SlimefunItems.ORE_CRUSHER, "", "&a&o使用碎礦機碎礦");
	public static final RecipeType GOLD_PAN = new RecipeType((SlimefunItemStack) SlimefunItems.GOLD_PAN, "", "&a&o使用淘金盤淘金獲得");
	public static final RecipeType COMPRESSOR = new RecipeType((SlimefunItemStack) SlimefunItems.COMPRESSOR, "", "&a&o使用壓縮機壓縮得到");
	public static final RecipeType PRESSURE_CHAMBER = new RecipeType((SlimefunItemStack) SlimefunItems.PRESSURE_CHAMBER, "", "&a&o使用壓力室得到");
	public static final RecipeType MAGIC_WORKBENCH = new RecipeType((SlimefunItemStack) SlimefunItems.MAGIC_WORKBENCH, "", "&a&o在魔法工作台合成");
	public static final RecipeType ORE_WASHER = new RecipeType((SlimefunItemStack) SlimefunItems.ORE_WASHER, "", "&a&o在洗礦機內清洗");
	public static final RecipeType ENHANCED_CRAFTING_TABLE = new RecipeType((SlimefunItemStack) SlimefunItems.ENHANCED_CRAFTING_TABLE, "", "&a&o普通的工作台", "&a&o無法承受這巨大的力量...");
	public static final RecipeType JUICER = new RecipeType((SlimefunItemStack) SlimefunItems.JUICER, "", "&a&o使用果汁機得到");
	public static final RecipeType ANCIENT_ALTAR = new RecipeType((SlimefunItemStack) SlimefunItems.ANCIENT_ALTAR, "", "&d你需要將這些物品", "&d擺放到遠古祭壇的基座上", "&c需要將書本合成表的上方對準祭壇的東方(F3可查看方位)");
	public static final RecipeType HEATED_PRESSURE_CHAMBER = new RecipeType((SlimefunItemStack) SlimefunItems.HEATED_PRESSURE_CHAMBER, "", "&a&o使用加熱壓力室", "&a&o取得這項物品");
	
	public static final RecipeType SHAPED_RECIPE = new RecipeType(new CustomItem(Material.CRAFTING_TABLE, "&e合成", "", "&a&o使用普通的工作台合成"));
	public static final RecipeType SHAPELESS_RECIPE = new RecipeType(new CustomItem(Material.CRAFTING_TABLE, "&e合成", "", "&a&o使用普通的工作台合成"));
	public static final RecipeType FURNACE = new RecipeType(new CustomItem(Material.FURNACE, "&e熔爐", "", "&a&o在熔爐內燒製"));
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
