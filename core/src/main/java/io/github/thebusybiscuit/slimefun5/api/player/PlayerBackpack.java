package io.github.thebusybiscuit.slimefun5.api.player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.IntSupplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.bakedlibs.dough.config.Config;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.items.backpacks.SlimefunBackpack;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.BackpackListener;
import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.PdcCompat;

/**
 * This class represents the instance of a {@link SlimefunBackpack} that is ready to
 * be opened.
 * 
 * It holds an actual {@link Inventory} and represents the backpack on the
 * level of an individual {@link ItemStack} as opposed to the class {@link SlimefunBackpack}.
 * 
 * @author TheBusyBiscuit
 *
 * @see SlimefunBackpack
 * @see BackpackListener
 */
public class PlayerBackpack {

    private static final String CONFIG_PREFIX = "backpacks.";

    private final UUID ownerId;
    private final int id;

    @Deprecated
    private PlayerProfile profile;
    @Deprecated
    private Config cfg;
    private Inventory inventory;
    private int size;

    private PlayerBackpack(@Nonnull UUID ownerId, int id, int size) {
        this.ownerId = ownerId;
        this.id = id;
        this.size = size;
    }

    /**
     * This constructor loads an existing Backpack
     * 
     * @param profile
     *            The {@link PlayerProfile} of this Backpack
     * @param id
     *            The id of this Backpack
     *
     * @deprecated Use {@link PlayerBackpack#load(UUID, int, int, HashMap)} instead
     */
    @Deprecated
    public PlayerBackpack(@Nonnull PlayerProfile profile, int id) {
        this(profile, id, profile.getConfig().getInt(CONFIG_PREFIX + id + ".size"));

        for (int i = 0; i < size; i++) {
            inventory.setItem(i, cfg.getItem(CONFIG_PREFIX + id + ".contents." + i));
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
     *
     * @deprecated Use {@link PlayerBackpack#newBackpack(UUID, int, int)} instead
     */
    @Deprecated
    public PlayerBackpack(@Nonnull PlayerProfile profile, int id, int size) {
        if (size < 9 || size > 54 || size % 9 != 0) {
            throw new IllegalArgumentException("Invalid size! Size must be one of: [9, 18, 27, 36, 45, 54]");
        }

        this.ownerId = profile.getUUID();
        this.profile = profile;
        this.id = id;
        this.cfg = profile.getConfig();
        this.size = size;

        cfg.setValue(CONFIG_PREFIX + id + ".size", size);
        markDirty();

        inventory = Bukkit.createInventory(null, size, "Backpack [" + size + " Slots]");
    }

    /**
     * This returns the id of this {@link PlayerBackpack}
     * 
     * @return The id of this {@link PlayerBackpack}
     */
    public int getId() {
        return id;
    }

    /**
     * This method returns the {@link PlayerProfile} this {@link PlayerBackpack} belongs to
     * 
     * @return The owning {@link PlayerProfile}
     * 
     * @deprecated Use {@link PlayerBackpack#getOwnerId()} instead
     */
    @Deprecated
    @Nonnull
    public PlayerProfile getOwner() {
        return profile != null ? profile : PlayerProfile.find(Bukkit.getOfflinePlayer(ownerId)).orElse(null);
    }

    public UUID getOwnerId() {
        return this.ownerId;
    }

    /**
     * This returns the size of this {@link PlayerBackpack}.
     * 
     * @return The size of this {@link PlayerBackpack}
     */
    public int getSize() {
        return size;
    }

    // ---- Backpack identity ("<owner-uuid>#<id>") --------------------------------------------------
    // Historically the identity lived in a visible "§7ID: <uuid>#<n>" lore line, which is ugly and
    // fragile (the id-only display rebuild kept dropping/re-skinning it). We now store it in the item's
    // persistent data instead - invisible, and immune to lore rewriting - while still reading the legacy
    // lore line so backpacks created by older Slimefun 4/5 versions keep working (they migrate on open).

    private static NamespacedKey identityKey;
    private static final String LEGACY_ID_PREFIX = ChatColor.GRAY + "ID: ";

    @Nonnull
    private static NamespacedKey identityKey() {
        if (identityKey == null) {
            identityKey = new NamespacedKey(Slimefun.instance(), "backpack_identity");
        }

        return identityKey;
    }

    /** The legacy visible "§7ID: <uuid>#<n>" identity, if this meta still carries one. */
    @Nonnull
    private static Optional<String> readLegacyIdentity(@Nullable ItemMeta meta) {
        if (meta == null || meta.getLore() == null) {
            return Optional.empty();
        }

        for (String line : meta.getLore()) {
            if (line.startsWith(LEGACY_ID_PREFIX) && line.indexOf('#') != -1 && !line.contains("<ID>")) {
                return Optional.of(line.substring(LEGACY_ID_PREFIX.length()));
            }
        }

        return Optional.empty();
    }

    /** The "<uuid>#<n>" identity of a backpack item: from persistent data, else a legacy lore line. */
    @Nonnull
    public static Optional<String> readIdentity(@Nullable ItemMeta meta) {
        if (meta == null) {
            return Optional.empty();
        }

        String stored = PdcCompat.getString(meta, identityKey());

        if (stored != null && stored.indexOf('#') != -1) {
            return Optional.of(stored);
        }

        return readLegacyIdentity(meta);
    }

    @Nonnull
    public static Optional<String> readIdentity(@Nullable ItemStack item) {
        return item == null ? Optional.empty() : readIdentity(item.getItemMeta());
    }

    /**
     * Stores the given "<uuid>#<n>" identity in the item's persistent data and removes any visible legacy
     * ID lore line, so the identity is hidden from the tooltip going forward.
     */
    public static void writeIdentity(@Nonnull ItemStack item, @Nonnull String identity) {
        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return;
        }

        PdcCompat.setString(meta, identityKey(), identity);

        List<String> lore = meta.getLore();

        if (lore != null) {
            lore.removeIf(line -> line.startsWith(LEGACY_ID_PREFIX));
            meta.setLore(lore.isEmpty() ? null : lore);
        }

        item.setItemMeta(meta);
    }

    /**
     * Ensures the backpack item carries a persistent identity: migrates a legacy lore id to persistent
     * data, or - if it has none at all - assigns a fresh one from {@code newIdSupplier} (only invoked in
     * that case, since creating a backpack has a side effect). No-op if it already has a stored identity.
     */
    public static void ensureIdentity(@Nonnull ItemStack item, @Nonnull UUID owner, @Nonnull IntSupplier newIdSupplier) {
        ItemMeta meta = item.getItemMeta();

        if (meta == null || PdcCompat.has(meta, identityKey(), "STRING")) {
            return;
        }

        String identity = readLegacyIdentity(meta).orElseGet(() -> owner + "#" + newIdSupplier.getAsInt());
        writeIdentity(item, identity);
    }

    /**
     * This method returns the {@link Inventory} of this {@link PlayerBackpack}
     * 
     * @return The {@link Inventory} of this {@link PlayerBackpack}
     */
    @Nonnull
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
        Slimefun.runSync(() -> {
            for (Player p : players) {
                p.openInventory(inventory);
            }
        });
    }

    /**
     * This will close the {@link Inventory} of this backpack for every {@link Player}
     * that has opened it.
     * 
     * This process has to run on the main server thread.
     * 
     * @return A {@link CompletableFuture}.
     */
    public CompletableFuture<Void> closeForAll() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        Slimefun.runSync(() -> {
            Iterator<HumanEntity> iterator = new ArrayList<>(inventory.getViewers()).iterator();

            while (iterator.hasNext()) {
                iterator.next().closeInventory();
            }

            future.complete(null);
        });

        return future;
    }

    /**
     * This will change the current size of this Backpack to the specified size.
     * 
     * @param size
     *            The new size for this Backpack
     */
    public void setSize(int size) {
        if (size < 9 || size > 54 || size % 9 != 0) {
            throw new IllegalArgumentException("Invalid size! Size must be one of: [9, 18, 27, 36, 45, 54]");
        }

        this.size = size;

        Inventory inv = Bukkit.createInventory(null, size, "Backpack [" + size + " Slots]");

        for (int slot = 0; slot < this.inventory.getSize(); slot++) {
            inv.setItem(slot, this.inventory.getItem(slot));
        }

        this.inventory = inv;

        markDirty();
    }

    /**
     * This method will save the contents of this backpack to a {@link File}.
     * 
     * @deprecated Handled by {@link PlayerProfile#save()} now
     */
    @Deprecated
    public void save() {
        for (int i = 0; i < size; i++) {
            cfg.setValue(CONFIG_PREFIX + id + ".contents." + i, inventory.getItem(i));
        }
    }

    /**
     * This method marks the backpack dirty, it will then be queued for an autosave
     * using {@link PlayerBackpack#save()}
     */
    public void markDirty() {
        if (profile != null) {
            profile.markDirty();
        }
    }

    private void setContents(int size, HashMap<Integer, ItemStack> contents) {
        if (this.inventory == null) {
            this.inventory = Bukkit.createInventory(null, size, "Backpack [" + size + " Slots]");
        }

        for (int i = 0; i < size; i++) {
            this.inventory.setItem(i, contents.get(i));
        }
    }

    @ParametersAreNonnullByDefault
    public static PlayerBackpack load(UUID ownerId, int id, int size, HashMap<Integer, ItemStack> contents) {
        PlayerBackpack backpack = new PlayerBackpack(ownerId, id, size);

        backpack.setContents(size, contents);

        return backpack;
    }

    @ParametersAreNonnullByDefault
    public static PlayerBackpack newBackpack(UUID ownerId, int id, int size) {
        if (size < 9 || size > 54 || size % 9 != 0) {
            throw new IllegalArgumentException("Invalid size! Size must be one of: [9, 18, 27, 36, 45, 54]");
        }

        PlayerBackpack backpack = new PlayerBackpack(ownerId, id, size);

        backpack.setContents(size, new HashMap<>());

        return backpack;
    }
}

