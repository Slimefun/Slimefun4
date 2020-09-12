package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public abstract class CropGrowthAccelerator extends AbstractGrowthAccelerator {

    private final Set<Material> crops = EnumSet.noneOf(Material.class);

    // We wanna strip the Slimefun Item id here
    private static final ItemStack organicFertilizer = new ItemStackWrapper(SlimefunItems.FERTILIZER);

    public CropGrowthAccelerator(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        crops.add(Material.WHEAT);
        crops.add(Material.POTATOES);
        crops.add(Material.CARROTS);
        crops.add(Material.NETHER_WART);
        crops.add(Material.BEETROOTS);
        crops.add(Material.COCOA);

        if (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_14)) {
            crops.add(Material.SWEET_BERRY_BUSH);
        }
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

                    if (crops.contains(block.getType()) && grow(b, inv, block)) {
                        return;
                    }
                }
            }
        }
    }

    private boolean grow(Block machine, BlockMenu inv, Block crop) {
        Ageable ageable = (Ageable) crop.getBlockData();

        if (ageable.getAge() < ageable.getMaximumAge()) {
            for (int slot : getInputSlots()) {
                if (SlimefunUtils.isItemSimilar(inv.getItemInSlot(slot), organicFertilizer, false, false)) {
                    removeCharge(machine.getLocation(), getEnergyConsumption());
                    inv.consumeItem(slot);

                    ageable.setAge(ageable.getAge() + 1);
                    crop.setBlockData(ageable);

                    crop.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, crop.getLocation().add(0.5D, 0.5D, 0.5D), 4, 0.1F, 0.1F, 0.1F);
                    return true;
                }
            }
        }

        return false;
    }

}
