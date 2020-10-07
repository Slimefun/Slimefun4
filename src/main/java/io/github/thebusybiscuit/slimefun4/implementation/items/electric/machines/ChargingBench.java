package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

/**
 * The {@link ChargingBench} is a powered machine that can be used to charge any {@link Rechargeable} item.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Rechargeable
 *
 */
public class ChargingBench extends AContainer {

    public ChargingBench(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
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
        if (getCharge(b.getLocation()) < getEnergyConsumption()) {
            return;
        }

        BlockMenu inv = BlockStorage.getInventory(b);

        for (int slot : getInputSlots()) {
            ItemStack item = inv.getItemInSlot(slot);

            if (charge(b, inv, slot, item)) {
                return;
            }
        }
    }

    private boolean charge(Block b, BlockMenu inv, int slot, ItemStack item) {
        SlimefunItem sfItem = SlimefunItem.getByItem(item);

        if (sfItem instanceof Rechargeable) {
            float charge = getEnergyConsumption() / 2F;

            if (((Rechargeable) sfItem).addItemCharge(item, charge)) {
                removeCharge(b.getLocation(), getEnergyConsumption());
            } else if (inv.fits(item, getOutputSlots())) {
                inv.pushItem(item, getOutputSlots());
                inv.replaceExistingItem(slot, null);
            }

            return true;
        } else if (sfItem != null && inv.fits(item, getOutputSlots())) {
            inv.pushItem(item, getOutputSlots());
            inv.replaceExistingItem(slot, null);
        }

        return false;
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
