package io.github.thebusybiscuit.slimefun4.api.recipes.items;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import io.github.thebusybiscuit.slimefun4.api.recipes.matching.RecipeMatchResult;

public class RecipeOutputTag extends RecipeOutputItem {

    private Tag<Material> tag;

    public RecipeOutputTag(Tag<Material> tag, int amount, double chance) {
        super(amount, chance);
        this.tag = tag;
    }

    public RecipeOutputTag(Tag<Material> tag, int amount) {
        this(tag, amount, 1);
    }

    public RecipeOutputTag(Tag<Material> tag) {
        this(tag, 1);
    }

    public Tag<Material> getTag() { return tag; }
    public void setTag(Tag<Material> tag) { this.tag = tag; }

    @Override
    public boolean matchItem(ItemStack item) {
        return false;
    }

    @Override
    public ItemStack generateOutput(RecipeMatchResult result) {
        Material[] arr = tag.getValues().toArray(Material[]::new);
        int i = ThreadLocalRandom.current().nextInt(arr.length);
        return new ItemStack(arr[i], getAmount());
    }

    @Override
    public String toString() {
        return "ROTag { " + tag + ", " + super.toString() + " }";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        
        RecipeOutputTag item = (RecipeOutputTag) obj;
        return item.tag.getKey().equals(tag.getKey()) &&
            item.getAmount() == getAmount();
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + tag.getKey().hashCode();
        hash = 31 * hash + getAmount();
        return hash;
    }

    @Override
    public JsonElement serialize(JsonSerializationContext context) {
        if (canUseShortSerialization()) {
            return new JsonPrimitive("#" + tag.getKey() + (getAmount() != 1 ? "|" + getAmount() : ""));
        }

        JsonObject item = new JsonObject();
        item.addProperty("tag", tag.getKey().toString());
        if (getAmount() != 1) {
            item.addProperty("amount", getAmount());
        }
        return item;
    }
    
}
