package io.github.thebusybiscuit.slimefun4.implementation.items.magical.staves;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.LimitedUseItem;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class FireStaff extends LimitedUseItem {

    public static final int FIRE_MAX_USES = 120;
    private static final NamespacedKey usageKey = new NamespacedKey(Slimefun.instance(), "firestaff_usage");

    @ParametersAreNonnullByDefault
    public FireStaff(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        setMaxUseCount(FIRE_MAX_USES);
    }

    @Override
    protected @Nonnull NamespacedKey getStorageKey() {
        return usageKey;
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Player p = e.getPlayer();
            ItemStack item = e.getItem();

            if (p.getFoodLevel() >= 4 || p.getGameMode() == GameMode.CREATIVE) {
                // Get a target block with max. 7 blocks of distance
                Location loc = p.getTargetBlock(null, 7).getLocation();

                if (loc.getWorld() != null && loc.getChunk().isLoaded()) {
                    if (loc.getWorld().getPVP() && Slimefun.getProtectionManager().hasPermission(p, loc, Interaction.PLACE_BLOCK)) {
                        e.cancel();
                        useItem(p, item, loc);
                    } else {
                        Slimefun.getLocalization().sendMessage(p, "messages.cannot-place", true);
                    }
                }
            } else {
                Slimefun.getLocalization().sendMessage(p, "messages.hungry", true);
            }
        };
    }

    @ParametersAreNonnullByDefault
    private void useItem(Player p, ItemStack item, Location loc) {
        World world = loc.getWorld();
        if (world != null) {
            p.getTargetBlock(null, 7).setType(Material.FIRE);
            p.getWorld().playSound(p.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, 1, 1);
            p.getWorld().spawnParticle(Particle.FLAME,p.getLocation(),1);

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
