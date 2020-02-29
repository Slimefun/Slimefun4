package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.AncientAltarTask;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.ancient_altar.AltarRecipe;
import me.mrCookieSlime.Slimefun.ancient_altar.Pedestals;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.*;
import org.bukkit.block.Block;
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
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AncientAltarListener implements Listener {

	private final Set<AltarRecipe> altarRecipes = new HashSet<>();
	private final Set<Location> altarsInUse = new HashSet<>();
	private final static Map<Location, Player> placedItem = new ConcurrentHashMap<>();

	private final List<Block> altars = new ArrayList<>();
	private final Set<UUID> removedItems = new HashSet<>();

	public void load(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public Set<Location> getAltarsInUse() {
		return altarsInUse;
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
		
		if (id.equals("ANCIENT_PEDESTAL")) {
			e.cancel();

			if (altarsInUse.contains(b.getLocation())) {
				return;
			}

			Item stack = findItem(b);

			if (stack == null) {
				if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR) return;

				if (b.getRelative(0, 1, 0).getType() != Material.AIR) {
					SlimefunPlugin.getLocal().sendMessage(e.getPlayer(), "machines.ANCIENT_PEDESTAL.obstructed", true);
					return;
				}

				insertItem(e.getPlayer(), b);
			}
			else if (!removedItems.contains(stack.getUniqueId())) {
                UUID uuid = stack.getUniqueId();
                removedItems.add(uuid);

                Slimefun.runSync(() -> removedItems.remove(uuid), 30L);

                stack.remove();
                e.getPlayer().getInventory().addItem(fixItemStack(stack.getItemStack(), stack.getCustomName()));
                e.getPlayer().playSound(b.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1F, 1F);
			}
		}
		else if (id.equals("ANCIENT_ALTAR")) {
			if (!Slimefun.hasUnlocked(e.getPlayer(), SlimefunItems.ANCIENT_ALTAR, true) || altarsInUse.contains(b.getLocation())) {
				e.cancel();
				return;
			}

			// Make altarinuse simply because that was the last block clicked.
			altarsInUse.add(b.getLocation());
			e.cancel();

			ItemStack catalyst = new CustomItem(e.getPlayer().getInventory().getItemInMainHand(), 1);
			List<Block> pedestals = Pedestals.getPedestals(b);

			if (!altars.contains(b)) {
				altars.add(b);
				if (pedestals.size() == 8) {
					pedestals.forEach(block -> altarsInUse.add(block.getLocation()));

					if (catalyst.getType() != Material.AIR) {
						List<ItemStack> input = new ArrayList<>();
						for (Block pedestal : pedestals) {
							Item stack = findItem(pedestal);
							if (stack != null) input.add(fixItemStack(stack.getItemStack(), stack.getCustomName()));
						}

						ItemStack result = Pedestals.getRecipeOutput(catalyst, input);
						if (result != null) {
							if (Slimefun.hasUnlocked(e.getPlayer(), result, true)) {
								List<ItemStack> consumed = new ArrayList<>();
								consumed.add(catalyst);

								if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
									ItemUtils.consumeItem(e.getPlayer().getInventory().getItemInMainHand(), false);
								}

								Slimefun.runSync(new AncientAltarTask(altars, b, b.getLocation().add(0.5, 1.3, 0.5), result, pedestals, consumed), 10L);
							}
							else {
								altars.remove(b);

								pedestals.forEach(block -> altarsInUse.remove(block.getLocation()));

								// Item not unlocked, no longer in use.
								altarsInUse.remove(b.getLocation());
							}
						}
						else {
							altars.remove(b);
							SlimefunPlugin.getLocal().sendMessage(e.getPlayer(), "machines.ANCIENT_ALTAR.unknown-recipe", true);

							pedestals.forEach(block -> altarsInUse.remove(block.getLocation()));

							// Bad recipe, no longer in use.
							altarsInUse.remove(b.getLocation());
						}
					}
					else {
						altars.remove(b);
						SlimefunPlugin.getLocal().sendMessage(e.getPlayer(), "machines.ANCIENT_ALTAR.unknown-catalyst", true);

						pedestals.forEach(block -> altarsInUse.remove(block.getLocation()));

						// Unknown catalyst, no longer in use
						altarsInUse.remove(b.getLocation());
					}
				}
				else {
					altars.remove(b);
					SlimefunPlugin.getLocal().sendMessage(e.getPlayer(), "machines.ANCIENT_ALTAR.not-enough-pedestals", true, msg -> msg.replace("%pedestals%", String.valueOf(pedestals.size())));

					// Not a valid altar so remove from inuse
					altarsInUse.remove(b.getLocation());  
				}
			}
		}
	}

	public static ItemStack fixItemStack(ItemStack itemStack, String customName) {
		ItemStack stack = itemStack.clone();

		if (customName.equals(ItemUtils.getItemName(new ItemStack(itemStack.getType())))) {
			ItemMeta im = stack.getItemMeta();
			im.setDisplayName(null);
			stack.setItemMeta(im);
		} 
		else {
			ItemMeta im = stack.getItemMeta();
			if (!customName.startsWith(String.valueOf(ChatColor.COLOR_CHAR))) customName = ChatColor.RESET + customName;
			im.setDisplayName(customName);
			stack.setItemMeta(im);
		}
		return stack;
	}

	public static Item findItem(Block b) {
		for (Entity n : b.getChunk().getEntities()) {
			if (n instanceof Item && b.getLocation().add(0.5, 1.2, 0.5).distanceSquared(n.getLocation()) < 0.5D && n.getCustomName() != null) {
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
        entity.setMetadata("no_pickup", new FixedMetadataValue(SlimefunPlugin.instance, "altar_item"));
        entity.setCustomNameVisible(true);
        entity.setCustomName(nametag);
        p.playSound(b.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.3F, 0.3F);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent e) {
		Block b = e.getBlockPlaced().getRelative(0, -1, 0);
		String item = BlockStorage.checkID(b);

		if (item != null && item.equalsIgnoreCase("ANCIENT_PEDESTAL")) {
			SlimefunPlugin.getLocal().sendMessage(e.getPlayer(), "messages.cannot-place", true);
			e.setCancelled(true);
		}
	}

	public Set<AltarRecipe> getRecipes() {
		return altarRecipes;
	}

}
