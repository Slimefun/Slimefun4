package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.Optional;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

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
public class CustomItemDataService implements PersistentDataService, Keyed {

    private final NamespacedKey namespacedKey;

    /**
     * This creates a new {@link CustomItemDataService} for the given {@link Plugin} and the
     * provided data key.
     * 
     * @param plugin
     *            The {@link Plugin} for this service to use
     * @param key
     *            The key under which to store data
     */
    public CustomItemDataService(Plugin plugin, String key) {
        // Null-Validation is performed in the NamespacedKey constructor
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

    /**
     * This method returns an {@link Optional} holding the data stored on the given {@link ItemStack}.
     * The {@link Optional} will be empty if the given {@link ItemStack} is null, doesn't have any {@link ItemMeta}
     * or if the requested data simply does not exist on that {@link ItemStack}.
     * 
     * @param item
     *            The {@link ItemStack} to check
     * 
     * @return An {@link Optional} describing the result
     */
    public Optional<String> getItemData(ItemStack item) {
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) {
            return Optional.empty();
        }

        return getItemData(item.getItemMeta());
    }

    /**
     * This method returns an {@link Optional}, either empty or holding the data stored
     * on the given {@link ItemMeta}.
     * 
     * @param meta
     *            The {@link ItemMeta} to check
     * 
     * @return An {@link Optional} describing the result
     */
    public Optional<String> getItemData(ItemMeta meta) {
        return getString(meta, namespacedKey);
    }

}
