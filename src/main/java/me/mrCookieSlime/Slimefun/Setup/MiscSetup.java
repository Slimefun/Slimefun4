package me.mrCookieSlime.Slimefun.Setup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItemSerializer;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItemSerializer.ItemFlag;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Alloy;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ReplacingAlloy;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunMachine;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.AutomatedCraftingChamber;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunRecipes;
import me.mrCookieSlime.Slimefun.utils.Settings;

public final class MiscSetup {
	
	private MiscSetup() {}
	
	public static void setupMisc() {
		SlimefunItem talisman = SlimefunItem.getByID("COMMON_TALISMAN");
		if (talisman != null && (boolean) Slimefun.getItemValue(talisman.getID(), "recipe-requires-nether-stars")) {
			talisman.setRecipe(new ItemStack[] {SlimefunItems.MAGIC_LUMP_2, SlimefunItems.GOLD_8K, SlimefunItems.MAGIC_LUMP_2, null, new ItemStack(Material.NETHER_STAR), null, SlimefunItems.MAGIC_LUMP_2, SlimefunItems.GOLD_8K, SlimefunItems.MAGIC_LUMP_2});
		}
		
		SlimefunItem.setRadioactive(SlimefunItems.URANIUM);
		SlimefunItem.setRadioactive(SlimefunItems.SMALL_URANIUM);
		SlimefunItem.setRadioactive(SlimefunItems.BLISTERING_INGOT);
		SlimefunItem.setRadioactive(SlimefunItems.BLISTERING_INGOT_2);
		SlimefunItem.setRadioactive(SlimefunItems.BLISTERING_INGOT_3);
		SlimefunItem.setRadioactive(SlimefunItems.NETHER_ICE);
		SlimefunItem.setRadioactive(SlimefunItems.ENRICHED_NETHER_ICE);
	}
	
	public static void loadItems(Settings settings) {
		Iterator<SlimefunItem> iterator = SlimefunItem.list().iterator();
		while (iterator.hasNext()) {
			SlimefunItem item = iterator.next();
			if (item == null) {
				Slimefun.getLogger().log(Level.WARNING, "Removed bugged Item ('NULL?')");
				iterator.remove();
			}
			else if (item.getItem() == null) {
				Slimefun.getLogger().log(Level.WARNING, "Removed bugged Item ('" + item.getID() + "')");
				iterator.remove();
			}
		}
		
		List<SlimefunItem> pre = new ArrayList<>();
		List<SlimefunItem> init = new ArrayList<>();
		List<SlimefunItem> post = new ArrayList<>();
		
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
			
			SlimefunMachine machine = (SlimefunMachine) SlimefunItem.getByID("ENHANCED_CRAFTING_TABLE");
			
			for (ItemStack[] inputs: RecipeType.getRecipeInputList(machine)) {
				StringBuilder builder = new StringBuilder();
				int i = 0;
				for (ItemStack item: inputs) {
					if (i > 0) {
						builder.append(" </slot> ");
					}
					
					builder.append(CustomItemSerializer.serialize(item, ItemFlag.MATERIAL, ItemFlag.ITEMMETA_DISPLAY_NAME, ItemFlag.ITEMMETA_LORE));
					
					i++;
				}
				
				SlimefunPlugin.getUtilities().automatedCraftingChamberRecipes.put(builder.toString(), RecipeType.getRecipeOutputList(machine, inputs));
			}
			
		}
		
		List<ItemStack[]> grinderRecipes = new ArrayList<>();
		
		SlimefunItem grinder = SlimefunItem.getByID("GRIND_STONE");
		if (grinder != null) {
			ItemStack[] input = null;
			for (ItemStack[] recipe: ((SlimefunMachine) grinder).getRecipes()) {
				if (input == null) input = recipe;
				else {
					if (input[0] != null && recipe[0] != null) {
						grinderRecipes.add(new ItemStack[] {input[0], recipe[0]});
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
						grinderRecipes.add(new ItemStack[] {input[0], recipe[0]});
					}
					input = null;
				}
			}
		}
		
		// Favour 8 Cobblestone -> 1 Sand Recipe over 1 Cobblestone -> 1 Gravel Recipe
		Stream<ItemStack[]> stream = grinderRecipes.stream();
		
		if (!settings.legacyOreGrinder) {
			stream = stream.sorted((a, b) -> Integer.compare(b[0].getAmount(), a[0].getAmount()));
		}
		
		stream.forEach(recipe -> SlimefunRecipes.registerMachineRecipe("ELECTRIC_ORE_GRINDER", 4, new ItemStack[] {recipe[0]}, new ItemStack[] {recipe[1]}));
		
		SlimefunItem smeltery = SlimefunItem.getByID("SMELTERY");
		if (smeltery != null) {
			ItemStack[] input = null;
			for (ItemStack[] recipe: ((SlimefunMachine) smeltery).getRecipes()) {
				if (input == null) input = recipe;
				else {
					if (input[0] != null && recipe[0] != null) {
						List<ItemStack> inputs = new ArrayList<>();
						boolean dust = false;
						
						for (ItemStack i: input) {
							if (i != null) {
								inputs.add(i);
								if (SlimefunManager.isItemSimilar(i, SlimefunItems.ALUMINUM_DUST, true)) dust = true;
								if (SlimefunManager.isItemSimilar(i, SlimefunItems.COPPER_DUST, true)) dust = true;
								if (SlimefunManager.isItemSimilar(i, SlimefunItems.GOLD_DUST, true)) dust = true;
								if (SlimefunManager.isItemSimilar(i, SlimefunItems.IRON_DUST, true)) dust = true;
								if (SlimefunManager.isItemSimilar(i, SlimefunItems.LEAD_DUST, true)) dust = true;
								if (SlimefunManager.isItemSimilar(i, SlimefunItems.MAGNESIUM_DUST, true)) dust = true;
								if (SlimefunManager.isItemSimilar(i, SlimefunItems.SILVER_DUST, true)) dust = true;
								if (SlimefunManager.isItemSimilar(i, SlimefunItems.TIN_DUST, true)) dust = true;
								if (SlimefunManager.isItemSimilar(i, SlimefunItems.ZINC_DUST, true)) dust = true;
							}
						}

						// We want to exclude Dust to Ingot Recipes
						if (!(dust && inputs.size() == 1)) {
							SlimefunRecipes.registerMachineRecipe("ELECTRIC_SMELTERY", 12, inputs.toArray(new ItemStack[inputs.size()]), new ItemStack[] {recipe[0]});
						}
					}
					
					input = null;
				}
			}
		}
		
		CommandSender sender = Bukkit.getConsoleSender();
		
		for (PostSlimefunLoadingHandler handler: SlimefunPlugin.getUtilities().postHandlers) {
			handler.run(pre, init, post);
		}
		
		sender.sendMessage(ChatColor.GREEN + "###################### - Slimefun - ######################");
		sender.sendMessage(ChatColor.GREEN + "Successfully loaded " + SlimefunItem.list().size() + " Items (" + Research.list().size() + " Researches)");
		sender.sendMessage(ChatColor.GREEN + "( " + SlimefunPlugin.getUtilities().vanillaItems + " Items from Slimefun, " + (SlimefunItem.list().size() - SlimefunPlugin.getUtilities().vanillaItems) + " Items from Addons )");
		sender.sendMessage(ChatColor.GREEN + "##########################################################");
		SlimefunPlugin.getItemCfg().save();
		SlimefunPlugin.getResearchCfg().save();
		SlimefunPlugin.getWhitelist().save();
	}

	public static void setupItemSettings() {
		for (World world: Bukkit.getWorlds()) {
			SlimefunPlugin.getWhitelist().setDefaultValue(world.getName() + ".enabled-items.SLIMEFUN_GUIDE", true);
		}
		
		Slimefun.setItemVariable("ORE_CRUSHER", "double-ores", true);
		
		for (Enchantment e : Enchantment.values()) {
			for (int i = 1; i <= e.getMaxLevel(); i++) {
				Slimefun.setItemVariable("MAGICIAN_TALISMAN", "allow-enchantments." + e.getKey().getKey() + ".level." + i, true);
			}
		}
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
	}

}
