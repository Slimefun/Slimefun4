package io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans;

import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ResurrectedTalisman extends Talisman {
    private Location respawnLocation;

    public ResurrectedTalisman(SlimefunItemStack item, ItemStack[] recipe) {
        super(item, recipe, true, true, "resurrected", new PotionEffect(PotionEffectType.GLOWING, 400, 0), new PotionEffect(PotionEffectType.ABSORPTION, 400, 4), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 20));
    }
}
