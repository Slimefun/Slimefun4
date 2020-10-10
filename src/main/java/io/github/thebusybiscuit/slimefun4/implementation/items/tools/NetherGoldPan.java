package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class NetherGoldPan extends GoldPan {

    public NetherGoldPan(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    protected Material getTargetMaterial() {
        return Material.SOUL_SAND;
    }

    @Override
    protected Set<GoldPanDrop> getGoldPanDrops() {
        Set<GoldPanDrop> settings = new HashSet<>();

        settings.add(new GoldPanDrop("chance.QUARTZ", 50, new ItemStack(Material.QUARTZ)));
        settings.add(new GoldPanDrop("chance.GOLD_NUGGET", 25, new ItemStack(Material.GOLD_NUGGET)));
        settings.add(new GoldPanDrop("chance.NETHER_WART", 10, new ItemStack(Material.NETHER_WART)));
        settings.add(new GoldPanDrop("chance.BLAZE_POWDER", 8, new ItemStack(Material.BLAZE_POWDER)));
        settings.add(new GoldPanDrop("chance.GLOWSTONE_DUST", 5, new ItemStack(Material.GLOWSTONE_DUST)));
        settings.add(new GoldPanDrop("chance.GHAST_TEAR", 2, new ItemStack(Material.GHAST_TEAR)));

        return settings;
    }

}
