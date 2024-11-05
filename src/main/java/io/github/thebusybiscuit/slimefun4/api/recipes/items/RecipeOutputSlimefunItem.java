package io.github.thebusybiscuit.slimefun4.api.recipes.items;

import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.matching.RecipeMatchResult;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

public class RecipeOutputSlimefunItem extends RecipeOutputItem {

    private String slimefunId;

    public RecipeOutputSlimefunItem(String slimefunId, int amount) {
        super(amount);
        this.slimefunId = slimefunId;
    }

    public RecipeOutputSlimefunItem(String slimefunId) {
        this(slimefunId, 1);
    }

    public RecipeOutputSlimefunItem(SlimefunItemStack sfItemStack) {
        this(sfItemStack.getItemId(), sfItemStack.getAmount());
    }

    public String getSlimefunId() { return slimefunId; }
    public void setSlimefunId(String slimefunId) { this.slimefunId = slimefunId; }

    @Override
    public boolean matchItem(ItemStack item) {
        return SlimefunUtils.isItemSimilar(item, SlimefunItem.getById(slimefunId).getItem(), true);
    }

    @Override
    public ItemStack generateOutput(RecipeMatchResult result) {
        return SlimefunItem.getById(slimefunId).getItem().clone();
    }

    @Override
    public String toString() {
        return "ROSlimefunItem { id=" + slimefunId + ", " + super.toString() + " }";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        
        RecipeOutputSlimefunItem item = (RecipeOutputSlimefunItem) obj;
        return item.slimefunId.equals(slimefunId) &&
            item.getAmount() == getAmount();
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
        return item;
    }
    
}
