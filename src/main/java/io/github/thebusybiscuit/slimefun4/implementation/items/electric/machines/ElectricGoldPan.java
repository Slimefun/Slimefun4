package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCategory;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeSearchResult;
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

    @Override
    public Collection<RecipeCategory> getCraftedCategories() {
        return List.of(RecipeCategory.GOLD_PAN, RecipeCategory.NETHER_GOLD_PAN);
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
    @Deprecated
    protected MachineRecipe findNextRecipe(BlockMenu menu) {
        if (!isOutputLimitOverridden() && !hasFreeSlot(menu)) {
            return null;
        }

        for (final int slot : getInputSlots()) {
            final ItemStack item = menu.getItemInSlot(slot);
            if (item == null) {
                continue;
            }

            final ItemStack[] givenItem = new ItemStack[] { item };

            final RecipeSearchResult searchResult = searchRecipes(givenItem);

            if (searchResult.isMatch()) {
                final int duration = searchResult.getSearchCategory().equals(RecipeCategory.GOLD_PAN) ? 3 : 4;
                final ItemStack output = searchResult.getRecipe().getOutput().generateOutput();

                if (output != null && output.getType() != Material.AIR && menu.fits(output, getOutputSlots())) {
                    menu.consumeItem(slot);
                    return new MachineRecipe(duration / getSpeed(), givenItem, new ItemStack[] { output });
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
    public @Nonnull String getMachineIdentifier() {
        return "ELECTRIC_GOLD_PAN";
    }

}
