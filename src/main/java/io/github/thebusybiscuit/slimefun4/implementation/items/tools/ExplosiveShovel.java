package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import io.github.starwishsama.extra.ProtectionChecker;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialTools;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * The {@link ExplosiveShovel} works similar to the {@link ExplosivePickaxe}.
 * However it can only break blocks that a shovel can break.
 *
 * @author Linox
 * @see ExplosivePickaxe
 */
public class ExplosiveShovel extends ExplosiveTool {

    public ExplosiveShovel(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    protected void breakBlock(Player p, ItemStack item, Block b, int fortune, List<ItemStack> drops) {
        if (!isUnbreakable(b.getType().name()) && ProtectionChecker.check(p, b, true) && MaterialTools.getBreakableByShovel().contains(b.getType()) && SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.BREAK_BLOCK)) {
            SlimefunPlugin.getProtectionManager().logAction(p, b, ProtectableAction.BREAK_BLOCK);

            b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());

            for (ItemStack drop : b.getDrops(getItem())) {
                if (drop != null) {
                    b.getWorld().dropItemNaturally(b.getLocation(), drop);
                }
            }

            b.setType(Material.AIR);
            damageItem(p, item);
        }
    }

}