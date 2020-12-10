package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;

/**
 * This {@link Listener} listens to the {@link PlayerInteractEvent}.
 * It is also responsible for calling our {@link PlayerRightClickEvent} and triggering any
 * {@link ItemUseHandler} or {@link BlockUseHandler} for the clicked {@link ItemStack} or {@link Block}.
 * 
 * @author TheBusyBiscuit
 * @author Liruxo
 * 
 * @see PlayerRightClickEvent
 * @see ItemUseHandler
 * @see BlockUseHandler
 *
 */
public class SlimefunItemInteractListener implements Listener {

    public SlimefunItemInteractListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            // Exclude the Debug Fish here because it is handled in a seperate Listener
            if (SlimefunUtils.isItemSimilar(e.getItem(), SlimefunItems.DEBUG_FISH, true)) {
                return;
            }

            // Fire our custom Event
            PlayerRightClickEvent event = new PlayerRightClickEvent(e);
            Bukkit.getPluginManager().callEvent(event);

            boolean itemUsed = e.getHand() == EquipmentSlot.OFF_HAND;

            // Only handle the Item if it hasn't been denied
            if (event.useItem() != Result.DENY) {
                rightClickItem(e, event, itemUsed);
            }

            if (!itemUsed && event.useBlock() != Result.DENY && !rightClickBlock(event)) {
                return;
            }

            /**
             * If the original Event was not denied but the custom one was,
             * we also want to deny the original one.
             * This only applies for non-denied events because we do not want to
             * override any protective checks.
             */

            if (e.useInteractedBlock() != Result.DENY) {
                e.setUseInteractedBlock(event.useBlock());
            }

            if (e.useItemInHand() != Result.DENY) {
                e.setUseItemInHand(event.useItem());
            }
        }
    }

    @ParametersAreNonnullByDefault
    private boolean rightClickItem(PlayerInteractEvent e, PlayerRightClickEvent event, boolean defaultValue) {
        Optional<SlimefunItem> optional = event.getSlimefunItem();

        if (optional.isPresent()) {
            if (Slimefun.hasUnlocked(e.getPlayer(), optional.get(), true)) {
                return optional.get().callItemHandler(ItemUseHandler.class, handler -> handler.onRightClick(event));
            } else {
                event.setUseItem(Result.DENY);
            }
        }

        return defaultValue;
    }

    @ParametersAreNonnullByDefault
    private boolean rightClickBlock(PlayerRightClickEvent event) {
        Optional<SlimefunItem> optional = event.getSlimefunBlock();

        if (optional.isPresent()) {
            if (!Slimefun.hasUnlocked(event.getPlayer(), optional.get(), true)) {
                event.getInteractEvent().setCancelled(true);
                return false;
            }

            boolean interactable = optional.get().callItemHandler(BlockUseHandler.class, handler -> handler.onRightClick(event));

            if (!interactable) {
                String id = optional.get().getId();
                Player p = event.getPlayer();

                if (BlockMenuPreset.isInventory(id)) {
                    openInventory(p, id, event.getInteractEvent().getClickedBlock(), event);
                    return false;
                }
            }
        }

        return true;
    }

    @ParametersAreNonnullByDefault
    private void openInventory(Player p, String id, Block clickedBlock, PlayerRightClickEvent event) {
        if (!p.isSneaking() || event.getItem().getType() == Material.AIR) {
            event.getInteractEvent().setCancelled(true);

            if (BlockStorage.hasUniversalInventory(id)) {
                UniversalBlockMenu menu = BlockStorage.getUniversalInventory(id);

                if (menu.canOpen(clickedBlock, p)) {
                    menu.open(p);
                } else {
                    SlimefunPlugin.getLocalization().sendMessage(p, "inventory.no-access", true);
                }
            } else if (BlockStorage.getStorage(clickedBlock.getWorld()).hasInventory(clickedBlock.getLocation())) {
                BlockMenu menu = BlockStorage.getInventory(clickedBlock.getLocation());

                if (menu.canOpen(clickedBlock, p)) {
                    menu.open(p);
                } else {
                    SlimefunPlugin.getLocalization().sendMessage(p, "inventory.no-access", true);
                }
            }
        }
    }

}
