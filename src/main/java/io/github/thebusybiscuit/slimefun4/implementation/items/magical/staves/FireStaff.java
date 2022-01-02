package io.github.thebusybiscuit.slimefun4.implementation.items.magical.staves;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.LimitedUseItem;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class FireStaff extends LimitedUseItem {
    public static final int MAX_USES = 64;

    private static final NamespacedKey usageKey = new NamespacedKey(Slimefun.instance(), "firestaff_usage");

    @ParametersAreNonnullByDefault
    public FireStaff(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(group, item, recipeType, recipe);

        setMaxUseCount(MAX_USES);
    }

    @Override
    protected @Nonnull
    NamespacedKey getStorageKey() {
        return usageKey;
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Player p = e.getPlayer();
            ItemStack item = e.getItem();
            Location loc = p.getTargetBlock(null, 7).getLocation();

            if (p.getFoodLevel() >= 4 || p.getGameMode() == GameMode.CREATIVE) {
                // Get a target block with max. 7 blocks of distance
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


        if (!p.isSneaking()) {
            if (loc.getBlock().getType() == Material.WATER) {
                loc.getBlock().setType(Material.AIR);
                p.playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
            }

            if (loc.getBlock().getRelative(BlockFace.UP).getType().isAir()) {
                loc.getBlock().getRelative(BlockFace.UP).setType(Material.FIRE);
                p.playSound(loc, Sound.ITEM_FLINTANDSTEEL_USE,1,1);
            }

            if (loc.getBlock().getRelative(BlockFace.DOWN).getType().isAir()) {
                loc.getBlock().getRelative(BlockFace.DOWN).setType(Material.FIRE);
                p.playSound(loc, Sound.ITEM_FLINTANDSTEEL_USE,1,1);
            }

            if (loc.getBlock().getRelative(BlockFace.EAST).getType().isAir()) {
                loc.getBlock().getRelative(BlockFace.EAST).setType(Material.FIRE);
                p.playSound(loc, Sound.ITEM_FLINTANDSTEEL_USE,1,1);
            }

            if (loc.getBlock().getRelative(BlockFace.SOUTH).getType().isAir()) {
                loc.getBlock().getRelative(BlockFace.SOUTH).setType(Material.FIRE);
                p.playSound(loc, Sound.ITEM_FLINTANDSTEEL_USE,1,1);
            }

            if (loc.getBlock().getRelative(BlockFace.NORTH).getType().isAir()) {
                loc.getBlock().getRelative(BlockFace.NORTH).setType(Material.FIRE);
                p.playSound(loc, Sound.ITEM_FLINTANDSTEEL_USE,1,1);
            }

            if (loc.getBlock().getRelative(BlockFace.WEST).getType().isAir()) {
                loc.getBlock().getRelative(BlockFace.WEST).setType(Material.FIRE);
                p.playSound(loc, Sound.ITEM_FLINTANDSTEEL_USE,1,1);
            }

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


