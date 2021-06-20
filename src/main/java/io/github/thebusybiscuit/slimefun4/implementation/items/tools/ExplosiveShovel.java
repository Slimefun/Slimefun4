package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link ExplosiveShovel} works similar to the {@link ExplosivePickaxe}.
 * However it can only break blocks that a shovel can break.
 * 
 * @author Linox
 *
 * @see ExplosivePickaxe
 * @see ExplosiveTool
 *
 */
public class ExplosiveShovel extends ExplosiveTool {

    @ParametersAreNonnullByDefault
    public ExplosiveShovel(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    protected boolean canBreak(Player p, Block b) {
        return SlimefunTag.EXPLOSIVE_SHOVEL_BLOCKS.isTagged(b.getType()) && SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.BREAK_BLOCK);
    }

}
