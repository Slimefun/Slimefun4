package io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.thebusybiscuit.cscorelib2.data.PersistentJsonDataType;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;

import org.bukkit.potion.PotionEffectType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import javax.annotation.Nonnull;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import javax.annotation.Nullable;
import java.lang.Runnable;
import java.util.UUID;

/**
 * This {@link SlimefunItem} is a special variant of the {@Link Talisman}. This specific {@Link Talisman} saves you
 * from a death caused by the void.
 *
 * @author svr333
 *
 */

public class ResurrectedTalisman extends Talisman {

    private final NamespacedKey locationKey = new NamespacedKey(SlimefunPlugin.instance(), "resurrected_location");

    public ResurrectedTalisman(SlimefunItemStack item, ItemStack[] recipe) {
        super(item, recipe, true, true, "resurrected", new PotionEffect(PotionEffectType.GLOWING, 400, 0), new PotionEffect(PotionEffectType.ABSORPTION, 400, 4), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 20));

        addItemHandler(getItemHandler());
    }

    @Nonnull
    public ItemUseHandler getItemHandler() {
        return e -> {
            Location currentLoc = e.getPlayer().getLocation();
            JsonObject json = createJsonFromLocation(currentLoc);
            ItemStack item = e.getItem();

            if (SlimefunPlugin.getProtectionManager().hasPermission(e.getPlayer(), currentLoc, ProtectableAction.PLACE_BLOCK) && item.hasItemMeta()) {
                ItemMeta itemMeta = item.getItemMeta();
                
                itemMeta.getPersistentDataContainer().set(locationKey, PersistentJsonDataType.JSON_OBJECT, json);
                item.setItemMeta(itemMeta);
    
                SlimefunPlugin.getLocalization().sendMessage(e.getPlayer(), "messages.talisman.resurrected-location", true);
            }

            SlimefunPlugin.getLocalization().sendMessage(e.getPlayer(), "messages.talisman.resurrected-location-failed", true);
        };
    }

    @Nullable
    public Location getSavedLocation(@Nonnull ItemStack item) {
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        JsonObject json = pdc.get(locationKey, PersistentJsonDataType.JSON_OBJECT);

        if (json != null) {
            return parseLocationFromJsonObject(json);
        } else {
            return null;
        }
    }

    @Nonnull
    private JsonObject createJsonFromLocation(@Nonnull Location loc) {
        JsonObject json = new JsonObject();

        json.addProperty("world", loc.getWorld().getUID().toString());
        json.addProperty("x", loc.getX());
        json.addProperty("y", loc.getY());
        json.addProperty("z", loc.getZ());

        return json;
    }

    @Nullable
    private Location parseLocationFromJsonObject(@Nonnull JsonObject json) {
        UUID uuid = UUID.fromString(json.get("world").getAsString());
        World world = Bukkit.getWorld(uuid);

        if (world == null) {
            return null;
        }

        int x = json.get("x").getAsInt();
        int y = json.get("y").getAsInt();
        int z = json.get("z").getAsInt();

        return new Location(world, x, y, z);
    }
}
