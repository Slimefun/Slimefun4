package io.github.thebusybiscuit.slimefun4.api.recipes;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import io.github.thebusybiscuit.slimefun4.api.recipes.matching.InputMatchResult;
import io.github.thebusybiscuit.slimefun4.api.recipes.matching.MatchProcedure;
import io.github.thebusybiscuit.slimefun4.api.recipes.matching.RecipeMatchResult;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

public class Recipe {

    private final Optional<String> id;
    private final String filename; // this is where a recipe gets saved to, it doesn't have to be unique among recipes
    private AbstractRecipeInput input;
    private AbstractRecipeOutput output;
    private final Set<RecipeType> types = new HashSet<>();
    private Optional<Integer> energy;
    private Optional<Integer> craftingTime;
    private final Set<String> permissionNodes = new HashSet<>();

    public Recipe(Optional<String> id, String filename, AbstractRecipeInput input, AbstractRecipeOutput output, Collection<RecipeType> types, Optional<Integer> energy,
            Optional<Integer> craftingTime, Collection<String> permissionNodes) {
        this.id = id;
        this.filename = filename;
        this.input = input;
        this.output = output;
        for (RecipeType type : types) {
            this.types.add(type);
        }
        this.energy = energy;
        this.craftingTime = craftingTime;
        for (String perm : permissionNodes) {
            this.permissionNodes.add(perm);
        }
    }

    public static Recipe fromItemStacks(String subdirectory, String id, ItemStack[] inputs, ItemStack[] outputs, RecipeType type, MatchProcedure match) {
        return new Recipe(
            Optional.of(id),
            Path.of(subdirectory, id.toLowerCase()).toString(),
            RecipeInput.fromItemStacks(inputs, match),
            RecipeOutput.fromItemStacks(outputs),
            List.of(type),
            Optional.empty(),
            Optional.empty(),
            List.of()
        );
    }

    public static Recipe fromItemStacks(String subdirectory, String id, ItemStack[] inputs, ItemStack[] outputs, RecipeType type) {
        return fromItemStacks(subdirectory, id, inputs, outputs, type, type.getDefaultMatchProcedure());
    }

    public static Recipe fromItemStacks(String id, ItemStack[] inputs, ItemStack[] outputs, RecipeType type, MatchProcedure match) {
        return new Recipe(
            Optional.of(id),
            id.toLowerCase(),
            RecipeInput.fromItemStacks(inputs, match),
            RecipeOutput.fromItemStacks(outputs),
            List.of(type),
            Optional.empty(),
            Optional.empty(),
            List.of()
        );
    }

    public static Recipe fromItemStacks(String id, ItemStack[] inputs, ItemStack[] outputs, RecipeType type) {
        return fromItemStacks(id, inputs, outputs, type, type.getDefaultMatchProcedure());
    }

    public static Recipe fromItemStacks(ItemStack[] inputs, ItemStack[] outputs, RecipeType type, MatchProcedure match) {
        return new Recipe(
            Optional.empty(),
            "other_recipes.json",
            RecipeInput.fromItemStacks(inputs, match),
            RecipeOutput.fromItemStacks(outputs),
            List.of(type),
            Optional.empty(),
            Optional.empty(),
            List.of()
        );
    }

    public static Recipe fromItemStacks(ItemStack[] inputs, ItemStack[] outputs, RecipeType type) {
        return fromItemStacks(inputs, outputs, type, type.getDefaultMatchProcedure());
    }

    public Optional<String> getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public AbstractRecipeInput getInput() {
        return input;
    }
    public void setInput(AbstractRecipeInput input) {
        this.input = input;
    }

    public AbstractRecipeOutput getOutput() {
        return output;
    }
    public void setOutput(AbstractRecipeOutput output) {
        this.output = output;
    }

    public Optional<Integer> getEnergy() {
        return energy;
    }
    public void setEnergy(Optional<Integer> energy) {
        this.energy = energy;
    }

    public Optional<Integer> getCraftingTime() {
        return craftingTime;
    }
    public void setCraftingTime(Optional<Integer> craftingTime) {
        this.craftingTime = craftingTime;
    }

    public Set<String> getPermissionNodes() {
        return permissionNodes;
    }

    public Set<RecipeType> getTypes() {
        return Collections.unmodifiableSet(types);
    }
    public void addRecipeType(RecipeType type) {
        if (types.contains(type)) {
            return;
        }
        types.add(type);
        Slimefun.getRecipeService().addRecipeToType(this, type);
    }

    public RecipeMatchResult match(List<ItemStack> givenItems) {
        InputMatchResult result = getInput().match(givenItems);
        return new RecipeMatchResult(this, result);
    }

    public RecipeMatchResult matchAs(MatchProcedure match, List<ItemStack> givenItems) {
        InputMatchResult result = getInput().matchAs(match, givenItems);
        return new RecipeMatchResult(this, result);
    }

    /**
     * The guide display will need to be overhauled,
     * as it only displays individual items, and not
     * individual recipes; Furthermore, the input
     * cycling animation only works for vanilla recipes.
     * 
     * In the meantime, DO NOT rely on this method or
     * any similar ones in the recipe components
     */
    @Deprecated
    public ItemStack[] getInputDisplay() {
        return input.getInputDisplay();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Recipe { ");
        builder.append(input.toString());
        builder.append(", ");
        builder.append(output.toString());
        if (!types.isEmpty()) {
            builder.append(", RecipeType(s) { ");
            builder.append(String.join(", ", types.stream().map(t -> t.toString()).toList()));
            builder.append(" }");
        }
        if (energy.isPresent()) {
            builder.append(", energy=");
            builder.append(energy.get());
        }
        if (craftingTime.isPresent()) {
            builder.append(", craftingTime=");
            builder.append(craftingTime.get());
        }
        if (!permissionNodes.isEmpty()) {
            builder.append(", Permission(s) { ");
            builder.append(String.join(", ", permissionNodes));
            builder.append(" }");
        }
        builder.append(" }");
        return builder.toString();
    }

    /**
     * Serialize this recipe to JSON. If you are subclassing
     * Recipe, make sure to override this method and serialize the
     * key of the custom deserializer in the "class" field
     * @param context
     * @return
     */
    public JsonElement serialize(JsonSerializationContext context) {
        JsonObject recipe = new JsonObject();

        if (id.isPresent()) {
            recipe.addProperty("id", id.get());
        }
        if (!input.isEmpty()) {
            recipe.add("input", input.serialize(context));
        }
        if (!output.isEmpty()) {
            recipe.add("output", output.serialize(context));
        }
        if (types.size() == 1) {
            recipe.addProperty("type", types.stream().findFirst().get().toString());
        } else if (types.size() > 1) {
            JsonArray t = new JsonArray(types.size());
            for (RecipeType recipeType : types) {
                t.add(recipeType.getKey().toString());
            }
            recipe.add("type", t);
        }
        if (energy.isPresent()) {
            recipe.addProperty("energy", energy.get());
        }
        if (craftingTime.isPresent()) {
            recipe.addProperty("crafting-time", craftingTime.get());
        }
        if (!permissionNodes.isEmpty()) {
            JsonArray p = new JsonArray(permissionNodes.size());
            for (String node : permissionNodes) {
                p.add(node);
            }
            recipe.add("permission-node", p);
        }

        return recipe;
    }

}
