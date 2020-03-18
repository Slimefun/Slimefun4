package io.github.thebusybiscuit.slimefun4.implementation.setup;

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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.AutomatedCraftingChamber;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.EnhancedCraftingTable;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.GrindStone;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.MakeshiftSmeltery;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.OreCrusher;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.Smeltery;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItemSerializer;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItemSerializer.ItemFlag;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public final class PostSetup {

    private PostSetup() {}

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
        }
        catch (IOException e) {
            Slimefun.getLogger().log(Level.SEVERE, "Failed to load wiki.json file", e);
        }
    }

    public static void setupItemSettings() {
        for (World world : Bukkit.getWorlds()) {
            SlimefunPlugin.getWhitelist().setDefaultValue(world.getName() + ".enabled-items.SLIMEFUN_GUIDE", true);
        }

        Slimefun.setItemVariable("ORE_CRUSHER", "double-ores", true);

        for (Enchantment enchantment : Enchantment.values()) {
            for (int i = 1; i <= enchantment.getMaxLevel(); i++) {
                Slimefun.setItemVariable("MAGICIAN_TALISMAN", "allow-enchantments." + enchantment.getKey().getKey() + ".level." + i, true);
            }
        }
    }

    public static void loadItems() {
        Iterator<SlimefunItem> iterator = SlimefunPlugin.getRegistry().getEnabledSlimefunItems().iterator();

        while (iterator.hasNext()) {
            SlimefunItem item = iterator.next();

            if (item == null) {
                Slimefun.getLogger().log(Level.WARNING, "Removed bugged Item ('NULL?')");
                iterator.remove();
            }
            else {
                item.load();
            }
        }

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

        List<ItemStack[]> grinderRecipes = new ArrayList<>();

        GrindStone grinder = (GrindStone) SlimefunItems.GRIND_STONE.getItem();
        if (grinder != null) {
            ItemStack[] input = null;

            for (ItemStack[] recipe : grinder.getRecipes()) {
                if (input == null) {
                    input = recipe;
                }
                else {
                    if (input[0] != null && recipe[0] != null) {
                        grinderRecipes.add(new ItemStack[] { input[0], recipe[0] });
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
                }
                else {
                    if (input[0] != null && recipe[0] != null) {
                        grinderRecipes.add(new ItemStack[] { input[0], recipe[0] });
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

        stream.forEach(recipe -> registerMachineRecipe("ELECTRIC_ORE_GRINDER", 4, new ItemStack[] { recipe[0] }, new ItemStack[] { recipe[1] }));

        Smeltery smeltery = (Smeltery) SlimefunItems.SMELTERY.getItem();
        if (smeltery != null) {
            ItemStack[] input = null;

            for (ItemStack[] recipe : smeltery.getRecipes()) {
                if (input == null) {
                    input = recipe;
                }
                else {
                    if (input[0] != null && recipe[0] != null) {
                        List<ItemStack> inputs = new ArrayList<>();

                        for (ItemStack item : input) {
                            if (item != null) {
                                inputs.add(item);
                            }
                        }

                        // We want to exclude Dust to Ingot Recipes
                        if (inputs.size() == 1 && isDust(inputs.get(0))) {
                            ((MakeshiftSmeltery) SlimefunItems.MAKESHIFT_SMELTERY.getItem()).addRecipe(new ItemStack[] { inputs.get(0) }, recipe[0]);

                            registerMachineRecipe("ELECTRIC_INGOT_FACTORY", 8, new ItemStack[] { inputs.get(0) }, new ItemStack[] { recipe[0] });
                            registerMachineRecipe("ELECTRIC_INGOT_PULVERIZER", 3, new ItemStack[] { recipe[0] }, new ItemStack[] { inputs.get(0) });
                        }
                        else {
                            registerMachineRecipe("ELECTRIC_SMELTERY", 12, inputs.toArray(new ItemStack[0]), new ItemStack[] { recipe[0] });
                        }
                    }

                    input = null;
                }
            }
        }

        CommandSender sender = Bukkit.getConsoleSender();

        int total = SlimefunPlugin.getRegistry().getEnabledSlimefunItems().size();
        int vanilla = SlimefunPlugin.getRegistry().countVanillaItems();

        sender.sendMessage("");
        sender.sendMessage(ChatColor.GREEN + "######################### - Slimefun v" + SlimefunPlugin.getVersion() + " - #########################");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GREEN + "Successfully loaded " + total + " Items and " + SlimefunPlugin.getRegistry().getResearches().size() + " Researches");
        sender.sendMessage(ChatColor.GREEN + "( " + vanilla + " Items from Slimefun, " + (total - vanilla) + " Items from " + SlimefunPlugin.getInstalledAddons().size() + " Addons )");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GREEN + "Slimefun is an Open-Source project that is maintained by community developers!");

        if (SlimefunPlugin.getUpdater().getBranch().isOfficial()) {
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GREEN + " -- Source Code:   https://github.com/TheBusyBiscuit/Slimefun4");
            sender.sendMessage(ChatColor.GREEN + " -- Wiki:          https://github.com/TheBusyBiscuit/Slimefun4/wiki");
            sender.sendMessage(ChatColor.GREEN + " -- Addons:        https://github.com/TheBusyBiscuit/Slimefun4/wiki/Addons");
            sender.sendMessage(ChatColor.GREEN + " -- Bug Reports:   https://github.com/TheBusyBiscuit/Slimefun4/issues");
            sender.sendMessage(ChatColor.GREEN + " -- Discord:       https://discord.gg/fsD4Bkh");
        }
        else {
            sender.sendMessage(ChatColor.GREEN + " -- UNOFFICIALLY MODIFIED BUILD - NO OFFICIAL SUPPORT GIVEN");
        }

        sender.sendMessage("");

        SlimefunPlugin.getItemCfg().save();
        SlimefunPlugin.getResearchCfg().save();
        SlimefunPlugin.getWhitelist().save();
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
