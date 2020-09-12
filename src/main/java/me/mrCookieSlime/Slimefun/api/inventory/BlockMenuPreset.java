package me.mrCookieSlime.Slimefun.api.inventory;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

public abstract class BlockMenuPreset extends ChestMenu {

    private final Set<Integer> occupiedSlots = new HashSet<>();
    private final String inventoryTitle;
    private final String id;

    // -1 means "automatically update according to the contents"
    private int size = -1;

    private final boolean universal;
    private boolean locked;

    private ItemManipulationEvent event;

    public BlockMenuPreset(String id, String title) {
        this(id, title, false);
    }

    public BlockMenuPreset(String id, String title, boolean universal) {
        super(title);

        this.id = id;
        this.inventoryTitle = title;
        this.universal = universal;
        init();

        SlimefunPlugin.getRegistry().getMenuPresets().put(id, this);
    }

    private void checkIfLocked() {
        if (locked) {
            throw new UnsupportedOperationException("You cannot modify the BlockMenuPreset anymore, modify the individual instances instead.");
        }
    }

    public abstract void init();

    public abstract boolean canOpen(Block b, Player p);

    public abstract int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow);

    public void registerEvent(ItemManipulationEvent event) {
        this.event = event;
    }

    public void newInstance(BlockMenu menu, Block b) {
        // This method can optionally be overridden by implementations
    }

    public int[] getSlotsAccessedByItemTransport(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
        // This method will default to that method, it can be overridden by subclasses though
        return getSlotsAccessedByItemTransport(flow);
    }

    @Override
    public void replaceExistingItem(int slot, ItemStack item) {
        throw new UnsupportedOperationException("BlockMenuPreset does not support this method.");
    }

    @Override
    public ChestMenu addItem(int slot, ItemStack item) {
        checkIfLocked();

        occupiedSlots.add(slot);
        return super.addItem(slot, item);
    }

    @Override
    public ChestMenu addMenuClickHandler(int slot, MenuClickHandler handler) {
        checkIfLocked();
        return super.addMenuClickHandler(slot, handler);
    }

    public ChestMenu setSize(int size) {
        checkIfLocked();

        if (size % 9 == 0 && size >= 0 && size < 55) {
            this.size = size;
            return this;
        }
        else {
            throw new IllegalArgumentException("The size of a BlockMenuPreset must be a multiple of 9 and within the bounds 0-54, received: " + size);
        }
    }

    /**
     * This method returns the size of this {@link BlockMenuPreset}.
     * If the size has not been determined yet, this will return -1.
     * 
     * @return The size of this {@link BlockMenuPreset}
     */
    public int getSize() {
        return size;
    }

    private boolean isSizeAutomaticallyInferred() {
        return size == -1;
    }

    /**
     * This returns the title of this {@link BlockMenuPreset}, the title will
     * be visible in every {@link InventoryView} for any menu created using this {@link BlockMenuPreset}.
     * 
     * @return The inventory title for this {@link BlockMenuPreset}
     */
    public String getTitle() {
        return inventoryTitle;
    }

    /**
     * This method returns whether this {@link BlockMenuPreset} will spawn a {@link UniversalBlockMenu}.
     * 
     * @return Whether this {@link BlockMenuPreset} is universal
     */
    public boolean isUniversal() {
        return universal;
    }

    public Set<Integer> getPresetSlots() {
        return occupiedSlots;
    }

    public Set<Integer> getInventorySlots() {
        Set<Integer> emptySlots = new HashSet<>();

        if (isSizeAutomaticallyInferred()) {
            for (int i = 0; i < toInventory().getSize(); i++) {
                if (!occupiedSlots.contains(i)) {
                    emptySlots.add(i);
                }
            }
        }
        else {
            for (int i = 0; i < size; i++) {
                if (!occupiedSlots.contains(i)) {
                    emptySlots.add(i);
                }
            }
        }

        return emptySlots;
    }

    protected void clone(DirtyChestMenu menu) {
        menu.setPlayerInventoryClickable(true);

        for (int slot : occupiedSlots) {
            menu.addItem(slot, getItemInSlot(slot));
        }

        if (size > -1) {
            menu.addItem(size - 1, null);
        }

        if (menu instanceof BlockMenu) {
            BlockMenu blockMenu = (BlockMenu) menu;
            newInstance(blockMenu, blockMenu.getLocation());
        }

        for (int slot = 0; slot < 54; slot++) {
            if (getMenuClickHandler(slot) != null) {
                menu.addMenuClickHandler(slot, getMenuClickHandler(slot));
            }
        }

        menu.addMenuOpeningHandler(getMenuOpeningHandler());
        menu.addMenuCloseHandler(getMenuCloseHandler());
        menu.registerEvent(event);
    }

    public void newInstance(BlockMenu menu, Location l) {
        Validate.notNull(l, "Cannot create a new BlockMenu without a Location");

        Slimefun.runSync(() -> {
            locked = true;

            try {
                newInstance(menu, l.getBlock());
            }
            catch (Exception | LinkageError x) {
                getSlimefunItem().error("An Error occurred while trying to create a BlockMenu", x);
            }
        });
    }

    /**
     * This returns the id of the associated {@link SlimefunItem}.
     * It also doubles as the id for this {@link BlockMenuPreset}.
     * 
     * @return Our identifier
     */
    public String getID() {
        return id;
    }

    /**
     * This returns the {@link SlimefunItem} associated with this {@link BlockMenuPreset}.
     * 
     * @return The associated {@link SlimefunItem}
     */
    public SlimefunItem getSlimefunItem() {
        return SlimefunItem.getByID(id);
    }

    public static BlockMenuPreset getPreset(String id) {
        return id == null ? null : SlimefunPlugin.getRegistry().getMenuPresets().get(id);
    }

    public static boolean isInventory(String id) {
        return SlimefunPlugin.getRegistry().getMenuPresets().containsKey(id);
    }

    public static boolean isUniversalInventory(String id) {
        BlockMenuPreset preset = SlimefunPlugin.getRegistry().getMenuPresets().get(id);
        return preset != null && preset.isUniversal();
    }

}
