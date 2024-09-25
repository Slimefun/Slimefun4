package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.multiversion.StackResolver;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
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
        recipeList = new ArrayList<>();
        recipeList.add(StackResolver.of(Material.ROTTEN_FLESH));
        recipeList.add(StackResolver.of(Material.LEATHER));

        recipeList.add(StackResolver.of(Material.WET_SPONGE));
        recipeList.add(StackResolver.of(Material.SPONGE));

        recipeList.add(StackResolver.of(Material.KELP));
        recipeList.add(StackResolver.of(Material.DRIED_KELP));

        recipeList.add(StackResolver.of(Material.POTION));
        recipeList.add(StackResolver.of(Material.GLASS_BOTTLE));

        recipeList.add(StackResolver.of(Material.SPLASH_POTION));
        recipeList.add(StackResolver.of(Material.GLASS_BOTTLE));

        recipeList.add(StackResolver.of(Material.LINGERING_POTION));
        recipeList.add(StackResolver.of(Material.GLASS_BOTTLE));

        recipeList.add(StackResolver.of(Material.WATER_BUCKET));
        recipeList.add(StackResolver.of(Material.BUCKET));

        recipeList.add(StackResolver.of(Material.COOKED_BEEF));
        recipeList.add(SlimefunItems.BEEF_JERKY);

        recipeList.add(StackResolver.of(Material.COOKED_PORKCHOP));
        recipeList.add(SlimefunItems.PORK_JERKY);

        recipeList.add(StackResolver.of(Material.COOKED_CHICKEN));
        recipeList.add(SlimefunItems.CHICKEN_JERKY);

        recipeList.add(StackResolver.of(Material.COOKED_MUTTON));
        recipeList.add(SlimefunItems.MUTTON_JERKY);

        recipeList.add(StackResolver.of(Material.COOKED_RABBIT));
        recipeList.add(SlimefunItems.RABBIT_JERKY);

        recipeList.add(StackResolver.of(Material.COOKED_COD));
        recipeList.add(SlimefunItems.FISH_JERKY);

        recipeList.add(StackResolver.of(Material.COOKED_SALMON));
        recipeList.add(SlimefunItems.FISH_JERKY);

        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_19)) {
            recipeList.add(StackResolver.of(Material.MUD));
            recipeList.add(StackResolver.of(Material.CLAY));
        }

        for (Material sapling : Tag.SAPLINGS.getValues()) {
            recipeList.add(StackResolver.of(sapling));
            recipeList.add(StackResolver.of(Material.STICK, 2));
        }

        for (Material leaves : Tag.LEAVES.getValues()) {
            recipeList.add(StackResolver.of(leaves));
            recipeList.add(StackResolver.of(Material.STICK));
        }

        // Now convert them to machine recipes
        for (int i = 0; i < recipeList.size(); i += 2) {
            registerRecipe(6, recipeList.get(i), recipeList.get(i + 1));
        }
    }

    @Override
    public ItemStack getProgressBar() {
        return StackResolver.of(Material.FLINT_AND_STEEL);
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
