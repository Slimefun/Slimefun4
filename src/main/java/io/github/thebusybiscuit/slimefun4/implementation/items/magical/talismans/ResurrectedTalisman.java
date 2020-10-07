package io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * This {@link SlimefunItem} is a special variant of the {@Link Talisman}. This specific {@Link Talisman} saves you
 * from a death caused by the void.
 *
 * @author svr333
 *
 */

public class ResurrectedTalisman extends Talisman {
    private static final NamespacedKey locationKey = new NamespacedKey(SlimefunPlugin.instance(), "resurrected_location");

    public ResurrectedTalisman(SlimefunItemStack item, ItemStack[] recipe) {
        super(item, recipe, true, true, "resurrected", new PotionEffect(PotionEffectType.GLOWING, 400, 0), new PotionEffect(PotionEffectType.ABSORPTION, 400, 4), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 20));

        addItemHandler(getItemHandler());
    }

    public ItemUseHandler getItemHandler() {
        return e -> {
            Location currentLoc = e.getPlayer().getLocation();
            JsonObject json = createJsonFromLocation(currentLoc);

            PersistentDataContainer pdc = e.getItem().getItemMeta().getPersistentDataContainer();
            pdc.set(locationKey, PersistentDataType.STRING, json.toString());

            SlimefunPlugin.getLocalization().sendMessage(e.getPlayer(), "messages.talisman.resurrected-location", true);
        };
    }

    @Nullable
    public static Location getSavedLocation(ItemStack item) {
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        String data = pdc.get(locationKey, PersistentDataType.STRING);
        /* Data here is always null, it doesnt get saved properly */

        if (data != null) {
            return parseLocationFromJson(data);
        }
        else {
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
    private static Location parseLocationFromJson(@Nonnull String rawData) {
        JsonObject json = new JsonParser().parse(rawData).getAsJsonObject();

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
