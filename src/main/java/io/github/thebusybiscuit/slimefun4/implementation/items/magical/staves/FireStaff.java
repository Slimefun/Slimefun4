package io.github.thebusybiscuit.slimefun4.implementation.items.magical.staves;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.LimitedUseItem;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * This {@link SlimefunItem} set block on fire where you are pointing.
 * It has limited usage similar to {@link StormStaff}
 *
 * @author TerslenK
 */
public class FireStaff extends LimitedUseItem {

    public static final int MAX_USES = Slimefun.getCfg().getInt("staves.fire-staff-use-limit");

    private static final NamespacedKey usageKey = new NamespacedKey(Slimefun.instance(), "firestaff_usage");

    @ParametersAreNonnullByDefault
    public FireStaff(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(group, item, recipeType, recipe);

        setMaxUseCount(MAX_USES);
    }

    @Override
    protected @Nonnull NamespacedKey getStorageKey() {
        return usageKey;
    }

    @Override
    public @Nonnull ItemUseHandler getItemHandler() {
        return e -> {
            Player p = e.getPlayer();
            ItemStack item = e.getItem();
            Location loc = p.getTargetBlock(null, 7).getLocation();

            if (p.getFoodLevel() >= 4 || p.getGameMode() == GameMode.CREATIVE) {
                // Get a target block with max. 7 blocks of distance
                if (loc.getWorld() != null && loc.getChunk().isLoaded()) {
                    if (loc.getWorld().getPVP() && Slimefun.getProtectionManager().hasPermission(p, loc, Interaction.PLACE_BLOCK)) {
                        e.cancel();
                        useItem(p,item);
                    } else {
                        Slimefun.getLocalization().sendMessage(p, "messages.cannot-place", true);
                    }
                }
            } else {
                Slimefun.getLocalization().sendMessage(p, "messages.hungry", true);
            }
        };
    }

    public BlockFace getBlockFace(Player p) {
        List<Block> lastTwoTargetBlocks = p.getLastTwoTargetBlocks(null, 100);
        if (lastTwoTargetBlocks.size() != 2 || !lastTwoTargetBlocks.get(1).getType().isOccluding()) return null;
        Block targetBlock = lastTwoTargetBlocks.get(1);
        Block adjacentBlock = lastTwoTargetBlocks.get(0);
        return targetBlock.getFace(adjacentBlock);
    }

    @ParametersAreNonnullByDefault
    private void useItem(Player p, ItemStack item) {
        p.getTargetBlock(null,7).getRelative(getBlockFace(p)).setType(Material.FIRE);

        if (p.getGameMode() != GameMode.CREATIVE) {
            FoodLevelChangeEvent event = new FoodLevelChangeEvent(p, p.getFoodLevel() - 4);
            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                p.setFoodLevel(event.getFoodLevel());
            }
        }
        damageItem(p, item);
    }
}


