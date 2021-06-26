package io.github.thebusybiscuit.slimefun4.implementation.items.magical.staves;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.LimitedUseItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * This {@link SlimefunItem} casts a {@link LightningStrike} where you are pointing.
 * Unlike the other Staves, it has a limited amount of uses.
 *
 * @author Linox
 * @author Walshy
 * @author TheBusyBiscuit
 */
public class StormStaff extends LimitedUseItem {

    public static final int MAX_USES = 8;

    private final NamespacedKey usageKey = new NamespacedKey(SlimefunPlugin.instance(), "stormstaff_usage");

    @ParametersAreNonnullByDefault
    public StormStaff(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

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

            if (p.getFoodLevel() >= 4 || p.getGameMode() == GameMode.CREATIVE) {
                // Get a target block with max. 30 blocks of distance
                Location loc = p.getTargetBlock(null, 30).getLocation();

                if (loc.getWorld() != null && loc.getChunk().isLoaded()) {
                    if (loc.getWorld().getPVP() && SlimefunPlugin.getProtectionManager().hasPermission(p, loc, ProtectableAction.ATTACK_PLAYER)) {
                        e.cancel();
                        useItem(p, item, loc);
                    } else {
                        SlimefunPlugin.getLocalization().sendMessage(p, "messages.no-pvp", true);
                    }
                }
            } else {
                SlimefunPlugin.getLocalization().sendMessage(p, "messages.hungry", true);
            }
        };
    }

    @ParametersAreNonnullByDefault
    private void useItem(Player p, ItemStack item, Location loc) {
        World world = loc.getWorld();
        if (world != null) {
            world.strikeLightning(loc);
        }

        if (item.getType() == Material.SHEARS) {
            return;
        }

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
