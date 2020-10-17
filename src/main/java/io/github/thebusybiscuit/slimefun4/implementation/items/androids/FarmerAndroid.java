package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class FarmerAndroid extends ProgrammableAndroid {

    public FarmerAndroid(Category category, int tier, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, tier, item, recipeType, recipe);
    }

    @Override
    public AndroidType getAndroidType() {
        return AndroidType.FARMER;
    }

    private boolean isFullGrown(Block block) {
        BlockData data = block.getBlockData();

        if (!(data instanceof Ageable)) {
            return false;
        }

        Ageable ageable = (Ageable) data;
        return ageable.getAge() >= ageable.getMaximumAge();
    }

    @Override
    protected void farm(BlockMenu menu, Block block) {
        if (isFullGrown(block)) {
            ItemStack drop = getDropFromCrop(block.getType());

            if (drop != null && menu.fits(drop, getOutputSlots())) {
                menu.pushItem(drop, getOutputSlots());
                Ageable ageable = (Ageable) block.getBlockData();
                ageable.setAge(0);
                block.setBlockData(ageable);
                block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
            }
        }
    }

    private ItemStack getDropFromCrop(Material crop) {
        Random random = ThreadLocalRandom.current();

        switch (crop) {
        case WHEAT:
            return new ItemStack(Material.WHEAT, random.nextInt(2) + 1);
        case POTATOES:
            return new ItemStack(Material.POTATO, random.nextInt(3) + 1);
        case CARROTS:
            return new ItemStack(Material.CARROT, random.nextInt(3) + 1);
        case BEETROOTS:
            return new ItemStack(Material.BEETROOT, random.nextInt(3) + 1);
        case COCOA:
            return new ItemStack(Material.COCOA_BEANS, random.nextInt(3) + 1);
        case NETHER_WART:
            return new ItemStack(Material.NETHER_WART, random.nextInt(3) + 1);
        case SWEET_BERRY_BUSH:
            return new ItemStack(Material.SWEET_BERRIES, random.nextInt(3) + 1);
        default:
            return null;
        }
    }

}
