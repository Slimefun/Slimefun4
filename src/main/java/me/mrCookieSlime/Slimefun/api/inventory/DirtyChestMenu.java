package me.mrCookieSlime.Slimefun.api.inventory;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.inventory.InvUtils;
import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

public class DirtyChestMenu extends ChestMenu {

    protected final BlockMenuPreset preset;
    protected ItemManipulationEvent event;
    protected int changes = 1;

    public DirtyChestMenu(BlockMenuPreset preset) {
        super(preset.getTitle());

        this.preset = preset;
    }

    /**
     * This method checks whether this {@link DirtyChestMenu} is currently viewed by a {@link Player}.
     * 
     * @return Whether anyone is currently viewing this {@link Inventory}
     */
    public boolean hasViewer() {
        Inventory inv = toInventory();
        return inv != null && !inv.getViewers().isEmpty();
    }

    public void markDirty() {
        changes++;
    }

    public boolean isDirty() {
        return changes > 0;
    }

    public int getUnsavedChanges() {
        return changes;
    }

    public BlockMenuPreset getPreset() {
        return preset;
    }

    public boolean canOpen(Block b, Player p) {
        return preset.canOpen(b, p);
    }

    public void close() {
        for (HumanEntity human : new ArrayList<>(toInventory().getViewers())) {
            human.closeInventory();
        }
    }

    public void registerEvent(ItemManipulationEvent event) {
        this.event = event;
    }

    @Override
    public ChestMenu addMenuOpeningHandler(MenuOpeningHandler handler) {
        if (handler instanceof SaveHandler) {
            return super.addMenuOpeningHandler(new SaveHandler(this, ((SaveHandler) handler).getOpeningHandler()));
        }
        else {
            return super.addMenuOpeningHandler(new SaveHandler(this, handler));
        }
    }

    public boolean fits(ItemStack item, int... slots) {
        return InvUtils.fits(toInventory(), new ItemStackWrapper(item), slots);
    }

    public ItemStack pushItem(ItemStack item, int... slots) {
        if (item == null || item.getType() == Material.AIR) {
            throw new IllegalArgumentException("Cannot push null or AIR");
        }

        int amount = item.getAmount();

        for (int slot : slots) {
            if (amount <= 0) {
                break;
            }

            ItemStack stack = getItemInSlot(slot);

            if (stack == null) {
                replaceExistingItem(slot, item);
                return null;
            }
            else if (stack.getAmount() < stack.getMaxStackSize() && ItemUtils.canStack(item, stack)) {
                amount -= (stack.getMaxStackSize() - stack.getAmount());
                stack.setAmount(Math.min(stack.getAmount() + item.getAmount(), stack.getMaxStackSize()));
            }
        }

        if (amount > 0) {
            return new CustomItem(item, amount);
        }
        else {
            return null;
        }
    }

    public void consumeItem(int slot) {
        consumeItem(slot, 1);
    }

    public void consumeItem(int slot, int amount) {
        consumeItem(slot, amount, false);
    }

    public void consumeItem(int slot, int amount, boolean replaceConsumables) {
        ItemUtils.consumeItem(getItemInSlot(slot), amount, replaceConsumables);
        markDirty();
    }

    @Override
    public void replaceExistingItem(int slot, ItemStack item) {
        replaceExistingItem(slot, item, true);
    }

    public void replaceExistingItem(int slot, ItemStack item, boolean event) {
        if (event && this.event != null) {
            ItemStack previous = getItemInSlot(slot);
            item = this.event.onEvent(slot, previous, item);
        }

        super.replaceExistingItem(slot, item);
        markDirty();
    }

    public static class SaveHandler implements MenuOpeningHandler {

        private final DirtyChestMenu menu;
        private final MenuOpeningHandler handler;

        public SaveHandler(DirtyChestMenu menu, MenuOpeningHandler handler) {
            this.menu = menu;
            this.handler = handler;
        }

        @Override
        public void onOpen(Player p) {
            handler.onOpen(p);
            menu.markDirty();
        }

        public MenuOpeningHandler getOpeningHandler() {
            return handler;
        }

    }

}
