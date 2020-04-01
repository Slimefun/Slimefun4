package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.Optional;
import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AltarRecipe;
import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AncientAltar;
import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AncientPedestal;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.AncientAltarTask;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * This {@link Listener} is responsible for providing the core mechanics of the {@link AncientAltar}
 * and the {@link AncientPedestal}, it also handles the crafting of items using the Altar.
 *
 * @author Redemption198
 * @author TheBusyBiscuit
 */
public class AncientAltarListener implements Listener {

    private final Set<AltarRecipe> altarRecipes = new HashSet<>();
    private final Set<Location> altarsInUse = new HashSet<>();

    private final List<Block> altars = new ArrayList<>();
    private final Set<UUID> removedItems = new HashSet<>();

    public void load(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public Set<Location> getAltarsInUse() {
        return altarsInUse;
    }

    public List<Block> getAltars() {
        return altars;
    }

    public Set<AltarRecipe> getRecipes() {
        return altarRecipes;
    }

    @EventHandler
    public void onInteract(PlayerRightClickEvent e) {
        if (e.useBlock() == Result.DENY) return;

        Optional<Block> blockOptional = e.getClickedBlock();
        if (!blockOptional.isPresent()) return;

        Block b = blockOptional.get();
        if (b.getType() != Material.ENCHANTING_TABLE && b.getType() != Material.DISPENSER) return;

        Optional<SlimefunItem> slimefunBlock = e.getSlimefunBlock();
        if (!slimefunBlock.isPresent()) return;

        String id = slimefunBlock.get().getID();

        if (id.equals(SlimefunItems.ANCIENT_PEDESTAL.getItemID())) {
            e.cancel();
            usePedestal(b, e.getPlayer());
        } else if (id.equals("ANCIENT_ALTAR")) {
            if (!Slimefun.hasUnlocked(e.getPlayer(), SlimefunItems.ANCIENT_ALTAR, true) || altarsInUse.contains(b.getLocation())) {
                e.cancel();
                return;
            }

            // Make altarinuse simply because that was the last block clicked.
            altarsInUse.add(b.getLocation());
            e.cancel();

            useAltar(b, e.getPlayer());
        }
    }

    private void usePedestal(Block pedestal, Player p) {
        if (altarsInUse.contains(pedestal.getLocation())) {
            return;
        }

        Item stack = findItem(pedestal);

        if (stack == null) {
            if (p.getInventory().getItemInMainHand().getType() == Material.AIR) return;

            if (pedestal.getRelative(0, 1, 0).getType() != Material.AIR) {
                SlimefunPlugin.getLocal().sendMessage(p, "machines.ANCIENT_PEDESTAL.obstructed", true);
                return;
            }

            insertItem(p, pedestal);
        } else if (!removedItems.contains(stack.getUniqueId())) {
            UUID uuid = stack.getUniqueId();
            removedItems.add(uuid);

            Slimefun.runSync(() -> removedItems.remove(uuid), 30L);  
        	
            p.getInventory().addItem(fixItemStack(stack.getItemStack(), stack.getCustomName()));
            p.playSound(pedestal.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1F, 1F);
            removeDisplayItem(pedestal);
        } 
    }
    public static void removeDisplayItem(Block b) {
        getEntity(b).ifPresent(Item::remove);
    }
    private static Optional<Item> getEntity(Block b) {
    	for (Entity n : b.getChunk().getEntities()) {
            if (n instanceof Item && b.getLocation().add(0.5, 1.2, 0.5).distanceSquared(n.getLocation()) < 1.0D && n.getCustomName() != null) {
            	Item item = (Item) n;
                return Optional.of(item);
                }
            }
    	return Optional.empty();
    }
    private void useAltar(Block b, Player p) {
        ItemStack catalyst = new CustomItem(p.getInventory().getItemInMainHand(), 1);
        List<Block> pedestals = getPedestals(b);

        if (!altars.contains(b)) {
            altars.add(b);

            if (pedestals.size() == 8) {
                pedestals.forEach(block -> altarsInUse.add(block.getLocation()));

                if (catalyst.getType() != Material.AIR) {
                    List<ItemStack> input = new ArrayList<>();
                    for (Block pedestal : pedestals) {
                        Item stack = findItem(pedestal);

                        if (stack != null) {
                            input.add(fixItemStack(stack.getItemStack(), stack.getCustomName()));
                        }
                    }

                    ItemStack result = getRecipeOutput(catalyst, input);
                    if (result != null) {
                        if (Slimefun.hasUnlocked(p, result, true)) {
                            List<ItemStack> consumed = new ArrayList<>();
                            consumed.add(catalyst);

                            if (p.getGameMode() != GameMode.CREATIVE) {
                                ItemUtils.consumeItem(p.getInventory().getItemInMainHand(), false);
                            }

                            Slimefun.runSync(new AncientAltarTask(b, result, pedestals, consumed), 10L);
                        } else {
                            altars.remove(b);

                            pedestals.forEach(block -> altarsInUse.remove(block.getLocation()));

                            // Item not unlocked, no longer in use.
                            altarsInUse.remove(b.getLocation());
                        }
                    } else {
                        altars.remove(b);
                        SlimefunPlugin.getLocal().sendMessage(p, "machines.ANCIENT_ALTAR.unknown-recipe", true);

                        pedestals.forEach(block -> altarsInUse.remove(block.getLocation()));

                        // Bad recipe, no longer in use.
                        altarsInUse.remove(b.getLocation());
                    }
                } else {
                    altars.remove(b);
                    SlimefunPlugin.getLocal().sendMessage(p, "machines.ANCIENT_ALTAR.unknown-catalyst", true);

                    pedestals.forEach(block -> altarsInUse.remove(block.getLocation()));

                    // Unknown catalyst, no longer in use
                    altarsInUse.remove(b.getLocation());
                }
            } else {
                altars.remove(b);
                SlimefunPlugin.getLocal().sendMessage(p, "machines.ANCIENT_ALTAR.not-enough-pedestals", true, msg -> msg.replace("%pedestals%", String.valueOf(pedestals.size())));

                // Not a valid altar so remove from inuse
                altarsInUse.remove(b.getLocation());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        Block pedestal = e.getBlockPlaced().getRelative(BlockFace.DOWN);

        if (pedestal.getType() == Material.DISPENSER) {
            String id = BlockStorage.checkID(pedestal);

            if (id != null && id.equals(SlimefunItems.ANCIENT_PEDESTAL.getItemID())) {
                SlimefunPlugin.getLocal().sendMessage(e.getPlayer(), "messages.cannot-place", true);
                e.setCancelled(true);
            }
        }
    }
    public ItemStack fixItemStack(ItemStack itemStack, String customName) {
        ItemStack stack = itemStack.clone();

        if (customName.equals(ItemUtils.getItemName(new ItemStack(itemStack.getType())))) {
            ItemMeta im = stack.getItemMeta();
            im.setDisplayName(null);
            stack.setItemMeta(im);
        } else {
            ItemMeta im = stack.getItemMeta();
            if (!customName.startsWith(String.valueOf(ChatColor.COLOR_CHAR))) customName = ChatColor.RESET + customName;
            im.setDisplayName(customName);
            stack.setItemMeta(im);
        }
        return stack;
    }

    public Item findItem(Block b) {
        for (Entity n : b.getChunk().getEntities()) {
            if (n instanceof Item && b.getLocation().add(0.5, 1.2, 0.5).distanceSquared(n.getLocation()) < 1.0D && n.getCustomName() != null) {
                return (Item) n;
            }
        }
        return null;
    }

    private void insertItem(Player p, Block b) {
        ItemStack hand = p.getInventory().getItemInMainHand();
        ItemStack stack = new CustomItem(hand, 1);

        if (p.getGameMode() != GameMode.CREATIVE) {
            ItemUtils.consumeItem(hand, false);
        }

        String nametag = ItemUtils.getItemName(stack);
        Item entity = b.getWorld().dropItem(b.getLocation().add(0.5, 1.2, 0.5), new CustomItem(stack, "&5&dALTAR &3Probe - &e" + System.nanoTime()));
        entity.setVelocity(new Vector(0, 0.1, 0));
        SlimefunUtils.markAsNoPickup(entity, "altar_item");
        entity.setCustomNameVisible(true);
        entity.setCustomName(nametag);
        p.playSound(b.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.3F, 0.3F);
    }

    private List<Block> getPedestals(Block altar) {
        List<Block> list = new ArrayList<>();
        String id = SlimefunItems.ANCIENT_PEDESTAL.getItemID();

        if (BlockStorage.check(altar.getRelative(2, 0, -2), id)) {
            list.add(altar.getRelative(2, 0, -2));
        }
        if (BlockStorage.check(altar.getRelative(3, 0, 0), id)) {
            list.add(altar.getRelative(3, 0, 0));
        }
        if (BlockStorage.check(altar.getRelative(2, 0, 2), id)) {
            list.add(altar.getRelative(2, 0, 2));
        }
        if (BlockStorage.check(altar.getRelative(0, 0, 3), id)) {
            list.add(altar.getRelative(0, 0, 3));
        }
        if (BlockStorage.check(altar.getRelative(-2, 0, 2), id)) {
            list.add(altar.getRelative(-2, 0, 2));
        }
        if (BlockStorage.check(altar.getRelative(-3, 0, 0), id)) {
            list.add(altar.getRelative(-3, 0, 0));
        }
        if (BlockStorage.check(altar.getRelative(-2, 0, -2), id)) {
            list.add(altar.getRelative(-2, 0, -2));
        }
        if (BlockStorage.check(altar.getRelative(0, 0, -3), id)) {
            list.add(altar.getRelative(0, 0, -3));
        }

        return list;
    }

    public ItemStack getRecipeOutput(ItemStack catalyst, List<ItemStack> input) {
        if (input.size() != 8) return null;

        if (SlimefunUtils.isItemSimilar(catalyst, SlimefunItems.BROKEN_SPAWNER, false)) {
            if (checkRecipe(SlimefunItems.BROKEN_SPAWNER, input) == null) {
                return null;
            }

            ItemStack spawner = SlimefunItems.REPAIRED_SPAWNER.clone();
            ItemMeta im = spawner.getItemMeta();
            im.setLore(Arrays.asList(catalyst.getItemMeta().getLore().get(0)));
            spawner.setItemMeta(im);
            return spawner;
        }

        return checkRecipe(catalyst, input);
    }

    private ItemStack checkRecipe(ItemStack catalyst, List<ItemStack> items) {
        for (AltarRecipe recipe : altarRecipes) {
            if (SlimefunUtils.isItemSimilar(catalyst, recipe.getCatalyst(), true)) {
                for (int i = 0; i < 8; i++) {
                    if (SlimefunUtils.isItemSimilar(items.get(i), recipe.getInput().get(0), true)) {
                        for (int j = 1; j < 8; j++) {
                            if (!SlimefunUtils.isItemSimilar(items.get((i + j) % items.size()), recipe.getInput().get(j), true)) {
                                break;
                            } else if (j == 7) {
                                return recipe.getOutput();
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

}
