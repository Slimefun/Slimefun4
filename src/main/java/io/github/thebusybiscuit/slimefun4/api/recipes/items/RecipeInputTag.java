package io.github.thebusybiscuit.slimefun4.api.recipes.items;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import io.github.thebusybiscuit.slimefun4.api.recipes.matching.ItemMatchResult;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

public class RecipeInputTag extends RecipeInputItem {

    private Tag<Material> tag;

    public RecipeInputTag(Tag<Material> tag, int amount, int durabilityCost) {
        super(amount, durabilityCost);
        this.tag = tag;
    }

    public RecipeInputTag(Tag<Material> tag, int amount) {
        super(amount);
        this.tag = tag;
    }

    public RecipeInputTag(Tag<Material> tag) {
        this(tag, 1);
    }

    public Tag<Material> getTag() { return tag; }
    public void setTag(Tag<Material> tag) { this.tag = tag; }

    @Override
    public ItemMatchResult matchItem(ItemStack item, AbstractRecipeInputItem root) {
        for (Material mat : tag.getValues()) {
            ItemStack template = new ItemStack(mat);
            if (SlimefunUtils.isItemSimilar(item, template, true)) {
                return new ItemMatchResult(
                    SlimefunUtils.isItemSimilar(item, template, false),
                    root,
                    item
                );
            }
        }
        return new ItemMatchResult(false, root, item);
    }

    @Override
    public boolean isEmpty() {
        return tag.getValues().isEmpty() || getAmount() < 1;
    }

    @Override
    public RecipeInputTag clone() {
        return new RecipeInputTag(tag, getAmount(), getDurabilityCost());
    }

    @Override
    public String toString() {
        return "RITag { " + tag + ", " + super.toString() + " }";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        
        RecipeInputTag item = (RecipeInputTag) obj;
        return item.tag.getKey().equals(tag.getKey()) &&
            item.getAmount() == getAmount() &&
            item.getDurabilityCost() == getDurabilityCost();
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + tag.getKey().hashCode();
        hash = 31 * hash + getAmount();
        hash = 31 * hash + getDurabilityCost();
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
        if (getDurabilityCost() != 0) {
            item.addProperty("durability", getDurabilityCost());
        }
        return item;
    }
    
}
