package io.github.thebusybiscuit.slimefun4.implementation.items.gps;

import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.RainbowBlock;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.handlers.RainbowTicker;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * The {@link TeleporterPylon} is a special kind of {@link RainbowBlock} which is required
 * for the {@link Teleporter}.
 *
 * @author TheBusyBiscuit
 * @see Teleporter
 * @see RainbowBlock
 * @see RainbowTicker
 */
public class TeleporterPylon extends RainbowBlock {

    public TeleporterPylon(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput, new RainbowTicker(Material.CYAN_STAINED_GLASS, Material.PURPLE_STAINED_GLASS));
    }

}