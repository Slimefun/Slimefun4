package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.VanillaItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This {@link Listener} prevents any {@link SlimefunItem} from being used in a vanilla
 * machine like the workbench, grindstone, brewing stand or an anvil.
 * 
 * @author TheBusyBiscuit
 * @author NathanAdhitya
 * @author Steve
 * @author VoidAngel
 *
 */
public class VanillaMachinesListener implements Listener {

    public VanillaMachinesListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onGrindstone(InventoryClickEvent e) {
        // The Grindstone was only ever added in MC 1.14
        MinecraftVersion minecraftVersion = SlimefunPlugin.getMinecraftVersion();
        if (!minecraftVersion.isAtLeast(MinecraftVersion.MINECRAFT_1_14)) {
            return;
        }

        if (e.getRawSlot() == 2 && e.getWhoClicked() instanceof Player && e.getInventory().getType() == InventoryType.GRINDSTONE) {
            ItemStack item1 = e.getInventory().getContents()[0];
            ItemStack item2 = e.getInventory().getContents()[1];

            if (checkForUnallowedItems(item1, item2)) {
                e.setResult(Result.DENY);
            }
        }

    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        for (ItemStack item : e.getInventory().getContents()) {
            SlimefunItem sfItem = SlimefunItem.getByItem(item);

            if (sfItem != null && !sfItem.isUseableInWorkbench()) {
                e.setResult(Result.DENY);
                SlimefunPlugin.getLocalization().sendMessage((Player) e.getWhoClicked(), "workbench.not-enhanced", true);
                break;
            }
        }
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent e) {
        if (e.getInventory().getResult() != null) {
            for (ItemStack item : e.getInventory().getContents()) {
                SlimefunItem sfItem = SlimefunItem.getByItem(item);

                if (sfItem != null && !sfItem.isUseableInWorkbench()) {
                    e.getInventory().setResult(null);
                    break;
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onAnvil(InventoryClickEvent e) {
        if (e.getRawSlot() == 2 && e.getInventory().getType() == InventoryType.ANVIL && e.getWhoClicked() instanceof Player) {
            ItemStack item1 = e.getInventory().getContents()[0];
            ItemStack item2 = e.getInventory().getContents()[1];

            if (checkForUnallowedItems(item1, item2)) {
                e.setResult(Result.DENY);
                SlimefunPlugin.getLocalization().sendMessage((Player) e.getWhoClicked(), "anvil.not-working", true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPreBrew(InventoryClickEvent e) {
        Inventory clickedInventory = e.getClickedInventory();
        Inventory topInventory = e.getView().getTopInventory();

        if (clickedInventory != null && topInventory.getType() == InventoryType.BREWING && topInventory.getHolder() instanceof BrewingStand) {
            if (e.getAction() == InventoryAction.HOTBAR_SWAP) {
                e.setCancelled(true);
                return;
            }

            if (clickedInventory.getType() == InventoryType.BREWING) {
                e.setCancelled(isUnallowed(SlimefunItem.getByItem(e.getCursor())));
            } else {
                e.setCancelled(isUnallowed(SlimefunItem.getByItem(e.getCurrentItem())));
            }

            if (e.getResult() == Result.DENY) {
                SlimefunPlugin.getLocalization().sendMessage((Player) e.getWhoClicked(), "brewing_stand.not-working", true);
            }
        }
    }

    private boolean checkForUnallowedItems(ItemStack item1, ItemStack item2) {
        if (SlimefunGuide.isGuideItem(item1) || SlimefunGuide.isGuideItem(item2)) {
            return true;
        }
        else {
            SlimefunItem sfItem1 = SlimefunItem.getByItem(item1);
            SlimefunItem sfItem2 = SlimefunItem.getByItem(item2);

            if (isUnallowed(sfItem1) || isUnallowed(sfItem2)) {
                return true;
            }
        }

        return false;
    }

    private boolean isUnallowed(SlimefunItem item) {
        return item != null && !(item instanceof VanillaItem) && !item.isDisabled();
    }
}
