package io.github.thebusybiscuit.slimefun4.api.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nonnull;

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

public class RecipeBuilder {

    @FunctionalInterface
    static interface InputGenerator {
        AbstractRecipeInput create(List<AbstractRecipeInputItem> inputs, MatchProcedure match, int width, int height);
    }

    private final List<AbstractRecipeInputItem> inputItems = new ArrayList<>();
    private final List<AbstractRecipeOutputItem> outputItems = new ArrayList<>();
    private final List<RecipeType> types = new ArrayList<>();
    private final List<String> permissionNodes = new ArrayList<>();
    private MatchProcedure match = null;
    private int width = 3;
    private int height = 3;
    private InputGenerator inputGenerator = RecipeInput::new;
    private Function<List<AbstractRecipeOutputItem>, AbstractRecipeOutput> outputGenerator = RecipeOutput::new;
    private Optional<Integer> energy = Optional.empty();
    private Optional<Integer> craftingTime = Optional.empty();
    private Optional<String> id = Optional.empty();
    private String filename = null;
    
    public RecipeBuilder() {}

    public Recipe build() {
        return new Recipe(
            id,
            filename,
            inputGenerator.create(inputItems, match, width, height),
            outputGenerator.apply(outputItems),
            types,
            energy,
            craftingTime,
            permissionNodes
        );
    }

    public RecipeBuilder i(@Nonnull AbstractRecipeInputItem i) {
        inputItems.add(i);
        return this;
    }

    // Forwards arguments to the respective ctors
    public RecipeBuilder i(ItemStack item, int amount, int durabilityCost) { return i(RecipeInputItem.fromItemStack(item, amount, durabilityCost)); }
    public RecipeBuilder i(ItemStack item, int amount) { return i(RecipeInputItem.fromItemStack(item, amount)); }
    public RecipeBuilder i(ItemStack item) { return i(RecipeInputItem.fromItemStack(item)); }
    public RecipeBuilder i(@Nonnull Material mat, int amount, int durabilityCost) { return i(new RecipeInputItemStack(mat, amount, durabilityCost)); }
    public RecipeBuilder i(@Nonnull Material mat, int amount) { return i(new RecipeInputItemStack(mat, amount)); }
    public RecipeBuilder i(@Nonnull Material mat) { return i(new RecipeInputItemStack(mat)); }
    public RecipeBuilder i(@Nonnull String id, int amount, int durabilityCost) { return i(new RecipeInputSlimefunItem(id, amount, durabilityCost)); }
    public RecipeBuilder i(@Nonnull String id, int amount) { return i(new RecipeInputSlimefunItem(id, amount)); }
    public RecipeBuilder i(@Nonnull String id) { return i(new RecipeInputSlimefunItem(id)); }
    public RecipeBuilder i(@Nonnull Tag<Material> id, int amount, int durabilityCost) { return i(new RecipeInputTag(id, amount, durabilityCost)); }
    public RecipeBuilder i(@Nonnull Tag<Material> id, int amount) { return i(new RecipeInputTag(id, amount)); }
    public RecipeBuilder i(@Nonnull Tag<Material> id) { return i(new RecipeInputTag(id)); }
    public RecipeBuilder i(@Nonnull List<AbstractRecipeInputItem> group) { return i(new RecipeInputGroup(group)); }

    public RecipeBuilder i(@Nonnull ItemStack[] items) {
        for (ItemStack item : items) {
            i(item);
        }
        return this;
    }

    public RecipeBuilder i() {
        return i(RecipeInputItem.EMPTY);
    }

    public RecipeBuilder i(int amount) {
        for (int i = 0; i < amount; i++) {
            i(RecipeInputItem.EMPTY);
        }
        return this;
    }


    public RecipeBuilder inputGenerator(@Nonnull InputGenerator generator) {
        this.inputGenerator = generator;
        return this;
    }

    public RecipeBuilder o(@Nonnull AbstractRecipeOutputItem o) {
        outputItems.add(o);
        return this;
    }

    public RecipeBuilder o(ItemStack item, int amount) { return o(RecipeOutputItem.fromItemStack(item, amount)); }
    public RecipeBuilder o(ItemStack item) { return o(RecipeOutputItem.fromItemStack(item)); }
    public RecipeBuilder o(@Nonnull Material item, int amount) { return o(new RecipeOutputItemStack(item, amount)); }
    public RecipeBuilder o(@Nonnull Material item) { return o(new RecipeOutputItemStack(item)); }
    public RecipeBuilder o(@Nonnull String id, int amount) { return o(new RecipeOutputSlimefunItem(id, amount)); }
    public RecipeBuilder o(@Nonnull String id) { return o(new RecipeOutputSlimefunItem(id)); }
    public RecipeBuilder o(@Nonnull Tag<Material> id, int amount) { return o(new RecipeOutputTag(id, amount)); }
    public RecipeBuilder o(@Nonnull Tag<Material> id) { return o(new RecipeOutputTag(id)); }
    
    public RecipeBuilder o() {
        return o(RecipeOutputItem.EMPTY);
    }

    public RecipeBuilder outputGenerator(@Nonnull Function<List<AbstractRecipeOutputItem>, AbstractRecipeOutput> generator) {
        this.outputGenerator = generator;
        return this;
    }

    public RecipeBuilder type(@Nonnull RecipeType t) {
        types.add(t);
        if (match == null) {
            match = t.getDefaultMatchProcedure();
        }
        return this;
    }

    public RecipeBuilder match(@Nonnull MatchProcedure match) {
        this.match = match;
        return this;
    }

    public RecipeBuilder dim(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public RecipeBuilder permission(@Nonnull String p) {
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

    public RecipeBuilder id(@Nonnull String id) {
        this.id = Optional.of(id);
        if (filename == null){
            filename = id;
        }
        return this;
    }

    public RecipeBuilder filename(@Nonnull String filename) {
        this.filename = filename;
        return this;
    }

}
