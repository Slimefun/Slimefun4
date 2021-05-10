package io.github.thebusybiscuit.slimefun4.implementation.items.teleporter;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link SharedActivationPlate} is a teleporter activation plate
 * to which everyone has access.
 * 
 * @author TheBusyBiscuit
 * 
 * @see PersonalActivationPlate
 *
 */
public class SharedActivationPlate extends AbstractTeleporterPlate {

    @ParametersAreNonnullByDefault
    public SharedActivationPlate(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean hasAccess(Player p, Block b) {
        // Shared - Everyone has access
        return true;
    }
}
