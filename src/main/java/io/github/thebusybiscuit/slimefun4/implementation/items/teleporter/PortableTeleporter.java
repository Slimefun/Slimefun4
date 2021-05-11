package io.github.thebusybiscuit.slimefun4.implementation.items.teleporter;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * This item allows a {@link Player} to access and teleport to his waypoints
 * from anywhere.
 *
 * @author martinbrom
 *
 * @see Teleporter
 */
public class PortableTeleporter extends SimpleSlimefunItem<ItemUseHandler> implements Rechargeable {

    private static final float COST = 2F;
    private static final float CAPACITY = 30F;

    @ParametersAreNonnullByDefault
    public PortableTeleporter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            ItemStack item = e.getItem();
            e.cancel();

            if (removeItemCharge(item, COST)) {
                SlimefunPlugin.getGPSNetwork().getTeleportationManager().openTeleporterGUI(
                        e.getPlayer(), e.getPlayer().getUniqueId(), e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN));
            }
        };
    }

    @Override
    public float getMaxItemCharge(ItemStack item) {
        return CAPACITY;
    }
}
