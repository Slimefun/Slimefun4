package me.mrCookieSlime.Slimefun.listeners;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Particles.FireworkShow;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.HandledBlock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.NotPlaceable;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockPlaceHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.utils.Utilities;

public class ToolListener implements Listener {
	
	// Materials that require a Block under it, e.g. Pressure Plates
	private final Set<Material> sensitiveMaterials = new HashSet<>();
	private final Utilities utilities;
	
	public ToolListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		utilities = SlimefunPlugin.getUtilities();
		
		sensitiveMaterials.add(Material.STONE_PRESSURE_PLATE);
		sensitiveMaterials.add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
		sensitiveMaterials.add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
		sensitiveMaterials.addAll(Tag.SAPLINGS.getValues());
		sensitiveMaterials.addAll(Tag.WOODEN_PRESSURE_PLATES.getValues());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockRegister(BlockPlaceEvent e) {
		if (BlockStorage.hasBlockInfo(e.getBlock())) {
			e.setCancelled(true);
			return;
		}
		
		ItemStack item = e.getItemInHand();
		
		SlimefunItem sfItem = SlimefunItem.getByItem(item);
		if (sfItem != null && !sfItem.isDisabled() && !(sfItem instanceof NotPlaceable)) {
			if (!Slimefun.hasUnlocked(e.getPlayer(), sfItem, true)) {
				e.setCancelled(true);
			}
			else {
				BlockState state = e.getBlock().getState();
				boolean supportsPersistentData = state instanceof TileState;
				
				if (supportsPersistentData) {
					SlimefunPlugin.getBlockDataService().setBlockData((TileState) state, sfItem.getID());
				}
				
				BlockStorage.addBlockInfo(e.getBlock(), "id", sfItem.getID(), true);
				
				SlimefunBlockHandler blockHandler = utilities.blockHandlers.get(sfItem.getID());
				if (blockHandler != null) {
					blockHandler.onPlace(e.getPlayer(), e.getBlock(), sfItem);
				} 
				else {
					for (ItemHandler handler : SlimefunItem.getHandlers("BlockPlaceHandler")) {
						if (((BlockPlaceHandler) handler).onBlockPlace(e, item)) break;
					}
				}
			}
		}
		else {
			for (ItemHandler handler : SlimefunItem.getHandlers("BlockPlaceHandler")) {
				if (((BlockPlaceHandler) handler).onBlockPlace(e, item)) break;
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		ItemStack item = e.getItemInHand();
		
		if (utilities.cancelPlace.remove(e.getPlayer().getUniqueId()))
			e.setCancelled(true);

		if (SlimefunManager.isItemSimilar(item, SlimefunItems.BASIC_CIRCUIT_BOARD, true)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.ADVANCED_CIRCUIT_BOARD, true)) e.setCancelled(true);

		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.PORTABLE_CRAFTER, true)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.PORTABLE_DUSTBIN, true)) e.setCancelled(true);
		
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.BACKPACK_SMALL, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.BACKPACK_MEDIUM, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.BACKPACK_LARGE, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.WOVEN_BACKPACK, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.GILDED_BACKPACK, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.RADIANT_BACKPACK, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.BOUND_BACKPACK, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.COOLER, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.ENDER_BACKPACK, false)) e.setCancelled(true);

		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.CARBON, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.COMPRESSED_CARBON, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.CARBON_CHUNK, false)) e.setCancelled(true);

		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.ANDROID_MEMORY_CORE, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.LAVA_CRYSTAL, false)) e.setCancelled(true);

		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.TINY_URANIUM, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.SMALL_URANIUM, false)) e.setCancelled(true);
		
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.BROKEN_SPAWNER, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.GPS_MARKER_TOOL, true)) {
			e.setCancelled(true);
			Slimefun.getGPSNetwork().addWaypoint(e.getPlayer(), e.getBlock().getLocation());
		}
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.CHRISTMAS_PRESENT, false)) {
			e.setCancelled(true);
			
			if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
				ItemUtils.consumeItem(item, false);
			}
			
			FireworkShow.launchRandom(e.getPlayer(), 3);
			List<ItemStack> gifts = new ArrayList<>();
			
			gifts.add(new CustomItem(SlimefunItems.CHRISTMAS_HOT_CHOCOLATE, 1));
			gifts.add(new CustomItem(SlimefunItems.CHRISTMAS_CHOCOLATE_APPLE, 4));
			gifts.add(new CustomItem(SlimefunItems.CHRISTMAS_CARAMEL_APPLE, 4));
			gifts.add(new CustomItem(SlimefunItems.CHRISTMAS_CAKE, 4));
			gifts.add(new CustomItem(SlimefunItems.CHRISTMAS_COOKIE, 8));
			gifts.add(new CustomItem(SlimefunItems.CHRISTMAS_PRESENT, 1));
			gifts.add(new CustomItem(SlimefunItems.CHRISTMAS_EGG_NOG, 1));
			gifts.add(new CustomItem(SlimefunItems.CHRISTMAS_MILK, 1));
			gifts.add(new CustomItem(SlimefunItems.CHRISTMAS_APPLE_CIDER, 1));
			gifts.add(new CustomItem(SlimefunItems.CHRISTMAS_FRUIT_CAKE, 4));
			gifts.add(new CustomItem(SlimefunItems.CHRISTMAS_APPLE_PIE, 4));
			gifts.add(new ItemStack(Material.EMERALD));
			
			e.getBlockPlaced().getWorld().dropItemNaturally(e.getBlockPlaced().getLocation(), gifts.get(ThreadLocalRandom.current().nextInt(gifts.size())));
		}
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.CARGO_INPUT, false)) {
			if (e.getBlock().getY() != e.getBlockAgainst().getY()) {
				SlimefunPlugin.getLocal().sendMessage(e.getPlayer(), "machines.CARGO_NODES.must-be-placed", true);
				e.setCancelled(true);
			}
		}
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.CARGO_OUTPUT, false)) {
			if (e.getBlock().getY() != e.getBlockAgainst().getY()) {
				SlimefunPlugin.getLocal().sendMessage(e.getPlayer(), "machines.CARGO_NODES.must-be-placed", true);
				e.setCancelled(true);
			}
		}
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.CARGO_OUTPUT_ADVANCED, false)) {
			if (e.getBlock().getY() != e.getBlockAgainst().getY()) {
				SlimefunPlugin.getLocal().sendMessage(e.getPlayer(), "machines.CARGO_NODES.must-be-placed", true);
				e.setCancelled(true);
			}
		}
		else if (SlimefunManager.isItemSimilar(item, SlimefunItems.CT_IMPORT_BUS, false) && e.getBlock().getY() != e.getBlockAgainst().getY()) {
			SlimefunPlugin.getLocal().sendMessage(e.getPlayer(), "machines.CARGO_NODES.must-be-placed", true);
			e.setCancelled(true);
		}
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent e) {
		boolean allow = true;
		List<ItemStack> drops = new ArrayList<>();
		ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
		int fortune = 1;
		
		Block block2 = e.getBlock().getRelative(BlockFace.UP);
		if (sensitiveMaterials.contains(block2.getType())) {
			SlimefunItem sfItem = BlockStorage.check(e.getBlock().getRelative(BlockFace.UP));
			
			if (sfItem == null) {
				BlockState state = block2.getState();
				if (state instanceof TileState) {
					Optional<String> blockData = SlimefunPlugin.getBlockDataService().getBlockData((TileState) state);
					
					if (blockData.isPresent()) {
						sfItem = SlimefunItem.getByID(blockData.get());
					}
				}
			}
			
			if (sfItem != null && !(sfItem instanceof HandledBlock)) {
				SlimefunBlockHandler blockHandler = utilities.blockHandlers.get(sfItem.getID());
				if (blockHandler != null) {
					allow = blockHandler.onBreak(e.getPlayer(), block2, sfItem, UnregisterReason.PLAYER_BREAK);
				} 
				
				if (allow) {
					block2.getWorld().dropItemNaturally(block2.getLocation(), BlockStorage.retrieve(block2));
					block2.setType(Material.AIR);
				}
				else {
					e.setCancelled(true);
					return;
				}
			}
		}

		SlimefunItem sfItem = BlockStorage.check(e.getBlock());
		
		if (sfItem == null) {
			BlockState state = e.getBlock().getState();
			
			if (state instanceof TileState) {
				Optional<String> blockData = SlimefunPlugin.getBlockDataService().getBlockData((TileState) state);
				
				if (blockData.isPresent()) {
					sfItem = SlimefunItem.getByID(blockData.get());
				}
			}
		}
		
		if (sfItem != null && !(sfItem instanceof HandledBlock)) {
			SlimefunBlockHandler blockHandler = utilities.blockHandlers.get(sfItem.getID());
			if (blockHandler != null) {
				allow = blockHandler.onBreak(e.getPlayer(), e.getBlock(), sfItem, UnregisterReason.PLAYER_BREAK);
			} 
			else {
				// Walk over all registered block break handlers until one says that it'll handle it.
				for (ItemHandler handler: SlimefunItem.getHandlers("BlockBreakHandler")) {
					if (((BlockBreakHandler) handler).onBlockBreak(e, item, fortune, drops)) break;
				}
			}
			if (allow) {
				drops.addAll(sfItem.getDrops());
				BlockStorage.clearBlockInfo(e.getBlock());
			}
			else {
				e.setCancelled(true);
				return;
			}
		}
		else if (item != null) {
			if (item.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS) && !item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
				Random random = ThreadLocalRandom.current();
				
				fortune = random.nextInt(item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 2) - 1;
				if (fortune <= 0) fortune = 1;
				fortune = (e.getBlock().getType() == Material.LAPIS_ORE ? 4 + random.nextInt(5) : 1) * (fortune + 1);
			}
			
			for (ItemHandler handler : SlimefunItem.getHandlers("BlockBreakHandler")) {
				if (((BlockBreakHandler) handler).onBlockBreak(e, item, fortune, drops)) break;
			}
		}
		
		if (!drops.isEmpty()) {
			e.getBlock().setType(Material.AIR);
			
			if (e.isDropItems()) {
				for (ItemStack drop : drops) {
					if (drop != null) {
						e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), drop);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityExplode(EntityExplodeEvent e) {
		Iterator<Block> blocks = e.blockList().iterator();
		
		while (blocks.hasNext()) {
			Block block = blocks.next();
			String id = BlockStorage.checkID(block);
    		if (id != null) {
    			blocks.remove();
    			if (!id.equalsIgnoreCase("HARDENED_GLASS") && !id.equalsIgnoreCase("WITHER_PROOF_OBSIDIAN") && !id.equalsIgnoreCase("WITHER_PROOF_GLASS") && !id.equalsIgnoreCase("FORCEFIELD_PROJECTOR") && !id.equalsIgnoreCase("FORCEFIELD_RELAY")) {
    				boolean success = true;
    				SlimefunItem sfItem = SlimefunItem.getByID(id);
    				
    				SlimefunBlockHandler blockHandler = utilities.blockHandlers.get(sfItem.getID());
    				if (blockHandler != null) {
    					success = blockHandler.onBreak(null, block, sfItem, UnregisterReason.EXPLODE);
    				}
    				if (success) {
    					BlockStorage.clearBlockInfo(block);
        				block.setType(Material.AIR);
    				}
    			}
    		}
		}
	    
	}
	
	@EventHandler
	public void onLiquidFlow(BlockFromToEvent e) {
		Block block = e.getToBlock();
		String item = BlockStorage.checkID(block);
		if (item != null) e.setCancelled(true);
	}

}
