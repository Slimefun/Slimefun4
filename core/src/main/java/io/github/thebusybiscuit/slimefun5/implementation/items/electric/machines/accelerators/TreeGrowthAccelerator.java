package io.github.thebusybiscuit.slimefun5.implementation.items.electric.machines.accelerators;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.BlockDataCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.ParticleCompat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.ReflectionCompat;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun5.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedParticle;
import io.github.thebusybiscuit.slimefun5.utils.itemstack.ItemStackWrapper;

import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

/**
 * The {@link TreeGrowthAccelerator} is an electrical machine that works similar to
 * the {@link CropGrowthAccelerator} but boosts the growth of nearby trees.
 * 
 * @author TheBusyBiscuit
 * 
 * @see CropGrowthAccelerator
 * @see AnimalGrowthAccelerator
 *
 */
public class TreeGrowthAccelerator extends AbstractGrowthAccelerator {

    private static final int ENERGY_CONSUMPTION = 24;
    private static final int RADIUS = 9;

    // We wanna strip the Slimefun Item id here
    private static final ItemStack organicFertilizer = ItemStackWrapper.wrap(SlimefunItems.FERTILIZER.item());

    @ParametersAreNonnullByDefault
    public TreeGrowthAccelerator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public int getCapacity() {
        return 1024;
    }

    @Override
    protected void tick(@Nonnull Block b) {
        BlockMenu inv = BlockStorage.getInventory(b);

        if (getCharge(b.getLocation()) >= ENERGY_CONSUMPTION) {
            for (int x = -RADIUS; x <= RADIUS; x++) {
                for (int z = -RADIUS; z <= RADIUS; z++) {
                    Block block = b.getRelative(x, 0, z);

                    if (Tag.SAPLINGS.isTagged(block.getType())) {
                        boolean isGrowthBoosted = tryToBoostGrowth(b, inv, block);

                        if (isGrowthBoosted) {
                            // Finish this tick and wait for the next.
                            return;
                        }
                    }
                }
            }
        }
    }

    @ParametersAreNonnullByDefault
    private boolean tryToBoostGrowth(Block machine, BlockMenu inv, Block sapling) {
        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            // On 1.17+ we can actually simulate bonemeal :O
            return applyBoneMeal(machine, sapling, inv);
        } else {
            Sapling saplingData = (Sapling) BlockDataCompat.getBlockData(sapling);
            return saplingData.getStage() < saplingData.getMaximumStage() && updateSaplingData(machine, sapling, inv, saplingData);
        }
    }

    @ParametersAreNonnullByDefault
    private boolean applyBoneMeal(Block machine, Block sapling, BlockMenu inv) {
        for (int slot : getInputSlots()) {
            if (isFertilizer(inv.getItemInSlot(slot))) {
                removeCharge(machine.getLocation(), ENERGY_CONSUMPTION);

                ReflectionCompat.invoke(sapling, "applyBoneMeal", BlockFace.UP);

                inv.consumeItem(slot);
                ParticleCompat.spawn(sapling.getWorld(), VersionedParticle.HAPPY_VILLAGER, sapling.getLocation().add(0.5D, 0.5D, 0.5D), 4, 0.1F, 0.1F, 0.1F);
                return true;
            }
        }

        return false;
    }

    @ParametersAreNonnullByDefault
    private boolean updateSaplingData(Block machine, Block block, BlockMenu inv, Sapling sapling) {
        for (int slot : getInputSlots()) {
            if (isFertilizer(inv.getItemInSlot(slot))) {
                removeCharge(machine.getLocation(), ENERGY_CONSUMPTION);

                sapling.setStage(sapling.getStage() + 1);
                BlockDataCompat.setBlockData(block, sapling, false);

                inv.consumeItem(slot);
                ParticleCompat.spawn(block.getWorld(), VersionedParticle.HAPPY_VILLAGER, block.getLocation().add(0.5D, 0.5D, 0.5D), 4, 0.1F, 0.1F, 0.1F);
                return true;
            }
        }

        return false;
    }

    protected boolean isFertilizer(@Nullable ItemStack item) {
        return SlimefunUtils.isItemSimilar(item, organicFertilizer, false, false);
    }

}

