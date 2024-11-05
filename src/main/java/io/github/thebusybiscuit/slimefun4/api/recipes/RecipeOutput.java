package io.github.thebusybiscuit.slimefun4.api.recipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import io.github.thebusybiscuit.slimefun4.api.recipes.items.AbstractRecipeOutputItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeOutputItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.AbstractRecipeOutputItem.SpaceRequirement;
import io.github.thebusybiscuit.slimefun4.api.recipes.matching.RecipeMatchResult;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

public class RecipeOutput extends AbstractRecipeOutput {

    public static final AbstractRecipeOutput EMPTY = new AbstractRecipeOutput() {

        @Override
        public Inserter checkSpace(RecipeMatchResult result, Inventory inventory, int[] outputSlots) {
            return new Inserter(inventory);
        }

        @Override
        public List<ItemStack> generateOutput(RecipeMatchResult result) {
            return List.of();
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public String toString() {
            return "Empty Recipe Output";
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this;
        }

        @Override
        public JsonElement serialize(JsonSerializationContext context) {
            return new JsonObject();
        }
        
    };

    private List<AbstractRecipeOutputItem> items;

    public RecipeOutput(List<AbstractRecipeOutputItem> items) {
        this.items = items;
    }

    public static RecipeOutput fromItemStacks(ItemStack[] items) {
        return new RecipeOutput(Arrays.stream(items).map(item -> RecipeOutputItem.fromItemStack(item)).toList());
    }

    public List<AbstractRecipeOutputItem> getItems() {
        return this.items;
    }
    public AbstractRecipeOutputItem getItem(int index) {
        return this.items.get(index);
    }

    public List<ItemStack> generateOutput(RecipeMatchResult result) {
        return items.stream().map(i -> i.generateOutput(result)).toList();
    }

    @Override
    public Inserter checkSpace(RecipeMatchResult result, Inventory inventory, int[] outputSlots) {
        // Check all outputs slots to see if they are empty
        List<Integer> freeSlots = new LinkedList<>();
        List<Integer> filledSlots = new ArrayList<>();
        for (int i : outputSlots) {
            ItemStack item = inventory.getItem(i);
            if (item == null || item.getType().isAir()) {
                freeSlots.add(i);
            } else {
                filledSlots.add(i);
            }
        }

        // Go through each output and check if there is space, if none, then put it into the leftovers
        Map<Integer, Integer> addToStacks = new HashMap<>();
        Map<Integer, ItemStack> newStacks = new HashMap<>();
        List<ItemStack> leftovers = new ArrayList<>();
        for (AbstractRecipeOutputItem output : items) {
            ItemStack outputStack = output.generateOutput(result);
            if (output.getSpaceRequirement() == SpaceRequirement.EMPTY_SLOT) {
                if (freeSlots.isEmpty()) {
                    leftovers.add(outputStack);
                } else {
                    int newSlot = freeSlots.removeFirst();
                    newStacks.put(newSlot, outputStack);
                }
            } else {
                // Search for matching item
                int amount = outputStack.getAmount();
                int stackSize = outputStack.getType().getMaxStackSize(); // TODO item components
                for (int i : filledSlots) {
                    if (amount <= 0) break;
                    ItemStack filledItem = inventory.getItem(i);
                    if (!SlimefunUtils.isItemSimilar(filledItem, outputStack, true, false)) {
                        continue;
                    }
                    int filledAmount = filledItem.getAmount();
                    int currentAdd = addToStacks.getOrDefault(i, 0);
                    if (filledAmount + currentAdd >= stackSize) {
                        continue;
                    } else if (filledAmount + currentAdd + amount > stackSize) {
                        int diff = stackSize - filledAmount - currentAdd;
                        amount -= diff;
                        addToStacks.put(i, diff + currentAdd);
                    } else {
                        addToStacks.put(i, amount + currentAdd);
                        amount = 0;
                    }
                }
                if (amount > 0) {
                    outputStack.setAmount(amount);
                    if (freeSlots.isEmpty()) {
                        leftovers.add(outputStack);
                    } else {
                        int newSlot = freeSlots.removeFirst();
                        newStacks.put(newSlot, outputStack);
                    }
                }
            }
        }
        return new Inserter(inventory, addToStacks, newStacks, leftovers);
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public String toString() {
        return "RecipeOutput { " + items + " }";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        RecipeOutput output = (RecipeOutput) obj;
        return output.items.equals(items);
    }

    @Override
    public JsonElement serialize(JsonSerializationContext context) {
        JsonObject output = new JsonObject();
        JsonArray arr = new JsonArray();
        for (AbstractRecipeOutputItem item : items) {
            arr.add(context.serialize(item, AbstractRecipeOutputItem.class));
        }
        output.add("items", arr);
        return output;
    }
    
}
