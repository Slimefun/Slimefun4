package io.github.thebusybiscuit.slimefun5.implementation.items.electric.generators;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import com.cryptomorin.xseries.XMaterial;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

public class BioGenerator extends AGenerator {

    @ParametersAreNonnullByDefault
    public BioGenerator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected void registerDefaultFuelTypes() {
        registerFuel(new MachineFuel(2, new ItemStack(Material.ROTTEN_FLESH)));
        registerFuel(new MachineFuel(2, new ItemStack(Material.SPIDER_EYE)));
        registerFuel(new MachineFuel(2, new ItemStack(Material.BONE)));
        registerFuel(new MachineFuel(2, new ItemStack(Material.STRING)));
        registerFuel(new MachineFuel(3, new ItemStack(Material.APPLE)));
        registerFuel(new MachineFuel(3, MaterialCompat.stack(XMaterial.MELON_SLICE)));
        registerFuel(new MachineFuel(27, new ItemStack(Material.MELON)));
        registerFuel(new MachineFuel(3, new ItemStack(Material.PUMPKIN)));
        registerFuel(new MachineFuel(3, new ItemStack(Material.PUMPKIN_SEEDS)));
        registerFuel(new MachineFuel(3, new ItemStack(Material.MELON_SEEDS)));
        registerFuel(new MachineFuel(3, new ItemStack(Material.WHEAT)));
        registerFuel(new MachineFuel(3, MaterialCompat.stack(XMaterial.WHEAT_SEEDS)));
        registerFuel(new MachineFuel(3, new ItemStack(Material.CARROT)));
        registerFuel(new MachineFuel(3, new ItemStack(Material.POTATO)));
        registerFuel(new MachineFuel(3, new ItemStack(Material.SUGAR_CANE)));
        registerFuel(new MachineFuel(3, MaterialCompat.stack(XMaterial.NETHER_WART)));
        registerFuel(new MachineFuel(2, new ItemStack(Material.RED_MUSHROOM)));
        registerFuel(new MachineFuel(2, new ItemStack(Material.BROWN_MUSHROOM)));
        registerFuel(new MachineFuel(2, new ItemStack(Material.VINE)));
        registerFuel(new MachineFuel(2, new ItemStack(Material.CACTUS)));
        registerFuel(new MachineFuel(2, MaterialCompat.stack(XMaterial.LILY_PAD)));
        registerFuel(new MachineFuel(8, MaterialCompat.stack(XMaterial.CHORUS_FRUIT)));
        registerFuel(new MachineFuel(1, MaterialCompat.stack(XMaterial.KELP)));
        registerFuel(new MachineFuel(2, MaterialCompat.stack(XMaterial.DRIED_KELP)));
        registerFuel(new MachineFuel(20, MaterialCompat.stack(XMaterial.DRIED_KELP_BLOCK)));
        registerFuel(new MachineFuel(1, MaterialCompat.stack(XMaterial.SEAGRASS)));
        registerFuel(new MachineFuel(2, MaterialCompat.stack(XMaterial.SEA_PICKLE)));
        registerFuel(new MachineFuel(1, MaterialCompat.stack(XMaterial.BAMBOO)));
        registerFuel(new MachineFuel(2, MaterialCompat.stack(XMaterial.SWEET_BERRIES)));
        registerFuel(new MachineFuel(2, MaterialCompat.stack(XMaterial.COCOA_BEANS)));
        registerFuel(new MachineFuel(3, MaterialCompat.stack(XMaterial.BEETROOT)));
        registerFuel(new MachineFuel(3, MaterialCompat.stack(XMaterial.BEETROOT_SEEDS)));

        // Small Flowers (formally just dandelions and poppies).
        for (Material m : Tag.SMALL_FLOWERS.getValues()) {
            registerFuel(new MachineFuel(1, new ItemStack(m)));
        }

        registerFuel(new MachineFuel(4, MaterialCompat.stack(XMaterial.HONEYCOMB)));
        registerFuel(new MachineFuel(40, MaterialCompat.stack(XMaterial.HONEYCOMB_BLOCK)));

        registerFuel(new MachineFuel(4, MaterialCompat.stack(XMaterial.SHROOMLIGHT)));
        registerFuel(new MachineFuel(2, MaterialCompat.stack(XMaterial.CRIMSON_FUNGUS)));
        registerFuel(new MachineFuel(2, MaterialCompat.stack(XMaterial.WARPED_FUNGUS)));
        registerFuel(new MachineFuel(16, SlimefunItems.STRANGE_NETHER_GOO.item()));

        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            registerFuel(new MachineFuel(2, MaterialCompat.stack(XMaterial.GLOW_BERRIES)));
            registerFuel(new MachineFuel(3, MaterialCompat.stack(XMaterial.SMALL_DRIPLEAF)));
            registerFuel(new MachineFuel(3, MaterialCompat.stack(XMaterial.BIG_DRIPLEAF)));
            registerFuel(new MachineFuel(2, MaterialCompat.stack(XMaterial.GLOW_LICHEN)));
            registerFuel(new MachineFuel(20, MaterialCompat.stack(XMaterial.SPORE_BLOSSOM)));
        }

        // Leaves
        for (Material m : Tag.LEAVES.getValues()) {
            registerFuel(new MachineFuel(1, new ItemStack(m)));
        }

        // Saplings
        for (Material m : Tag.SAPLINGS.getValues()) {
            registerFuel(new MachineFuel(1, new ItemStack(m)));
        }

        // Corals
        for (Material m : Tag.CORALS.getValues()) {
            registerFuel(new MachineFuel(2, new ItemStack(m)));
        }

        for (Material m : Tag.CORAL_BLOCKS.getValues()) {
            registerFuel(new MachineFuel(2, new ItemStack(m)));
        }
    }

    @Override
    public ItemStack getProgressBar() {
        return MaterialCompat.stack(XMaterial.GOLDEN_HOE);
    }

}

