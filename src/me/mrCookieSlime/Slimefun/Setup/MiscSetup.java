package me.mrCookieSlime.Slimefun.Setup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.Colors;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItemSerializer;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItemSerializer.ItemFlag;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Alloy;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ReplacingAlloy;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunMachine;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.AutomatedCraftingChamber;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunRecipes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class MiscSetup {
	
	public static void setupMisc() {
		if (SlimefunItem.getByName("COMMON_TALISMAN") != null && (Boolean) Slimefun.getItemValue("COMMON_TALISMAN", "recipe-requires-nether-stars")) {
			SlimefunItem.getByName("COMMON_TALISMAN").setRecipe(new ItemStack[] {SlimefunItems.MAGIC_LUMP_2, SlimefunItems.GOLD_8K, SlimefunItems.MAGIC_LUMP_2, null, new ItemStack(Material.NETHER_STAR), null, SlimefunItems.MAGIC_LUMP_2, SlimefunItems.GOLD_8K, SlimefunItems.MAGIC_LUMP_2});
		}
		SlimefunItem.setRadioactive(SlimefunItems.URANIUM);
		SlimefunItem.setRadioactive(SlimefunItems.SMALL_URANIUM);
		SlimefunItem.setRadioactive(SlimefunItems.BLISTERING_INGOT);
		SlimefunItem.setRadioactive(SlimefunItems.BLISTERING_INGOT_2);
		SlimefunItem.setRadioactive(SlimefunItems.BLISTERING_INGOT_3);
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
				System.err.println("[Slimefun] Removed bugged Item ('" + item.getName() + "')");
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

		AutomatedCraftingChamber crafter = (AutomatedCraftingChamber) SlimefunItem.getByName("AUTOMATED_CRAFTING_CHAMBER");
		
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
			
			for (ItemStack[] inputs: RecipeType.getRecipeInputList((SlimefunMachine) SlimefunItem.getByName("ENHANCED_CRAFTING_TABLE"))) {
				StringBuilder builder = new StringBuilder();
				int i = 0;
				for (ItemStack item: inputs) {
					if (i > 0) {
						builder.append(" </slot> ");
					}
					
					builder.append(CustomItemSerializer.serialize(item, ItemFlag.DATA, ItemFlag.ITEMMETA_DISPLAY_NAME, ItemFlag.ITEMMETA_LORE, ItemFlag.MATERIAL));
					
					i++;
				}
				
				AutomatedCraftingChamber.recipes.put(builder.toString(), RecipeType.getRecipeOutputList((SlimefunMachine) SlimefunItem.getByName("ENHANCED_CRAFTING_TABLE"), inputs));
			}
			
		}
		
		SlimefunItem grinder = SlimefunItem.getByName("GRIND_STONE");
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

		SlimefunItem crusher = SlimefunItem.getByName("ORE_CRUSHER");
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

		SlimefunItem smeltery = SlimefunItem.getByName("SMELTERY");
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
		
		// Weapons
		Slimefun.addOfficialWikiPage("GRANDMAS_WALKING_STICK", "Walking-Sticks");
		Slimefun.addOfficialWikiPage("GRANDPAS_WALKING_STICK", "Walking-Sticks");
		Slimefun.addOfficialWikiPage("SWORD_OF_BEHEADING", "Sword-of-Beheading");
		Slimefun.addOfficialWikiPage("BLADE_OF_VAMPIRES", "Blade-of-Vampires");
		Slimefun.addOfficialWikiPage("SEISMIC_AXE", "Seismic-Axe");
		Slimefun.addOfficialWikiPage("SOULBOUND_SWORD", "Soulbound-Weapons");
		Slimefun.addOfficialWikiPage("SOULBOUND_BOW", "Soulbound-Weapons");
		Slimefun.addOfficialWikiPage("EXPLOSIVE_BOW", "Special-Bows");
		Slimefun.addOfficialWikiPage("ICY_BOW", "Special-Bows");
		
		// Various Items
		Slimefun.addOfficialWikiPage("PORTABLE_CRAFTER", "Portable-Crafter");
		Slimefun.addOfficialWikiPage("PORTABLE_DUSTBIN", "Portable-Dustbin");
		Slimefun.addOfficialWikiPage("ENDER_BACKPACK", "Ender-Backpack");
		Slimefun.addOfficialWikiPage("RAG", "Medical-Supplies");
		Slimefun.addOfficialWikiPage("BANDAGE", "Medical-Supplies");
		Slimefun.addOfficialWikiPage("SPLINT", "Medical-Supplies");
		Slimefun.addOfficialWikiPage("VITAMINS", "Medical-Supplies");
		Slimefun.addOfficialWikiPage("MEDICINE", "Medical-Supplies");
		Slimefun.addOfficialWikiPage("SMALL_BACKPACK", "Backpacks");
		Slimefun.addOfficialWikiPage("MEDIUM_BACKPACK", "Backpacks");
		Slimefun.addOfficialWikiPage("LARGE_BACKPACK", "Backpacks");
		Slimefun.addOfficialWikiPage("WOVEN_BACKPACK", "Backpacks");
		Slimefun.addOfficialWikiPage("GILDED_BACKPACK", "Backpacks");
		Slimefun.addOfficialWikiPage("BOUND_BACKPACK", "Backpacks");
		Slimefun.addOfficialWikiPage("COOLER", "Cooler");
		
		// Basic Machines
		Slimefun.addOfficialWikiPage("ENHANCED_CRAFTING_TABLE", "Enhanced-Crafting-Table");
		Slimefun.addOfficialWikiPage("GRIND_STONE", "Grind-Stone");
		Slimefun.addOfficialWikiPage("ARMOR_FORGE", "Armor-Forge");
		Slimefun.addOfficialWikiPage("ORE_CRUSHER", "Ore-Crusher");
		Slimefun.addOfficialWikiPage("COMPRESSOR", "Compressor");
		Slimefun.addOfficialWikiPage("SMELTERY", "Smeltery");
		Slimefun.addOfficialWikiPage("PRESSURE_CHAMBER", "Pressure-Chamber");
		Slimefun.addOfficialWikiPage("MAGIC_WORKBENCH", "Magic-Workbench");
		Slimefun.addOfficialWikiPage("ORE_WASHER", "Ore-Washer");
		Slimefun.addOfficialWikiPage("SAW_MILL", "Saw-Mill");
		Slimefun.addOfficialWikiPage("DIGITAL_MINER", "Digital-Miners");
		Slimefun.addOfficialWikiPage("ADVANCED_DIGITAL_MINER", "Digital-Miners");
		Slimefun.addOfficialWikiPage("JUICER", "Juicer");
		Slimefun.addOfficialWikiPage("COMPOSTER", "Composter");
		Slimefun.addOfficialWikiPage("CRUCIBLE", "Crucible");
		Slimefun.addOfficialWikiPage("ENHANCED_FURNACE", "Enhanced-Furnaces");
		Slimefun.addOfficialWikiPage("ENHANCED_FURNACE_2", "Enhanced-Furnaces");
		Slimefun.addOfficialWikiPage("ENHANCED_FURNACE_3", "Enhanced-Furnaces");
		Slimefun.addOfficialWikiPage("ENHANCED_FURNACE_4", "Enhanced-Furnaces");
		Slimefun.addOfficialWikiPage("ENHANCED_FURNACE_5", "Enhanced-Furnaces");
		Slimefun.addOfficialWikiPage("ENHANCED_FURNACE_6", "Enhanced-Furnaces");
		Slimefun.addOfficialWikiPage("ENHANCED_FURNACE_7", "Enhanced-Furnaces");
		Slimefun.addOfficialWikiPage("ENHANCED_FURNACE_8", "Enhanced-Furnaces");
		Slimefun.addOfficialWikiPage("ENHANCED_FURNACE_9", "Enhanced-Furnaces");
		Slimefun.addOfficialWikiPage("ENHANCED_FURNACE_10", "Enhanced-Furnaces");
		Slimefun.addOfficialWikiPage("ENHANCED_FURNACE_11", "Enhanced-Furnaces");
		Slimefun.addOfficialWikiPage("REINFORCED_FURNACE", "Enhanced-Furnaces");
		Slimefun.addOfficialWikiPage("CARBONADO_EDGED_FURNACE", "Enhanced-Furnaces");
		
		// Tools
		Slimefun.addOfficialWikiPage("GOLD_PAN", "Gold-Pan");
		Slimefun.addOfficialWikiPage("GRAPPLING_HOOK", "Grappling-Hook");
		Slimefun.addOfficialWikiPage("SMELTERS_PICKAXE", "Smelters-Pickaxe");
		Slimefun.addOfficialWikiPage("LUMBER_AXE", "Lumber-Axe");
		Slimefun.addOfficialWikiPage("PICKAXE_OF_CONTAINMENT", "Pickaxe-Of-Containment");
		Slimefun.addOfficialWikiPage("HERCULES_PICKAXE", "Hercules-Pickaxe");
		Slimefun.addOfficialWikiPage("EXPLOSIVE_PICKAXE", "Explosive-Pickaxe");
		Slimefun.addOfficialWikiPage("PICKAXE_OF_THE_SEEKER", "Pickaxe-Of-The-Seeker");
		Slimefun.addOfficialWikiPage("PICKAXE_OF_VEIN_MINING", "Pickaxe-Of-Vein-Mining");
		Slimefun.addOfficialWikiPage("SOULBOUND_PICKAXE", "Soulbound-Tools");
		Slimefun.addOfficialWikiPage("SOULBOUND_AXE", "Soulbound-Tools");
		Slimefun.addOfficialWikiPage("SOULBOUND_SHOVEL", "Soulbound-Tools");
		Slimefun.addOfficialWikiPage("SOULBOUND_HOE", "Soulbound-Tools");
		
		// Food
		Slimefun.addOfficialWikiPage("FORTUNE_COOKIE", "Fortune-Cookie");
		Slimefun.addOfficialWikiPage("BEEF_JERKY", "Beef-Jerky");
		Slimefun.addOfficialWikiPage("MAGIC_SUGAR", "Magic-Sugar");
		Slimefun.addOfficialWikiPage("MONSTER_JERKY", "Monster-Jerky");
		Slimefun.addOfficialWikiPage("APPLE_JUICE", "Juices");
		Slimefun.addOfficialWikiPage("CARROT_JUICE", "Juices");
		Slimefun.addOfficialWikiPage("MELON_JUICE", "Juices");
		Slimefun.addOfficialWikiPage("PUMPKIN_JUICE", "Juices");
		Slimefun.addOfficialWikiPage("GOLDEN_APPLE_JUICE", "Juices");
		
		// Armor
		Slimefun.addOfficialWikiPage("CHAIN_HELMET", "Chain-Armor");
		Slimefun.addOfficialWikiPage("CHAIN_CHESTPLATE", "Chain-Armor");
		Slimefun.addOfficialWikiPage("CHAIN_LEGGINGS", "Chain-Armor");
		Slimefun.addOfficialWikiPage("CHAIN_BOOTS", "Chain-Armor");
		
		Slimefun.addOfficialWikiPage("DAMASCUS_STEEL_HELMET", "Damascus-Steel-Armor");
		Slimefun.addOfficialWikiPage("DAMASCUS_STEEL_CHESTPLATE", "Damascus-Steel-Armor");
		Slimefun.addOfficialWikiPage("DAMASCUS_STEEL_LEGGINGS", "Damascus-Steel-Armor");
		Slimefun.addOfficialWikiPage("DAMASCUS_STEEL_BOOTS", "Damascus-Steel-Armor");
		
		Slimefun.addOfficialWikiPage("GOLD_12K_HELMET", "Gold-Armor");
		Slimefun.addOfficialWikiPage("GOLD_12K_CHESTPLATE", "Gold-Armor");
		Slimefun.addOfficialWikiPage("GOLD_12K_LEGGINGS", "Gold-Armor");
		Slimefun.addOfficialWikiPage("GOLD_12K_BOOTS", "Gold-Armor");

		Slimefun.addOfficialWikiPage("REINFORCED_ALLOY_HELMET", "Reinforced-Armor");
		Slimefun.addOfficialWikiPage("REINFORCED_ALLOY_CHESTPLATE", "Reinforced-Armor");
		Slimefun.addOfficialWikiPage("REINFORCED_ALLOY_LEGGINGS", "Reinforced-Armor");
		Slimefun.addOfficialWikiPage("REINFORCED_ALLOY_BOOTS", "Reinforced-Armor");
		
		Slimefun.addOfficialWikiPage("GILDED_IRON_HELMET", "Gilded-Iron-Armor");
		Slimefun.addOfficialWikiPage("GILDED_IRON_CHESTPLATE", "Gilded-Iron-Armor");
		Slimefun.addOfficialWikiPage("GILDED_IRON_LEGGINGS", "Gilded-Iron-Armor");
		Slimefun.addOfficialWikiPage("GILDED_IRON_BOOTS", "Gilded-Iron-Armor");

		Slimefun.addOfficialWikiPage("SCUBA_HELMET", "Hazmat-Suit");
		Slimefun.addOfficialWikiPage("HAZMAT_CHESTPLATE", "Hazmat-Suit");
		Slimefun.addOfficialWikiPage("HAZMAT_LEGGINGS", "Hazmat-Suit");
		Slimefun.addOfficialWikiPage("RUBBER_BOOTS", "Hazmat-Suit");
		
		Slimefun.addOfficialWikiPage("CACTUS_HELMET", "Cactus-Armor");
		Slimefun.addOfficialWikiPage("CACTUS_CHESTPLATE", "Cactus-Armor");
		Slimefun.addOfficialWikiPage("CACTUS_LEGGINGS", "Cactus-Armor");
		Slimefun.addOfficialWikiPage("CACTUS_BOOTS", "Cactus-Armor");
		
		// Resources
		Slimefun.addOfficialWikiPage("COPPER_INGOT", "Ingots");
		Slimefun.addOfficialWikiPage("TIN_INGOT", "Ingots");
		Slimefun.addOfficialWikiPage("SILVER_INGOT", "Ingots");
		Slimefun.addOfficialWikiPage("LEAD_INGOT", "Ingots");
		Slimefun.addOfficialWikiPage("ALUMINUM_INGOT", "Ingots");
		Slimefun.addOfficialWikiPage("ZINC_INGOT", "Ingots");
		Slimefun.addOfficialWikiPage("MAGNESIUM_INGOT", "Ingots");
		Slimefun.addOfficialWikiPage("GOLD_4K", "Ingots");
		Slimefun.addOfficialWikiPage("GOLD_6K", "Ingots");
		Slimefun.addOfficialWikiPage("GOLD_8K", "Ingots");
		Slimefun.addOfficialWikiPage("GOLD_10K", "Ingots");
		Slimefun.addOfficialWikiPage("GOLD_12K", "Ingots");
		Slimefun.addOfficialWikiPage("GOLD_14K", "Ingots");
		Slimefun.addOfficialWikiPage("GOLD_16K", "Ingots");
		Slimefun.addOfficialWikiPage("GOLD_18K", "Ingots");
		Slimefun.addOfficialWikiPage("GOLD_20K", "Ingots");
		Slimefun.addOfficialWikiPage("GOLD_22K", "Ingots");
		Slimefun.addOfficialWikiPage("GOLD_24K", "Ingots");
		Slimefun.addOfficialWikiPage("GOLD_24K", "Ingots");
		Slimefun.addOfficialWikiPage("BLISTERING_INGOT", "Ingots");
		Slimefun.addOfficialWikiPage("BLISTERING_INGOT_2", "Ingots");
		Slimefun.addOfficialWikiPage("BLISTERING_INGOT_3", "Ingots");
		Slimefun.addOfficialWikiPage("REINFORCED_ALLOY_INGOT", "Ingots");
		Slimefun.addOfficialWikiPage("HARDENED_METAL", "Ingots");
		Slimefun.addOfficialWikiPage("GILDED_IRON", "Ingots");
		Slimefun.addOfficialWikiPage("REDSTONE_ALLOY_INGOT", "Ingots");
		Slimefun.addOfficialWikiPage("FERROSILICON", "Ingots");
		Slimefun.addOfficialWikiPage("DAMASCUS_STEEL_INGOT", "Ingots");
		Slimefun.addOfficialWikiPage("STEEL_INGOT", "Ingots");
		Slimefun.addOfficialWikiPage("ALUMINUM_BRONZE_INGOT", "Ingots");
		Slimefun.addOfficialWikiPage("ALUMINUM_BRASS_INGOT", "Ingots");
		Slimefun.addOfficialWikiPage("CORINTHIAN_BRONZE_INGOT", "Ingots");
		Slimefun.addOfficialWikiPage("BRASS_INGOT", "Ingots");
		Slimefun.addOfficialWikiPage("BRONZE_INGOT", "Ingots");
		Slimefun.addOfficialWikiPage("BILLON_INGOT", "Ingots");
		Slimefun.addOfficialWikiPage("DURALUMIN_INGOT", "Ingots");
		Slimefun.addOfficialWikiPage("NICKEL_INGOT", "Ingots");
		Slimefun.addOfficialWikiPage("COBALT_INGOT", "Ingots");
		Slimefun.addOfficialWikiPage("SOLDER_INGOT", "Ingots");
		
		Slimefun.addOfficialWikiPage("IRON_DUST", "Dusts");
		Slimefun.addOfficialWikiPage("GOLD_DUST", "Dusts");
		Slimefun.addOfficialWikiPage("ALUMINUM_DUST", "Dusts");
		Slimefun.addOfficialWikiPage("ZINC_DUST", "Dusts");
		Slimefun.addOfficialWikiPage("TIN_DUST", "Dusts");
		Slimefun.addOfficialWikiPage("COPPER_DUST", "Dusts");
		Slimefun.addOfficialWikiPage("MAGNESIUM_DUST", "Dusts");
		Slimefun.addOfficialWikiPage("LEAD_DUST", "Dusts");
		Slimefun.addOfficialWikiPage("SILVER_DUST", "Dusts");

		Slimefun.addOfficialWikiPage("SYNTHETIC_DIAMOND", "Valuables");
		Slimefun.addOfficialWikiPage("SYNTHETIC_SAPPHIRE", "Valuables");
		Slimefun.addOfficialWikiPage("SYNTHETIC_EMERALD", "Valuables");
		Slimefun.addOfficialWikiPage("RAW_CARBONADO", "Valuables");
		Slimefun.addOfficialWikiPage("CARBONADO", "Valuables");
		Slimefun.addOfficialWikiPage("CARBON_CHUNK", "Valuables");
		Slimefun.addOfficialWikiPage("SULFATE", "Valuables");
		Slimefun.addOfficialWikiPage("CARBON", "Valuables");
		Slimefun.addOfficialWikiPage("COMPRESSED_CARBON", "Valuables");
		Slimefun.addOfficialWikiPage("SILICON", "Valuables");

		Slimefun.addOfficialWikiPage("BUCKET_OF_OIL", "Oil");
		Slimefun.addOfficialWikiPage("BUCKET_OF_FUEL", "Oil");
		Slimefun.addOfficialWikiPage("URANIUM", "Radioactive-Materials");
		Slimefun.addOfficialWikiPage("NEPTUNIUM", "Radioactive-Materials");
		Slimefun.addOfficialWikiPage("PLUTONIUM", "Radioactive-Materials");
		Slimefun.addOfficialWikiPage("BOOSTED_URANIUM", "Radioactive-Materials");
		Slimefun.addOfficialWikiPage("BLISTERING_INGOT", "Radioactive-Materials");
		Slimefun.addOfficialWikiPage("BLISTERING_INGOT_2", "Radioactive-Materials");
		Slimefun.addOfficialWikiPage("BLISTERING_INGOT_3", "Radioactive-Materials");
		
		// Energy & Electricity
		Slimefun.addOfficialWikiPage("ENERGY_REGULATOR", "Energy-Regulator");
		Slimefun.addOfficialWikiPage("CHARGING_BENCH", "Charging-Station");
		Slimefun.addOfficialWikiPage("SMALL_CAPACITOR", "Energy-Capacitor");
		Slimefun.addOfficialWikiPage("MEDIUM_CAPACITOR", "Energy-Capacitor");
		Slimefun.addOfficialWikiPage("BIG_CAPACITOR", "Energy-Capacitor");
		Slimefun.addOfficialWikiPage("LARGE_CAPACITOR", "Energy-Capacitor");
		Slimefun.addOfficialWikiPage("CARBONADO_EDGED_CAPACITOR", "Energy-Capacitor");
		Slimefun.addOfficialWikiPage("ELECTRIC_FURNACE", "Electric-Furnace");
		Slimefun.addOfficialWikiPage("ELECTRIC_FURNACE_2", "Electric-Furnace");
		Slimefun.addOfficialWikiPage("ELECTRIC_FURNACE_3", "Electric-Furnace");
		Slimefun.addOfficialWikiPage("ELECTRIC_GOLD_PAN", "Electric-Gold-Pan");
		Slimefun.addOfficialWikiPage("ELECTRIC_GOLD_PAN_2", "Electric-Gold-Pan");
		Slimefun.addOfficialWikiPage("ELECTRIC_GOLD_PAN_3", "Electric-Gold-Pan");
		Slimefun.addOfficialWikiPage("SOLAR_GENERATOR", "Solar-Generator");
		Slimefun.addOfficialWikiPage("SOLAR_GENERATOR_2", "Solar-Generator");
		Slimefun.addOfficialWikiPage("SOLAR_GENERATOR_3", "Solar-Generator");
		Slimefun.addOfficialWikiPage("SOLAR_GENERATOR_4", "Solar-Generator");
	}

}
