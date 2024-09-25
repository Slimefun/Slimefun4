package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.utils.multiversion.StackResolver;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.collections.RandomizedSet;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;

import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class FishermanAndroid extends ProgrammableAndroid {

    private final RandomizedSet<ItemStack> fishingLoot = new RandomizedSet<>();

    @ParametersAreNonnullByDefault
    public FishermanAndroid(ItemGroup itemGroup, int tier, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, tier, item, recipeType, recipe);

        // Fish
        for (Material fish : Tag.ITEMS_FISHES.getValues()) {
            fishingLoot.add(StackResolver.of(fish), 25);
        }

        // Junk
        fishingLoot.add(StackResolver.of(Material.BONE), 10);
        fishingLoot.add(StackResolver.of(Material.STRING), 10);
        fishingLoot.add(StackResolver.of(Material.INK_SAC), 8);
        fishingLoot.add(StackResolver.of(Material.KELP), 6);
        fishingLoot.add(StackResolver.of(Material.STICK), 5);
        fishingLoot.add(StackResolver.of(Material.ROTTEN_FLESH), 3);
        fishingLoot.add(StackResolver.of(Material.LEATHER), 2);
        fishingLoot.add(StackResolver.of(Material.BAMBOO), 3);

        // "loot"
        fishingLoot.add(StackResolver.of(Material.SADDLE), 1);
        fishingLoot.add(StackResolver.of(Material.NAME_TAG), 1);
        fishingLoot.add(StackResolver.of(Material.NAUTILUS_SHELL), 1);
    }

    @Override
    public AndroidType getAndroidType() {
        return AndroidType.FISHERMAN;
    }

    @Override
    protected void fish(Block b, BlockMenu menu) {
        Block water = b.getRelative(BlockFace.DOWN);

        if (water.getType() == Material.WATER) {
            SoundEffect.FISHERMAN_ANDROID_FISHING_SOUND.playAt(water);

            if (ThreadLocalRandom.current().nextInt(100) < 10 * getTier()) {
                ItemStack drop = fishingLoot.getRandom();
                menu.pushItem(drop.clone(), getOutputSlots());
            }
        }
    }
}
