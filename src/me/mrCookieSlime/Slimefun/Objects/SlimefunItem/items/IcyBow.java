package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunBow;
import me.mrCookieSlime.Slimefun.Objects.handlers.BowShootHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class IcyBow extends SlimefunBow {

    public IcyBow(ItemStack item, String id, ItemStack[] recipe) {
        super(item, id, recipe);
    }

    @Override
    public BowShootHandler getItemHandler() {
        return (e, n) -> {
            if (SlimefunManager.isItemSimiliar(SlimefunPlugin.getUtilities().arrows.get(e.getDamager().getUniqueId()), SlimefunItems.ICY_BOW, true)) {
                n.getWorld().playEffect(n.getLocation(), Effect.STEP_SOUND, Material.ICE);
                n.getWorld().playEffect(n.getEyeLocation(), Effect.STEP_SOUND, Material.ICE);
                n.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 2, 10));
                n.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 2, -10));
                return true;
            }
            else return false;
        };
    }

}
