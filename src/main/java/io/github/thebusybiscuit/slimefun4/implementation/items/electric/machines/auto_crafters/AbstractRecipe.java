package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.auto_crafters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
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

    @Nullable
    public static AbstractRecipe of(@Nullable Recipe recipe) {
        if (recipe instanceof ShapedRecipe) {
            return new AbstractRecipe((ShapedRecipe) recipe) {

                @Override
                public void show(@Nonnull Inventory inv) {
                    // TODO Implement Recipe Preview

                }

            };
        } else if (recipe instanceof ShapelessRecipe) {
            return new AbstractRecipe((ShapelessRecipe) recipe) {

                @Override
                public void show(@Nonnull Inventory inv) {
                    // TODO Implement Recipe Preview
                }

            };
        } else {
            return null;
        }
    }

    @Nullable
    public static AbstractRecipe of(@Nonnull SlimefunItem item) {
        return new AbstractRecipe(item) {

            @Override
            public void show(@Nonnull Inventory inv) {
                // TODO Implement Recipe Preview
            }
        };
    }

    private AbstractRecipe(@Nonnull SlimefunItem item) {
        Validate.notNull(item, "The SlimefunItem should not be null!");
        Validate.isTrue(item.getRecipeType().equals(RecipeType.ENHANCED_CRAFTING_TABLE), "The item must be crafted in an enhanced crafting table!");

        result = item.getRecipeOutput();
        inputs = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            ItemStack ingredient = item.getRecipe()[i];
            inputs.add(stack -> SlimefunUtils.isItemSimilar(stack, ingredient, true));
        }
    }

    private AbstractRecipe(@Nonnull ShapelessRecipe recipe) {
        this(new ArrayList<>(recipe.getChoiceList()), recipe.getResult());
    }

    private AbstractRecipe(@Nonnull ShapedRecipe recipe) {
        this(getChoices(recipe), recipe.getResult());
    }

    @Nonnull
    private static Collection<Predicate<ItemStack>> getChoices(@Nonnull ShapedRecipe recipe) {
        List<Predicate<ItemStack>> choices = new ArrayList<>();

        for (String row : recipe.getShape()) {
            for (char c : row.toCharArray()) {
                RecipeChoice choice = recipe.getChoiceMap().get(c);

                if (choice != null) {
                    choices.add(choice);
                }
            }
        }

        return choices;
    }

    @Nonnull
    private static RecipeChoice[] getShape(@Nonnull Recipe recipe) {
        return SlimefunPlugin.getMinecraftRecipeService().getRecipeShape(recipe);
    }

    @Nonnull
    public Collection<Predicate<ItemStack>> getInputs() {
        return inputs;
    }

    @Nonnull
    public ItemStack getResult() {
        return result;
    }

    public abstract void show(@Nonnull Inventory inv);

}
