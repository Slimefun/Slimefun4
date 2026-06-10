package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import java.util.Collections;
import java.util.Map;

import org.bukkit.inventory.RecipeChoice;

/**
 * Java-8 universal port: {@code ShapedRecipe#getChoiceMap()} (returning {@code Map<Character,
 * RecipeChoice>}) is 1.13+. Reached reflectively, returning an empty map on older servers (which used
 * the ItemStack-based {@code getIngredientMap()} instead).
 */
public final class RecipeCompat {

    private RecipeCompat() {}

    @SuppressWarnings("unchecked")
    public static Map<Character, RecipeChoice> getChoiceMap(Object shapedRecipe) {
        Object map = ReflectionCompat.invoke(shapedRecipe, "getChoiceMap");
        return map instanceof Map ? (Map<Character, RecipeChoice>) map : Collections.emptyMap();
    }

    /**
     * Reflective {@code ShapelessRecipe#getChoiceList()} (1.13+) → {@code List<RecipeChoice>}; empty on
     * older servers (which used the ItemStack-based {@code getIngredientList()}).
     */
    @SuppressWarnings("unchecked")
    public static java.util.List<RecipeChoice> getChoiceList(Object shapelessRecipe) {
        Object list = ReflectionCompat.invoke(shapelessRecipe, "getChoiceList");
        return list instanceof java.util.List ? (java.util.List<RecipeChoice>) list : Collections.emptyList();
    }
}
