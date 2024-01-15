package io.github.thebusybiscuit.slimefun4.api.recipes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.bakedlibs.dough.recipes.MinecraftRecipe;
import io.github.thebusybiscuit.slimefun4.api.recipes.components.ItemComponent;
import io.github.thebusybiscuit.slimefun4.api.recipes.components.RecipeComponent;
import io.github.thebusybiscuit.slimefun4.api.recipes.output.ItemOutput;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AltarRecipe;
import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AncientAltar;

public class RecipeCategory implements Keyed {

    public static final RecipeCategory MULTIBLOCK = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "multiblock"), new CustomItemStack(Material.BRICKS, "&bMultiBlock", "", "&a&oBuild it in the World"));
    public static final RecipeCategory ARMOR_FORGE = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "armor_forge"), new CustomItemStack(SlimefunItems.ARMOR_FORGE, "", "&a&oCraft it in an Armor Forge"));
    public static final RecipeCategory GRIND_STONE = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "grind_stone"), new CustomItemStack(SlimefunItems.GRIND_STONE, "", "&a&oGrind it using the Grind Stone"), RecipeStructure.SUBSET);
    public static final RecipeCategory ORE_CRUSHER = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "ore_crusher"), new CustomItemStack(SlimefunItems.ORE_CRUSHER, "", "&a&oCrush it using the Ore Crusher"), RecipeStructure.SUBSET);
    public static final RecipeCategory GOLD_PAN = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "gold_pan"), new CustomItemStack(SlimefunItems.GOLD_PAN, "", "&a&oUse a Gold Pan on Gravel to obtain this Item"), RecipeStructure.SUBSET);
    public static final RecipeCategory NETHER_GOLD_PAN = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "nether_gold_pan"), new CustomItemStack(SlimefunItems.NETHER_GOLD_PAN, "", "&a&oUse a Nether Gold Pan on Soul Sand or Soul Soil to obtain this Item"), RecipeStructure.SUBSET);
    public static final RecipeCategory COMPRESSOR = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "compressor"), new CustomItemStack(SlimefunItems.COMPRESSOR, "", "&a&oCompress it using the Compressor"), RecipeStructure.SUBSET);
    public static final RecipeCategory PRESSURE_CHAMBER = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "pressure_chamber"), new CustomItemStack(SlimefunItems.PRESSURE_CHAMBER, "", "&a&oCompress it using the Pressure Chamber"), RecipeStructure.SUBSET);
    public static final RecipeCategory MAGIC_WORKBENCH = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "magic_workbench"), new CustomItemStack(SlimefunItems.MAGIC_WORKBENCH, "", "&a&oCraft it in a Magic Workbench"));
    public static final RecipeCategory ORE_WASHER = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "ore_washer"), new CustomItemStack(SlimefunItems.ORE_WASHER, "", "&a&oWash it in an Ore Washer"), RecipeStructure.SUBSET);
    public static final RecipeCategory ENHANCED_CRAFTING_TABLE = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "enhanced_crafting_table"), new CustomItemStack(SlimefunItems.ENHANCED_CRAFTING_TABLE, "", "&a&oA regular Crafting Table cannot", "&a&ohold this massive Amount of Power..."));
    public static final RecipeCategory JUICER = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "juicer"), new CustomItemStack(SlimefunItems.JUICER, "", "&a&oUsed for Juice Creation"), RecipeStructure.SUBSET);
    public static final RecipeCategory TABLE_SAW = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "table-saw"), new CustomItemStack(Material.STONECUTTER, "&bTable Saw", "", "&a&oCut it in a Table Saw"), RecipeStructure.SUBSET);
/**
     * @deprecated Smeltery recipes have moved to {@code DUST_SMELTING} and {@code INGOT_SMELTING}
     */
    @Deprecated
    public static final RecipeCategory SMELTERY = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "smeltery"), new CustomItemStack(SlimefunItems.SMELTERY, "", "&a&oSmelt it using a Smeltery"), RecipeStructure.SUBSET) {
        @Override
        public void onRegisterRecipe(Recipe recipe) {
            int dusts = 0;
            int nonEmpty = 0;
            for (final RecipeComponent comp : recipe.getInputs().getComponents()) {
                if (!comp.isAir()) {
                    if (comp.getSlimefunItemIDs().size() > 0 && comp.getSlimefunItemIDs().get(0).endsWith("_DUST")) {
                        dusts++;
                    }
                    nonEmpty++;
                }
            }
            if (dusts == 1 && nonEmpty == 1) {
                DUST_SMELTING.registerRecipe(recipe);
            } else {
                INGOT_SMELTING.registerRecipe(recipe);
            }
        }
    };
    
    public static final RecipeCategory INGOT_SMELTING = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "ingot_smelting"), new CustomItemStack(SlimefunItems.SMELTERY, "", "&a&oSmelt it using a Smeltery"), RecipeStructure.SUBSET) {
        @Override
        @Deprecated
        public RecipeType asRecipeType() {
            return RecipeType.SMELTERY;
        }
        @Override
        public String getTranslationKey() {
            return "slimefun.smeltery";
        }
    };
    public static final RecipeCategory DUST_SMELTING = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "dust_smelting"), new CustomItemStack(SlimefunItems.SMELTERY, "", "&a&oSmelt it using a Smeltery"), RecipeStructure.SUBSET) {
        @Override
        @Deprecated
        public RecipeType asRecipeType() {
            return RecipeType.SMELTERY;
        }
        @Override
        public String getTranslationKey() {
            return "slimefun.smeltery";
        }
        @Override
        public void onRegisterRecipe(Recipe recipe) { 
            // Add the inverse of this recipe (if applicable) to the ingot pulverizer category
            Optional<RecipeComponent> dust = recipe.getInputs().getComponents().stream().filter(comp -> !comp.isAir()).findFirst();
            if (dust.isPresent() && dust.get() instanceof final ItemComponent singleDust && recipe.getOutput() instanceof final ItemOutput itemOutput) {
                INGOT_PULVERIZER.registerRecipe(
                    Recipe.of(RecipeStructure.SUBSET, new ItemStack[] { itemOutput.getOutputTemplate() }, singleDust.getComponent())
                );
            }
        }
    };
    public static final RecipeCategory ANCIENT_ALTAR = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "ancient_altar"), SlimefunItems.ANCIENT_ALTAR, RecipeStructure.IDENTICAL) {
        @Override
        public void onRegisterRecipe(Recipe recipe) {
            AltarRecipe altarRecipe = new AltarRecipe(Arrays.asList(recipe.getInputs().asDisplayGrid()), recipe.getOutput().generateOutput());
            AncientAltar altar = ((AncientAltar) SlimefunItems.ANCIENT_ALTAR.getItem());
            altar.getCraftedRecipes().add(altarRecipe);
        }
    };

    public static final RecipeCategory MOB_DROP = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "mob_drop"), new CustomItemStack(Material.IRON_SWORD, "&bMob Drop", "", "&rKill the specified Mob to obtain this Item")) {
        @Override
        public void onRegisterRecipe(Recipe recipe) {
            String mob = ChatColor.stripColor(recipe.getInputs().getComponents().get(4).getDisplayItem().getItemMeta().getDisplayName()).toUpperCase(Locale.ROOT)
                    .replace(' ', '_');
            EntityType entity = EntityType.valueOf(mob);
            Set<ItemStack> dropping = Slimefun.getRegistry().getMobDrops().getOrDefault(entity, new HashSet<>());
            dropping.add(recipe.getOutput().generateOutput());
            Slimefun.getRegistry().getMobDrops().put(entity, dropping);
        }
    };
    public static final RecipeCategory BARTER_DROP = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "barter_drop"), new CustomItemStack(Material.GOLD_INGOT, "&bBarter Drop", "&aBarter with piglins for a chance", "&ato obtain this item")) {
        @Override
        public void onRegisterRecipe(Recipe recipe) {
            Slimefun.getRegistry().getBarteringDrops().add(recipe.getOutput().generateOutput());
        }
    };

    public static final RecipeCategory INTERACT = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "interact"), new CustomItemStack(Material.PLAYER_HEAD, "&bInteract", "", "&a&oRight click with this item"));

    public static final RecipeCategory HEATED_PRESSURE_CHAMBER = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "heated_pressure_chamber"), SlimefunItems.HEATED_PRESSURE_CHAMBER, RecipeStructure.SUBSET);
    public static final RecipeCategory FOOD_FABRICATOR = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "food_fabricator"), SlimefunItems.FOOD_FABRICATOR, RecipeStructure.SUBSET);
    public static final RecipeCategory FOOD_COMPOSTER = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "food_composter"), SlimefunItems.FOOD_COMPOSTER, RecipeStructure.SUBSET);
    public static final RecipeCategory FREEZER = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "freezer"), SlimefunItems.FREEZER, RecipeStructure.SUBSET);
    public static final RecipeCategory REFINERY = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "refinery"), SlimefunItems.REFINERY, RecipeStructure.SUBSET);
    public static final RecipeCategory INGOT_PULVERIZER = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "ingot_pulverizer"), SlimefunItems.ELECTRIC_INGOT_PULVERIZER, RecipeStructure.SUBSET);

    public static final RecipeCategory OIL_PUMP = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "oil_pump"), SlimefunItems.OIL_PUMP);
    public static final RecipeCategory GEO_MINER = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "geo_miner"), SlimefunItems.GEO_MINER);
    public static final RecipeCategory NUCLEAR_REACTOR = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "nuclear_reactor"), SlimefunItems.NUCLEAR_REACTOR, RecipeStructure.SUBSET);

    public static final RecipeCategory PICKAXE_OF_CONTAINMENT = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "pickaxe_of_containment"), SlimefunItems.PICKAXE_OF_CONTAINMENT, RecipeStructure.SUBSET);

    public static final RecipeCategory NULL = new RecipeCategory(new NamespacedKey(Slimefun.instance(), "null"), new ItemStack(Material.AIR));

    private final ItemStack displayItem;
    private final NamespacedKey key;
    private final RecipeStructure defaultStructure;

    public RecipeCategory(NamespacedKey key, ItemStack displayItem, RecipeStructure defaultStructure) {
        this.displayItem = displayItem;
        this.key = key;
        this.defaultStructure = defaultStructure;
    }

    public RecipeCategory(NamespacedKey key, ItemStack displayItem) {
        this(key, displayItem, RecipeStructure.SHAPED);
    }
    
    public RecipeCategory(MinecraftRecipe<?> recipe) {
        this.displayItem = new ItemStack(recipe.getMachine());
        this.defaultStructure = RecipeStructure.NULL; // This is for the guide display only, nothing is crafted with this
        this.key = NamespacedKey.minecraft(recipe.getRecipeClass().getSimpleName().toLowerCase(Locale.ROOT).replace("recipe", ""));
    }

    public void registerRecipes(@Nonnull List<Recipe> recipes) {
        Slimefun.getSlimefunRecipeService().registerRecipes(this, recipes);
    }

    public void registerRecipe(@Nonnull Recipe recipe) {
        Slimefun.getSlimefunRecipeService().registerRecipes(this, List.of(recipe));
    }

    /**
     * This can be overriden if a specific category should require it
     * 
     * @param recipe The recipe being registered using
     *               {@code Slimefun.registerRecipes()}
     */
    public void onRegisterRecipe(Recipe recipe) {
    }

    /**
     * For backwards compat (namely SlimefunItem.getRecipeType()).
     * To be removed when RecipeType is removed
     */
    @Deprecated
    public RecipeType asRecipeType() {
        return new RecipeType(key, displayItem);
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }

    public ItemStack getLocalizedItem(Player p) {
        return Slimefun.getLocalization().getRecipeCategoryItem(p, this);
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public String toString() {
        return key.toString();
    }

    public String getTranslationKey() {
        return getKey().getNamespace() + "." + getKey().getKey();
    }

    public RecipeStructure getDefaultStructure() {
        return defaultStructure;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof final RecipeCategory other) {
            return other.getKey().equals(getKey());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

}
