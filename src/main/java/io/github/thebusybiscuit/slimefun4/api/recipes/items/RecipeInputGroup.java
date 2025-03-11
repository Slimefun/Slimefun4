package io.github.thebusybiscuit.slimefun4.api.recipes.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import io.github.thebusybiscuit.slimefun4.api.recipes.matching.ItemMatchResult;

public class RecipeInputGroup extends AbstractRecipeInputItem {

    List<AbstractRecipeInputItem> items;

    public RecipeInputGroup(List<AbstractRecipeInputItem> items) {
        this.items = items;
    }

    public RecipeInputGroup(AbstractRecipeInputItem... items) {
        this.items = List.of(items);
    }

    public List<AbstractRecipeInputItem> getItems() { return items; }
    public void setItems(List<AbstractRecipeInputItem> items) { this.items = items; }

    @Override
    public ItemMatchResult matchItem(ItemStack item, AbstractRecipeInputItem root) {
        for (AbstractRecipeInputItem i : items) {
            ItemMatchResult result = i.matchItem(item, root);
            if (result.itemsMatch()) {
                return result;
            }
        }
        return new ItemMatchResult(false, root, item, 0);
    }

    @Override
    public ItemStack getItemDisplay() {
        // TODO guide display overhaul
        if (items.size() == 0) {
            return new ItemStack(Material.AIR);
        }
        return items.get(0).getItemDisplay();
    }

    @Override
    public boolean isEmpty() {
        return items.size() == 0 || items.stream().allMatch(i -> i.isEmpty());
    }

    @Override
    public RecipeInputGroup clone() {
        List<AbstractRecipeInputItem> items = new ArrayList<>();
        for (AbstractRecipeInputItem item : this.items) {
            items.add(item.clone());
        }
        return new RecipeInputGroup(items);
    }

    @Override
    public String toString() {
        return "RIGroup { " + items + " }";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        
        RecipeInputGroup item = (RecipeInputGroup) obj;
        if (item.items.size() != items.size()) return false;
        for (int i = 0; i < items.size(); i++) {
            if (!item.items.get(i).equals(items.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }

    @Override
    public JsonElement serialize(JsonSerializationContext context) {
        JsonObject group = new JsonObject();
        JsonArray arr = new JsonArray(items.size());
        for (AbstractRecipeInputItem item : items) {
            arr.add(context.serialize(item, AbstractRecipeInputItem.class));
        }
        group.add("group", arr);
        return group;
    }
    
}
