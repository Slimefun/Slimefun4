package io.github.thebusybiscuit.slimefun4.api.recipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import io.github.thebusybiscuit.slimefun4.api.recipes.items.AbstractRecipeInputItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeInputItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.matching.MatchProcedure;
import io.github.thebusybiscuit.slimefun4.utils.RecipeUtils;
import io.github.thebusybiscuit.slimefun4.utils.RecipeUtils.BoundingBox;

public class RecipeInput extends AbstractRecipeInput {

    public static final AbstractRecipeInput EMPTY = new AbstractRecipeInput() {

        @Override
        public int getWidth() {
            return 0;
        }

        @Override
        public void setWidth(int width) {}

        @Override
        public int getHeight() {
            return 0;
        }

        @Override
        public void setHeight(int height) {}

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public List<AbstractRecipeInputItem> getItems() {
            return Collections.emptyList();
        }

        @Override
        public void setItems(List<AbstractRecipeInputItem> items) {}

        @Override
        public MatchProcedure getMatchProcedure() {
            return MatchProcedure.EMPTY;
        }

        @Override
        public void setMatchProcedure(MatchProcedure matchProcedure) {}

        @Override
        public Optional<BoundingBox> getBoundingBox() {
            return Optional.empty();
        }

        @Override
        public String toString() {
            return "RInput { EMPTY }";
        }

        @Override
        public JsonElement serialize(JsonSerializationContext context) {
            return new JsonObject();
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this;
        }
        
    };

    private List<AbstractRecipeInputItem> items;
    private MatchProcedure match;
    private int width;
    private int height;
    private Optional<BoundingBox> boundingBox;

    public RecipeInput(List<AbstractRecipeInputItem> items, MatchProcedure match, int width, int height) {
        this.items = items;
        this.match = match;
        this.width = width;
        this.height = height;
        saveBoundingBox();
    }

    public RecipeInput(List<AbstractRecipeInputItem> items, MatchProcedure match) {
        this.items = items;
        this.match = match;
        this.width = items.size();
        this.height = 1;
        saveBoundingBox();
    }

    public static RecipeInput fromItemStacks(ItemStack[] items, MatchProcedure match) {
        return new RecipeInput(Arrays.stream(items).map(item -> RecipeInputItem.fromItemStack(item)).toList(), match, 3, 3);
    }

    protected void saveBoundingBox() {
        if (this.match.recipeShouldSaveBoundingBox()) {
            this.boundingBox = Optional.of(RecipeUtils.calculateBoundingBox(items, width, height, item -> item.isEmpty()));
        }
    }
    
    public int getWidth() {
        return width;
    };
    public void setWidth(int width) {
        this.width = width;
        saveBoundingBox();
    };

    public int getHeight() {
        return height;
    };
    public void setHeight(int height) {
        this.height = height;
        saveBoundingBox();
    };

    public List<AbstractRecipeInputItem> getItems() {
        return Collections.unmodifiableList(items);
    };
    public void setItems(List<AbstractRecipeInputItem> items) {
        this.items = items;
        saveBoundingBox();
    };
    public void setItems(List<AbstractRecipeInputItem> items, int width, int height) {
        this.items = items;
        this.width = width;
        this.height = height;
        saveBoundingBox();
    };

    public MatchProcedure getMatchProcedure() { return match; };
    public void setMatchProcedure(MatchProcedure match) {
        this.match = match;
        saveBoundingBox();
    };

    public Optional<BoundingBox> getBoundingBox() { return boundingBox; }
    
    @Override
    public String toString() {
        return "RecipeInput { " + items + ", match=" + match + ", w=" + width + ", h=" + height + ", bb=" + boundingBox + " }";
    }

    @Override
    public boolean isEmpty() {
        if (boundingBox.isPresent()) {
            return boundingBox.get().getWidth() == 0 || boundingBox.get().getHeight() == 0;
        }
        return items.stream().allMatch(i -> i.isEmpty());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        RecipeInput input = (RecipeInput) obj;
        return input.items.equals(items) &&
            input.width == width &&
            input.height == height &&
            input.match == match;
    }

    @Override
    public JsonElement serialize(JsonSerializationContext context) {
        int current = 0;
        Map<AbstractRecipeInputItem, Integer> keys = new LinkedHashMap<>();
        List<String> template = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            char[] line = new char[width];
            Arrays.fill(line, ' ');
            for (int x = 0; x < width; x++) {
                int i = y * width + x;
                if (i >= items.size()) {
                    break;
                }

                AbstractRecipeInputItem inputItem = items.get(i);
                if (inputItem.isEmpty()) {
                    continue;
                }

                int keyNum;
                if (keys.containsKey(inputItem)) {
                    keyNum = keys.get(inputItem);
                } else {
                    keys.put(inputItem, current);
                    keyNum = current;
                    current++;
                }

                line[x] = RecipeUtils.getKeyCharByNumber(keyNum);
            }
            template.add(new String(line));
        }

        JsonObject input = new JsonObject();
        JsonElement jsonTemplate;
        if (template.size() == 1) {
            jsonTemplate = new JsonPrimitive(template.get(0));
        } else {
            JsonArray arr = new JsonArray();
            for (String line : template) {
                arr.add(line);
            }
            jsonTemplate = arr;
        }
        JsonObject key = new JsonObject();
        for (Map.Entry<AbstractRecipeInputItem, Integer> entry : keys.entrySet()) {
            key.add(
                String.valueOf(RecipeUtils.getKeyCharByNumber(entry.getValue())),
                context.serialize(entry.getKey(), AbstractRecipeInputItem.class)
            );
        }
        input.add("items", jsonTemplate);
        input.add("key", key);
        input.addProperty("match", match.getKey().toString());
        return input;
    }

}
