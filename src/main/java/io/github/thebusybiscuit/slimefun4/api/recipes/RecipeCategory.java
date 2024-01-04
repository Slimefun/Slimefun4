package io.github.thebusybiscuit.slimefun4.api.recipes;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

public class RecipeCategory implements Keyed {

    private final ItemStack displayItem;
    private final NamespacedKey key;
    private final RecipeStructure defaultStructure;

    public RecipeCategory(ItemStack displayItem, NamespacedKey key, RecipeStructure defaultStructure) {
        this.displayItem = displayItem;
        this.key = key;
        this.defaultStructure = defaultStructure;
    }

    RecipeCategory(ItemStack displayItem, String key, RecipeStructure defaultStructure) {
        this(displayItem, new NamespacedKey(Slimefun.instance(), key), defaultStructure);
    }

    public RecipeCategory(ItemStack displayItem, NamespacedKey key) {
        this(displayItem, key, RecipeStructure.SHAPED);
    }

    RecipeCategory(ItemStack displayItem, String key) {
        this(displayItem, key, RecipeStructure.SHAPED);
    }

    public void registerRecipes(@Nonnull List<Recipe> recipes) {
        Slimefun.getSlimefunRecipeService().registerRecipes(this, recipes);
    }

    public void registerRecipes(@Nonnull Recipe recipes) {
        Slimefun.getSlimefunRecipeService().registerRecipes(this, List.of(recipes));
    }

    /**
     * This can be overriden if a specific category should require it
     * 
     * @param recipe The recipe being registered using
     *               {@code Slimefun.registerRecipes()}
     */
    public void onRegisterRecipe(Recipe recipe) {
    }

    /**
     * For backwards compat (namely SlimefunItem.getRecipeType()).
     * To be removed when RecipeType is removed
     */
    @Deprecated
    public RecipeType asRecipeType() {
        return new RecipeType(key, displayItem);
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    public RecipeStructure getDefaultStructure() {
        return defaultStructure;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof final RecipeCategory other) {
            return other.getKey().equals(getKey());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

    @ParametersAreNonnullByDefault
    private static void registerBarterDrop(ItemStack[] recipe, ItemStack output) {
        Slimefun.getRegistry().getBarteringDrops().add(output);
    }

    @ParametersAreNonnullByDefault
    private static void registerMobDrop(ItemStack[] recipe, ItemStack output) {
        String mob = ChatColor.stripColor(recipe[4].getItemMeta().getDisplayName()).toUpperCase(Locale.ROOT)
                .replace(' ', '_');
        EntityType entity = EntityType.valueOf(mob);
        Set<ItemStack> dropping = Slimefun.getRegistry().getMobDrops().getOrDefault(entity, new HashSet<>());
        dropping.add(output);
        Slimefun.getRegistry().getMobDrops().put(entity, dropping);
    }

}
