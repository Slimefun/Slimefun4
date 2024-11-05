package io.github.thebusybiscuit.slimefun4.api.recipes.items;

import java.util.Optional;

import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.matching.ItemMatchResult;
import io.github.thebusybiscuit.slimefun4.utils.RecipeUtils;

public abstract class RecipeInputItem extends AbstractRecipeInputItem {

    public static final AbstractRecipeInputItem EMPTY = new AbstractRecipeInputItem() {
    
        @Override
        protected ItemMatchResult matchItem(ItemStack item, AbstractRecipeInputItem root) {
            return new ItemMatchResult(item == null || item.getType().isAir(), this, item, 0);
        }
    
        @Override
        public boolean isEmpty() {
            return true;
        }
    
        @Override
        public AbstractRecipeInputItem clone() {
            return this;
        }

        @Override
        public ItemStack getItemDisplay() {
            return new ItemStack(Material.AIR);
        }
    
        @Override
        public String toString() {
            return "EMPTY";
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (obj == this) return true;
            if (obj instanceof final AbstractRecipeInputItem item) {
                return item.isEmpty();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public JsonElement serialize(JsonSerializationContext context) {
            // Shouldn't ever be called but eh
            return new JsonPrimitive("minecraft:air");
        }
        
    };

    private int amount;
    private int durabilityCost = 0;

    public RecipeInputItem(int amount, int durabilityCost) {
        this.amount = amount;
        this.durabilityCost = durabilityCost;
    }

    public RecipeInputItem(int amount) {
        this.amount = amount;
    }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    
    public int getDurabilityCost() { return durabilityCost; }
    public void setDurabilityCost(int durabilityCost) { this.durabilityCost = durabilityCost; }

    @Override
    public abstract RecipeInputItem clone();

    /**
     * Converts a string into a RecipeSingleItem
     * @param string A namespace string in the format
     * <ul>
     *   <li><code>minecraft:&lt;minecraft_id&gt;</code></li>
     *   <li><code>slimefun:&lt;slimefun_id&gt;</code></li>
     * </ul>
     * @return
     */
    public static AbstractRecipeInputItem fromString(String string) {
        if (string == null) {
            return RecipeInputItem.EMPTY;
        }
        String[] split = string.split(":");
        if (split.length != 2) {
            return RecipeInputItem.EMPTY;
        }
        String[] pipeSplit = split[1].split("\\|");
        String namespace = split[0];
        String id = pipeSplit[0];
        int amount = 1;
        if (pipeSplit.length > 1) {
            amount = Integer.parseInt(pipeSplit[1]);
        }
        if (namespace.startsWith("#")) {
            // Is a tag
            Optional<Tag<Material>> tag = RecipeUtils.tagFromString(namespace.substring(1), id);
            if (tag.isPresent()) {
                return new RecipeInputTag(tag.get(), amount);
            }
            return RecipeInputItem.EMPTY;
        }
        if (namespace.equals("minecraft")) {
            Material mat = Material.matchMaterial(id);
            return mat == null ? RecipeInputItem.EMPTY : new RecipeInputItemStack(mat, amount);
        } else if (namespace.equals("slimefun")) {
            return new RecipeInputSlimefunItem(id.toUpperCase(), amount);
        }
        return RecipeInputItem.EMPTY;
    }

    public static AbstractRecipeInputItem fromItemStack(@Nullable ItemStack item, int amount, int durabilityCost) {
        if (item == null || item.getType().isAir()) {
            return RecipeInputItem.EMPTY;
        }

        SlimefunItem sfItem = SlimefunItem.getByItem(item);
        
        if (sfItem != null) {
            return new RecipeInputSlimefunItem(sfItem.getId(), amount, durabilityCost);
        } else {
            return new RecipeInputItemStack(item, amount, durabilityCost);
        }
    }
    public static AbstractRecipeInputItem fromItemStack(@Nullable ItemStack item, int amount) {
        return fromItemStack(item, amount, 0);
    }
    public static AbstractRecipeInputItem fromItemStack(@Nullable ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return RecipeInputItem.EMPTY;
        }
        return fromItemStack(item, item.getAmount());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("amount=");

        builder.append(amount);
        if (durabilityCost != 0) {
            builder.append(", durabilityCost=");
            builder.append(durabilityCost);
        }

        return builder.toString();
    }

    @Override
    public boolean canUseShortSerialization() {
        return durabilityCost == 0;
    }
    
}
