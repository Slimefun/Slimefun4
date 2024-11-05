package io.github.thebusybiscuit.slimefun4.api.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.recipes.items.AbstractRecipeInputItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.AbstractRecipeOutputItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeInputGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeInputItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeInputItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeInputSlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeInputTag;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeOutputItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeOutputItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeOutputSlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeOutputTag;
import io.github.thebusybiscuit.slimefun4.api.recipes.matching.MatchProcedure;
import io.github.thebusybiscuit.slimefun4.core.services.RecipeService;

public class RecipeBuilder {

    private final List<AbstractRecipeInputItem> inputItems = new ArrayList<>();
    private final List<AbstractRecipeOutputItem> outputItems = new ArrayList<>();
    private final List<RecipeType> types = new ArrayList<>();
    private final List<String> permissionNodes = new ArrayList<>();
    private MatchProcedure match = MatchProcedure.DUMMY;
    private BiFunction<List<AbstractRecipeInputItem>, MatchProcedure, AbstractRecipeInput> inputGenerator = RecipeInput::new;
    private Function<List<AbstractRecipeOutputItem>, AbstractRecipeOutput> outputGenerator = RecipeOutput::new;
    private Optional<Integer> energy = Optional.empty();
    private Optional<Integer> craftingTime = Optional.empty();
    private Optional<String> id = Optional.empty();
    private String filename = RecipeService.SAVED_RECIPE_DIR + "other_recipes.json";
    
    public RecipeBuilder() {}

    public Recipe build() {
        return new Recipe(
            id,
            filename,
            inputGenerator.apply(inputItems, match),
            outputGenerator.apply(outputItems),
            types,
            energy,
            craftingTime,
            permissionNodes
        );
    }

    public RecipeBuilder i(AbstractRecipeInputItem i) {
        inputItems.add(i);
        return this;
    }

    // Forwards arguments to the respective ctors
    public RecipeBuilder i(ItemStack item, int amount, int durabilityCost) { return i(RecipeInputItem.fromItemStack(item, amount, durabilityCost)); }
    public RecipeBuilder i(ItemStack item, int amount) { return i(RecipeInputItem.fromItemStack(item, amount)); }
    public RecipeBuilder i(ItemStack item) { return i(RecipeInputItem.fromItemStack(item)); }
    public RecipeBuilder i(Material mat, int amount, int durabilityCost) { return i(new RecipeInputItemStack(mat, amount, durabilityCost)); }
    public RecipeBuilder i(Material mat, int amount) { return i(new RecipeInputItemStack(mat, amount)); }
    public RecipeBuilder i(Material mat) { return i(new RecipeInputItemStack(mat)); }
    public RecipeBuilder i(String id, int amount, int durabilityCost) { return i(new RecipeInputSlimefunItem(id, amount, durabilityCost)); }
    public RecipeBuilder i(String id, int amount) { return i(new RecipeInputSlimefunItem(id, amount)); }
    public RecipeBuilder i(String id) { return i(new RecipeInputSlimefunItem(id)); }
    public RecipeBuilder i(Tag<Material> id, int amount, int durabilityCost) { return i(new RecipeInputTag(id, amount, durabilityCost)); }
    public RecipeBuilder i(Tag<Material> id, int amount) { return i(new RecipeInputTag(id, amount)); }
    public RecipeBuilder i(Tag<Material> id) { return i(new RecipeInputTag(id)); }
    public RecipeBuilder i(List<AbstractRecipeInputItem> group) { return i(new RecipeInputGroup(group)); }

    public RecipeBuilder i() {
        return i(RecipeInputItem.EMPTY);
    }

    public RecipeBuilder i(int amount) {
        for (int i = 0; i < amount; i++) {
            i(RecipeInputItem.EMPTY);
        }
        return this;
    }


    public RecipeBuilder inputGenerator(BiFunction<List<AbstractRecipeInputItem>, MatchProcedure, AbstractRecipeInput> generator) {
        this.inputGenerator = generator;
        return this;
    }

    public RecipeBuilder o(AbstractRecipeOutputItem o) {
        outputItems.add(o);
        return this;
    }

    public RecipeBuilder o(ItemStack item, int amount) { return o(RecipeOutputItem.fromItemStack(item, amount)); }
    public RecipeBuilder o(ItemStack item) { return o(RecipeOutputItem.fromItemStack(item)); }
    public RecipeBuilder o(Material item, int amount) { return o(new RecipeOutputItemStack(item, amount)); }
    public RecipeBuilder o(Material item) { return o(new RecipeOutputItemStack(item)); }
    public RecipeBuilder o(String id, int amount) { return o(new RecipeOutputSlimefunItem(id, amount)); }
    public RecipeBuilder o(String id) { return o(new RecipeOutputSlimefunItem(id)); }
    public RecipeBuilder o(Tag<Material> id, int amount) { return o(new RecipeOutputTag(id, amount)); }
    public RecipeBuilder o(Tag<Material> id) { return o(new RecipeOutputTag(id)); }
    
    public RecipeBuilder o() {
        return o(RecipeOutputItem.EMPTY);
    }

    public RecipeBuilder outputGenerator(Function<List<AbstractRecipeOutputItem>, AbstractRecipeOutput> generator) {
        this.outputGenerator = generator;
        return this;
    }

    public RecipeBuilder t(RecipeType t) {
        types.add(t);
        return this;
    }

    public RecipeBuilder permission(String p) {
        permissionNodes.add(p);
        return this;
    }

    public RecipeBuilder energy(int energy) {
        this.energy = Optional.of(energy);
        return this;
    }

    public RecipeBuilder craftingTime(int ticks) {
        this.craftingTime = Optional.of(ticks);
        return this;
    }

    public RecipeBuilder id(String id) {
        this.id = Optional.of(id);
        return this;
    }

    public RecipeBuilder filename(String filename) {
        this.filename = RecipeService.SAVED_RECIPE_DIR + filename;
        return this;
    }

}
