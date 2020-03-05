package io.github.thebusybiscuit.slimefun4.api.player;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.SlimefunBackpack;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.BackpackListener;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;

/**
 * This class represents the instance of a {@link SlimefunBackpack} that is ready to
 * be opened.
 * 
 * It holds an actual {@link Inventory} and represents the backpack on the
 * level of an individual{@link ItemStack} as opposed to the class {@link SlimefunBackpack}.
 * 
 * @author TheBusyBiscuit
 *
 * @see SlimefunBackpack
 * @see BackpackListener
 */
public class PlayerBackpack {

    private final PlayerProfile profile;
    private final int id;
    private final Config cfg;

    private Inventory inventory;
    private int size;

    /**
     * This constructor loads an existing Backpack
     * 
     * @param profile
     *            The {@link PlayerProfile} of this Backpack
     * @param id
     *            The id of this Backpack
     */
    public PlayerBackpack(PlayerProfile profile, int id) {
        this(profile, id, profile.getConfig().getInt("backpacks." + id + ".size"));

        for (int i = 0; i < size; i++) {
            inventory.setItem(i, cfg.getItem("backpacks." + id + ".contents." + i));
        }
    }

    /**
     * This constructor creates a new Backpack
     * 
     * @param profile
     *            The {@link PlayerProfile} of this Backpack
     * @param id
     *            The id of this Backpack
     * @param size
     *            The size of this Backpack
     */
    public PlayerBackpack(PlayerProfile profile, int id, int size) {
        this.profile = profile;
        this.id = id;
        this.cfg = profile.getConfig();
        this.size = size;

        cfg.setValue("backpacks." + id + ".size", size);
        profile.markDirty();

        inventory = Bukkit.createInventory(null, size, "Backpack [" + size + " Slots]");
    }

    public int getID() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public Inventory getInventory() {
        return inventory;
    }

    /**
     * This will open the {@link Inventory} of this backpack to every {@link Player}
     * that was passed onto this method.
     * 
     * @param players
     *            The players who this Backpack will be shown to
     */
    public void open(Player... players) {
        for (Player p : players) {
            p.openInventory(inventory);
        }
    }

    /**
     * This will change the current size of this Backpack to the specified size.
     * 
     * @param size
     *            The new size for this Backpack
     */
    public void setSize(int size) {
        this.size = size;
        cfg.setValue("backpacks." + id + ".size", size);

        Inventory inv = Bukkit.createInventory(null, size, "Backpack [" + size + " Slots]");

        for (int slot = 0; slot < this.inventory.getSize(); slot++) {
            inv.setItem(slot, this.inventory.getItem(slot));
        }

        this.inventory = inv;

        markDirty();
    }

    /**
     * This method will save the contents of this backpack to a {@link File}.
     */
    public void save() {
        for (int i = 0; i < size; i++) {
            cfg.setValue("backpacks." + id + ".contents." + i, inventory.getItem(i));
        }
    }

    /**
     * This method marks the backpack dirty, it will then be queued for an autosave
     * using {@link PlayerBackpack#save()}
     */
    public void markDirty() {
        profile.markDirty();
    }

}
