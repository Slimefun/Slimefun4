package me.mrCookieSlime.Slimefun.Setup;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.items.VanillaItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.SlimefunArmorPiece;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public final class SlimefunManager {

    private SlimefunManager() {
    }

    public static void registerArmorSet(ItemStack baseComponent, ItemStack[] items, String idSyntax, PotionEffect[][] effects, boolean magical, SlimefunAddon addon) {
        String[] components = new String[]{"_HELMET", "_CHESTPLATE", "_LEGGINGS", "_BOOTS"};
        Category category = magical ? Categories.MAGIC_ARMOR : Categories.ARMOR;
        List<ItemStack[]> recipes = new ArrayList<>();

        recipes.add(new ItemStack[]{baseComponent, baseComponent, baseComponent, baseComponent, null, baseComponent, null, null, null});
        recipes.add(new ItemStack[]{baseComponent, null, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent});
        recipes.add(new ItemStack[]{baseComponent, baseComponent, baseComponent, baseComponent, null, baseComponent, baseComponent, null, baseComponent});
        recipes.add(new ItemStack[]{null, null, null, baseComponent, null, baseComponent, baseComponent, null, baseComponent});

        for (int i = 0; i < 4; i++) {
            if (i < effects.length && effects[i].length > 0) {
                new SlimefunArmorPiece(category, new SlimefunItemStack(idSyntax + components[i], items[i]), RecipeType.ARMOR_FORGE, recipes.get(i), effects[i]).register(addon);
            } else {
                new SlimefunItem(category, new SlimefunItemStack(idSyntax + components[i], items[i]), RecipeType.ARMOR_FORGE, recipes.get(i)).register(addon);
            }
        }
    }

    public static void registerArmorSet(ItemStack baseComponent, ItemStack[] items, String idSyntax, boolean vanilla, SlimefunAddon addon) {
        String[] components = new String[]{"_HELMET", "_CHESTPLATE", "_LEGGINGS", "_BOOTS"};
        Category cat = Categories.ARMOR;
        List<ItemStack[]> recipes = new ArrayList<>();
        recipes.add(new ItemStack[]{baseComponent, baseComponent, baseComponent, baseComponent, null, baseComponent, null, null, null});
        recipes.add(new ItemStack[]{baseComponent, null, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent, baseComponent});
        recipes.add(new ItemStack[]{baseComponent, baseComponent, baseComponent, baseComponent, null, baseComponent, baseComponent, null, baseComponent});
        recipes.add(new ItemStack[]{null, null, null, baseComponent, null, baseComponent, baseComponent, null, baseComponent});

        for (int i = 0; i < 4; i++) {
            if (vanilla) {
                new VanillaItem(cat, items[i], idSyntax + components[i], RecipeType.ARMOR_FORGE, recipes.get(i)).register(addon);
            } else {
                new SlimefunItem(cat, new SlimefunItemStack(idSyntax + components[i], items[i]), RecipeType.ARMOR_FORGE, recipes.get(i)).register(addon);
            }
        }
    }

    /**
     * Checks if two items are similar.
     *
     * @param item      the item
     * @param sfitem    the other item
     * @param checkLore Whether to compare lore
     * @return Whether the items are similar
     * @deprecated Use {@link SlimefunUtils#isItemSimilar(ItemStack, ItemStack, boolean)} instead
     */
    @Deprecated
    public static boolean isItemSimilar(ItemStack item, ItemStack sfitem, boolean checkLore) {
        return SlimefunUtils.isItemSimilar(item, sfitem, checkLore);
    }

    /**
     * Checks if the Inventory has a similar item
     *
     * @param inventory the Inventory
     * @param itemStack the item
     * @param checkLore Whether to compare lore
     * @return Whether here is such an item
     * @deprecated Use {@link SlimefunUtils#containsSimilarItem(Inventory, ItemStack, boolean)}
     */
    @Deprecated
    public static boolean containsSimilarItem(Inventory inventory, ItemStack itemStack, boolean checkLore) {
        return SlimefunUtils.containsSimilarItem(inventory, itemStack, checkLore);
    }

    /**
     * Checks if an Item is soulbound.
     *
     * @param item The {@link ItemStack} to check
     * @return Whether it is soulbound
     * @deprecated Use {@link SlimefunUtils#isSoulbound(ItemStack)} instead.
     */
    @Deprecated
    public static boolean isItemSoulbound(ItemStack item) {
        return SlimefunUtils.isSoulbound(item);
    }
}