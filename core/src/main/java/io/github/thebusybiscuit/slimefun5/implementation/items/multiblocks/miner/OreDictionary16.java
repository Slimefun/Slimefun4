package io.github.thebusybiscuit.slimefun5.implementation.items.multiblocks.miner;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Our {@link OreDictionary} implementation for MC 1.16 or higher.
 * 
 * @author TheBusyBiscuit
 *
 */
class OreDictionary16 implements OreDictionary {

    @Override
    public ItemStack getDrops(@Nonnull Material material, @Nonnull Random random) {
        switch (material.name()) {
            case "COAL_ORE": return new ItemStack(Material.COAL);
            case "DIAMOND_ORE": return new ItemStack(Material.DIAMOND);
            case "EMERALD_ORE": return new ItemStack(Material.EMERALD);
            case "REDSTONE_ORE": return new ItemStack(Material.REDSTONE, 4 + random.nextInt(2));
            case "LAPIS_ORE": return new ItemStack(Material.valueOf("LAPIS_LAZULI"), 4 + random.nextInt(4));
            case "NETHER_QUARTZ_ORE": return new ItemStack(Material.QUARTZ);
            case "IRON_ORE": return new ItemStack(Material.IRON_ORE);
            case "GOLD_ORE": return new ItemStack(Material.GOLD_ORE);
            case "NETHER_GOLD_ORE": return new ItemStack(Material.GOLD_NUGGET, 2 + random.nextInt(4));
            case "ANCIENT_DEBRIS": return new ItemStack(Material.valueOf("ANCIENT_DEBRIS"));
            default: return new ItemStack(material);
        }
    }

}

