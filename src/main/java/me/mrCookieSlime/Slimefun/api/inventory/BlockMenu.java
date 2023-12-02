package me.mrCookieSlime.Slimefun.api.inventory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import com.jeff_media.customblockdata.CustomBlockData;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import me.mrCookieSlime.Slimefun.api.serializers.ItemStackSerializer;

// This class will be deprecated, relocated and rewritten in a future version.
public class BlockMenu extends DirtyChestMenu {

    private Location location;

    public BlockMenu(@Nonnull BlockMenuPreset preset, Location l) {
        super(preset);
        this.location = l;
        Block b = l.getBlock();
        
        Plugin plugin = Slimefun.getPlugin(Slimefun.class);
        CustomBlockData blockData = new CustomBlockData(b, plugin);

        for (int i = 0; i < 54; i++) {
            if (blockData.has(new NamespacedKey(plugin, String.valueOf(i)))) {
                byte[] encodedItem = blockData.get(new NamespacedKey(plugin, String.valueOf(i)), PersistentDataType.BYTE_ARRAY);
                try {
                    final ItemStack itemStack = ItemStackSerializer.fromBytes(encodedItem);
                    addItem(i, itemStack);
                } catch (IOException | ClassNotFoundException e) {
                    Slimefun.logger().log(Level.SEVERE, "Could not deserialize ItemStack from byte array: {0}", e.getStackTrace());
                }
        }
        }
        preset.clone(this);

        this.getContents();
    }

    public void save(Location l) {
        if (!isDirty()) {
            return;
        }

        Block b = l.getBlock();
        if (b == null) {
            return;
        }

        // To force CS-CoreLib to build the Inventory
        this.getContents();

        Plugin plugin = Slimefun.getPlugin(Slimefun.class);
        CustomBlockData blockData = new CustomBlockData(b, plugin);
        blockData.set(new NamespacedKey(plugin, "preset"), PersistentDataType.STRING, preset.getID());

        for (int slot : preset.getInventorySlots()) {
            byte[] encodedItem;
            try {
                encodedItem = ItemStackSerializer.toBytes(getItemInSlot(slot));
            } catch (IOException e) {
                Bukkit.getLogger().severe("Could not convert ItemStack to byte array:");
                e.printStackTrace();
                return;
            }
            blockData.set(
                new NamespacedKey(plugin, String.valueOf(slot)),
                PersistentDataType.BYTE_ARRAY,
                encodedItem
            );
        }

        changes = 0;
    }

    public void move(Location l) {
        this.delete(this.location);
        this.location = l;
        this.preset.newInstance(this, l);
        this.save(l);
    }

    /**
     * Reload this {@link BlockMenu} based on its {@link BlockMenuPreset}.
     */
    public void reload() {
        this.preset.clone(this);
    }

    public Block getBlock() {
        return location.getBlock();
    }

    public Location getLocation() {
        return location;
    }

    /**
     * This method drops the contents of this {@link BlockMenu} on the ground at the given
     * {@link Location}.
     * 
     * @param l
     *            Where to drop these items
     * @param slots
     *            The slots of items that should be dropped
     */
    public void dropItems(Location l, int... slots) {
        for (int slot : slots) {
            ItemStack item = getItemInSlot(slot);

            if (item != null) {
                l.getWorld().dropItemNaturally(l, item);
                replaceExistingItem(slot, null);
            }
        }
    }

    public void delete(Location l) {
        Block b = l.getBlock();
        if (b == null) {
            return;
        }

        Plugin plugin = Slimefun.getPlugin(Slimefun.class);
        CustomBlockData blockData = new CustomBlockData(b, plugin);
        blockData.clear();
    }
}
