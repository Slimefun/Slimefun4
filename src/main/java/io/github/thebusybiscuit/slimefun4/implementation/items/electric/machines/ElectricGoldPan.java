package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.GoldPan;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.NetherGoldPan;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

/**
 * The {@link ElectricGoldPan} is an electric machine based on the {@link GoldPan}.
 * It also serves as a {@link NetherGoldPan}.
 * 
 * @author TheBusyBiscuit
 * @author svr333
 * 
 * @see GoldPan
 * @see NetherGoldPan
 *
 */
public class ElectricGoldPan extends AContainer implements RecipeDisplayItem {

    private final GoldPan goldPan = SlimefunItems.GOLD_PAN.getItem(GoldPan.class);
    private final GoldPan netherGoldPan = SlimefunItems.NETHER_GOLD_PAN.getItem(GoldPan.class);

    @ParametersAreNonnullByDefault
    public ElectricGoldPan(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> recipes = new ArrayList<>();

        recipes.addAll(goldPan.getDisplayRecipes());
        recipes.addAll(netherGoldPan.getDisplayRecipes());

        return recipes;
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.DIAMOND_SHOVEL);
    }

    @Override
    protected MachineRecipe findNextRecipe(BlockMenu menu) {
        if (!hasFreeSlot(menu)) {
            return null;
        }

        for (int slot : getInputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);

            if (item == null || item.getType().isAir()) {
                continue;
            }

            if (goldPan.isInputMaterialCorrect(item.getType())) {
                ItemStack output = goldPan.getRandomOutput();

                MachineRecipe recipe = new MachineRecipe(3 / getSpeed(), new ItemStack[] { item }, new ItemStack[] { output });

                if (output.getType() != Material.AIR && menu.fits(output, getOutputSlots())) {
                    menu.consumeItem(slot);
                    return recipe;
                }
            } else if (netherGoldPan.isInputMaterialCorrect(item.getType())) {
                ItemStack output = netherGoldPan.getRandomOutput();
                MachineRecipe recipe = new MachineRecipe(4 / getSpeed(), new ItemStack[] { item }, new ItemStack[] { output });

                if (output.getType() != Material.AIR && menu.fits(output, getOutputSlots())) {
                    menu.consumeItem(slot);
                    return recipe;
                }
            }
        }

        return null;
    }

    private boolean hasFreeSlot(@Nonnull BlockMenu menu) {
        for (int slot : getOutputSlots()) {
            if (menu.getItemInSlot(slot) == null) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getMachineIdentifier() {
        return "ELECTRIC_GOLD_PAN";
    }
}
