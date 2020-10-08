package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.OreWasher;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public abstract class ElectricDustWasher extends AContainer {

    private OreWasher oreWasher;
    private final boolean legacyMode;

    public ElectricDustWasher(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        legacyMode = SlimefunPlugin.getCfg().getBoolean("options.legacy-dust-washer");
    }

    @Override
    public void preRegister() {
        super.preRegister();

        oreWasher = (OreWasher) SlimefunItems.ORE_WASHER.getItem();
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.GOLDEN_SHOVEL);
    }

    @Override
    protected MachineRecipe findNextRecipe(BlockMenu menu) {
        for (int slot : getInputSlots()) {
            if (SlimefunUtils.isItemSimilar(menu.getItemInSlot(slot), SlimefunItems.SIFTED_ORE, true, false)) {
                if (!legacyMode && !hasFreeSlot(menu)) {
                    return null;
                }

                ItemStack dust = oreWasher.getRandomDust();
                MachineRecipe recipe = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[] { dust });

                if (!legacyMode || menu.fits(recipe.getOutput()[0], getOutputSlots())) {
                    menu.consumeItem(slot);
                    return recipe;
                }
            } else if (SlimefunUtils.isItemSimilar(menu.getItemInSlot(slot), SlimefunItems.PULVERIZED_ORE, true)) {
                MachineRecipe recipe = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[] { SlimefunItems.PURE_ORE_CLUSTER });

                if (menu.fits(recipe.getOutput()[0], getOutputSlots())) {
                    menu.consumeItem(slot);
                    return recipe;
                }
            }
        }

        return null;
    }

    private boolean hasFreeSlot(BlockMenu menu) {
        for (int slot : getOutputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);

            if (item == null || item.getType() == Material.AIR) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getMachineIdentifier() {
        return "ELECTRIC_DUST_WASHER";
    }

}
