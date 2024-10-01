package io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators;

import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.utils.compatibility.StackResolver;
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
        registerFuel(new MachineFuel(2, StackResolver.of(Material.ROTTEN_FLESH)));
        registerFuel(new MachineFuel(2, StackResolver.of(Material.SPIDER_EYE)));
        registerFuel(new MachineFuel(2, StackResolver.of(Material.BONE)));
        registerFuel(new MachineFuel(2, StackResolver.of(Material.STRING)));
        registerFuel(new MachineFuel(3, StackResolver.of(Material.APPLE)));
        registerFuel(new MachineFuel(3, StackResolver.of(Material.MELON_SLICE)));
        registerFuel(new MachineFuel(27, StackResolver.of(Material.MELON)));
        registerFuel(new MachineFuel(3, StackResolver.of(Material.PUMPKIN)));
        registerFuel(new MachineFuel(3, StackResolver.of(Material.PUMPKIN_SEEDS)));
        registerFuel(new MachineFuel(3, StackResolver.of(Material.MELON_SEEDS)));
        registerFuel(new MachineFuel(3, StackResolver.of(Material.WHEAT)));
        registerFuel(new MachineFuel(3, StackResolver.of(Material.WHEAT_SEEDS)));
        registerFuel(new MachineFuel(3, StackResolver.of(Material.CARROT)));
        registerFuel(new MachineFuel(3, StackResolver.of(Material.POTATO)));
        registerFuel(new MachineFuel(3, StackResolver.of(Material.SUGAR_CANE)));
        registerFuel(new MachineFuel(3, StackResolver.of(Material.NETHER_WART)));
        registerFuel(new MachineFuel(2, StackResolver.of(Material.RED_MUSHROOM)));
        registerFuel(new MachineFuel(2, StackResolver.of(Material.BROWN_MUSHROOM)));
        registerFuel(new MachineFuel(2, StackResolver.of(Material.VINE)));
        registerFuel(new MachineFuel(2, StackResolver.of(Material.CACTUS)));
        registerFuel(new MachineFuel(2, StackResolver.of(Material.LILY_PAD)));
        registerFuel(new MachineFuel(8, StackResolver.of(Material.CHORUS_FRUIT)));
        registerFuel(new MachineFuel(1, StackResolver.of(Material.KELP)));
        registerFuel(new MachineFuel(2, StackResolver.of(Material.DRIED_KELP)));
        registerFuel(new MachineFuel(20, StackResolver.of(Material.DRIED_KELP_BLOCK)));
        registerFuel(new MachineFuel(1, StackResolver.of(Material.SEAGRASS)));
        registerFuel(new MachineFuel(2, StackResolver.of(Material.SEA_PICKLE)));
        registerFuel(new MachineFuel(1, StackResolver.of(Material.BAMBOO)));
        registerFuel(new MachineFuel(2, StackResolver.of(Material.SWEET_BERRIES)));
        registerFuel(new MachineFuel(2, StackResolver.of(Material.COCOA_BEANS)));
        registerFuel(new MachineFuel(3, StackResolver.of(Material.BEETROOT)));
        registerFuel(new MachineFuel(3, StackResolver.of(Material.BEETROOT_SEEDS)));

        // Small Flowers (formally just dandelions and poppies).
        for (Material m : Tag.SMALL_FLOWERS.getValues()) {
            registerFuel(new MachineFuel(1, StackResolver.of(m)));
        }

        registerFuel(new MachineFuel(4, StackResolver.of(Material.HONEYCOMB)));
        registerFuel(new MachineFuel(40, StackResolver.of(Material.HONEYCOMB_BLOCK)));

        registerFuel(new MachineFuel(4, StackResolver.of(Material.SHROOMLIGHT)));
        registerFuel(new MachineFuel(2, StackResolver.of(Material.CRIMSON_FUNGUS)));
        registerFuel(new MachineFuel(2, StackResolver.of(Material.WARPED_FUNGUS)));
        registerFuel(new MachineFuel(16, SlimefunItems.STRANGE_NETHER_GOO));

        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            registerFuel(new MachineFuel(2, StackResolver.of(Material.GLOW_BERRIES)));
            registerFuel(new MachineFuel(3, StackResolver.of(Material.SMALL_DRIPLEAF)));
            registerFuel(new MachineFuel(3, StackResolver.of(Material.BIG_DRIPLEAF)));
            registerFuel(new MachineFuel(2, StackResolver.of(Material.GLOW_LICHEN)));
            registerFuel(new MachineFuel(20, StackResolver.of(Material.SPORE_BLOSSOM)));
        }

        // Leaves
        for (Material m : Tag.LEAVES.getValues()) {
            registerFuel(new MachineFuel(1, StackResolver.of(m)));
        }

        // Saplings
        for (Material m : Tag.SAPLINGS.getValues()) {
            registerFuel(new MachineFuel(1, StackResolver.of(m)));
        }

        // Corals
        for (Material m : Tag.CORALS.getValues()) {
            registerFuel(new MachineFuel(2, StackResolver.of(m)));
        }

        for (Material m : Tag.CORAL_BLOCKS.getValues()) {
            registerFuel(new MachineFuel(2, StackResolver.of(m)));
        }
    }

    @Override
    public ItemStack getProgressBar() {
        return StackResolver.of(Material.GOLDEN_HOE);
    }

}
