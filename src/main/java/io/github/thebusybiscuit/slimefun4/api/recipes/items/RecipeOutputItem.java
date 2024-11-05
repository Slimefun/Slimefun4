package io.github.thebusybiscuit.slimefun4.api.recipes.items;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import io.github.thebusybiscuit.slimefun4.api.recipes.matching.RecipeMatchResult;
import io.github.thebusybiscuit.slimefun4.utils.RecipeUtils;

public abstract class RecipeOutputItem extends AbstractRecipeOutputItem {
    
    /**
     * Should not be used in recipes.
     * If you need an empty recipe output, use RecipeOutput.EMPTY instead
     */
    public static final AbstractRecipeOutputItem EMPTY = new AbstractRecipeOutputItem() {
        @Override
        public boolean matchItem(ItemStack item) {
            return item == null || item.getType().isAir();
        }

        @Override
        public SpaceRequirement getSpaceRequirement() {
            return SpaceRequirement.MATCHING_ITEM;
        }

        @Override
        public ItemStack generateOutput(RecipeMatchResult result) {
            return new ItemStack(Material.AIR);
        }

        @Override
        public String toString() {
            return "EMPTY";
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this;
        }

        @Override
        public JsonElement serialize(JsonSerializationContext context) {
            return new JsonPrimitive("minecraft:air");
        }
    };

    private int amount;

    public RecipeOutputItem(int amount, double outputChance) {
        this.amount = amount;
    }

    public RecipeOutputItem(int amount) {
        this(amount, 1.0);
    }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    @Override
    public SpaceRequirement getSpaceRequirement() {
        return SpaceRequirement.MATCHING_ITEM;
    }

    /**
     * Converts a string into a RecipeSingleItem
     * @param string A namespace string in the format
     * <ul>
     *   <li><code>minecraft:&lt;minecraft_id&gt;</code></li>
     *   <li><code>slimefun:&lt;slimefun_id&gt;</code></li>
     * </ul>
     * @return
     */
    public static AbstractRecipeOutputItem fromString(String string) {
        if (string == null) {
            return RecipeOutputItem.EMPTY;
        }
        String[] split = string.split(":");
        if (split.length != 2) {
            return RecipeOutputItem.EMPTY;
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
                return new RecipeOutputTag(tag.get(), amount);
            }
            return RecipeOutputItem.EMPTY;
        }
        if (namespace.equals("minecraft")) {
            Material mat = Material.matchMaterial(id);
            return mat == null ? RecipeOutputItem.EMPTY : new RecipeOutputItemStack(mat, amount);
        } else if (namespace.equals("slimefun")) {
            return new RecipeOutputSlimefunItem(id.toUpperCase(), amount);
        }
        return RecipeOutputItem.EMPTY;
    }

    @Override
    public String toString() {
        return "amount=" + amount;
    }

    @Override
    public boolean canUseShortSerialization() {
        return true;
    }
    
}
