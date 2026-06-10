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
        registerFuel(new MachineFuel(3, new ItemStack(XMaterial.MELON_SLICE.parseMaterial())));
        registerFuel(new MachineFuel(27, new ItemStack(Material.MELON)));
        registerFuel(new MachineFuel(3, new ItemStack(Material.PUMPKIN)));
        registerFuel(new MachineFuel(3, new ItemStack(Material.PUMPKIN_SEEDS)));
        registerFuel(new MachineFuel(3, new ItemStack(Material.MELON_SEEDS)));
        registerFuel(new MachineFuel(3, new ItemStack(Material.WHEAT)));
        registerFuel(new MachineFuel(3, new ItemStack(XMaterial.WHEAT_SEEDS.parseMaterial())));
        registerFuel(new MachineFuel(3, new ItemStack(Material.CARROT)));
        registerFuel(new MachineFuel(3, new ItemStack(Material.POTATO)));
        registerFuel(new MachineFuel(3, new ItemStack(Material.SUGAR_CANE)));
        registerFuel(new MachineFuel(3, new ItemStack(XMaterial.NETHER_WART.parseMaterial())));
        registerFuel(new MachineFuel(2, new ItemStack(Material.RED_MUSHROOM)));
        registerFuel(new MachineFuel(2, new ItemStack(Material.BROWN_MUSHROOM)));
        registerFuel(new MachineFuel(2, new ItemStack(Material.VINE)));
        registerFuel(new MachineFuel(2, new ItemStack(Material.CACTUS)));
        registerFuel(new MachineFuel(2, new ItemStack(XMaterial.LILY_PAD.parseMaterial())));
        registerFuel(new MachineFuel(8, new ItemStack(XMaterial.CHORUS_FRUIT.parseMaterial())));
        registerFuel(new MachineFuel(1, new ItemStack(XMaterial.KELP.parseMaterial())));
        registerFuel(new MachineFuel(2, new ItemStack(XMaterial.DRIED_KELP.parseMaterial())));
        registerFuel(new MachineFuel(20, new ItemStack(XMaterial.DRIED_KELP_BLOCK.parseMaterial())));
        registerFuel(new MachineFuel(1, new ItemStack(XMaterial.SEAGRASS.parseMaterial())));
        registerFuel(new MachineFuel(2, new ItemStack(XMaterial.SEA_PICKLE.parseMaterial())));
        registerFuel(new MachineFuel(1, new ItemStack(XMaterial.BAMBOO.parseMaterial())));
        registerFuel(new MachineFuel(2, new ItemStack(XMaterial.SWEET_BERRIES.parseMaterial())));
        registerFuel(new MachineFuel(2, new ItemStack(XMaterial.COCOA_BEANS.parseMaterial())));
        registerFuel(new MachineFuel(3, new ItemStack(XMaterial.BEETROOT.parseMaterial())));
        registerFuel(new MachineFuel(3, new ItemStack(XMaterial.BEETROOT_SEEDS.parseMaterial())));

        // Small Flowers (formally just dandelions and poppies).
        for (Material m : Tag.SMALL_FLOWERS.getValues()) {
            registerFuel(new MachineFuel(1, new ItemStack(m)));
        }

        registerFuel(new MachineFuel(4, new ItemStack(XMaterial.HONEYCOMB.parseMaterial())));
        registerFuel(new MachineFuel(40, new ItemStack(XMaterial.HONEYCOMB_BLOCK.parseMaterial())));

        registerFuel(new MachineFuel(4, new ItemStack(XMaterial.SHROOMLIGHT.parseMaterial())));
        registerFuel(new MachineFuel(2, new ItemStack(XMaterial.CRIMSON_FUNGUS.parseMaterial())));
        registerFuel(new MachineFuel(2, new ItemStack(XMaterial.WARPED_FUNGUS.parseMaterial())));
        registerFuel(new MachineFuel(16, SlimefunItems.STRANGE_NETHER_GOO.item()));

        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            registerFuel(new MachineFuel(2, new ItemStack(XMaterial.GLOW_BERRIES.parseMaterial())));
            registerFuel(new MachineFuel(3, new ItemStack(XMaterial.SMALL_DRIPLEAF.parseMaterial())));
            registerFuel(new MachineFuel(3, new ItemStack(XMaterial.BIG_DRIPLEAF.parseMaterial())));
            registerFuel(new MachineFuel(2, new ItemStack(XMaterial.GLOW_LICHEN.parseMaterial())));
            registerFuel(new MachineFuel(20, new ItemStack(XMaterial.SPORE_BLOSSOM.parseMaterial())));
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
        return new ItemStack(XMaterial.GOLDEN_HOE.parseMaterial());
    }

}

