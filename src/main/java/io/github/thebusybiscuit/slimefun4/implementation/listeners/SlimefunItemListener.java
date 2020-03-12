package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockUseHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemDropHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;

public class SlimefunItemListener implements Listener {

    public SlimefunItemListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            PlayerRightClickEvent event = new PlayerRightClickEvent(e);
            Bukkit.getPluginManager().callEvent(event);

            boolean itemUsed = e.getHand() == EquipmentSlot.OFF_HAND;

            if (event.useItem() != Result.DENY) {
                Optional<SlimefunItem> optional = event.getSlimefunItem();

                if (optional.isPresent() && Slimefun.hasUnlocked(e.getPlayer(), optional.get(), true)) {
                    itemUsed = optional.get().callItemHandler(ItemUseHandler.class, handler -> handler.onRightClick(event));
                }
            }

            if (!itemUsed && event.useBlock() != Result.DENY) {
                Optional<SlimefunItem> optional = event.getSlimefunBlock();

                if (optional.isPresent()) {
                    if (!Slimefun.hasUnlocked(e.getPlayer(), optional.get(), true)) {
                        e.setCancelled(true);
                        return;
                    }

                    boolean interactable = optional.get().callItemHandler(BlockUseHandler.class, handler -> handler.onRightClick(event));

                    if (!interactable) {
                        String id = optional.get().getID();
                        Player p = e.getPlayer();
                        ItemStack item = event.getItem();

                        if (BlockMenuPreset.isInventory(id) && !canPlaceCargoNodes(p, item, e.getClickedBlock().getRelative(e.getBlockFace()))) {
                            e.setCancelled(true);

                            if (!p.isSneaking() || item == null) {
                                if (BlockStorage.hasUniversalInventory(id)) {
                                    UniversalBlockMenu menu = BlockStorage.getUniversalInventory(id);

                                    if (menu.canOpen(e.getClickedBlock(), p)) {
                                        menu.open(p);
                                    }
                                    else {
                                        SlimefunPlugin.getLocal().sendMessage(p, "inventory.no-access", true);
                                    }
                                }
                                else if (BlockStorage.getStorage(e.getClickedBlock().getWorld()).hasInventory(e.getClickedBlock().getLocation())) {
                                    BlockMenu menu = BlockStorage.getInventory(e.getClickedBlock().getLocation());

                                    if (menu.canOpen(e.getClickedBlock(), p)) {
                                        menu.open(p);
                                    }
                                    else {
                                        SlimefunPlugin.getLocal().sendMessage(p, "inventory.no-access", true);
                                    }
                                }
                            }

                            return;
                        }
                    }
                }
            }

            if (e.useInteractedBlock() != Result.DENY) {
                e.setUseInteractedBlock(event.useBlock());
            }

            if (e.useItemInHand() != Result.DENY) {
                e.setUseItemInHand(event.useItem());
            }
        }
    }

    private boolean canPlaceCargoNodes(Player p, ItemStack item, Block b) {
        return canPlaceBlock(p, b) && (SlimefunManager.isItemSimilar(item, SlimefunItems.CARGO_INPUT, true) || SlimefunManager.isItemSimilar(item, SlimefunItems.CARGO_OUTPUT, true) || SlimefunManager.isItemSimilar(item, SlimefunItems.CARGO_OUTPUT_ADVANCED, true));
    }

    private boolean canPlaceBlock(Player p, Block relative) {
        return p.isSneaking() && relative.getType() == Material.AIR;
    }

    @EventHandler
    public void onIronGolemHeal(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof IronGolem) {
            PlayerInventory inv = e.getPlayer().getInventory();
            ItemStack item = null;

            if (e.getHand() == EquipmentSlot.HAND) {
                item = inv.getItemInMainHand();
            }
            else if (e.getHand() == EquipmentSlot.OFF_HAND) {
                item = inv.getItemInOffHand();
            }

            if (item != null && item.getType() == Material.IRON_INGOT && SlimefunItem.getByItem(item) != null) {
                e.setCancelled(true);
                SlimefunPlugin.getLocal().sendMessage(e.getPlayer(), "messages.no-iron-golem-heal");

                // This is just there to update the Inventory...
                // Somehow cancelling it isn't enough.
                if (e.getHand() == EquipmentSlot.HAND) {
                    inv.setItemInMainHand(item);
                }
                else if (e.getHand() == EquipmentSlot.OFF_HAND) {
                    inv.setItemInOffHand(item);
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
