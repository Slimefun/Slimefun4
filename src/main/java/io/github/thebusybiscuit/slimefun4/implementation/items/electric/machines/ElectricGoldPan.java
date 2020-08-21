package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.GoldPan;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public abstract class ElectricGoldPan extends AContainer implements RecipeDisplayItem {

    private final GoldPan goldPan = (GoldPan) SlimefunItems.GOLD_PAN.getItem();
    private final GoldPan netherGoldPan = (GoldPan) SlimefunItems.NETHER_GOLD_PAN.getItem();

    public ElectricGoldPan(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
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

    public abstract int getSpeed();

    @Override
    protected void tick(Block b) {
        BlockMenu menu = BlockStorage.getInventory(b);

        if (isProcessing(b)) {
            int timeleft = progress.get(b);

            if (timeleft > 0 && getSpeed() < 10) {
                ChestMenuUtils.updateProgressbar(menu, 22, timeleft, processing.get(b).getTicks(), getProgressBar());

                if (ChargableBlock.getCharge(b) < getEnergyConsumption()) {
                    return;
                }

                ChargableBlock.addCharge(b, -getEnergyConsumption());
                progress.put(b, timeleft - 1);
            }
            else {
                if (ChargableBlock.getCharge(b) < getEnergyConsumption()) {
                    return;
                }

                ChargableBlock.addCharge(b, -getEnergyConsumption());

                menu.replaceExistingItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));

                ItemStack output = processing.get(b).getOutput()[0];

                if (output.getType() != Material.AIR) {
                    menu.pushItem(output.clone(), getOutputSlots());
                }

                progress.remove(b);
                processing.remove(b);
            }
        }
        else {
            for (int slot : getInputSlots()) {
                if (hasFreeSlot(menu) && process(b, menu, slot)) {
                    break;
                }
            }
        }
    }

    private boolean hasFreeSlot(BlockMenu menu) {
        for (int slot : getOutputSlots()) {
            if (menu.getItemInSlot(slot) == null) {
                return true;
            }
        }

        return false;
    }

    private boolean process(Block b, BlockMenu menu, int slot) {
        if (SlimefunUtils.isItemSimilar(menu.getItemInSlot(slot), new ItemStack(Material.GRAVEL), true)) {
            ItemStack output = goldPan.getRandomOutput();

            MachineRecipe r = new MachineRecipe(3 / getSpeed(), new ItemStack[0], new ItemStack[] { output });

            if (menu.fits(output, getOutputSlots())) {
                menu.consumeItem(slot);
                processing.put(b, r);
                progress.put(b, r.getTicks());
            }

            return true;
        }
        else if (SlimefunUtils.isItemSimilar(menu.getItemInSlot(slot), new ItemStack(Material.SOUL_SAND), true)) {
            ItemStack output = netherGoldPan.getRandomOutput();

            MachineRecipe r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[] { output });

            if (menu.fits(output, getOutputSlots())) {
                menu.consumeItem(slot);
                processing.put(b, r);
                progress.put(b, r.getTicks());
            }

            return true;
        }

        return false;
    }

    @Override
    public String getMachineIdentifier() {
        return "ELECTRIC_GOLD_PAN";
    }

}
