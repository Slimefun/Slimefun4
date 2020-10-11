package io.github.thebusybiscuit.slimefun4.implementation.items.magical;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.settings.IntRangeSetting;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link TelepositionScroll} is a magical {@link SlimefunItem} that makes nearby any {@link LivingEntity}
 * turn around by 180 degrees.
 * The radius is configurable.
 * 
 * @author TheBusyBiscuit
 *
 */
public class TelepositionScroll extends SimpleSlimefunItem<ItemUseHandler> {

    private final ItemSetting<Integer> radius = new IntRangeSetting("radius", 1, 10, Integer.MAX_VALUE);

    public TelepositionScroll(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemSetting(radius);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            int range = radius.getValue();

            for (Entity n : e.getPlayer().getNearbyEntities(range, range, range)) {
                if (n instanceof LivingEntity && !(n instanceof ArmorStand) && !n.getUniqueId().equals(e.getPlayer().getUniqueId())) {
                    float yaw = n.getLocation().getYaw() + 180F;

                    if (yaw > 360F) {
                        yaw = yaw - 360F;
                    }

                    n.teleport(new Location(n.getWorld(), n.getLocation().getX(), n.getLocation().getY(), n.getLocation().getZ(), yaw, n.getLocation().getPitch()));
                }
            }
        };
    }

}
