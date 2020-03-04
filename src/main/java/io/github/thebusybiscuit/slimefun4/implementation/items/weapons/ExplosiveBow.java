package io.github.thebusybiscuit.slimefun4.implementation.items.weapons;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.mrCookieSlime.Slimefun.Objects.handlers.BowShootHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class ExplosiveBow extends SlimefunBow {

    public ExplosiveBow(SlimefunItemStack item, ItemStack[] recipe) {
        super(item, recipe);
    }

    @Override
    public BowShootHandler onShoot() {
        return (e, n) -> {
            Vector vector = n.getVelocity();
            vector.setY(0.6);
            n.setVelocity(vector);
            n.getWorld().createExplosion(n.getLocation(), 0F);
            n.getWorld().playSound(n.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
        };
    }

}
