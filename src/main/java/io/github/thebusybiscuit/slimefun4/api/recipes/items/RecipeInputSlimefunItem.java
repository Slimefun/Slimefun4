package io.github.thebusybiscuit.slimefun4.api.recipes.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.matching.ItemMatchResult;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

public class RecipeInputSlimefunItem extends RecipeInputItem {

    private String slimefunId;

    public RecipeInputSlimefunItem(String slimefunId, int amount, int durabilityCost) {
        super(amount, durabilityCost);
        this.slimefunId = slimefunId;
    }

    public RecipeInputSlimefunItem(String slimefunId, int amount) {
        this(slimefunId, amount, 0);
    }

    public RecipeInputSlimefunItem(String slimefunId) {
        this(slimefunId, 1);
    }

    public String getSlimefunId() { return slimefunId; }
    public void setSlimefunId(String slimefunId) { this.slimefunId = slimefunId; }

    @Override
    public ItemStack getItemDisplay() {
        // TODO: guide display overhaul
        SlimefunItem sfItem = SlimefunItem.getById(slimefunId);
        if (sfItem == null) {
            return new ItemStack(Material.AIR);
        }
        ItemStack display = sfItem.getItem();
        if (getAmount() != display.getAmount()) {
            display = sfItem.getItem().clone();
            display.setAmount(getAmount());
            return display;
        }
        return sfItem.getItem();
    }

    @Override
    public ItemMatchResult matchItem(ItemStack item, AbstractRecipeInputItem root) {
        if (item == null || item.getType().isAir()) {
            return new ItemMatchResult(isEmpty(), root, item, getAmount(), getDurabilityCost());
        } else if (item.getAmount() < getAmount()) {
            return new ItemMatchResult(false, root, item, getAmount(), getDurabilityCost());
        }
        return new ItemMatchResult(
            SlimefunUtils.isItemSimilar(item, SlimefunItem.getById(slimefunId).getItem(), false),
            root, item, getAmount(), getDurabilityCost()
        );
    }

    @Override
    public RecipeInputSlimefunItem clone() {
        return new RecipeInputSlimefunItem(slimefunId, getAmount(), getDurabilityCost());
    }

    @Override
    public String toString() {
        return "RISlimefunItem { id=" + slimefunId + ", " + super.toString() + " }";
    }

    @Override
    public boolean isEmpty() {
        return this.getAmount() < 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        
        RecipeInputSlimefunItem item = (RecipeInputSlimefunItem) obj;
        return item.slimefunId.equals(slimefunId) &&
            item.getAmount() == getAmount() &&
            item.getDurabilityCost() == getDurabilityCost();
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + slimefunId.hashCode();
        hash = 31 * hash + getAmount();
        hash = 31 * hash + getDurabilityCost();
        return hash;
    }

    @Override
    public JsonElement serialize(JsonSerializationContext context) {
        if (canUseShortSerialization()) {
            return new JsonPrimitive("slimefun:" + slimefunId.toLowerCase() + (getAmount() != 1 ? "|" + getAmount() : ""));
        }

        JsonObject item = new JsonObject();
        item.addProperty("id", "slimefun:" + slimefunId.toLowerCase());
        if (getAmount() != 1) {
            item.addProperty("amount", getAmount());
        }
        if (getDurabilityCost() != 0) {
            item.addProperty("durability", getDurabilityCost());
        }
        return item;
    }
    
}
