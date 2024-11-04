package io.github.thebusybiscuit.slimefun4.api.recipes;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.common.base.Predicate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.thebusybiscuit.slimefun4.api.recipes.items.AbstractRecipeInputItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.AbstractRecipeOutputItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeInputGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeInputItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeInputItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeInputSlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeInputTag;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeOutputGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeOutputItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.RecipeOutputSlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.json.RecipeSerDes;
import io.github.thebusybiscuit.slimefun4.api.recipes.json.RecipeInputSerDes;
import io.github.thebusybiscuit.slimefun4.api.recipes.json.RecipeInputItemSerDes;
import io.github.thebusybiscuit.slimefun4.api.recipes.json.RecipeOutputSerDes;
import io.github.thebusybiscuit.slimefun4.api.recipes.json.RecipeOutputItemSerDes;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import be.seeseemelk.mockbukkit.MockBukkit;

class TestRecipes {

    private final String recipe1 = """
{
    __filename: "test",
    "input": {
        "items": [
            "     ",
            " 1123",
            " 1445"
        ],
        "key": {
            "1": "slimefun:tin_dust|2",
            "2": "minecraft:iron_ingot|60",
            "3": "#minecraft:logs|6",
            "4": {
                "group": [
                    "slimefun:copper_ingot|32",
                    {
                        "id": "minecraft:copper_ingot",
                        "amount": 48
                    }
                ]
            },
            "5": {
                "tag": "slimefun:ice_variants",
                "amount": 16
            }
        }
    },
    "output": {
        "items": [
            "slimefun:silver_dust|8"
        ]
    },
    "type": "slimefun:enhanced_crafting_table"
}
            """;

    private final String recipe2 = """
{
    __filename: "test",
    "input": {
        "items": ["1"],
        "key": {
            "1": "slimefun:tin_dust|2"
        }
    },
    "output": {
        "items": [
            "slimefun:silver_dust",
            {
                "id": "minecraft:oak_log",
                "amount": 55
            },
            {
                "group": [
                    {
                        "id": "minecraft:iron_ingot",
                        "amount": 12
                    },
                    {
                        "id": "slimefun:iron_dust",
                        "amount": 24
                    }
                ]
            },
            {
                "group": [
                    "minecraft:gold_ingot|8",
                    {
                        "id": "slimefun:gold_dust",
                        "amount": 16
                    }
                ],
                "weights": [1, 4]
            }
        ]
    },
    "type": "slimefun:enhanced_crafting_table"
}
            """;

    private static Gson gson;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        MockBukkit.load(Slimefun.class);
        gson = new GsonBuilder()
                .registerTypeAdapter(Recipe.class, new RecipeSerDes())
                .registerTypeAdapter(AbstractRecipeInput.class, new RecipeInputSerDes())
                .registerTypeAdapter(AbstractRecipeOutput.class, new RecipeOutputSerDes())
                .registerTypeAdapter(AbstractRecipeInputItem.class, new RecipeInputItemSerDes())
                .registerTypeAdapter(AbstractRecipeOutputItem.class, new RecipeOutputItemSerDes())
                .create();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    private List<AbstractRecipeOutputItem> o(Recipe recipe, int... i) {
        if (recipe.getOutput() instanceof final RecipeOutput output) {
            return Arrays.stream(i).mapToObj(idx ->output.getItem(idx)).toList();
        }
        return List.of();
    }

    private boolean isSlimefunItemOutput(List<AbstractRecipeOutputItem> items, String id, int amount) {
        return items.stream().allMatch(item -> item instanceof final RecipeOutputSlimefunItem sfItem
                && sfItem.getAmount() == amount
                && sfItem.getSlimefunId().equals(id));
    }

    private boolean isMinecraftItemOutput(List<AbstractRecipeOutputItem> items, Material mat, int amount) {
        return items.stream().allMatch(item -> item instanceof final RecipeOutputItemStack mcItem
        && mcItem.getAmount() == amount
        && mcItem.getTemplate().getType() == mat);
    }

    private boolean isGroupOutput(List<AbstractRecipeOutputItem> items, List<Predicate<AbstractRecipeOutputItem>> checks, float weightSum) {
        return items.stream().allMatch(item -> {
            if (item instanceof final RecipeOutputGroup group) {
                if (group.getOutputPool().sumWeights() != weightSum) {
                    return false;
                }
                var groupItems = group.getOutputPool().toArray(AbstractRecipeOutputItem[]::new);
                outer: for (Predicate<AbstractRecipeOutputItem> check : checks) {
                    for (AbstractRecipeOutputItem groupItem : groupItems) {
                        if (check.test(groupItem)) {
                            continue outer;
                        }
                    }
                    return false;
                }
                return true;
            }
            return false;
        });
    }

    private List<AbstractRecipeInputItem> i(Recipe recipe, int... i) {
        return Arrays.stream(i).mapToObj(idx -> recipe.getInput().getItem(idx)).toList();
    }

    private boolean isEmptyInput(List<AbstractRecipeInputItem> items) {
        return items.stream().allMatch(item -> item == RecipeInputItem.EMPTY);
    }

    private boolean isSlimefunItemInput(List<AbstractRecipeInputItem> items, String id, int amount) {
        return items.stream().allMatch(item -> item instanceof final RecipeInputSlimefunItem sfItem
                && sfItem.getAmount() == amount
                && sfItem.getSlimefunId().equals(id));
    }

    private boolean isMinecraftItemInput(List<AbstractRecipeInputItem> items, Material mat, int amount) {
        return items.stream().allMatch(item -> item instanceof final RecipeInputItemStack mcItem
        && mcItem.getAmount() == amount
        && mcItem.getTemplate().getType() == mat);
    }

    private boolean isTagInput(List<AbstractRecipeInputItem> items, Tag<Material> tag, int amount) {
        return items.stream().allMatch(item -> item instanceof final RecipeInputTag tagItem
        && tagItem.getTag().getValues().equals(tag.getValues())
        && tagItem.getAmount() == amount);
    }

    private boolean isGroupInput(List<AbstractRecipeInputItem> items, List<Predicate<AbstractRecipeInputItem>> checks) {
        return items.stream().allMatch(item -> {
            if (item instanceof final RecipeInputGroup group) {
                for (int i = 0; i < group.getItems().size(); i++) {
                    final var el = group.getItems().get(i);
                    if (!checks.get(i).test(el)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        });
    }

    @Test
    @DisplayName("Test Recipe Input Deserialization")
    void testInputDeserialization() {
        Recipe recipe = gson.fromJson(recipe1, Recipe.class);
        System.out.println(recipe);

        Assertions.assertEquals(5, recipe.getInput().getWidth());
        Assertions.assertEquals(3, recipe.getInput().getHeight());
        Assertions.assertTrue(isEmptyInput(i(recipe, 0, 1, 2, 3, 4, 5, 10)));

        Assertions.assertTrue(isSlimefunItemInput(i(recipe, 6, 7, 11), "TIN_DUST", 2));
        Assertions.assertTrue(isMinecraftItemInput(i(recipe, 8), Material.IRON_INGOT, 60));
        Assertions.assertTrue(isTagInput(i(recipe, 9), Tag.LOGS, 6));
        Assertions.assertTrue(isGroupInput(i(recipe, 12, 13), List.of(
                item -> isSlimefunItemInput(List.of(item), "COPPER_INGOT", 32),
                item -> isMinecraftItemInput(List.of(item), Material.COPPER_INGOT, 48)
        )));

        Assertions.assertTrue(isTagInput(i(recipe, 14), SlimefunTag.ICE_VARIANTS, 16));
    }

    @Test
    @DisplayName("Test Recipe Output Deserialization")
    void testOutputDeserialization() {
        Recipe recipe = gson.fromJson(recipe2, Recipe.class);
        System.out.println(recipe);

        Assertions.assertEquals(1, recipe.getInput().getWidth());
        Assertions.assertEquals(1, recipe.getInput().getHeight());
        Assertions.assertTrue(isSlimefunItemOutput(o(recipe, 0), "SILVER_DUST", 1));
        Assertions.assertTrue(isMinecraftItemOutput(o(recipe, 1), Material.OAK_LOG, 55));
        Assertions.assertTrue(isGroupOutput(o(recipe, 2), List.of(
            item -> isMinecraftItemOutput(List.of(item), Material.IRON_INGOT, 12),
            item -> isSlimefunItemOutput(List.of(item), "IRON_DUST", 24)
        ), 2));
        Assertions.assertTrue(isGroupOutput(o(recipe, 3), List.of(
            item -> isMinecraftItemOutput(List.of(item), Material.GOLD_INGOT, 8),
            item -> isSlimefunItemOutput(List.of(item), "GOLD_DUST", 16)
        ), 5));
    }

    @Test
    @DisplayName("Test Recipe Input Item Serialization")
    void testRecipeInputItemSerialization() {
        var i1 = new RecipeInputItemStack(new ItemStack(Material.ACACIA_BOAT));
        var i2 = new RecipeInputItemStack(new ItemStack(Material.STICK, 3));
        var i3 = new RecipeInputItemStack(new ItemStack(Material.IRON_SWORD), 2);
        var i4 = new RecipeInputSlimefunItem("IRON_DUST", 64);
        var i5 = new RecipeInputTag(SlimefunTag.TORCHES, 3);
        Assertions.assertEquals(
            "\"minecraft:acacia_boat\"",
            gson.toJson(i1, AbstractRecipeInputItem.class)
        );
        Assertions.assertEquals(
            "\"minecraft:stick|3\"",
            gson.toJson(i2, AbstractRecipeInputItem.class)
        );
        Assertions.assertEquals(
            "{\"id\":\"minecraft:iron_sword\",\"durability\":2}",
            gson.toJson(i3, AbstractRecipeInputItem.class)
        );
        Assertions.assertEquals(
            "\"slimefun:iron_dust|64\"",
            gson.toJson(i4, AbstractRecipeInputItem.class)
        );
        Assertions.assertEquals(
            "\"#slimefun:torches|3\"",
            gson.toJson(i5, AbstractRecipeInputItem.class)
        );
    }

}
