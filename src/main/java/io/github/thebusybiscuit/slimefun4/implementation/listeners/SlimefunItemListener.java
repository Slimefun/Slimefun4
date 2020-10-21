package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemDropHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;

public class SlimefunItemListener implements Listener {

    public SlimefunItemListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (SlimefunUtils.isItemSimilar(e.getItem(), SlimefunItems.DEBUG_FISH, true)) {
                return;
            }

            PlayerRightClickEvent event = new PlayerRightClickEvent(e);
            Bukkit.getPluginManager().callEvent(event);

            boolean itemUsed = e.getHand() == EquipmentSlot.OFF_HAND;

            if (event.useItem() != Result.DENY) {
                rightClickItem(e, event, itemUsed);
            }

            if (!itemUsed && event.useBlock() != Result.DENY && !rightClickBlock(e, event)) {
                return;
            }

            if (e.useInteractedBlock() != Result.DENY) {
                e.setUseInteractedBlock(event.useBlock());
            }

            if (e.useItemInHand() != Result.DENY) {
                e.setUseItemInHand(event.useItem());
            }
        }
    }

    @Nonnull
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
    private boolean rightClickBlock(PlayerInteractEvent e, PlayerRightClickEvent event) {
        Optional<SlimefunItem> optional = event.getSlimefunBlock();

        if (optional.isPresent()) {
            if (!Slimefun.hasUnlocked(e.getPlayer(), optional.get(), true)) {
                e.setCancelled(true);
                return false;
            }

            boolean interactable = optional.get().callItemHandler(BlockUseHandler.class, handler -> handler.onRightClick(event));

            if (!interactable) {
                String id = optional.get().getId();
                Player p = e.getPlayer();

                if (BlockMenuPreset.isInventory(id)) {
                    openInventory(p, id, e, event);
                    return false;
                }
            }
        }

        return true;
    }

    @ParametersAreNonnullByDefault
    private void openInventory(Player p, String id, PlayerInteractEvent e, PlayerRightClickEvent event) {
        if (!p.isSneaking() || Material.AIR == event.getItem().getType()) {
            e.setCancelled(true);

            if (BlockStorage.hasUniversalInventory(id)) {
                UniversalBlockMenu menu = BlockStorage.getUniversalInventory(id);

                if (menu.canOpen(e.getClickedBlock(), p)) {
                    menu.open(p);
                } else {
                    SlimefunPlugin.getLocalization().sendMessage(p, "inventory.no-access", true);
                }
            } else if (BlockStorage.getStorage(e.getClickedBlock().getWorld()).hasInventory(e.getClickedBlock().getLocation())) {
                BlockMenu menu = BlockStorage.getInventory(e.getClickedBlock().getLocation());

                if (menu.canOpen(e.getClickedBlock(), p)) {
                    menu.open(p);
                } else {
                    SlimefunPlugin.getLocalization().sendMessage(p, "inventory.no-access", true);
                }
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        for (ItemHandler handler : SlimefunItem.getPublicItemHandlers(ItemDropHandler.class)) {
            if (((ItemDropHandler) handler).onItemDrop(e, e.getPlayer(), e.getItemDrop())) {
                return;
            }
        }
    }

}
