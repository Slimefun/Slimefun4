package me.mrCookieSlime.Slimefun.listeners;

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

public class AncientAltarListener implements Listener {

	public AncientAltarListener(SlimefunStartup plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	List<Block> altars = new ArrayList<>();
	Set<UUID> removed_items = new HashSet<>();

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		Block b = e.getClickedBlock();
		SlimefunItem item = BlockStorage.check(b);
		if (item != null) {
			if (item.getID().equals("ANCIENT_PEDESTAL")) {
				if (Variables.altarinuse.contains(b.getLocation())) {
					e.setCancelled(true);
					return;
				}				
				e.setCancelled(true);
				Item stack = findItem(b);
				if (stack == null) {
					if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) return;
					if(b.getRelative(0, 1, 0).getType() != Material.AIR) {
						Messages.local.sendTranslation(e.getPlayer(), "machines.ANCIENT_PEDESTAL.obstructed", true);
						return;
					}
					insertItem(e.getPlayer(), b);
				}
				else if (!removed_items.contains(stack.getUniqueId())) {
					final UUID uuid = stack.getUniqueId();
					removed_items.add(uuid);

					SlimefunStartup.instance.getServer().getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, () -> {
						removed_items.remove(uuid);
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
				}
				Variables.altarinuse.add(b.getLocation());  // make altarinuse simply because that was the last block clicked.
				e.setCancelled(true);

				ItemStack catalyst = new CustomItem(e.getPlayer().getInventory().getItemInMainHand(), 1);
				List<Block> pedestals = Pedestals.getPedestals(b);

				if (!altars.contains(e.getClickedBlock())) {
					altars.add(e.getClickedBlock());
					if (pedestals.size() == 8) {
						pedestals.forEach((pblock)->{
							Variables.altarinuse.add(pblock.getLocation());
						});
						if (catalyst != null && !catalyst.getType().equals(Material.AIR)) {
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
								Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new RitualAnimation(altars, b, b.getLocation().add(0.5, 1.3, 0.5), result, pedestals, consumed), 10L);
							}
							else {
								altars.remove(e.getClickedBlock());
								Messages.local.sendTranslation(e.getPlayer(), "machines.ANCIENT_ALTAR.unknown-recipe", true);
								pedestals.forEach((pblock)->{
									Variables.altarinuse.remove(pblock.getLocation());
								});
								Variables.altarinuse.remove(b.getLocation());  // bad recipe, no longer in use.								
							}
						}
						else {
							altars.remove(e.getClickedBlock());
							Messages.local.sendTranslation(e.getPlayer(), "machines.ANCIENT_ALTAR.unknown-catalyst", true);
							pedestals.forEach((pblock)->{
								Variables.altarinuse.remove(pblock.getLocation());
							});
							Variables.altarinuse.remove(b.getLocation());  // unkown catalyst, no longer in use							
						}
					}
					else {
						altars.remove(e.getClickedBlock());
						Messages.local.sendTranslation(e.getPlayer(), "machines.ANCIENT_ALTAR.not-enough-pedestals", true, new Variable("%pedestals%", String.valueOf(pedestals.size())));
						Variables.altarinuse.remove(b.getLocation());  // not a valid altar so remove from inuse
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
			if (n instanceof Item) {
				if (b.getLocation().add(0.5, 1.2, 0.5).distanceSquared(n.getLocation()) < 0.5D && n.getCustomName() != null) return (Item) n;
			}
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
			entity.setMetadata("no_pickup", new FixedMetadataValue(SlimefunStartup.instance, "altar_item"));
			entity.setCustomNameVisible(true);
			entity.setCustomName(nametag);
			p.playSound(b.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.3F, 0.3F);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
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
