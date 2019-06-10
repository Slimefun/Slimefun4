package me.mrCookieSlime.Slimefun.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Hopper;
import org.bukkit.block.Skull;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Variable;
import me.mrCookieSlime.CSCoreLibPlugin.events.ItemUseEvent;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Player.PlayerInventory;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.SlimefunGuide;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Variables;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Misc.BookDesign;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Juice;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.MultiTool;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.energy.ItemEnergy;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;

public class ItemListener implements Listener {

	public ItemListener(SlimefunStartup plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onIgnitionChamberItemMove(InventoryMoveItemEvent e) {
		if (e.getInitiator().getHolder() instanceof Hopper) {
			if (BlockStorage.check(((Hopper) e.getInitiator().getHolder()).getBlock(), "IGNITION_CHAMBER")) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void debug(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.PHYSICAL) || !e.getHand().equals(EquipmentSlot.HAND)) return;
		Player p = e.getPlayer();
		if (SlimefunManager.isItemSimiliar(e.getPlayer().getInventory().getItemInMainHand(), SlimefunItems.DEBUG_FISH, true) || SlimefunManager.isItemSimiliar(e.getPlayer().getInventory().getItemInOffHand(), SlimefunItems.DEBUG_FISH, true)) {
			e.setCancelled(true);
			if (p.isOp()) {
				switch (e.getAction()) {
				case LEFT_CLICK_BLOCK: {
					if (p.isSneaking()) {
						if (BlockStorage.hasBlockInfo(e.getClickedBlock())) {
							BlockStorage.clearBlockInfo(e.getClickedBlock());
						}
					}
					else e.setCancelled(false);
					break;
				}
				case RIGHT_CLICK_BLOCK: {
					if (p.isSneaking()) {
						Block b = e.getClickedBlock().getRelative(e.getBlockFace());
						b.setType(Material.PLAYER_HEAD);
						try {
							CustomSkull.setSkull(b, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTllYjlkYTI2Y2YyZDMzNDEzOTdhN2Y0OTEzYmEzZDM3ZDFhZDEwZWFlMzBhYjI1ZmEzOWNlYjg0YmMifX19");
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					else if (BlockStorage.hasBlockInfo(e.getClickedBlock())) {
						p.sendMessage(" ");
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d" + e.getClickedBlock().getType() + " &e@ X: " + e.getClickedBlock().getX() + " Y: " + e.getClickedBlock().getY() + " Z: " + e.getClickedBlock().getZ()));
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dID: " + "&e" + BlockStorage.checkID(e.getClickedBlock())));
						if (e.getClickedBlock().getState() instanceof Skull) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dSkull: " + "&2\u2714"));
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &dRotation: &e" + ((Rotatable) e.getClickedBlock().getBlockData()).getRotation().toString()));
						}
						if (BlockStorage.getStorage(e.getClickedBlock().getWorld()).hasInventory(e.getClickedBlock().getLocation())) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dInventory: " + "&2\u2714"));
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dInventory: " + "&4\u2718"));
						}
						if (BlockStorage.check(e.getClickedBlock()).isTicking()) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dTicking: " + "&2\u2714"));
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &dAsync: &e" + (BlockStorage.check(e.getClickedBlock()).getBlockTicker().isSynchronized() ? "&4\u2718": "&2\u2714")));
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &dTimings: &e" + SlimefunStartup.ticker.getTimings(e.getClickedBlock()) + "ms"));
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &dTotal Timings: &e" + SlimefunStartup.ticker.getTimings(BlockStorage.checkID(e.getClickedBlock())) + "ms"));
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &dChunk Timings: &e" + SlimefunStartup.ticker.getTimings(e.getClickedBlock().getChunk()) + "ms"));
						}
						else if (BlockStorage.check(e.getClickedBlock()).getEnergyTicker() != null) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dTicking: " + "&b~ &3(Indirect)"));
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &dTimings: &e" + SlimefunStartup.ticker.getTimings(e.getClickedBlock()) + "ms"));
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &dChunk Timings: &e" + SlimefunStartup.ticker.getTimings(e.getClickedBlock().getChunk()) + "ms"));
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dTicking: " + "&4\u2718"));
							p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&dTicking: " + "&4\u2718"));
						}
						if (ChargableBlock.isChargable(e.getClickedBlock())) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dChargable: " + "&2\u2714"));
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &dEnergy: &e" + ChargableBlock.getCharge(e.getClickedBlock()) + " / " + ChargableBlock.getMaxCharge(e.getClickedBlock())));
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dChargable: " + "&4\u2718"));
						}
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + BlockStorage.getBlockInfoAsJson(e.getClickedBlock())));
						p.sendMessage(" ");
					}
					break;
				}
				default:
					break;

				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onRightClick(ItemUseEvent e) {
		if (e.getParentEvent() != null && !e.getParentEvent().getHand().equals(EquipmentSlot.HAND)) {
			return;
		}

		final Player p = e.getPlayer();
		ItemStack item = e.getItem();

		// Fix for placing water on player heads
		if (e.getParentEvent().getAction() == Action.RIGHT_CLICK_BLOCK && item != null && item.getType() == Material.WATER_BUCKET) {
			Location water = e.getClickedBlock().getRelative(e.getParentEvent().getBlockFace()).getLocation();
			if ((p.getWorld().getBlockAt(water).getType() == Material.PLAYER_HEAD || p.getWorld().getBlockAt(water).getType() == Material.PLAYER_WALL_HEAD) && BlockStorage.hasBlockInfo(water)) {
				e.setCancelled(true);
				p.getWorld().getBlockAt(water).getState().update(true, false);
				return;
			}
		}

		if (SlimefunManager.isItemSimiliar(item, SlimefunGuide.getItem(BookDesign.BOOK), true)) {
			if (p.isSneaking()) SlimefunGuide.openSettings(p, item);
			else SlimefunGuide.openGuide(p, true);
		}
		else if (SlimefunManager.isItemSimiliar(item, SlimefunGuide.getItem(BookDesign.CHEST), true)) {
			if (p.isSneaking()) SlimefunGuide.openSettings(p, item);
			else SlimefunGuide.openGuide(p, false);
		}
		else if (SlimefunManager.isItemSimiliar(item, SlimefunGuide.getItem(BookDesign.CHEAT_SHEET), true)) {
			if (p.isSneaking()) SlimefunGuide.openSettings(p, item);
			else p.chat("/sf cheat");
		}
		else if (SlimefunManager.isItemSimiliar(item, SlimefunGuide.getDeprecatedItem(true), true)) {
			item = SlimefunGuide.getItem(BookDesign.BOOK);
			p.getInventory().setItemInMainHand(item);
			PlayerInventory.update(p);

			if (p.isSneaking()) SlimefunGuide.openSettings(p, item);
			else SlimefunGuide.openGuide(p, true);
		}
		else if (SlimefunManager.isItemSimiliar(item, SlimefunGuide.getDeprecatedItem(false), true)) {
			item = SlimefunGuide.getItem(BookDesign.CHEST);
			p.getInventory().setItemInMainHand(item);
			PlayerInventory.update(p);

			if (p.isSneaking()) SlimefunGuide.openSettings(p, item);
			else SlimefunGuide.openGuide(p, false);
		}
		else if (SlimefunManager.isItemSimiliar(e.getPlayer().getInventory().getItemInMainHand(), SlimefunItems.DEBUG_FISH, true) || SlimefunManager.isItemSimiliar(e.getPlayer().getInventory().getItemInOffHand(), SlimefunItems.DEBUG_FISH, true)) {
		}
		else if (Slimefun.hasUnlocked(p, item, true)) {
			for (ItemHandler handler : SlimefunItem.getHandlers("ItemInteractionHandler")) {
				if (((ItemInteractionHandler) handler).onRightClick(e, p, item)) return;
			}
			if (SlimefunManager.isItemSimiliar(item, SlimefunItems.DURALUMIN_MULTI_TOOL, false)
					|| SlimefunManager.isItemSimiliar(item, SlimefunItems.SOLDER_MULTI_TOOL, false)
					|| SlimefunManager.isItemSimiliar(item, SlimefunItems.BILLON_MULTI_TOOL, false)
					|| SlimefunManager.isItemSimiliar(item, SlimefunItems.STEEL_MULTI_TOOL, false)
					|| SlimefunManager.isItemSimiliar(item, SlimefunItems.DAMASCUS_STEEL_MULTI_TOOL, false)
					|| SlimefunManager.isItemSimiliar(item, SlimefunItems.REINFORCED_ALLOY_MULTI_TOOL, false)
					|| SlimefunManager.isItemSimiliar(item, SlimefunItems.CARBONADO_MULTI_TOOL, false)) {
				e.setCancelled(true);
				ItemStack tool = null;
				for (ItemStack mTool : new ItemStack[] {SlimefunItems.DURALUMIN_MULTI_TOOL, SlimefunItems.SOLDER_MULTI_TOOL, SlimefunItems.BILLON_MULTI_TOOL, SlimefunItems.STEEL_MULTI_TOOL, SlimefunItems.DAMASCUS_STEEL_MULTI_TOOL, SlimefunItems.REINFORCED_ALLOY_MULTI_TOOL, SlimefunItems.CARBONADO_MULTI_TOOL}) {
					if (mTool.getItemMeta().getLore().get(0).equalsIgnoreCase(item.getItemMeta().getLore().get(0))) {
						tool = mTool;
						break;
					}
				}
				if (tool != null) {
					List<Integer> modes = ((MultiTool) SlimefunItem.getByItem(tool)).getModes();
					int index = 0;
					if (Variables.mode.containsKey(p.getUniqueId())) index = Variables.mode.get(p.getUniqueId());

					if (!p.isSneaking()) {
						float charge = ItemEnergy.getStoredEnergy(item);
						float cost = 0.3F;
						if (charge >= cost) {
							p.getEquipment().setItemInMainHand(ItemEnergy.chargeItem(item, -cost));
							Bukkit.getPluginManager().callEvent(new ItemUseEvent(e.getParentEvent(), SlimefunItem.getByID((String) Slimefun.getItemValue(SlimefunItem.getByItem(tool).getID(), "mode." + modes.get(index) + ".item")).getItem(), e.getClickedBlock()));
						}
					}
					else {
						index++;
						if (index == modes.size()) index = 0;
						Messages.local.sendTranslation(p, "messages.mode-change", true, new Variable("%device%", "Multi Tool"), new Variable("%mode%", (String) Slimefun.getItemValue(SlimefunItem.getByItem(tool).getID(), "mode." + modes.get(index) + ".name")));
						Variables.mode.put(p.getUniqueId(), index);
					}
				}
			}
			else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.HEAVY_CREAM, true)) e.setCancelled(true);

			if (e.getClickedBlock() != null && BlockStorage.hasBlockInfo(e.getClickedBlock())) {
				String id = BlockStorage.checkID(e.getClickedBlock());
				if (BlockMenuPreset.isInventory(id)) {
					if (canPlaceBlock(p, e.getClickedBlock().getRelative(e.getParentEvent().getBlockFace())) && SlimefunManager.isItemSimiliar(item, SlimefunItems.CARGO_INPUT, true));
					else if (canPlaceBlock(p, e.getClickedBlock().getRelative(e.getParentEvent().getBlockFace())) && SlimefunManager.isItemSimiliar(item, SlimefunItems.CARGO_OUTPUT, true));
					else if (canPlaceBlock(p, e.getClickedBlock().getRelative(e.getParentEvent().getBlockFace())) && SlimefunManager.isItemSimiliar(item, SlimefunItems.CARGO_OUTPUT_ADVANCED, true));
					else if (canPlaceBlock(p, e.getClickedBlock().getRelative(e.getParentEvent().getBlockFace())) && SlimefunManager.isItemSimiliar(item, SlimefunItems.CT_IMPORT_BUS, true));
					else if (canPlaceBlock(p, e.getClickedBlock().getRelative(e.getParentEvent().getBlockFace())) && SlimefunManager.isItemSimiliar(item, SlimefunItems.CT_EXPORT_BUS, true));
					else if (!p.isSneaking() || item == null || item.getType() == Material.AIR) {
						e.setCancelled(true);
						BlockStorage storage = BlockStorage.getStorage(e.getClickedBlock().getWorld());

						if (storage.hasUniversalInventory(id)) {
							UniversalBlockMenu menu = storage.getUniversalInventory(id);
							if (menu.canOpen(e.getClickedBlock(), p)) menu.open(p);
						}
						else if (storage.hasInventory(e.getClickedBlock().getLocation())) {
							BlockMenu menu = BlockStorage.getInventory(e.getClickedBlock().getLocation());
							if (menu.canOpen(e.getClickedBlock(), p)) menu.open(p);
						}
					}
				}
			}
		}
		else e.setCancelled(true);
	}

	private boolean canPlaceBlock(Player p, Block relative) {
		return p.isSneaking() && relative.getType().equals(Material.AIR);
	}

	@EventHandler
	public void onEat(PlayerItemConsumeEvent e) {
		if (e.getItem() != null) {
			final Player p = e.getPlayer();
			ItemStack item = e.getItem();
			if (Slimefun.hasUnlocked(p, item, true)) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.MONSTER_JERKY, true)) {
					e.setCancelled(true);
					if (SlimefunManager.isItemSimiliar(p.getInventory().getItemInOffHand(), SlimefunItems.MONSTER_JERKY, true)) {
						p.getInventory().setItemInOffHand(InvUtils.decreaseItem(p.getInventory().getItemInOffHand(), 1));
					}
					else{
						p.getInventory().setItemInMainHand(InvUtils.decreaseItem(p.getInventory().getItemInMainHand(), 1));
					}
					PlayerInventory.update(p);
					p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 5, 0));
				}
				else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.FORTUNE_COOKIE, true)) p.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.local.getTranslation("messages.fortune-cookie").get(CSCoreLib.randomizer().nextInt(Messages.local.getTranslation("messages.fortune-cookie").size()))));
				else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.BEEF_JERKY, true)) p.setSaturation((Integer) Slimefun.getItemValue("BEEF_JERKY", "Saturation"));
				else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.MEDICINE, true)) {
					if (p.hasPotionEffect(PotionEffectType.POISON)) p.removePotionEffect(PotionEffectType.POISON);
					if (p.hasPotionEffect(PotionEffectType.WITHER)) p.removePotionEffect(PotionEffectType.WITHER);
					if (p.hasPotionEffect(PotionEffectType.SLOW)) p.removePotionEffect(PotionEffectType.SLOW);
					if (p.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
					if (p.hasPotionEffect(PotionEffectType.WEAKNESS)) p.removePotionEffect(PotionEffectType.WEAKNESS);
					if (p.hasPotionEffect(PotionEffectType.CONFUSION)) p.removePotionEffect(PotionEffectType.CONFUSION);
					if (p.hasPotionEffect(PotionEffectType.BLINDNESS)) p.removePotionEffect(PotionEffectType.BLINDNESS);
					p.setFireTicks(0);
				}
				else if (item.getType() == Material.POTION) {
					SlimefunItem sfItem = SlimefunItem.getByItem(item);
					if (sfItem != null && sfItem instanceof Juice) {
						// Fix for Saturation on potions is no longer working
						for (PotionEffect effect : ((PotionMeta) item.getItemMeta()).getCustomEffects()) {
							if (effect.getType().equals(PotionEffectType.SATURATION)) {
								p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, effect.getDuration(), effect.getAmplifier()));
								break;
							}
						}

						// Determine from which hand the juice is being drunk, and its amount
						int mode = 0;
						if (SlimefunManager.isItemSimiliar(item, p.getInventory().getItemInMainHand(), true)) {
							if (p.getInventory().getItemInMainHand().getAmount() == 1) {
								mode = 0;
							}
							else {
								mode = 2;
							}
						}
						else if (SlimefunManager.isItemSimiliar(item, p.getInventory().getItemInOffHand(), true)) {
							if (p.getInventory().getItemInOffHand().getAmount() == 1) {
								mode = 1;
							}
							else {
								mode = 2;
							}
						}

						// Remove the glass bottle once drunk
						final int m = mode;

						Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, () -> {
							if (m == 0) p.getEquipment().setItemInMainHand(null);
							else if (m == 1) p.getEquipment().setItemInOffHand(null);
							else if (m == 2) p.getInventory().removeItem(new ItemStack(Material.GLASS_BOTTLE, 1));
						}, 0L);
					}
				}
			}
			else e.setCancelled(true);
		}
	}

	@EventHandler
	public void onCraft(CraftItemEvent e) {
		for (ItemStack item : e.getInventory().getContents()) {
			if (SlimefunItem.getByItem(item) != null && !(SlimefunItem.getByItem(item).isReplacing())) {
				e.setCancelled(true);
				Messages.local.sendTranslation((Player) e.getWhoClicked(), "workbench.not-enhanced", true);
				break;
			}
		}
	}
	@EventHandler
	public void onPrepareCraft(PrepareItemCraftEvent e) {
		for (ItemStack item : e.getInventory().getContents()) {
			if (SlimefunItem.getByItem(item) != null && !(SlimefunItem.getByItem(item).isReplacing())) {
				e.getInventory().setResult(null);
				break;
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityChangeBlock(EntityChangeBlockEvent e) {
		if (e.getEntity() instanceof FallingBlock) {
			if (Variables.blocks.contains(e.getEntity().getUniqueId())) {
				e.setCancelled(true);
				e.getEntity().remove();
			}
		}
		else if (e.getEntity() instanceof Wither) {
			SlimefunItem item = BlockStorage.check(e.getBlock());
			if (item != null) {
				if (item.getID().equals("WITHER_PROOF_OBSIDIAN")) e.setCancelled(true);
				else if (item.getID().equals("WITHER_PROOF_GLASS")) e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onAnvil(InventoryClickEvent e) {
		if (e.getRawSlot() == 2 && e.getWhoClicked() instanceof Player && e.getInventory().getType() == InventoryType.ANVIL) {
		if (SlimefunManager.isItemSimiliar(e.getInventory().getContents()[0], SlimefunItems.ELYTRA, true)) return;
		if (SlimefunItem.getByItem(e.getInventory().getContents()[0]) != null && !SlimefunItem.isDisabled(e.getInventory().getContents()[0])) {
				e.setCancelled(true);
				Messages.local.sendTranslation((Player) e.getWhoClicked(), "anvil.not-working", true);
			}
		}
	}

	@EventHandler (ignoreCancelled = true)
	public void onPreBrew(InventoryClickEvent e) {
		Inventory inventory = e.getInventory();
		if (inventory instanceof BrewerInventory && inventory.getHolder() instanceof BrewingStand) {
			if (e.getRawSlot() < inventory.getSize()) e.setCancelled(SlimefunItem.getByItem(e.getCursor()) != null);
		}
	}

}
