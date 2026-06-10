package io.github.thebusybiscuit.slimefun5.implementation.items.electric.machines.accelerators;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.BlockDataCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.ParticleCompat;

import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun5.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedParticle;
import io.github.thebusybiscuit.slimefun5.utils.itemstack.ItemStackWrapper;
import io.github.thebusybiscuit.slimefun5.utils.tags.SlimefunTag;

import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public abstract class CropGrowthAccelerator extends AbstractGrowthAccelerator {

    // We wanna strip the Slimefun Item id here
    private static final ItemStack organicFertilizer = ItemStackWrapper.wrap(SlimefunItems.FERTILIZER.item());

    protected CropGrowthAccelerator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    public abstract int getEnergyConsumption();

    public abstract int getRadius();

    public abstract int getSpeed();

    @Override
    public int getCapacity() {
        return 1024;
    }

    @Override
    protected void tick(Block b) {
        BlockMenu inv = BlockStorage.getInventory(b);

        if (getCharge(b.getLocation()) >= getEnergyConsumption()) {
            for (int x = -getRadius(); x <= getRadius(); x++) {
                for (int z = -getRadius(); z <= getRadius(); z++) {
                    Block block = b.getRelative(x, 0, z);

                    if (SlimefunTag.CROP_GROWTH_ACCELERATOR_BLOCKS.isTagged(block.getType()) && grow(b, inv, block)) {
                        return;
                    }
                }
            }
        }
    }

    private boolean grow(Block machine, BlockMenu inv, Block crop) {
        Ageable ageable = (Ageable) BlockDataCompat.getBlockData(crop);

        if (ageable.getAge() < ageable.getMaximumAge()) {
            for (int slot : getInputSlots()) {
                if (SlimefunUtils.isItemSimilar(inv.getItemInSlot(slot), organicFertilizer, false, false)) {
                    removeCharge(machine.getLocation(), getEnergyConsumption());
                    inv.consumeItem(slot);

                    ageable.setAge(ageable.getAge() + 1);
                    BlockDataCompat.setBlockData(crop, ageable);

                    ParticleCompat.spawn(crop.getWorld(), VersionedParticle.HAPPY_VILLAGER, crop.getLocation().add(0.5D, 0.5D, 0.5D), 4, 0.1F, 0.1F, 0.1F);
                    return true;
                }
            }
        }

        return false;
    }

}

