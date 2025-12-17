package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotHopperable;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

/**
 * The {@link AutoDrier} is an implementation of {@link AContainer} that features recipes
 * related to "drying out" items.
 * It also allows you to convert Rotten Flesh into Leather.
 * 
 * @author Linox
 *
 */
public class AutoDrier extends AContainer implements RecipeDisplayItem, NotHopperable {

    private List<ItemStack> recipeList;

    @ParametersAreNonnullByDefault
    public AutoDrier(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected void registerDefaultRecipes() {
        List<ItemStack> rawRecipes = new ArrayList<>();

        rawRecipes.add(new ItemStack(Material.ROTTEN_FLESH));
        rawRecipes.add(new ItemStack(Material.LEATHER));

        rawRecipes.add(new ItemStack(Material.WET_SPONGE));
        rawRecipes.add(new ItemStack(Material.SPONGE));

        rawRecipes.add(new ItemStack(Material.KELP));
        rawRecipes.add(new ItemStack(Material.DRIED_KELP));

        rawRecipes.add(new ItemStack(Material.POTION));
        rawRecipes.add(new ItemStack(Material.GLASS_BOTTLE));

        rawRecipes.add(new ItemStack(Material.SPLASH_POTION));
        rawRecipes.add(new ItemStack(Material.GLASS_BOTTLE));

        rawRecipes.add(new ItemStack(Material.LINGERING_POTION));
        rawRecipes.add(new ItemStack(Material.GLASS_BOTTLE));

        rawRecipes.add(new ItemStack(Material.WATER_BUCKET));
        rawRecipes.add(new ItemStack(Material.BUCKET));

        rawRecipes.add(new ItemStack(Material.COOKED_BEEF));
        rawRecipes.add(SlimefunItems.BEEF_JERKY.item());

        rawRecipes.add(new ItemStack(Material.COOKED_PORKCHOP));
        rawRecipes.add(SlimefunItems.PORK_JERKY.item());

        rawRecipes.add(new ItemStack(Material.COOKED_CHICKEN));
        rawRecipes.add(SlimefunItems.CHICKEN_JERKY.item());

        rawRecipes.add(new ItemStack(Material.COOKED_MUTTON));
        rawRecipes.add(SlimefunItems.MUTTON_JERKY.item());

        rawRecipes.add(new ItemStack(Material.COOKED_RABBIT));
        rawRecipes.add(SlimefunItems.RABBIT_JERKY.item());

        rawRecipes.add(new ItemStack(Material.COOKED_COD));
        rawRecipes.add(SlimefunItems.FISH_JERKY.item());

        rawRecipes.add(new ItemStack(Material.COOKED_SALMON));
        rawRecipes.add(SlimefunItems.FISH_JERKY.item());

        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_19)) {
            rawRecipes.add(new ItemStack(Material.MUD));
            rawRecipes.add(new ItemStack(Material.CLAY));
        }

        for (Material sapling : Tag.SAPLINGS.getValues()) {
            rawRecipes.add(new ItemStack(sapling));
            rawRecipes.add(new ItemStack(Material.STICK, 2));
        }

        for (Material leaves : Tag.LEAVES.getValues()) {
            rawRecipes.add(new ItemStack(leaves));
            rawRecipes.add(new ItemStack(Material.STICK));
        }

        this.recipeList = new ArrayList<>();

        for (int i = 0; i < rawRecipes.size(); i += 2) {
            ItemStack input = rawRecipes.get(i);
            ItemStack output = rawRecipes.get(i + 1);

            SlimefunItem sfItem = SlimefunItem.getByItem(output);

            if (sfItem != null && sfItem.isDisabled()) {
                continue;
            }

            registerRecipe(6, input, output);
            
            this.recipeList.add(input);
            this.recipeList.add(output);
        }
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.FLINT_AND_STEEL);
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        return recipeList;
    }

    @Override
    public String getMachineIdentifier() {
        return "AUTO_DRIER";
    }
}