package me.mrCookieSlime.Slimefun.api.inventory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.config.Config;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

import me.mrCookieSlime.Slimefun.api.BlockStorage;

// This class will be deprecated, relocated and rewritten in a future version.
public class BlockMenu extends DirtyChestMenu {

    private Location location;

    private static String serializeLocation(Location l) {
        return l.getWorld().getName() + ';' + l.getBlockX() + ';' + l.getBlockY() + ';' + l.getBlockZ();
    }

    public BlockMenu(BlockMenuPreset preset, Location l) {
        super(preset);
        this.location = l;

        preset.clone(this);
        this.getContents();
    }

    public BlockMenu(BlockMenuPreset preset, Location l, Config cfg) {
        super(preset);
        this.location = l;

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

    public void save(Location l) {
        if (!isDirty()) {
            return;
        }

        // To force CS-CoreLib to build the Inventory
        this.getContents();

        File file = new File("data-storage/Slimefun/stored-inventories/" + serializeLocation(l) + ".sfi");
        Config cfg = new Config(file);
        cfg.setValue("preset", preset.getID());

        for (int slot : preset.getInventorySlots()) {
            cfg.setValue(String.valueOf(slot), getItemInSlot(slot));
        }

        cfg.save();

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

    @Override
    public void open(Player... players) {
        // Re-skin the decorative info items into the opening player's language before showing.
        if (players.length == 1) {
            try {
                Slimefun.getMenuTranslationService().applyToMenu(this, players[0]);
            } catch (Exception | LinkageError ignored) {
                // Translation is cosmetic - never block the menu from opening.
            }
        }

        // Machine outputs are cloned from the recipe before the display is baked onto item templates, so
        // they can sit in a slot showing only their bare material name; re-skin the menu's items to the
        // server default now so they render correctly here (a player taking one re-skins it to their own
        // language on pickup).
        translateContents();

        // Mark this block as viewed so the (async) ticker runs its mutations on the main thread while a
        // player is looking - closing the async-tick-vs-click dupe. Unmarked on close (BlockMenuListener).
        BlockStorage.setInventoryViewed(location, true);

        super.open(players);
    }

    private void translateContents() {
        org.bukkit.inventory.Inventory inv = toInventory();

        if (inv == null) {
            return;
        }

        for (int slot = 0; slot < inv.getSize(); slot++) {
            ItemStack stack = inv.getItem(slot);

            if (stack != null && Slimefun.getItemTranslationService().applyServerDefaultTranslation(stack)) {
                inv.setItem(slot, stack);
            }
        }
    }

    @Override
    public ItemStack pushItem(ItemStack item, int... slots) {
        // Bring a freshly-produced output to its proper display the moment it lands, but only while a
        // player is watching this machine - so the common (unwatched) path, incl. cargo inserts, pays
        // nothing beyond the viewed-set check.
        if (item != null && BlockStorage.isInventoryViewed(location)) {
            Slimefun.getItemTranslationService().applyServerDefaultTranslation(item);
        }

        return super.pushItem(item, slots);
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
        File file = new File("data-storage/Slimefun/stored-inventories/" + serializeLocation(l) + ".sfi");

        if (file.exists()) {
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
                Slimefun.logger().log(Level.WARNING, e, () -> "Could not delete file \"" + file.getName() + '"');
            }
        }
    }
}

