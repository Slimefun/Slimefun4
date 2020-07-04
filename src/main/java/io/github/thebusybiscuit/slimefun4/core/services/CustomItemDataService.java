package io.github.thebusybiscuit.slimefun4.core.services;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

/**
 * This Service is responsible for applying NBT data to a {@link SlimefunItemStack}.
 * This is used to ensure that the id of a {@link SlimefunItem} is stored alongside any
 * {@link ItemStack} at all times.
 *
 * @author TheBusyBiscuit
 * @see SlimefunItemStack
 */
public class CustomItemDataService implements PersistentDataService, Keyed {

    private final NamespacedKey namespacedKey;

    public CustomItemDataService(Plugin plugin, String key) {
        namespacedKey = new NamespacedKey(plugin, key);
    }

    @Override
    public NamespacedKey getKey() {
        return namespacedKey;
    }

    public void setItemData(ItemStack item, String id) {
        ItemMeta im = item.getItemMeta();
        setItemData(im, id);
        item.setItemMeta(im);
    }

    public void setItemData(ItemMeta im, String id) {
        setString(im, namespacedKey, id);
    }

    public Optional<String> getItemData(ItemStack item) {
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) {
            return Optional.empty();
        }
        return getItemData(item.getItemMeta());
    }

    public Optional<String> getItemData(ItemMeta meta) {
        return getString(meta, namespacedKey);
    }
}
