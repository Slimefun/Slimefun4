package io.github.thebusybiscuit.slimefun4.api.recipes.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import io.github.thebusybiscuit.slimefun4.api.recipes.matching.RecipeMatchResult;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

public class RecipeOutputItemStack extends RecipeOutputItem {

    private ItemStack template;

    public RecipeOutputItemStack(ItemStack template, int amount) {
        super(amount);
        this.template = template;
    }

    public RecipeOutputItemStack(ItemStack template) {
        this(template, template.getAmount());
    }

    public RecipeOutputItemStack(Material template, int amount) {
        super(amount);
        this.template = new ItemStack(template, amount);
    }

    public RecipeOutputItemStack(Material template) {
        this(template, 1);
    }


    public ItemStack getTemplate() {
        return template;
    }

    @Override
    public ItemStack generateOutput(RecipeMatchResult result) {
        return template.clone();
    }

    @Override
    public boolean matchItem(ItemStack item) {
        return SlimefunUtils.isItemSimilar(item, template, true);
    }

    @Override
    public String toString() {
        return "ROItemStack { " + template + ", " + super.toString() + " }";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        
        RecipeOutputItemStack item = (RecipeOutputItemStack) obj;
        return item.template.equals(template) &&
            item.getAmount() == getAmount();
    }

    @Override
    public boolean canUseShortSerialization() {
        return super.canUseShortSerialization() && !template.hasItemMeta();
    }

    @Override
    public JsonElement serialize(JsonSerializationContext context) {
        if (canUseShortSerialization()) {
            return new JsonPrimitive(template.getType().getKey() + (getAmount() != 1 ? "|" + getAmount() : ""));
        }

        JsonObject item = new JsonObject();
        item.addProperty("id", template.getType().getKey().toString());
        if (getAmount() != 1) {
            item.addProperty("amount", getAmount());
        }
        return item;
    }

}
