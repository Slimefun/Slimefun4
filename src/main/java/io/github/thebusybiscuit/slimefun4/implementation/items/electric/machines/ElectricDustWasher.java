package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.OreWasher;
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
                menu.pushItem(processing.get(b).getOutput()[0].clone(), getOutputSlots());

                progress.remove(b);
                processing.remove(b);
            }
        }
        else {
            for (int slot : getInputSlots()) {
                if (process(b, menu, slot)) {
                    break;
                }
            }
        }
    }

    private boolean process(Block b, BlockMenu menu, int slot) {
        if (SlimefunUtils.isItemSimilar(menu.getItemInSlot(slot), SlimefunItems.SIFTED_ORE, true)) {
            if (!legacyMode && !hasFreeSlot(menu)) {
                return true;
            }

            ItemStack adding = oreWasher.getRandomDust();
            MachineRecipe r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[] { adding });

            if (!legacyMode || menu.fits(r.getOutput()[0], getOutputSlots())) {
                menu.consumeItem(slot);
                processing.put(b, r);
                progress.put(b, r.getTicks());
            }

            return true;
        }
        else if (SlimefunUtils.isItemSimilar(menu.getItemInSlot(slot), SlimefunItems.PULVERIZED_ORE, true)) {
            MachineRecipe r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[] { SlimefunItems.PURE_ORE_CLUSTER });

            if (menu.fits(r.getOutput()[0], getOutputSlots())) {
                menu.consumeItem(slot);
                processing.put(b, r);
                progress.put(b, r.getTicks());
            }

            return true;
        }

        return false;
    }

    private boolean hasFreeSlot(BlockMenu menu) {
        for (int slot : getOutputSlots()) {
            if (menu.getItemInSlot(slot) == null) {
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
