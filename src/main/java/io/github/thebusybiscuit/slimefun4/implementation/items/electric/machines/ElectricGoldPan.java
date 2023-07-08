package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.GoldPan;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.NetherGoldPan;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

/**
 * The {@link ElectricGoldPan} is an electric machine based on the {@link GoldPan}.
 * It also serves as a {@link NetherGoldPan}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see GoldPan
 * @see NetherGoldPan
 *
 */
public class ElectricGoldPan extends AContainer implements RecipeDisplayItem {

    private final ItemSetting<Boolean> overrideOutputLimit = new ItemSetting<>(this, "override-output-limit", false);

    private final GoldPan goldPan = SlimefunItems.GOLD_PAN.getItem(GoldPan.class);
    private final GoldPan netherGoldPan = SlimefunItems.NETHER_GOLD_PAN.getItem(GoldPan.class);

    private final ItemStack gravel = new ItemStack(Material.GRAVEL);
    private final ItemStack soulSand = new ItemStack(Material.SOUL_SAND);

    @ParametersAreNonnullByDefault
    public ElectricGoldPan(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        addItemSetting(overrideOutputLimit);
    }

    /**
     * This returns whether the {@link ElectricGoldPan} will stop proccessing inputs
     * if both output slots contain items or if that default behavior should be
     * overriden and allow the {@link ElectricGoldPan} to continue processing inputs
     * even if both output slots are occupied. Note this option will allow players
     * to force specific outputs from the {@link ElectricGoldPan} but can be
     * necessary when a server has disabled cargo networks.
     * 
     * @return If output limits are overriden
     */
    public boolean isOutputLimitOverriden() {
        return overrideOutputLimit.getValue();
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
        if (!isOutputLimitOverriden() && !hasFreeSlot(menu)) {
            return null;
        }

        for (int slot : getInputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);

            if (SlimefunUtils.isItemSimilar(item, gravel, true, false)) {
                ItemStack output = goldPan.getRandomOutput();
                MachineRecipe recipe = new MachineRecipe(3 / getSpeed(), new ItemStack[] { gravel }, new ItemStack[] { output });

                if (output.getType() != Material.AIR && menu.fits(output, getOutputSlots())) {
                    menu.consumeItem(slot);
                    return recipe;
                }
            } else if (SlimefunUtils.isItemSimilar(item, soulSand, true, false)) {
                ItemStack output = netherGoldPan.getRandomOutput();
                MachineRecipe recipe = new MachineRecipe(4 / getSpeed(), new ItemStack[] { soulSand }, new ItemStack[] { output });

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
