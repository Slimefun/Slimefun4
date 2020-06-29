package io.github.thebusybiscuit.slimefun4.implementation.items.weapons;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.slimefun4.core.handlers.BowShootHandler;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class IcyBow extends SlimefunBow {

    public IcyBow(Category category, SlimefunItemStack item, ItemStack[] recipe) {
        super(category, item, recipe);
    }

    @Override
    public BowShootHandler onShoot() {
        return (e, n) -> {
            n.getWorld().playEffect(n.getLocation(), Effect.STEP_SOUND, Material.ICE);
            n.getWorld().playEffect(n.getEyeLocation(), Effect.STEP_SOUND, Material.ICE);
            n.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 2, 10));
            n.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 2, -10));
        };
    }

}
