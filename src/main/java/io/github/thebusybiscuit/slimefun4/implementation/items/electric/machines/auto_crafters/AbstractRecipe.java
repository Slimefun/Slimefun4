package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.auto_crafters;

import java.util.Collection;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import io.github.thebusybiscuit.slimefun4.implementation.tasks.AsyncRecipeChoiceTask;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public abstract class AbstractRecipe {

    private final Collection<Predicate<ItemStack>> inputs;
    private final ItemStack result;

    @ParametersAreNonnullByDefault
    protected AbstractRecipe(Collection<Predicate<ItemStack>> inputs, ItemStack result) {
        Validate.notEmpty(inputs, "The input predicates cannot be null or an empty array");
        Validate.notNull(result, "The recipe result must not be null!");

        this.inputs = inputs;
        this.result = result;
    }

    @Nonnull
    public Collection<Predicate<ItemStack>> getIngredients() {
        return inputs;
    }

    @Nonnull
    public ItemStack getResult() {
        return result;
    }

    public abstract void show(@Nonnull ChestMenu menu, @Nonnull AsyncRecipeChoiceTask task);

    @Nullable
    public static AbstractRecipe of(@Nullable Recipe recipe) {
        if (recipe instanceof ShapedRecipe) {
            return new VanillaRecipe((ShapedRecipe) recipe);
        } else if (recipe instanceof ShapelessRecipe) {
            return new VanillaRecipe((ShapelessRecipe) recipe);
        } else {
            return null;
        }
    }

    @Nullable
    public static AbstractRecipe of(@Nullable SlimefunItem item) {
        if (item != null && item.getRecipeType().equals(RecipeType.ENHANCED_CRAFTING_TABLE)) {
            return new EnhancedRecipe(item);
        } else {
            return null;
        }
    }

}
