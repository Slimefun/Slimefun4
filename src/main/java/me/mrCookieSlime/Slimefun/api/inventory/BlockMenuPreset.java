package me.mrCookieSlime.Slimefun.api.inventory;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

public abstract class BlockMenuPreset extends ChestMenu {

    private String inventoryTitle;
    private Set<Integer> occupied = new HashSet<>();
    private String id;
    private int size = -1;
    private boolean universal;

    private ItemManipulationEvent event;

    public BlockMenuPreset(String id, String title) {
        this(id, title, false);
    }

    public BlockMenuPreset(String id, String title, boolean universal) {
        super(title);

        this.id = id;
        this.inventoryTitle = title;
        this.init();
        this.universal = universal;
        SlimefunPlugin.getRegistry().getMenuPresets().put(id, this);
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
    public ChestMenu addItem(int slot, ItemStack item) {
        occupied.add(slot);
        return super.addItem(slot, item);
    }

    public ChestMenu setSize(int size) {
        this.size = size;
        return this;
    }

    public int getSize() {
        return size;
    }

    public String getTitle() {
        return inventoryTitle;
    }

    public Set<Integer> getPresetSlots() {
        return occupied;
    }

    public Set<Integer> getInventorySlots() {
        Set<Integer> empty = new HashSet<>();

        if (size > -1) {
            for (int i = 0; i < size; i++) {
                if (!occupied.contains(i)) empty.add(i);
            }
        }
        else {
            for (int i = 0; i < toInventory().getSize(); i++) {
                if (!occupied.contains(i)) empty.add(i);
            }
        }
        return empty;
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

    public boolean isUniversal() {
        return universal;
    }

    protected void clone(DirtyChestMenu menu) {
        menu.setPlayerInventoryClickable(true);

        for (int slot : occupied) {
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
            if (getMenuClickHandler(slot) != null) menu.addMenuClickHandler(slot, getMenuClickHandler(slot));
        }

        menu.addMenuOpeningHandler(getMenuOpeningHandler());
        menu.addMenuCloseHandler(getMenuCloseHandler());
        menu.registerEvent(this.event);
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

    public void newInstance(BlockMenu menu, Location l) {
        Slimefun.runSync(() -> {
            try {
                newInstance(menu, l.getBlock());
            }
            catch (Throwable x) {
                getSlimefunItem().error("An eror occured while trying to create a BlockMenu", x);
            }
        });
    }

}
