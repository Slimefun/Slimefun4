package io.github.thebusybiscuit.slimefun5.nms.v1_21_R1;

import io.github.thebusybiscuit.slimefun5.compat.SlimefunNMS;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class SlimefunNMSImpl implements SlimefunNMS {

    private NamespacedKey getKey(String key) {
        // NamespacedKey must be lowercase
        return new NamespacedKey("slimefun", key.toLowerCase());
    }

    @Override
    public String getNBTString(ItemStack item, String key) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.get(getKey(key), PersistentDataType.STRING);
    }

    @Override
    public ItemStack setNBTString(ItemStack item, String key, String value) {
        if (item == null || item.getType().isAir()) {
            return item;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(getKey(key), PersistentDataType.STRING, value);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void setBlockTypeFast(Block block, Material material) {
        block.setType(material, false);
    }

    @Override
    public void breakBlockFast(Block block) {
        block.setType(Material.AIR, false);
    }
}
