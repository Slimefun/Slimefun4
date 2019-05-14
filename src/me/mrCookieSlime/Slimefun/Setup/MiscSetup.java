package me.mrCookieSlime.Slimefun.Setup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.Colors;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItemSerializer;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItemSerializer.ItemFlag;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Misc.PostSlimefunLoadingHandler;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Alloy;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ReplacingAlloy;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunMachine;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.AutomatedCraftingChamber;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunRecipes;

public class MiscSetup {
	
	public static List<PostSlimefunLoadingHandler> post_handlers = new ArrayList<PostSlimefunLoadingHandler>();
	
	public static void setupMisc() {
		if (SlimefunItem.getByID("COMMON_TALISMAN") != null && (Boolean) Slimefun.getItemValue("COMMON_TALISMAN", "recipe-requires-nether-stars")) {
			SlimefunItem.getByID("COMMON_TALISMAN").setRecipe(new ItemStack[] {SlimefunItems.MAGIC_LUMP_2, SlimefunItems.GOLD_8K, SlimefunItems.MAGIC_LUMP_2, null, new ItemStack(Material.NETHER_STAR), null, SlimefunItems.MAGIC_LUMP_2, SlimefunItems.GOLD_8K, SlimefunItems.MAGIC_LUMP_2});
		}
		SlimefunItem.setRadioactive(SlimefunItems.URANIUM);
		SlimefunItem.setRadioactive(SlimefunItems.SMALL_URANIUM);
		SlimefunItem.setRadioactive(SlimefunItems.BLISTERING_INGOT);
		SlimefunItem.setRadioactive(SlimefunItems.BLISTERING_INGOT_2);
		SlimefunItem.setRadioactive(SlimefunItems.BLISTERING_INGOT_3);
		SlimefunItem.setRadioactive(SlimefunItems.NETHER_ICE);
		SlimefunItem.setRadioactive(SlimefunItems.ENRICHED_NETHER_ICE);
	}
	
	public static void loadItems() {
		
		Iterator<SlimefunItem> iterator = SlimefunItem.items.iterator();
		while (iterator.hasNext()) {
			SlimefunItem item = iterator.next();
			if (item == null) {
				System.err.println("[Slimefun] Removed bugged Item ('NULL?')");
				iterator.remove();
			}
			else if (item.getItem() == null) {
				System.err.println("[Slimefun] Removed bugged Item ('" + item.getID() + "')");
				iterator.remove();
			}
		}
		
		List<SlimefunItem> pre = new ArrayList<SlimefunItem>();
		List<SlimefunItem> init = new ArrayList<SlimefunItem>();
		List<SlimefunItem> post = new ArrayList<SlimefunItem>();
		
		for (SlimefunItem item: SlimefunItem.list()) {
			if (item instanceof Alloy || item instanceof ReplacingAlloy) pre.add(item);
			else if (item instanceof SlimefunMachine) init.add(item);
			else post.add(item);
		}
		
		for (SlimefunItem item: pre) {
			item.load();
		}
		for (SlimefunItem item: init) {
			item.load();
		}
		for (SlimefunItem item: post) {
			item.load();
		}

		AutomatedCraftingChamber crafter = (AutomatedCraftingChamber) SlimefunItem.getByID("AUTOMATED_CRAFTING_CHAMBER");
		
		if (crafter != null) {
//			Iterator<Recipe> recipes = Bukkit.recipeIterator();
//			
//			while (recipes.hasNext()) {
//				Recipe r = recipes.next();
//				boolean allow = true;
//				if (Bukkit.getPluginManager().isPluginEnabled("SensibleToolbox")) {
//					BaseSTBItem item = SensibleToolbox.getItemRegistry().fromItemStack(r.getResult());
//					allow = item == null;
//				}
//				
//				if (allow) {
//					if (r instanceof ShapedRecipe) {
//						
//					}
//					else if (r instanceof ShapelessRecipe) {
//						
//					}
//				}
//			}
			
			for (ItemStack[] inputs: RecipeType.getRecipeInputList((SlimefunMachine) SlimefunItem.getByID("ENHANCED_CRAFTING_TABLE"))) {
				StringBuilder builder = new StringBuilder();
				int i = 0;
				for (ItemStack item: inputs) {
					if (i > 0) {
						builder.append(" </slot> ");
					}
					
					builder.append(CustomItemSerializer.serialize(item, ItemFlag.DATA, ItemFlag.ITEMMETA_DISPLAY_NAME, ItemFlag.ITEMMETA_LORE, ItemFlag.MATERIAL));
					
					i++;
				}
				
				AutomatedCraftingChamber.recipes.put(builder.toString(), RecipeType.getRecipeOutputList((SlimefunMachine) SlimefunItem.getByID("ENHANCED_CRAFTING_TABLE"), inputs));
			}
			
		}
		
		SlimefunItem grinder = SlimefunItem.getByID("GRIND_STONE");
		if (grinder != null) {
			ItemStack[] input = null;
			for (ItemStack[] recipe: ((SlimefunMachine) grinder).getRecipes()) {
				if (input == null) input = recipe;
				else {
					if (input[0] != null && recipe[0] != null) {
						SlimefunRecipes.registerMachineRecipe("ELECTRIC_ORE_GRINDER", 4, new ItemStack[] {input[0]}, new ItemStack[] {recipe[0]});
					}
					input = null;
				}
			}
		}

		SlimefunItem crusher = SlimefunItem.getByID("ORE_CRUSHER");
		if (crusher != null) {
			ItemStack[] input = null;
			for (ItemStack[] recipe: ((SlimefunMachine) crusher).getRecipes()) {
				if (input == null) input = recipe;
				else {
					if (input[0] != null && recipe[0] != null) {
						SlimefunRecipes.registerMachineRecipe("ELECTRIC_ORE_GRINDER", 4, new ItemStack[] {input[0]}, new ItemStack[] {recipe[0]});
					}
					input = null;
				}
			}
		}

		SlimefunItem smeltery = SlimefunItem.getByID("SMELTERY");
		if (smeltery != null) {
			ItemStack[] input = null;
			for (ItemStack[] recipe: ((SlimefunMachine) smeltery).getRecipes()) {
				if (input == null) input = recipe;
				else {
					if (input[0] != null && recipe[0] != null) {
						List<ItemStack> inputs = new ArrayList<ItemStack>();
						boolean dust = false;
						for (ItemStack i: input) {
							if (i != null) {
								inputs.add(i);
								if (SlimefunManager.isItemSimiliar(i, SlimefunItems.ALUMINUM_DUST, true)) dust = true;
								if (SlimefunManager.isItemSimiliar(i, SlimefunItems.COPPER_DUST, true)) dust = true;
								if (SlimefunManager.isItemSimiliar(i, SlimefunItems.GOLD_DUST, true)) dust = true;
								if (SlimefunManager.isItemSimiliar(i, SlimefunItems.IRON_DUST, true)) dust = true;
								if (SlimefunManager.isItemSimiliar(i, SlimefunItems.LEAD_DUST, true)) dust = true;
								if (SlimefunManager.isItemSimiliar(i, SlimefunItems.MAGNESIUM_DUST, true)) dust = true;
								if (SlimefunManager.isItemSimiliar(i, SlimefunItems.SILVER_DUST, true)) dust = true;
								if (SlimefunManager.isItemSimiliar(i, SlimefunItems.TIN_DUST, true)) dust = true;
								if (SlimefunManager.isItemSimiliar(i, SlimefunItems.ZINC_DUST, true)) dust = true;
							}
						}
						
						if (dust && inputs.size() == 1) {
							// Dust -> Ingot Recipe, we want to exclude those
						}
						else {
							SlimefunRecipes.registerMachineRecipe("ELECTRIC_SMELTERY", 12, inputs.toArray(new ItemStack[inputs.size()]), new ItemStack[] {recipe[0]});
						}
					}
					input = null;
				}
			}
		}
		
		CommandSender sender = Bukkit.getConsoleSender();
		ChatColor color = Colors.getRandom();
		
		for (PostSlimefunLoadingHandler handler: post_handlers) {
			handler.run(pre, init, post);
		}
		
		sender.sendMessage(color + "###################### - Slimefun - ######################");
		sender.sendMessage(color + "Successfully loaded " + SlimefunItem.list().size() + " Items (" + Research.list().size() + " Researches)");
		sender.sendMessage(color + "( " + SlimefunItem.vanilla + " Items from Slimefun, " + (SlimefunItem.list().size() - SlimefunItem.vanilla) + " Items from Addons )");
		sender.sendMessage(color + "##########################################################");
		SlimefunStartup.getItemCfg().save();
		SlimefunStartup.getResearchCfg().save();
		SlimefunStartup.getWhitelist().save();
	}

	public static void setupItemSettings() {
		for (World world: Bukkit.getWorlds()) {
			SlimefunStartup.getWhitelist().setDefaultValue(world.getName() + ".enabled-items.SLIMEFUN_GUIDE", true);
		}
		Slimefun.setItemVariable("ORE_CRUSHER", "double-ores", true);
	}
	
	public static void loadDescriptions() {
		Slimefun.addYoutubeVideo("ANCIENT_ALTAR", "https://youtu.be/mx2Y5DP8uZI");
		Slimefun.addYoutubeVideo("ANCIENT_PEDESTAL", "https://youtu.be/mx2Y5DP8uZI");
		
		Slimefun.addYoutubeVideo("BLISTERING_INGOT", "https://youtu.be/mPhKUv4JR_Y");
		Slimefun.addYoutubeVideo("BLISTERING_INGOT_2", "https://youtu.be/mPhKUv4JR_Y");
		Slimefun.addYoutubeVideo("BLISTERING_INGOT_3", "https://youtu.be/mPhKUv4JR_Y");
		
		Slimefun.addYoutubeVideo("INFERNAL_BONEMEAL", "https://youtu.be/gKxWqMlJDXY");
		
		Slimefun.addYoutubeVideo("RAINBOW_WOOL", "https://youtu.be/csvb0CxofdA");
		Slimefun.addYoutubeVideo("RAINBOW_GLASS", "https://youtu.be/csvb0CxofdA");
		Slimefun.addYoutubeVideo("RAINBOW_CLAY", "https://youtu.be/csvb0CxofdA");
		Slimefun.addYoutubeVideo("RAINBOW_GLASS_PANE", "https://youtu.be/csvb0CxofdA");

		Slimefun.addYoutubeVideo("RAINBOW_WOOL_XMAS", "https://youtu.be/l4pKk4SDE");
		Slimefun.addYoutubeVideo("RAINBOW_GLASS_XMAS", "https://youtu.be/l4pKk4SDE");
		Slimefun.addYoutubeVideo("RAINBOW_CLAY_XMAS", "https://youtu.be/l4pKk4SDE");
		Slimefun.addYoutubeVideo("RAINBOW_GLASS_PANE_XMAS", "https://youtu.be/l4pKk4SDE");

		Slimefun.addYoutubeVideo("OIL_PUMP", "https://youtu.be/_XmJ6hrv9uY");
		Slimefun.addYoutubeVideo("GPS_GEO_SCANNER", "https://youtu.be/_XmJ6hrv9uY");
		Slimefun.addYoutubeVideo("REFINERY", "https://youtu.be/_XmJ6hrv9uY");
		Slimefun.addYoutubeVideo("BUCKET_OF_OIL", "https://youtu.be/_XmJ6hrv9uY");
		Slimefun.addYoutubeVideo("BUCKET_OF_FUEL", "https://youtu.be/_XmJ6hrv9uY");

		Slimefun.addYoutubeVideo("GPS_TELEPORTER_PYLON", "https://youtu.be/ZnEhG8Kw6zU");
		Slimefun.addYoutubeVideo("GPS_TELEPORTATION_MATRIX", "https://youtu.be/ZnEhG8Kw6zU");
		Slimefun.addYoutubeVideo("GPS_TELEPORTER_PYLON", "https://youtu.be/ZnEhG8Kw6zU");

		Slimefun.addYoutubeVideo("PROGRAMMABLE_ANDROID_WOODCUTTER", "https://youtu.be/AGLsWSMs6A0");
		Slimefun.addYoutubeVideo("PROGRAMMABLE_ANDROID_BUTCHER", "https://youtu.be/G-re3qV-LJQ");
		Slimefun.addYoutubeVideo("PROGRAMMABLE_ANDROID_2_BUTCHER", "https://youtu.be/G-re3qV-LJQ");

		Slimefun.addYoutubeVideo("INFUSED_HOPPER", "https://youtu.be/_H2HGwkfBh8");

		Slimefun.addYoutubeVideo("ELEVATOR_PLATE", "https://youtu.be/OdKMjo6vNIs");

		Slimefun.addYoutubeVideo("ENERGY_REGULATOR", "https://youtu.be/QvSUfBYagXk");
		Slimefun.addYoutubeVideo("COMBUSTION_REACTOR", "https://youtu.be/QvSUfBYagXk");
		Slimefun.addYoutubeVideo("MULTIMETER", "https://youtu.be/QvSUfBYagXk");

		Slimefun.addYoutubeVideo("FOOD_FABRICATOR", "https://youtu.be/qJdFfvTGOmI");
		Slimefun.addYoutubeVideo("AUTO_BREEDER", "https://youtu.be/qJdFfvTGOmI");
		Slimefun.addYoutubeVideo("ORGANIC_FOOD_MELON", "https://youtu.be/qJdFfvTGOmI");
		Slimefun.addYoutubeVideo("ORGANIC_FOOD_WHEAT", "https://youtu.be/qJdFfvTGOmI");
		Slimefun.addYoutubeVideo("ORGANIC_FOOD_APPLE", "https://youtu.be/qJdFfvTGOmI");
		Slimefun.addYoutubeVideo("ORGANIC_FOOD_CARROT", "https://youtu.be/qJdFfvTGOmI");
		Slimefun.addYoutubeVideo("ORGANIC_FOOD_SEEDS", "https://youtu.be/qJdFfvTGOmI");
		Slimefun.addYoutubeVideo("ORGANIC_FOOD_BEETROOT", "https://youtu.be/qJdFfvTGOmI");
		Slimefun.addYoutubeVideo("ORGANIC_FOOD_POTATO", "https://youtu.be/qJdFfvTGOmI");
		Slimefun.addYoutubeVideo("ANIMAL_GROWTH_ACCELERATOR", "https://youtu.be/bV4wEaSxXFw");
		

		Slimefun.addYoutubeVideo("FOOD_COMPOSTER", "https://youtu.be/LjzUlFKAHCI");
		Slimefun.addYoutubeVideo("FERTILIZER_WHEAT", "https://youtu.be/LjzUlFKAHCI");
		Slimefun.addYoutubeVideo("FERTILIZER_APPLE", "https://youtu.be/LjzUlFKAHCI");
		Slimefun.addYoutubeVideo("FERTILIZER_POTATO", "https://youtu.be/LjzUlFKAHCI");
		Slimefun.addYoutubeVideo("FERTILIZER_CARROT", "https://youtu.be/LjzUlFKAHCI");
		Slimefun.addYoutubeVideo("FERTILIZER_SEEDS", "https://youtu.be/LjzUlFKAHCI");
		Slimefun.addYoutubeVideo("FERTILIZER_BEETROOT", "https://youtu.be/LjzUlFKAHCI");
		Slimefun.addYoutubeVideo("FERTILIZER_MELON", "https://youtu.be/LjzUlFKAHCI");
		Slimefun.addYoutubeVideo("CROP_GROWTH_ACCELERATOR", "https://youtu.be/LjzUlFKAHCI");

		Slimefun.addYoutubeVideo("XP_COLLECTOR", "https://youtu.be/fHtJDPeLMlg");
		
		Slimefun.addYoutubeVideo("ELECTRIC_ORE_GRINDER", "https://youtu.be/A6OuK7sfnaI");
		Slimefun.addYoutubeVideo("ELECTRIC_GOLD_PAN", "https://youtu.be/A6OuK7sfnaI");
		Slimefun.addYoutubeVideo("ELECTRIC_DUST_WASHER", "https://youtu.be/A6OuK7sfnaI");
		Slimefun.addYoutubeVideo("ELECTRIC_INGOT_FACTORY", "https://youtu.be/A6OuK7sfnaI");

		Slimefun.addYoutubeVideo("AUTOMATED_CRAFTING_CHAMBER", "https://youtu.be/FZj7nu9sOYA");

		Slimefun.addYoutubeVideo("CARGO_MANAGER", "https://youtu.be/Lt2aGw5lQPI");
		Slimefun.addYoutubeVideo("CARGO_NODE_INPUT", "https://youtu.be/Lt2aGw5lQPI");
		Slimefun.addYoutubeVideo("CARGO_NODE_OUTPUT", "https://youtu.be/Lt2aGw5lQPI");

		Slimefun.addYoutubeVideo("GPS_CONTROL_PANEL", "https://youtu.be/kOopBkiRzjs");
		
		Slimefun.addYoutubeVideo("GPS_TRANSMITTER", "https://youtu.be/kOopBkiRzjs");
		Slimefun.addYoutubeVideo("GPS_TRANSMITTER_2", "https://youtu.be/kOopBkiRzjs");
		Slimefun.addYoutubeVideo("GPS_TRANSMITTER_3", "https://youtu.be/kOopBkiRzjs");
		Slimefun.addYoutubeVideo("GPS_TRANSMITTER_4", "https://youtu.be/kOopBkiRzjs");
		
		Slimefun.addYoutubeVideo("SOLAR_GENERATOR", "https://youtu.be/kOopBkiRzjs");
		Slimefun.addYoutubeVideo("SOLAR_GENERATOR_2", "https://youtu.be/kOopBkiRzjs");
		Slimefun.addYoutubeVideo("SOLAR_GENERATOR_3", "https://youtu.be/kOopBkiRzjs");
		Slimefun.addYoutubeVideo("SOLAR_GENERATOR_4", "https://youtu.be/kOopBkiRzjs");
		
		WikiSetup.setup();
	}

}
