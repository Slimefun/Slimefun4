package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.energy.ItemEnergy;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class ChargingBench extends AContainer {

    public ChargingBench(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public String getInventoryTitle() {
        return SlimefunItems.CHARGING_BENCH.clone().getItemMeta().getDisplayName();
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.GOLDEN_PICKAXE);
    }

    @Override
    public int getEnergyConsumption() {
        return 10;
    }

    @Override
    public int getCapacity() {
        return 128;
    }

    @Override
    protected void tick(Block b) {
        if (ChargableBlock.getCharge(b) < getEnergyConsumption()) {
            return;
        }

        BlockMenu inv = BlockStorage.getInventory(b);

        for (int slot : getInputSlots()) {
            ItemStack item = inv.getItemInSlot(slot);

            if (ItemEnergy.getMaxEnergy(item) > 0) {
                charge(b, inv, slot, item);
                return;
            }
        }
    }

    private void charge(Block b, BlockMenu inv, int slot, ItemStack item) {
        if (ItemEnergy.getStoredEnergy(item) < ItemEnergy.getMaxEnergy(item)) {
            ChargableBlock.addCharge(b, -getEnergyConsumption());
            float rest = ItemEnergy.addStoredEnergy(item, getEnergyConsumption() / 2F);

            if (rest > 0F) {
                if (inv.fits(item, getOutputSlots())) {
                    inv.pushItem(item, getOutputSlots());
                    inv.replaceExistingItem(slot, null);
                } else {
                    inv.replaceExistingItem(slot, item);
                }
            } else {
                inv.replaceExistingItem(slot, item);
            }
        } else if (inv.fits(item, getOutputSlots())) {
            inv.pushItem(item, getOutputSlots());
            inv.replaceExistingItem(slot, null);
        } else {
            inv.replaceExistingItem(slot, item);
        }
    }

    @Override
    public int getSpeed() {
        return 1;
    }

    @Override
    public String getMachineIdentifier() {
        return "CHARGING_BENCH";
    }

}