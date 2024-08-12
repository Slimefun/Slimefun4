package me.mrCookieSlime.Slimefun.api.inventory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

import io.github.bakedlibs.dough.blocks.BlockPosition;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

// This class will be deprecated, relocated and rewritten in a future version.
public class BlockMenu extends DirtyChestMenu {

    private BlockPosition position;

    private static String serializePosition(BlockPosition position) {
        return position.getWorld().getName() + ';' + position.getX() + ';' + position.getY() + ';' + position.getZ();
    }

    /**
     * @deprecated
     * @see BlockMenu#BlockMenu(BlockMenuPreset, BlockPosition)
     */
    @Deprecated
    public BlockMenu(BlockMenuPreset preset, Location l) {
        this(preset, new BlockPosition(l));
    }

    /**
     * @deprecated
     * @see BlockMenu#BlockMenu(BlockMenuPreset, BlockPosition, Config)
     */
    @Deprecated
    public BlockMenu(BlockMenuPreset preset, Location l, Config cfg) {
        this(preset, new BlockPosition(l), cfg);
    }

    public BlockMenu(BlockMenuPreset preset, BlockPosition position) {
        super(preset);
        this.position = position;

        preset.clone(this);
        this.getContents();
    }

    public BlockMenu(BlockMenuPreset preset, BlockPosition position, Config cfg) {
        super(preset);
        this.position = position;

        for (int i = 0; i < 54; i++) {
            if (cfg.contains(String.valueOf(i))) {
                addItem(i, cfg.getItem(String.valueOf(i)));
            }
        }

        preset.clone(this);

        if (preset.getSize() > -1 && !preset.getPresetSlots().contains(preset.getSize() - 1) && cfg.contains(String.valueOf(preset.getSize() - 1))) {
            addItem(preset.getSize() - 1, cfg.getItem(String.valueOf(preset.getSize() - 1)));
        }

        this.getContents();
    }

    /**
     * @deprecated
     * @see BlockMenu#save(BlockPosition)
     */
    @Deprecated
    public void save(Location l) {
        save(new BlockPosition(l));
    }

    public void save(BlockPosition position) {
        if (!isDirty()) {
            return;
        }

        // To force CS-CoreLib to build the Inventory
        this.getContents();

        File file = new File("data-storage/Slimefun/stored-inventories/" + serializePosition(position) + ".sfi");
        Config cfg = new Config(file);
        cfg.setValue("preset", preset.getID());

        for (int slot : preset.getInventorySlots()) {
            cfg.setValue(String.valueOf(slot), getItemInSlot(slot));
        }

        cfg.save();

        changes = 0;
    }

    /**
     * @deprecated
     * @see BlockMenu#move(BlockPosition)
     */
    @Deprecated
    public void move(Location l) {
        move(new BlockPosition(l));
    }

    public void move(BlockPosition position) {
        this.delete(this.position);
        this.position = position;
        this.preset.newInstance(this, position);
        this.save(position);
    }

    /**
     * Reload this {@link BlockMenu} based on its {@link BlockMenuPreset}.
     */
    public void reload() {
        this.preset.clone(this);
    }

    public Block getBlock() {
        return position.getBlock();
    }

    /**
     * @deprecated
     * @see BlockMenu#getPosition()
     */
    @Deprecated
    public Location getLocation() {
        return position.toLocation();
    }

    public BlockPosition getPosition() {
        return position;
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

    /**
     * @deprecated
     * @see BlockMenu#delete(BlockPosition)
     */
    @Deprecated
    public void delete(Location l) {
        delete(new BlockPosition(l));
    }

    public void delete(BlockPosition position) {
        File file = new File("data-storage/Slimefun/stored-inventories/" + serializePosition(position) + ".sfi");

        if (file.exists()) {
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
                Slimefun.logger().log(Level.WARNING, e, () -> "Could not delete file \"" + file.getName() + '"');
            }
        }
    }
}
