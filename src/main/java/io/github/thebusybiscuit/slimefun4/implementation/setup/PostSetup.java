package io.github.thebusybiscuit.slimefun4.implementation.setup;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.AutomatedCraftingChamber;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.*;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItemSerializer;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItemSerializer.ItemFlag;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class PostSetup {

    private PostSetup() {
    }

    public static void setupWiki() {
        Slimefun.getLogger().log(Level.INFO, "Loading Wiki pages...");

        JsonParser parser = new JsonParser();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(SlimefunPlugin.class.getResourceAsStream("/wiki.json")))) {
            JsonElement element = parser.parse(reader.lines().collect(Collectors.joining("")));
            JsonObject json = element.getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                SlimefunItem item = SlimefunItem.getByID(entry.getKey());

                if (item != null) {
                    item.addOficialWikipage(entry.getValue().getAsString());
                }
            }
        } catch (IOException e) {
            Slimefun.getLogger().log(Level.SEVERE, "Failed to load wiki.json file", e);
        }
    }

    public static void loadItems() {
        Iterator<SlimefunItem> iterator = SlimefunPlugin.getRegistry().getEnabledSlimefunItems().iterator();

        while (iterator.hasNext()) {
            SlimefunItem item = iterator.next();

            if (item == null) {
                Slimefun.getLogger().log(Level.WARNING, "Removed bugged Item ('NULL?')");
                iterator.remove();
            } else {
                item.load();
            }
        }

        loadAutomaticCraftingChamber();
        loadOreGrinderRecipes();
        loadSmelteryRecipes();

        CommandSender sender = Bukkit.getConsoleSender();

        int total = SlimefunPlugin.getRegistry().getEnabledSlimefunItems().size();
        int vanilla = SlimefunPlugin.getRegistry().countVanillaItems();

        sender.sendMessage("");
        sender.sendMessage(ChatColor.GREEN + "######################### - Slimefun v" + SlimefunPlugin.getVersion() + " - #########################");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GREEN + "成功载入了 " + total + " 个物品和 " + SlimefunPlugin.getRegistry().getResearches().size() + " 个研究");
        sender.sendMessage(ChatColor.GREEN + "( " + vanilla + " 个物品来自 Slimefun, " + (total - vanilla) + " 个物品来自 " + SlimefunPlugin.getInstalledAddons().size() + " 个扩展 )");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GREEN + "Slimefun 是一个由大型社区维护的开源项目.");

        sender.sendMessage("");
        sender.sendMessage(ChatColor.GREEN + " - 源码:  https://github.com/StarWishsama/Slimefun4");
        sender.sendMessage(ChatColor.GREEN + " - 问题反馈:  https://github.com/StarWishsama/Slimefun4/issues");


        sender.sendMessage("");

        SlimefunPlugin.getItemCfg().save();
        SlimefunPlugin.getResearchCfg().save();

        SlimefunPlugin.getRegistry().setAutoLoadingMode(true);
    }

    private static void loadAutomaticCraftingChamber() {
        AutomatedCraftingChamber crafter = (AutomatedCraftingChamber) SlimefunItems.AUTOMATED_CRAFTING_CHAMBER.getItem();

        if (crafter != null) {
            EnhancedCraftingTable machine = (EnhancedCraftingTable) SlimefunItems.ENHANCED_CRAFTING_TABLE.getItem();

            for (ItemStack[] inputs : RecipeType.getRecipeInputList(machine)) {
                StringBuilder builder = new StringBuilder();
                int i = 0;

                for (ItemStack item : inputs) {
                    if (i > 0) {
                        builder.append(" </slot> ");
                    }

                    builder.append(CustomItemSerializer.serialize(item, ItemFlag.MATERIAL, ItemFlag.ITEMMETA_DISPLAY_NAME, ItemFlag.ITEMMETA_LORE));

                    i++;
                }

                SlimefunPlugin.getRegistry().getAutomatedCraftingChamberRecipes().put(builder.toString(), RecipeType.getRecipeOutputList(machine, inputs));
            }

        }
    }

    private static void loadOreGrinderRecipes() {
        List<ItemStack[]> grinderRecipes = new ArrayList<>();

        GrindStone grinder = (GrindStone) SlimefunItems.GRIND_STONE.getItem();
        if (grinder != null) {
            ItemStack[] input = null;

            for (ItemStack[] recipe : grinder.getRecipes()) {
                if (input == null) {
                    input = recipe;
                } else {
                    if (input[0] != null && recipe[0] != null) {
                        grinderRecipes.add(new ItemStack[]{input[0], recipe[0]});
                    }

                    input = null;
                }
            }
        }

        OreCrusher crusher = (OreCrusher) SlimefunItems.ORE_CRUSHER.getItem();
        if (crusher != null) {
            ItemStack[] input = null;

            for (ItemStack[] recipe : crusher.getRecipes()) {
                if (input == null) {
                    input = recipe;
                } else {
                    if (input[0] != null && recipe[0] != null) {
                        grinderRecipes.add(new ItemStack[]{input[0], recipe[0]});
                    }
                    input = null;
                }
            }
        }

        // Favour 8 Cobblestone -> 1 Sand Recipe over 1 Cobblestone -> 1 Gravel Recipe
        Stream<ItemStack[]> stream = grinderRecipes.stream();

        if (!SlimefunPlugin.getCfg().getBoolean("options.legacy-ore-grinder")) {
            stream = stream.sorted((a, b) -> Integer.compare(b[0].getAmount(), a[0].getAmount()));
        }

        stream.forEach(recipe -> registerMachineRecipe("ELECTRIC_ORE_GRINDER", 4, new ItemStack[]{recipe[0]}, new ItemStack[]{recipe[1]}));
    }

    private static void loadSmelteryRecipes() {
        Smeltery smeltery = (Smeltery) SlimefunItems.SMELTERY.getItem();
        if (smeltery != null) {
            ItemStack[] input = null;

            for (ItemStack[] recipe : smeltery.getRecipes()) {
                if (input == null) {
                    input = recipe;
                } else {
                    if (input[0] != null && recipe[0] != null) {
                        List<ItemStack> inputs = new ArrayList<>();

                        for (ItemStack item : input) {
                            if (item != null) {
                                inputs.add(item);
                            }
                        }

                        // We want to exclude Dust to Ingot Recipes
                        if (inputs.size() == 1 && isDust(inputs.get(0))) {
                            ((MakeshiftSmeltery) SlimefunItems.MAKESHIFT_SMELTERY.getItem()).addRecipe(new ItemStack[]{inputs.get(0)}, recipe[0]);

                            registerMachineRecipe("ELECTRIC_INGOT_FACTORY", 8, new ItemStack[]{inputs.get(0)}, new ItemStack[]{recipe[0]});
                            registerMachineRecipe("ELECTRIC_INGOT_PULVERIZER", 3, new ItemStack[]{recipe[0]}, new ItemStack[]{inputs.get(0)});
                        } else {
                            registerMachineRecipe("ELECTRIC_SMELTERY", 12, inputs.toArray(new ItemStack[0]), new ItemStack[]{recipe[0]});
                        }
                    }

                    input = null;
                }
            }
        }
    }

    private static boolean isDust(ItemStack item) {
        SlimefunItem sfItem = SlimefunItem.getByItem(item);
        return sfItem != null && sfItem.getID().endsWith("_DUST");
    }

    private static void registerMachineRecipe(String machine, int seconds, ItemStack[] input, ItemStack[] output) {
        for (SlimefunItem item : SlimefunPlugin.getRegistry().getEnabledSlimefunItems()) {
            if (item instanceof AContainer && ((AContainer) item).getMachineIdentifier().equals(machine)) {
                ((AContainer) item).registerRecipe(seconds, input, output);
            }
        }
    }
}