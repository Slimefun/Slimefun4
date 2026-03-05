package io.github.thebusybiscuit.slimefun4.test.mocks;

import be.seeseemelk.mockbukkit.inventory.InventoryViewMock;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Temporary class which implements {@link #getItem(int)} and {@link #setItem(int, ItemStack)}
 * provided {@link #getInventory(int)} and {@link #convertSlot(int)} are implemented by the backing
 * {@link InventoryView}
 * <p>
 * This class should be replaced by MockBukkit when <a href="https://github.com/MockBukkit/MockBukkit/pull/1011">this pr</a>
 * is merged.
 * <br>
 * Code is taken directly from CraftBukkit <a href="https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/src/main/java/org/bukkit/craftbukkit/inventory/CraftAbstractInventoryView.java">here</a>.
 *
 * @author md5sha256
 */
public class InventoryViewWrapper extends InventoryViewMock {

    private InventoryViewWrapper(HumanEntity player,
                                 String name,
                                 Inventory top,
                                 Inventory bottom,
                                 InventoryType type) {
        super(player, name, top, bottom, type);
    }

    @Nonnull
    public static InventoryViewWrapper wrap(@Nonnull InventoryView inventoryView) {
        HumanEntity player = inventoryView.getPlayer();
        String name = inventoryView.getTitle();
        Inventory top = inventoryView.getTopInventory();
        Inventory bottom = inventoryView.getBottomInventory();
        InventoryType inventoryType = inventoryView.getType();
        return new InventoryViewWrapper(player, name, top, bottom, inventoryType);
    }

    @Override
    @Nullable
    public ItemStack getItem(int slot) {
        Inventory inventory = getInventory(slot);
        return (inventory == null) ? null : inventory.getItem(convertSlot(slot));
    }

    @Override
    public void setItem(int slot, @Nullable ItemStack item) {
        Inventory inventory = getInventory(slot);
        if (inventory != null) {
            inventory.setItem(convertSlot(slot), item);
        }
    }
}
