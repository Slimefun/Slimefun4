package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunBow;
import me.mrCookieSlime.Slimefun.Objects.handlers.BowShootHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class ExplosiveBow extends SlimefunBow {

    public ExplosiveBow(ItemStack item, String id, ItemStack[] recipe) {
        super(item, id, recipe);
    }

    @Override
    public BowShootHandler getItemHandler() {
        return (e, n) -> {
            if (SlimefunManager.isItemSimiliar(SlimefunPlugin.getUtilities().arrows.get(e.getDamager().getUniqueId()), SlimefunItems.EXPLOSIVE_BOW, true)) {
                Vector vector = n.getVelocity();
                vector.setY(0.6);
                n.setVelocity(vector);
                n.getWorld().createExplosion(n.getLocation(), 0F);
                n.getWorld().playSound(n.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
                return true;
            }
            else return false;
        };
    }

}
