package me.mrCookieSlime.Slimefun.listeners;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Variable;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Player.PlayerInventory;
import me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.AncientAltar.Pedestals;
import me.mrCookieSlime.Slimefun.AncientAltar.RitualAnimation;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.Variables;

import org.bukkit.Bukkit;
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

public class AncientAltarListener implements Listener {

	public AncientAltarListener(SlimefunStartup plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	List<Block> altars = new ArrayList<Block>();
	Set<UUID> removed_items = new HashSet<UUID>();

	@EventHandler(priority=EventPriority.HIGH, ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		Block b = e.getClickedBlock();

		SlimefunItem item = BlockStorage.check(b);
		
		if (item != null) {
			if (item.getID().equals("ANCIENT_PEDESTAL")) {
				Block verifyblock = findAncientAltar(b);
				if (verifyblock != null) {
					if (Variables.altarinuse.contains(verifyblock.getLocation())) {
						e.setCancelled(true);
						return;
					}
				} else {
					e.setCancelled(true);
					return;
				}				
				
				e.setCancelled(true);
				Item stack = findItem(b);
				if (stack == null) {
					if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) return;
					if(b.getRelative(0, 1, 0).getType() != Material.AIR) {
						Messages.local.sendTranslation(e.getPlayer(), "machines.ANCIENT_PEDESTAL.obstructed", true);
						if (Variables.altarinuse.contains(b.getLocation())) Variables.altarinuse.remove(b.getLocation());
						return;
					}
					insertItem(e.getPlayer(), b);
				}
				else if (!removed_items.contains(stack.getUniqueId())) {
					final UUID uuid = stack.getUniqueId();
					removed_items.add(uuid);

					SlimefunStartup.instance.getServer().getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new Runnable() {

						@Override
						public void run() {
							removed_items.remove(uuid);
						}
					}, 30L);

					stack.remove();
					e.getPlayer().getInventory().addItem(fixItemStack(stack.getItemStack(), stack.getCustomName()));
					e.getPlayer().playSound(b.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1F, 1F);
					PlayerInventory.update(e.getPlayer());
				}
			}
			else if (item.getID().equals("ANCIENT_ALTAR")) {
				if (Variables.altarinuse.contains(b.getLocation())) {
					e.setCancelled(true);
					return;
				} else if (!Variables.altarinuse.contains(b.getLocation())) {
					Variables.altarinuse.add(b.getLocation());  // make altarinuse simply because that was the last block clicked.
				} else {
					e.setCancelled(true);
					return;
				}
				e.setCancelled(true);

				ItemStack catalyst = new CustomItem(e.getPlayer().getInventory().getItemInMainHand(), 1);
				List<Block> pedestals = Pedestals.getPedestals(b);

				if (!altars.contains(e.getClickedBlock())) {
					altars.add(e.getClickedBlock());
					if (pedestals.size() == 8) {
						if (catalyst != null && !catalyst.getType().equals(Material.AIR)) {
							List<ItemStack> input = new ArrayList<ItemStack>();
							for (Block pedestal: pedestals) {
								Item stack = findItem(pedestal);
								if (stack != null) input.add(fixItemStack(stack.getItemStack(), stack.getCustomName()));
							}
							ItemStack result = Pedestals.getRecipeOutput(catalyst, input);
							if (result != null) {
								List<ItemStack> consumed = new ArrayList<ItemStack>();
								consumed.add(catalyst);
								PlayerInventory.consumeItemInHand(e.getPlayer());
								if (!Variables.altarinuse.contains(b.getLocation())) Variables.altarinuse.add(b.getLocation());  // altarinuse already, but in case spam clicking meant it wasn't...
								Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new RitualAnimation(altars, b, b.getLocation().add(0.5, 1.3, 0.5), result, pedestals, consumed), 10L);
							}
							else {
								altars.remove(e.getClickedBlock());
								Messages.local.sendTranslation(e.getPlayer(), "machines.ANCIENT_ALTAR.unknown-recipe", true);
								if (Variables.altarinuse.contains(b.getLocation())) Variables.altarinuse.remove(b.getLocation());  // bad recipe, no longer in use.
							}
						}
						else {
							altars.remove(e.getClickedBlock());
							Messages.local.sendTranslation(e.getPlayer(), "machines.ANCIENT_ALTAR.unknown-catalyst", true);
							if (Variables.altarinuse.contains(b.getLocation())) Variables.altarinuse.remove(b.getLocation());  // unkown catalyst, no longer in use
						}
					}
					else {
						altars.remove(e.getClickedBlock());
						Messages.local.sendTranslation(e.getPlayer(), "machines.ANCIENT_ALTAR.not-enough-pedestals", true, new Variable("%pedestals%", String.valueOf(pedestals.size())));
						if (Variables.altarinuse.contains(b.getLocation())) Variables.altarinuse.remove(b.getLocation());  // not a proper altar, so not in use.
					}
				}
			}
		}
	}

	public static ItemStack fixItemStack(ItemStack itemStack, String customName) {
		ItemStack stack = itemStack.clone();
		if (customName.equals(StringUtils.formatItemName(itemStack.getData().toItemStack(1), false))) {
			ItemMeta im = stack.getItemMeta();
			im.setDisplayName(null);
			stack.setItemMeta(im);
		}
		else {
			ItemMeta im = stack.getItemMeta();
			im.setDisplayName(customName);
			stack.setItemMeta(im);
		}
		return stack;
	}

	public static Item findItem(Block b) {
		for (Entity n: b.getChunk().getEntities()) {
			if (n instanceof Item) {
				if (b.getLocation().add(0.5, 1.2, 0.5).distanceSquared(n.getLocation()) < 0.5D && n.getCustomName() != null) return (Item) n;
			}
		}
		return null;
	}

	private static Block getRelAltarBlock(Block b, Integer xC, Integer zC) {
		Block rel = b.getRelative(xC, 0, zC);
		SlimefunItem item = BlockStorage.check(rel);
		if (item != null) {
			if (item.getID().equals("ANCIENT_ALTAR")) return rel;
		}
		Block nonefound = null;
		return nonefound;  // if here then relative coordinates did not find an Altar
	}

	public static Block findAncientAltar(Block b) {
		SlimefunItem item = BlockStorage.check(b);
		if (item != null) {

			if (item.getID().equals("ANCIENT_ALTAR")) return b;
			if ((item.getID().equals("ANCIENT_PEDESTAL"))) {

				List<Block> foundaltars = new ArrayList<Block>();

				// Altar linear positions
				if (getRelAltarBlock(b,3,0) != null) foundaltars.add(getRelAltarBlock(b,3,0));
				if (getRelAltarBlock(b,0,3) != null) foundaltars.add(getRelAltarBlock(b,0,3));
				if (getRelAltarBlock(b,-3,0) != null) foundaltars.add(getRelAltarBlock(b,-3,0));
				if (getRelAltarBlock(b,0,-3) != null) foundaltars.add(getRelAltarBlock(b,0,-3));

				// Altar Diagonal Positions:
				if (getRelAltarBlock(b,-2,2) != null) foundaltars.add(getRelAltarBlock(b,-2,2));
				if (getRelAltarBlock(b,-2,-2) != null) foundaltars.add(getRelAltarBlock(b,-2,-2));
				if (getRelAltarBlock(b,2,2) != null) foundaltars.add(getRelAltarBlock(b,2,2));
				if (getRelAltarBlock(b,2,-2) != null) foundaltars.add(getRelAltarBlock(b,2,-2));

				Block fakealtarsetupfound = null;
				if (foundaltars.size() > 1) return fakealtarsetupfound;  // effectively disable bypass by placing multiple altar blocks around altar.
				if (foundaltars.size() == 1) return foundaltars.get(0);
			}
		}

		Block noblockfound = null;
		return noblockfound;  // should never get here.
	}

	private void insertItem(Player p, Block b) {
		final ItemStack stack = p.getInventory().getItemInMainHand();
		if (stack != null) {
			PlayerInventory.consumeItemInHand(p);
			String nametag = StringUtils.formatItemName(stack, false);
			Item entity = b.getWorld().dropItem(b.getLocation().add(0.5, 1.2, 0.5), new CustomItem(new CustomItem(stack, 1), "&5&dALTAR &3Probe - &e" + System.nanoTime()));
			entity.setVelocity(new Vector(0, 0.1, 0));
			entity.setMetadata("no_pickup", new FixedMetadataValue(SlimefunStartup.instance, "altar_item"));
			entity.setCustomNameVisible(true);
			entity.setCustomName(nametag);
			p.playSound(b.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.3F, 0.3F);
		}
	}

	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent e) {
		Block b = e.getBlockPlaced().getRelative(0, -1, 0);
		SlimefunItem item = BlockStorage.check(b);
		if(item == null) return;
		if(item.getID().equalsIgnoreCase("ANCIENT_PEDESTAL")) {
			Messages.local.sendTranslation(e.getPlayer(), "messages.cannot-place", true);
			e.setCancelled(true);
		}
	}
}
