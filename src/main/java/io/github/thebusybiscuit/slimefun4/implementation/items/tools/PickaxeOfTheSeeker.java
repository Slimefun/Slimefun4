package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class PickaxeOfTheSeeker extends SimpleSlimefunItem<ItemUseHandler> implements DamageableItem {

    public PickaxeOfTheSeeker(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Player p = e.getPlayer();
            Block closest = findClosestOre(p);

            e.setUseBlock(Result.DENY);

            if (closest == null) {
                SlimefunPlugin.getLocalization().sendMessage(p, "miner.no-ores");
            }
            else {
                double l = closest.getX() + 0.5 - p.getLocation().getX();
                double w = closest.getZ() + 0.5 - p.getLocation().getZ();

                double c = Math.sqrt(l * l + w * w);
                float alpha1 = (float) -(Math.asin(l / c) / Math.PI * 180);
                float alpha2 = (float) (Math.acos(w / c) / Math.PI * 180);

                float yaw = alpha2 > 90 ? (180 - alpha1) : alpha1;
                float pitch = (float) ((-Math.atan((closest.getY() - 0.5 - p.getLocation().getY()) / Math.sqrt(l * l + w * w))) * 180 / Math.PI);

                p.teleport(new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), yaw, pitch));
            }

            damageItem(p, e.getItem());
        };
    }

    private Block findClosestOre(Player p) {
        Block closest = null;

        for (int x = -4; x <= 4; x++) {
            for (int y = -4; y <= 4; y++) {
                for (int z = -4; z <= 4; z++) {
                    if (MaterialCollections.getAllOres().contains(p.getLocation().add(x, y, z).getBlock().getType()) && (closest == null || p.getLocation().distanceSquared(closest.getLocation()) > p.getLocation().distanceSquared(p.getLocation().add(x, y, z)))) {
                        closest = p.getLocation().getBlock().getRelative(x, y, z);
                    }
                }
            }
        }

        return closest;
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

}
