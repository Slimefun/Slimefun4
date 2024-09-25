package io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;

public class BioGenerator extends AGenerator {

    @ParametersAreNonnullByDefault
    public BioGenerator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected void registerDefaultFuelTypes() {
        registerFuel(new MachineFuel(2, ItemStack.of(Material.ROTTEN_FLESH)));
        registerFuel(new MachineFuel(2, ItemStack.of(Material.SPIDER_EYE)));
        registerFuel(new MachineFuel(2, ItemStack.of(Material.BONE)));
        registerFuel(new MachineFuel(2, ItemStack.of(Material.STRING)));
        registerFuel(new MachineFuel(3, ItemStack.of(Material.APPLE)));
        registerFuel(new MachineFuel(3, ItemStack.of(Material.MELON_SLICE)));
        registerFuel(new MachineFuel(27, ItemStack.of(Material.MELON)));
        registerFuel(new MachineFuel(3, ItemStack.of(Material.PUMPKIN)));
        registerFuel(new MachineFuel(3, ItemStack.of(Material.PUMPKIN_SEEDS)));
        registerFuel(new MachineFuel(3, ItemStack.of(Material.MELON_SEEDS)));
        registerFuel(new MachineFuel(3, ItemStack.of(Material.WHEAT)));
        registerFuel(new MachineFuel(3, ItemStack.of(Material.WHEAT_SEEDS)));
        registerFuel(new MachineFuel(3, ItemStack.of(Material.CARROT)));
        registerFuel(new MachineFuel(3, ItemStack.of(Material.POTATO)));
        registerFuel(new MachineFuel(3, ItemStack.of(Material.SUGAR_CANE)));
        registerFuel(new MachineFuel(3, ItemStack.of(Material.NETHER_WART)));
        registerFuel(new MachineFuel(2, ItemStack.of(Material.RED_MUSHROOM)));
        registerFuel(new MachineFuel(2, ItemStack.of(Material.BROWN_MUSHROOM)));
        registerFuel(new MachineFuel(2, ItemStack.of(Material.VINE)));
        registerFuel(new MachineFuel(2, ItemStack.of(Material.CACTUS)));
        registerFuel(new MachineFuel(2, ItemStack.of(Material.LILY_PAD)));
        registerFuel(new MachineFuel(8, ItemStack.of(Material.CHORUS_FRUIT)));
        registerFuel(new MachineFuel(1, ItemStack.of(Material.KELP)));
        registerFuel(new MachineFuel(2, ItemStack.of(Material.DRIED_KELP)));
        registerFuel(new MachineFuel(20, ItemStack.of(Material.DRIED_KELP_BLOCK)));
        registerFuel(new MachineFuel(1, ItemStack.of(Material.SEAGRASS)));
        registerFuel(new MachineFuel(2, ItemStack.of(Material.SEA_PICKLE)));
        registerFuel(new MachineFuel(1, ItemStack.of(Material.BAMBOO)));
        registerFuel(new MachineFuel(2, ItemStack.of(Material.SWEET_BERRIES)));
        registerFuel(new MachineFuel(2, ItemStack.of(Material.COCOA_BEANS)));
        registerFuel(new MachineFuel(3, ItemStack.of(Material.BEETROOT)));
        registerFuel(new MachineFuel(3, ItemStack.of(Material.BEETROOT_SEEDS)));

        // Small Flowers (formally just dandelions and poppies).
        for (Material m : Tag.SMALL_FLOWERS.getValues()) {
            registerFuel(new MachineFuel(1, ItemStack.of(m)));
        }

        registerFuel(new MachineFuel(4, ItemStack.of(Material.HONEYCOMB)));
        registerFuel(new MachineFuel(40, ItemStack.of(Material.HONEYCOMB_BLOCK)));

        registerFuel(new MachineFuel(4, ItemStack.of(Material.SHROOMLIGHT)));
        registerFuel(new MachineFuel(2, ItemStack.of(Material.CRIMSON_FUNGUS)));
        registerFuel(new MachineFuel(2, ItemStack.of(Material.WARPED_FUNGUS)));
        registerFuel(new MachineFuel(16, SlimefunItems.STRANGE_NETHER_GOO));

        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            registerFuel(new MachineFuel(2, ItemStack.of(Material.GLOW_BERRIES)));
            registerFuel(new MachineFuel(3, ItemStack.of(Material.SMALL_DRIPLEAF)));
            registerFuel(new MachineFuel(3, ItemStack.of(Material.BIG_DRIPLEAF)));
            registerFuel(new MachineFuel(2, ItemStack.of(Material.GLOW_LICHEN)));
            registerFuel(new MachineFuel(20, ItemStack.of(Material.SPORE_BLOSSOM)));
        }

        // Leaves
        for (Material m : Tag.LEAVES.getValues()) {
            registerFuel(new MachineFuel(1, ItemStack.of(m)));
        }

        // Saplings
        for (Material m : Tag.SAPLINGS.getValues()) {
            registerFuel(new MachineFuel(1, ItemStack.of(m)));
        }

        // Corals
        for (Material m : Tag.CORALS.getValues()) {
            registerFuel(new MachineFuel(2, ItemStack.of(m)));
        }

        for (Material m : Tag.CORAL_BLOCKS.getValues()) {
            registerFuel(new MachineFuel(2, ItemStack.of(m)));
        }
    }

    @Override
    public ItemStack getProgressBar() {
        return ItemStack.of(Material.GOLDEN_HOE);
    }

}
