package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.Optional;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.data.PersistentDataAPI;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * This Service is responsible for applying NBT data to a {@link SlimefunItemStack}.
 * This is used to ensure that the id of a {@link SlimefunItem} is stored alongside any
 * {@link ItemStack} at all times.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SlimefunItemStack
 *
 */
public class CustomItemDataService {

    private final NamespacedKey namespacedKey;

    public CustomItemDataService(Plugin plugin, String key) {
        namespacedKey = new NamespacedKey(plugin, key);
    }

    public void setItemData(ItemStack item, String id) {
        ItemMeta im = item.getItemMeta();
        setItemData(im, id);
        item.setItemMeta(im);
    }

    public void setItemData(ItemMeta im, String id) {
        PersistentDataAPI.setString(im, namespacedKey, id);
    }

    public Optional<String> getItemData(ItemStack item) {
        return getItemData(item.getItemMeta());
    }

    public Optional<String> getItemData(ItemMeta meta) {
        return PersistentDataAPI.getOptionalString(meta, namespacedKey);
    }

}
