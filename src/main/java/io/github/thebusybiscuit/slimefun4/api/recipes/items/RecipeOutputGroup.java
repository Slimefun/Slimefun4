package io.github.thebusybiscuit.slimefun4.api.recipes.items;

import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import io.github.bakedlibs.dough.collections.RandomizedSet;
import io.github.thebusybiscuit.slimefun4.api.recipes.matching.RecipeMatchResult;

public class RecipeOutputGroup extends AbstractRecipeOutputItem {

    RandomizedSet<AbstractRecipeOutputItem> outputPool;

    public RecipeOutputGroup(List<AbstractRecipeOutputItem> outputs, List<Integer> weights) {
        this.outputPool = new RandomizedSet<>();
        if (outputs.size() != weights.size()) {
            return;
        }

        for (int i = 0; i < outputs.size(); i++) {
            this.outputPool.add(outputs.get(i), weights.get(i));
        }
    }

    public RandomizedSet<AbstractRecipeOutputItem> getOutputPool() {
        return outputPool;
    }

    @Override
    public ItemStack generateOutput(RecipeMatchResult result) {
        return outputPool.getRandom().generateOutput(result);
    }

    @Override
    public boolean matchItem(ItemStack item) {
        return false;
    }

    @Override
    public String toString() {
        return "ROGroup { " + outputPool + " }";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        
        RecipeOutputGroup output = (RecipeOutputGroup) obj;
        return output.outputPool.toMap().equals(outputPool.toMap());
    }

    @Override
    public JsonElement serialize(JsonSerializationContext context) {
        JsonObject outputGroup = new JsonObject();
        JsonArray group = new JsonArray();
        JsonArray weights = new JsonArray();
        for (Map.Entry<AbstractRecipeOutputItem, Float> entry : outputPool.toMap().entrySet()) {
            group.add(context.serialize(entry.getKey(), AbstractRecipeOutputItem.class));
            weights.add((int) Math.round(entry.getValue() * outputPool.sumWeights()));
        }
        outputGroup.add("group", group);
        outputGroup.add("weights", weights);
        return outputGroup;
    }
    
}
