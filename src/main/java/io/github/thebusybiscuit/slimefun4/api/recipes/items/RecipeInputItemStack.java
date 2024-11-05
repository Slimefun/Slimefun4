package io.github.thebusybiscuit.slimefun4.api.recipes.items;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import io.github.thebusybiscuit.slimefun4.api.recipes.matching.ItemMatchResult;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

public class RecipeInputItemStack extends RecipeInputItem {

    private @Nonnull ItemStack template;

    @ParametersAreNonnullByDefault
    public RecipeInputItemStack(ItemStack template, int durabilityCost) {
        super(template.getAmount(), durabilityCost);
        this.template = template;
    }

    @ParametersAreNonnullByDefault
    public RecipeInputItemStack(ItemStack template) {
        this(template, 0);
    }

    @ParametersAreNonnullByDefault
    public RecipeInputItemStack(Material template, int amount, int durabilityCost) {
        super(amount, durabilityCost);
        this.template = new ItemStack(template);
    }

    @ParametersAreNonnullByDefault
    public RecipeInputItemStack(Material template, int amount) {
        this(template, amount, 0);
    }

    @ParametersAreNonnullByDefault
    public RecipeInputItemStack(Material template) {
        this(template, 1);
    }

    @Nonnull
    public ItemStack getTemplate() { return template; }
    public void setTemplate(@Nonnull ItemStack template) { this.template = template; }

    @Override
    public ItemMatchResult matchItem(ItemStack item, AbstractRecipeInputItem root) {
        if (item == null || item.getType().isAir()) {
            return new ItemMatchResult(isEmpty(), root, item, getAmount());
        } else if (item.getAmount() < getAmount()) {
            return new ItemMatchResult(false, root, item, getAmount());
        }
        return new ItemMatchResult(
            SlimefunUtils.isItemSimilar(item, template, false),
            root, item, getAmount()
        );
    }

    @Override
    public boolean isEmpty() {
        return template.getType().isAir() || getAmount() < 1;
    }

    @Override
    public RecipeInputItemStack clone() {
        return new RecipeInputItemStack(template.clone(), getDurabilityCost());
    }

    @Override
    public String toString() {
        return "RIItemStack { " + template + ", " + super.toString() + " }";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        
        RecipeInputItemStack item = (RecipeInputItemStack) obj;
        return item.template.equals(template) &&
            item.getAmount() == getAmount() &&
            item.getDurabilityCost() == getDurabilityCost();
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + template.hashCode();
        hash = 31 * hash + getAmount();
        hash = 31 * hash + getDurabilityCost();
        return hash;
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
        if (getDurabilityCost() != 0) {
            item.addProperty("durability", getDurabilityCost());
        }
        return item;
    }
    
}
