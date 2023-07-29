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

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

/**
 * The {@link ElectricGoldPan} is an electric machine based on the {@link GoldPan}.
 * It also serves as a {@link NetherGoldPan}.
 * 
 * @author TheBusyBiscuit
 * @author svr333
 * @author JustAHuman
 *
 * @see GoldPan
 * @see NetherGoldPan
 */
public class ElectricGoldPan extends AContainer implements RecipeDisplayItem {

    private final ItemSetting<Boolean> overrideOutputLimit = new ItemSetting<>(this, "override-output-limit", false);

    private final GoldPan goldPan = SlimefunItems.GOLD_PAN.getItem(GoldPan.class);
    private final GoldPan netherGoldPan = SlimefunItems.NETHER_GOLD_PAN.getItem(GoldPan.class);

    @ParametersAreNonnullByDefault
    public ElectricGoldPan(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        addItemSetting(overrideOutputLimit);
    }

    /**
     * @deprecated since RC-36
     * Use {@link ElectricGoldPan#isOutputLimitOverridden()} instead.
     */
    @Deprecated(since = "RC-36")
    public boolean isOutputLimitOverriden() {
        return isOutputLimitOverridden();
    }

    /**
     * This returns whether the {@link ElectricGoldPan} will stop processing inputs
     * if both output slots contain items or if that default behavior should be
     * overridden and allow the {@link ElectricGoldPan} to continue processing inputs
     * even if both output slots are occupied. Note this option will allow players
     * to force specific outputs from the {@link ElectricGoldPan} but can be
     * necessary when a server has disabled cargo networks.
     * 
     * @return If output limits are overridden
     */
    public boolean isOutputLimitOverridden() {
        return overrideOutputLimit.getValue();
    }

    @Override
    public @Nonnull List<ItemStack> getDisplayRecipes() {
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
        if (!isOutputLimitOverridden() && !hasFreeSlot(menu)) {
            return null;
        }

        for (int slot : getInputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);
            MachineRecipe recipe = null;
            ItemStack output = null;

            if (goldPan.isValidInput(item)) {
                output = goldPan.getRandomOutput();
                recipe = new MachineRecipe(3 / getSpeed(), new ItemStack[] { item }, new ItemStack[] { output });
            } else if (netherGoldPan.isValidInput(item)) {
                output = netherGoldPan.getRandomOutput();
                recipe = new MachineRecipe(4 / getSpeed(), new ItemStack[] { item }, new ItemStack[] { output });
            }

            if (output != null && output.getType() != Material.AIR && menu.fits(output, getOutputSlots())) {
                menu.consumeItem(slot);
                return recipe;
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
    public @Nonnull String getMachineIdentifier() {
        return "ELECTRIC_GOLD_PAN";
    }

}
