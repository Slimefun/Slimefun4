package me.mrCookieSlime.Slimefun.listeners;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;

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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.mrCookieSlime.CSCoreLibPlugin.events.ItemUseEvent;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.SlimefunGuide;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Juice;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.MultiTool;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemConsumptionHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemDropHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunGuideLayout;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.energy.ItemEnergy;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;
import me.mrCookieSlime.Slimefun.utils.Utilities;

public class ItemListener implements Listener {
	
	private Utilities utilities;
	private Random random = new Random();
	
	public ItemListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		utilities = SlimefunPlugin.getUtilities();
	}

	@EventHandler
	public void onIgnitionChamberItemMove(InventoryMoveItemEvent e) {
		if (e.getInitiator().getHolder() instanceof Hopper && BlockStorage.check(((Hopper) e.getInitiator().getHolder()).getBlock(), "IGNITION_CHAMBER")) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onGrindstone(InventoryClickEvent e) {
		if (e.getRawSlot() == 2 && e.getWhoClicked() instanceof Player && e.getInventory().getType() == InventoryType.GRINDSTONE) {
			ItemStack slot0 = e.getInventory().getContents()[0];
			ItemStack slot1 = e.getInventory().getContents()[1];
			if (SlimefunItem.getByItem(slot0) != null && !SlimefunItem.isDisabled(slot0))
				e.setCancelled(true);
			else if (SlimefunItem.getByItem(slot1) != null && !SlimefunItem.isDisabled(slot1))
				e.setCancelled(true);


			if (SlimefunManager.isItemSimiliar(slot0, SlimefunGuide.getItem(SlimefunGuideLayout.BOOK), true))
				e.setCancelled(true);
			else if (SlimefunManager.isItemSimiliar(slot0, SlimefunGuide.getItem(SlimefunGuideLayout.CHEST), true))
				e.setCancelled(true);

			if (SlimefunManager.isItemSimiliar(slot1, SlimefunGuide.getItem(SlimefunGuideLayout.BOOK), true))
				e.setCancelled(true);
			else if (SlimefunManager.isItemSimiliar(slot1, SlimefunGuide.getItem(SlimefunGuideLayout.CHEST), true))
				e.setCancelled(true);
		}
	}

	/*
	 * Handles Left click use and checks for disabled items.
	 */
	@EventHandler
	public void enabledCheck(PlayerInteractEvent e) {
		if (e.getAction() != Action.LEFT_CLICK_AIR && e.getAction() != Action.LEFT_CLICK_BLOCK) {
			return;
		}
		
		ItemStack item = e.getItem();
		if (item != null && !Slimefun.isEnabled(e.getPlayer(), item, true)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void debug(PlayerInteractEvent e) {
		if (e.getAction() == Action.PHYSICAL || e.getHand() != EquipmentSlot.HAND) return;
		
		Player p = e.getPlayer();
		
		if (SlimefunManager.isItemSimiliar(e.getItem(), SlimefunItems.DEBUG_FISH, true)) {
			e.setCancelled(true);
			if (p.isOp()) {
				switch (e.getAction()) {
				case LEFT_CLICK_BLOCK:
					if (p.isSneaking()) {
						if (BlockStorage.hasBlockInfo(e.getClickedBlock())) {
							BlockStorage.clearBlockInfo(e.getClickedBlock());
						}
					}
					else e.setCancelled(false);
					break;
				case RIGHT_CLICK_BLOCK:
					if (p.isSneaking()) {
						Block b = e.getClickedBlock().getRelative(e.getBlockFace());
						b.setType(Material.PLAYER_HEAD);
						try {
							CustomSkull.setSkull(b, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTllYjlkYTI2Y2YyZDMzNDEzOTdhN2Y0OTEzYmEzZDM3ZDFhZDEwZWFlMzBhYjI1ZmEzOWNlYjg0YmMifX19");
						} catch (Exception x) {
							Slimefun.getLogger().log(Level.SEVERE, "An Error occured while using the Debug-Fish for Slimefun " + Slimefun.getVersion(), x);
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
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &dTimings: &e" + SlimefunPlugin.getTicker().getTimings(e.getClickedBlock()) + "ms"));
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &dTotal Timings: &e" + SlimefunPlugin.getTicker().getTimings(BlockStorage.checkID(e.getClickedBlock())) + "ms"));
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &dChunk Timings: &e" + SlimefunPlugin.getTicker().getTimings(e.getClickedBlock().getChunk()) + "ms"));
						}
						else if (BlockStorage.check(e.getClickedBlock()).getEnergyTicker() != null) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dTicking: " + "&b~ &3(Indirect)"));
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &dTimings: &e" + SlimefunPlugin.getTicker().getTimings(e.getClickedBlock()) + "ms"));
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &dChunk Timings: &e" + SlimefunPlugin.getTicker().getTimings(e.getClickedBlock().getChunk()) + "ms"));
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
				default:
					break;

				}
			}
		}
	}
	
	@EventHandler
	public void onBucketUse(PlayerBucketEmptyEvent e) {
		// Fix for placing water on player heads
		Location l = e.getBlockClicked().getRelative(e.getBlockFace()).getLocation();
		if (BlockStorage.hasBlockInfo(l)) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onRightClick(ItemUseEvent e) {
		if (e.getParentEvent() != null && e.getParentEvent().getHand() != EquipmentSlot.HAND) {
			return;
		}

		final Player p = e.getPlayer();
		ItemStack item = e.getItem();

		if (SlimefunManager.isItemSimiliar(item, SlimefunGuide.getItem(SlimefunGuideLayout.BOOK), true)) {
			if (p.isSneaking()) SlimefunGuide.openSettings(p, item);
			else SlimefunGuide.openGuide(p, true);
		}
		else if (SlimefunManager.isItemSimiliar(item, SlimefunGuide.getItem(SlimefunGuideLayout.CHEST), true)) {
			if (p.isSneaking()) SlimefunGuide.openSettings(p, item);
			else SlimefunGuide.openGuide(p, false);
		}
		else if (SlimefunManager.isItemSimiliar(item, SlimefunGuide.getItem(SlimefunGuideLayout.CHEAT_SHEET), true)) {
			if (p.isSneaking()) SlimefunGuide.openSettings(p, item);
			else p.chat("/sf cheat");
		}
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.DEBUG_FISH, true)) {
			// Ignore the debug fish in here
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
				if (tool != null && tool.getType() != Material.AIR) {
					SlimefunItem sfItem = SlimefunItem.getByItem(tool);
					List<Integer> modes = ((MultiTool) sfItem).getModes();
					int index = 0;
					if (utilities.mode.containsKey(p.getUniqueId())) index = utilities.mode.get(p.getUniqueId());

					if (!p.isSneaking()) {
						float charge = ItemEnergy.getStoredEnergy(item);
						float cost = 0.3F;
						if (charge >= cost) {
							p.getEquipment().setItemInMainHand(ItemEnergy.chargeItem(item, -cost));
							Bukkit.getPluginManager().callEvent(new ItemUseEvent(e.getParentEvent(), SlimefunItem.getByID((String) Slimefun.getItemValue(sfItem.getID(), "mode." + modes.get(index) + ".item")).getItem(), e.getClickedBlock()));
						}
					}
					else {
						index++;
						if (index == modes.size()) index = 0;
						
						final int finalIndex = index;
						SlimefunPlugin.getLocal().sendMessage(p, "messages.mode-change", true, msg -> msg.replace("%device%", "Multi Tool").replace("%mode%", (String) Slimefun.getItemValue(sfItem.getID(), "mode." + modes.get(finalIndex) + ".name")));
						utilities.mode.put(p.getUniqueId(), index);
					}
				}
			}
			else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.HEAVY_CREAM, true)) e.setCancelled(true);

			if (e.getClickedBlock() != null && BlockStorage.hasBlockInfo(e.getClickedBlock())) {
				String id = BlockStorage.checkID(e.getClickedBlock());
				if (BlockMenuPreset.isInventory(id) && !canPlaceCargoNodes(p, item, e.getClickedBlock().getRelative(e.getParentEvent().getBlockFace())) && (!p.isSneaking() || item == null || item.getType() == Material.AIR)) {
					e.setCancelled(true);
					BlockStorage storage = BlockStorage.getStorage(e.getClickedBlock().getWorld());

					if (storage.hasUniversalInventory(id)) {
						UniversalBlockMenu menu = storage.getUniversalInventory(id);
						if (menu.canOpen(e.getClickedBlock(), p)) {
							menu.open(p);
						}
						else {
							SlimefunPlugin.getLocal().sendMessage(p, "inventory.no-access", true);
						}
					}
					else if (storage.hasInventory(e.getClickedBlock().getLocation())) {
						BlockMenu menu = BlockStorage.getInventory(e.getClickedBlock().getLocation());
						if (menu.canOpen(e.getClickedBlock(), p)) {
							menu.open(p);
						}
						else {
							SlimefunPlugin.getLocal().sendMessage(p, "inventory.no-access", true);
						}
					}
				}
			}
		}
		else e.setCancelled(true);
	}

	private boolean canPlaceCargoNodes(Player p, ItemStack item, Block b) {
		if (canPlaceBlock(p, b) && SlimefunManager.isItemSimiliar(item, SlimefunItems.CARGO_INPUT, true)) return true;
		else if (canPlaceBlock(p, b) && SlimefunManager.isItemSimiliar(item, SlimefunItems.CARGO_OUTPUT, true)) return true;
		else if (canPlaceBlock(p, b) && SlimefunManager.isItemSimiliar(item, SlimefunItems.CARGO_OUTPUT_ADVANCED, true)) return true;
		else if (canPlaceBlock(p, b) && SlimefunManager.isItemSimiliar(item, SlimefunItems.CT_IMPORT_BUS, true)) return true;
		else if (canPlaceBlock(p, b) && SlimefunManager.isItemSimiliar(item, SlimefunItems.CT_EXPORT_BUS, true)) return true;
		else return false;
	}

	private boolean canPlaceBlock(Player p, Block relative) {
		return p.isSneaking() && relative.getType() == Material.AIR;
	}

	@EventHandler
	public void onEat(PlayerItemConsumeEvent e) {
		if (e.getItem() != null) {
			final Player p = e.getPlayer();
			ItemStack item = e.getItem();
			
			if (Slimefun.hasUnlocked(p, item, true)) {
				for (ItemHandler handler : SlimefunItem.getHandlers("ItemConsumptionHandler")) {
					if (((ItemConsumptionHandler) handler).onConsume(e, p, item)) return;
				}
				
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.FORTUNE_COOKIE, true)) {
					List<String> messages = SlimefunPlugin.getLocal().getMessages("messages.fortune-cookie");
					String message = messages.get(random.nextInt(messages.size()));
					
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
				}
				else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.BEEF_JERKY, true)) {
					p.setSaturation((int) Slimefun.getItemValue("BEEF_JERKY", "Saturation"));
				}
				else if (item.getType() == Material.POTION) {
					SlimefunItem sfItem = SlimefunItem.getByItem(item);
					
					if (sfItem instanceof Juice) {
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

						Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> {
							if (m == 0) p.getEquipment().getItemInMainHand().setAmount(0);
							else if (m == 1) p.getEquipment().getItemInOffHand().setAmount(0);
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
				SlimefunPlugin.getLocal().sendMessage((Player) e.getWhoClicked(), "workbench.not-enhanced", true);
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
			if (utilities.blocks.contains(e.getEntity().getUniqueId())) {
				e.setCancelled(true);
				e.getEntity().remove();
			}
		}
		else if (e.getEntity() instanceof Wither) {
			String id = BlockStorage.checkID(e.getBlock());
			if (id != null) {
				if (id.equals("WITHER_PROOF_OBSIDIAN")) e.setCancelled(true);
				else if (id.equals("WITHER_PROOF_GLASS")) e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onAnvil(InventoryClickEvent e) {
		if (e.getRawSlot() == 2 && e.getWhoClicked() instanceof Player && e.getInventory().getType() == InventoryType.ANVIL) {
			ItemStack slot0 = e.getInventory().getContents()[0];
			ItemStack slot1 = e.getInventory().getContents()[1];
			
			if (SlimefunManager.isItemSimiliar(slot0, SlimefunItems.ELYTRA, true)) return;
			
			if (SlimefunItem.getByItem(slot0) != null && !SlimefunItem.isDisabled(slot0) ||
					SlimefunItem.getByItem(slot1) != null && !SlimefunItem.isDisabled(slot1) ||

					SlimefunManager.isItemSimiliar(slot0, SlimefunGuide.getItem(SlimefunGuideLayout.BOOK), true) ||
					SlimefunManager.isItemSimiliar(slot0, SlimefunGuide.getItem(SlimefunGuideLayout.CHEST), true)||

					SlimefunManager.isItemSimiliar(slot1, SlimefunGuide.getItem(SlimefunGuideLayout.BOOK), true) ||
					SlimefunManager.isItemSimiliar(slot1, SlimefunGuide.getItem(SlimefunGuideLayout.CHEST), true)) {

						e.setCancelled(true);
						SlimefunPlugin.getLocal().sendMessage((Player) e.getWhoClicked(), "anvil.not-working", true);
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPreBrew(InventoryClickEvent e) {
		Inventory inventory = e.getInventory();
		if (inventory instanceof BrewerInventory && inventory.getHolder() instanceof BrewingStand && e.getRawSlot() < inventory.getSize()) {
			e.setCancelled(SlimefunItem.getByItem(e.getCursor()) != null);
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		for (ItemHandler handler: SlimefunItem.getHandlers("ItemDropHandler")) {
			if (((ItemDropHandler) handler).onItemDrop(e, e.getPlayer(), e.getItemDrop())) return;
		}
	}

}
