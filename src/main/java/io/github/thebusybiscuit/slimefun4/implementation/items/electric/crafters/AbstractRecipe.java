package io.github.thebusybiscuit.slimefun4.implementation.items.electric.crafters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class AbstractRecipe {

    private final Collection<Predicate<ItemStack>> inputs;
    private final ItemStack result;

    @ParametersAreNonnullByDefault
    public AbstractRecipe(Collection<Predicate<ItemStack>> inputs, ItemStack result) {
        Validate.notEmpty(inputs, "The input predicates cannot be null or an empty array");
        Validate.notNull(result, "The recipe result must not be null!");

        this.inputs = inputs;
        this.result = result;
    }

    public AbstractRecipe(@Nonnull ShapelessRecipe recipe) {
        this(new ArrayList<>(recipe.getChoiceList()), recipe.getResult());
    }

    public AbstractRecipe(@Nonnull ShapedRecipe recipe) {
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
    public Collection<Predicate<ItemStack>> getInputs() {
        return inputs;
    }

    @Nonnull
    public ItemStack getResult() {
        return result;
    }

}
