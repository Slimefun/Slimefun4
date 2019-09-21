package me.mrCookieSlime.Slimefun.ancient_altar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Player.PlayerInventory;
import me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.utils.Utilities;

public class AncientAltarListener implements Listener {
	
	private Utilities utilities;

	public AncientAltarListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
		utilities = SlimefunPlugin.getUtilities();
	}

	private List<Block> altars = new ArrayList<>();
	private Set<UUID> removedItems = new HashSet<>();

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		Block b = e.getClickedBlock();
		String item = BlockStorage.checkID(b);
		if (item != null) {
			if (item.equals("ANCIENT_PEDESTAL")) {
				if (utilities.altarinuse.contains(b.getLocation())) {
					e.setCancelled(true);
					return;
				}				
				e.setCancelled(true);
				Item stack = findItem(b);
				if (stack == null) {
					if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR) return;
					if(b.getRelative(0, 1, 0).getType() != Material.AIR) {
						SlimefunPlugin.getLocal().sendMessage(e.getPlayer(), "machines.ANCIENT_PEDESTAL.obstructed", true);
						return;
					}
					insertItem(e.getPlayer(), b);
				}
				else if (!removedItems.contains(stack.getUniqueId())) {
					final UUID uuid = stack.getUniqueId();
					removedItems.add(uuid);

					SlimefunPlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> removedItems.remove(uuid), 30L);

					stack.remove();
					e.getPlayer().getInventory().addItem(fixItemStack(stack.getItemStack(), stack.getCustomName()));
					e.getPlayer().playSound(b.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1F, 1F);
					PlayerInventory.update(e.getPlayer());
				}
			}
			else if (item.equals("ANCIENT_ALTAR")) {
				if (utilities.altarinuse.contains(b.getLocation())) {
					e.setCancelled(true);
					return;
				}
				
				// Make altarinuse simply because that was the last block clicked.
				utilities.altarinuse.add(b.getLocation());
				e.setCancelled(true);

				ItemStack catalyst = new CustomItem(e.getPlayer().getInventory().getItemInMainHand(), 1);
				List<Block> pedestals = Pedestals.getPedestals(b);

				if (!altars.contains(e.getClickedBlock())) {
					altars.add(e.getClickedBlock());
					if (pedestals.size() == 8) {
						pedestals.forEach(block -> utilities.altarinuse.add(block.getLocation()));
						
						if (catalyst != null && catalyst.getType() != Material.AIR) {
							List<ItemStack> input = new ArrayList<>();
							for (Block pedestal: pedestals) {
								Item stack = findItem(pedestal);
								if (stack != null) input.add(fixItemStack(stack.getItemStack(), stack.getCustomName()));
							}

							ItemStack result = Pedestals.getRecipeOutput(catalyst, input);
							if (result != null) {
								List<ItemStack> consumed = new ArrayList<>();
								consumed.add(catalyst);
								PlayerInventory.consumeItemInHand(e.getPlayer());
								Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, new RitualAnimation(altars, b, b.getLocation().add(0.5, 1.3, 0.5), result, pedestals, consumed), 10L);
							}
							else {
								altars.remove(e.getClickedBlock());
								SlimefunPlugin.getLocal().sendMessage(e.getPlayer(), "machines.ANCIENT_ALTAR.unknown-recipe", true);
								
								pedestals.forEach(block -> utilities.altarinuse.remove(block.getLocation()));
								
								// Bad recipe, no longer in use.
								utilities.altarinuse.remove(b.getLocation());
							}
						}
						else {
							altars.remove(e.getClickedBlock());
							SlimefunPlugin.getLocal().sendMessage(e.getPlayer(), "machines.ANCIENT_ALTAR.unknown-catalyst", true);
							
							pedestals.forEach(block -> utilities.altarinuse.remove(block.getLocation()));
							
							// Unknown catalyst, no longer in use
							utilities.altarinuse.remove(b.getLocation());
						}
					}
					else {
						altars.remove(e.getClickedBlock());
						SlimefunPlugin.getLocal().sendMessage(e.getPlayer(), "machines.ANCIENT_ALTAR.not-enough-pedestals", true, msg -> msg.replace("%pedestals%", String.valueOf(pedestals.size())));
						
						// Not a valid altar so remove from inuse
						utilities.altarinuse.remove(b.getLocation());  
					}
				}
			}
		}
	}

	public static ItemStack fixItemStack(ItemStack itemStack, String customName) {
		ItemStack stack = itemStack.clone();
		if (customName.equals(StringUtils.formatItemName(new ItemStack(itemStack.getType()), false))) {
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
		for (Entity n: b.getChunk().getEntities()) {
			if (n instanceof Item && b.getLocation().add(0.5, 1.2, 0.5).distanceSquared(n.getLocation()) < 0.5D && n.getCustomName() != null) return (Item) n;
		}
		return null;
	}

	private void insertItem(Player p, Block b) {
		final ItemStack stack = p.getInventory().getItemInMainHand();
		if (stack != null) {
			PlayerInventory.consumeItemInHand(p);
			String nametag = StringUtils.formatItemName(stack, false);
			Item entity = b.getWorld().dropItem(b.getLocation().add(0.5, 1.2, 0.5), new CustomItem(new CustomItem(stack, 1), "&5&dALTAR &3Probe - &e" + System.nanoTime()));
			entity.setVelocity(new Vector(0, 0.1, 0));
			entity.setMetadata("no_pickup", new FixedMetadataValue(SlimefunPlugin.instance, "altar_item"));
			entity.setCustomNameVisible(true);
			entity.setCustomName(nametag);
			p.playSound(b.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.3F, 0.3F);
		}
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

}
