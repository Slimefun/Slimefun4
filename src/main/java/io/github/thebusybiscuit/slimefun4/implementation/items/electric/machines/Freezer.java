package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;

/**
 * The {@link Freezer} can freeze items into its frozen state, e.g. water to ice.
 *
 * @author TheBusyBiscuit
 * @author svr333
 * @author J3fftw1
 */
public class Freezer extends AContainer implements RecipeDisplayItem {

    private final ItemSetting<Boolean> useVanillaRatios = new ItemSetting<>(this, "use-vanilla-ratios", false);

    @ParametersAreNonnullByDefault
    public Freezer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        addItemSetting(useVanillaRatios);
    }

    @Override
    protected void registerDefaultRecipes() {
        // This if statement makes the transformation follow Minecraft logic
        if (useVanillaRatios.getValue()) {
            registerRecipe(4, new ItemStack[] { new ItemStack(Material.ICE, 9) }, new ItemStack[] { new ItemStack(Material.PACKED_ICE) });
            registerRecipe(6, new ItemStack[] { new ItemStack(Material.PACKED_ICE, 9) }, new ItemStack[] { new ItemStack(Material.BLUE_ICE) });
        } else {
            registerRecipe(4, new ItemStack[] { new ItemStack(Material.ICE) }, new ItemStack[] { new ItemStack(Material.PACKED_ICE) });
            registerRecipe(6, new ItemStack[] { new ItemStack(Material.PACKED_ICE) }, new ItemStack[] { new ItemStack(Material.BLUE_ICE) });
        }

        registerRecipe(2, new ItemStack[] { new ItemStack(Material.WATER_BUCKET) }, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.ICE) });
        registerRecipe(8, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) }, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.OBSIDIAN) });
        registerRecipe(8, new ItemStack[] { new ItemStack(Material.BLUE_ICE) }, new ItemStack[] { SlimefunItems.REACTOR_COOLANT_CELL });
        registerRecipe(6, new ItemStack[] { new ItemStack(Material.SNOW_BLOCK, 2) }, new ItemStack[] { new ItemStack(Material.ICE) });
        registerRecipe(6, new ItemStack[] { new ItemStack(Material.MAGMA_CREAM) }, new ItemStack[] { new ItemStack(Material.SLIME_BALL) });
        registerRecipe(6, new ItemStack[] { new ItemStack(Material.MAGMA_BLOCK, 2) }, new ItemStack[] { new ItemStack(Material.SLIME_BLOCK) });
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> displayRecipes = new ArrayList<>(recipes.size() * 2);

        for (MachineRecipe recipe : recipes) {
            displayRecipes.add(recipe.getInput()[0]);
            displayRecipes.add(recipe.getOutput()[recipe.getOutput().length - 1]);
        }

        return displayRecipes;
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.GOLDEN_PICKAXE);
    }

    @Override
    public String getMachineIdentifier() {
        return "FREEZER";
    }

}
