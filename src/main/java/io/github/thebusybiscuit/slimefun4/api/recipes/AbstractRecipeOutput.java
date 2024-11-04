package io.github.thebusybiscuit.slimefun4.api.recipes;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;

import io.github.thebusybiscuit.slimefun4.api.recipes.matching.RecipeMatchResult;

public abstract class AbstractRecipeOutput {

    // TODO find a better name
    public static class Inserter {
        private final Inventory inv;
        private final Map<Integer, Integer> addToStacks;
        private final Map<Integer, ItemStack> newStacks;
        private final List<ItemStack> leftovers;

        public Inserter(Inventory inv, Map<Integer, Integer> addToStacks, Map<Integer, ItemStack> newStacks, List<ItemStack> leftovers) {
            this.inv = inv;
            this.addToStacks = addToStacks;
            this.newStacks = newStacks;
            this.leftovers = leftovers;
        }

        public Inserter(Inventory inv) {
            this.inv = inv;
            this.addToStacks = Collections.emptyMap();
            this.newStacks = Collections.emptyMap();
            this.leftovers = Collections.emptyList();
        }

        public void insert() {
            for (Map.Entry<Integer, Integer> entry : addToStacks.entrySet()) {
                ItemStack item = inv.getItem(entry.getKey());
                item.setAmount(item.getAmount() + entry.getValue());
            }
            for (Map.Entry<Integer, ItemStack> entry : newStacks.entrySet()) {
                inv.setItem(entry.getKey(), entry.getValue());
            }
        }

        public List<ItemStack> getLeftovers() {
            return Collections.unmodifiableList(leftovers);
        }
    }

    @ParametersAreNonnullByDefault
    public abstract Inserter checkSpace(RecipeMatchResult matchResult, Inventory inventory, int[] outputSlots);

    @Nonnull
    @ParametersAreNonnullByDefault
    public List<ItemStack> insertIntoInventory(RecipeMatchResult matchResult, Inventory inventory, int[] outputSlots) {
        Inserter inserter = checkSpace(matchResult, inventory, outputSlots);
        inserter.insert();
        return inserter.getLeftovers();
    }
    
    @Nonnull
    public abstract List<ItemStack> generateOutput(@Nonnull RecipeMatchResult result);

    public abstract boolean isEmpty();

    @Override
    public abstract String toString();

    @Override
    public abstract boolean equals(Object obj);

    public abstract JsonElement serialize(JsonSerializationContext context);
    
}
