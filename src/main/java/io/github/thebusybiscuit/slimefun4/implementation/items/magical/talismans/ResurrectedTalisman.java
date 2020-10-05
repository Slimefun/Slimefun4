package io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans;

import com.google.gson.JsonObject;
import io.github.thebusybiscuit.slimefun4.core.handlers.EntityInteractHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
/*
*
*
* @Author svr333
*
*
* */
public class ResurrectedTalisman extends Talisman {
    private final NamespacedKey locationKey = new NamespacedKey(SlimefunPlugin.instance(), "resurrected_location");
    private Location respawnLocation;

    public ResurrectedTalisman(SlimefunItemStack item, ItemStack[] recipe) {
        super(item, recipe, true, true, "resurrected", new PotionEffect(PotionEffectType.GLOWING, 400, 0), new PotionEffect(PotionEffectType.ABSORPTION, 400, 4), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 20));
    }

    public EntityInteractHandler getItemHandler() {
        return (e, item, offhand) -> {
            Location currentLoc = e.getPlayer().getLocation();

            JsonObject json = new JsonObject();
            json.addProperty("world", currentLoc.getWorld().getUID().toString());
            json.addProperty("x", currentLoc.getX());
            json.addProperty("y", currentLoc.getY());
            json.addProperty("z", currentLoc.getZ());


            PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
            pdc.set(locationKey, PersistentDataType.STRING, json.toString());
        };
    }
}
