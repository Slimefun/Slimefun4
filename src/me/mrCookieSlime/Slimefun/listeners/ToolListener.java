package me.mrCookieSlime.Slimefun.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.SkullItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Particles.FireworkShow;
import me.mrCookieSlime.CSCoreLibPlugin.general.Player.PlayerInventory;
import me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Variables;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.HandledBlock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Talisman;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Interfaces.NotPlaceable;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockPlaceHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class ToolListener implements Listener {
	
	public ToolListener(SlimefunStartup plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockRegister(BlockPlaceEvent e) {
		if (BlockStorage.hasBlockInfo(e.getBlock())) {
			e.setCancelled(true);
			return;
		}
		ItemStack item = e.getItemInHand();
		if (item != null && item.getType() == Material.INK_SACK) return;
		SlimefunItem sfItem = SlimefunItem.getByItem(item);
		if (sfItem != null && !(sfItem instanceof NotPlaceable)){
			BlockStorage.addBlockInfo(e.getBlock(), "id", sfItem.getName(), true);
			if (SlimefunItem.blockhandler.containsKey(sfItem.getName())) {
				SlimefunItem.blockhandler.get(sfItem.getName()).onPlace(e.getPlayer(), e.getBlock(), sfItem);
			}
		}
		else {
			for (ItemHandler handler: SlimefunItem.getHandlers("BlockPlaceHandler")) {
				if (((BlockPlaceHandler) handler).onBlockPlace(e, item)) break;
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		ItemStack item = e.getItemInHand();
		
		if (Variables.cancelPlace.contains(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
			Variables.cancelPlace.remove(e.getPlayer().getUniqueId());
		}
		if (SlimefunManager.isItemSimiliar(item, SlimefunItems.BASIC_CIRCUIT_BOARD, true)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.ADVANCED_CIRCUIT_BOARD, true)) e.setCancelled(true);
		
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.BACKPACK_SMALL, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.BACKPACK_MEDIUM, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.BACKPACK_LARGE, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.WOVEN_BACKPACK, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.GILDED_BACKPACK, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.BOUND_BACKPACK, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.COOLER, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.ENDER_BACKPACK, false)) e.setCancelled(true);

		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.CARBON, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.COMPRESSED_CARBON, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.CARBON_CHUNK, false)) e.setCancelled(true);

		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.ANDROID_MEMORY_CORE, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.LAVA_CRYSTAL, false)) e.setCancelled(true);

		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.TINY_URANIUM, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.SMALL_URANIUM, false)) e.setCancelled(true);
		
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.BROKEN_SPAWNER, false)) e.setCancelled(true);
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.GPS_MARKER_TOOL, true)) {
			e.setCancelled(true);
			Slimefun.getGPSNetwork().addWaypoint(e.getPlayer(), e.getBlock().getLocation());
		}
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.PRESENT, false)) {
			e.setCancelled(true);
			PlayerInventory.consumeItemInHand(e.getPlayer());
			FireworkShow.launchRandom(e.getPlayer(), 3);
			List<ItemStack> gifts = new ArrayList<ItemStack>();
			for (int i = 0; i < 2; i++) {
				gifts.add(new CustomItem(SlimefunItems.HOT_CHOCOLATE, 1));
				gifts.add(new CustomItem(SlimefunItems.CHOCOLATE_APPLE, 4));
				gifts.add(new CustomItem(SlimefunItems.CARAMEL_APPLE, 4));
				gifts.add(new CustomItem(SlimefunItems.CHRISTMAS_CAKE, 4));
				gifts.add(new CustomItem(SlimefunItems.CHRISTMAS_COOKIE, 8));
				gifts.add(new CustomItem(SlimefunItems.PRESENT, 1));
				gifts.add(new CustomItem(SlimefunItems.EGG_NOG, 1));
				gifts.add(new CustomItem(SlimefunItems.MILK, 1));
				gifts.add(new CustomItem(SlimefunItems.APPLE_CIDER, 1));
				gifts.add(new CustomItem(SlimefunItems.FRUIT_CAKE, 4));
				gifts.add(new CustomItem(SlimefunItems.APPLE_PIE, 4));
			}
			gifts.add(new SkullItem("mrCookieSlime"));
			gifts.add(new SkullItem("timtower"));
			gifts.add(new SkullItem("bwfcwalshy"));
			gifts.add(new SkullItem("jadedcat"));
			gifts.add(new SkullItem("ZeldoKavira"));
			gifts.add(new SkullItem("eyamaz"));
			gifts.add(new SkullItem("Kaelten"));
			gifts.add(new SkullItem("ahamling27"));
			gifts.add(new SkullItem("Myrathi"));
			
			new String(
			"Good day to whoever is just looking through my code." + 
			"Since it is Christmas, I wanted to add some Christmas flavour to this Plugin." +
			"So, I hope you don't mind that I implemented some of your Heads >.>" +
			"Merry Christmas everyone!" +
			"" +
			"- mrCookieSlime"
			);
			e.getBlockPlaced().getWorld().dropItemNaturally(e.getBlockPlaced().getLocation(), gifts.get(SlimefunStartup.randomize(gifts.size())));
		}
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.CARGO_INPUT, false)) {
			if (e.getBlock().getY() != e.getBlockAgainst().getY()) {
				Messages.local.sendTranslation(e.getPlayer(), "machines.CARGO_NODES.must-be-placed", true);
				e.setCancelled(true);
			}
		}
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.CARGO_OUTPUT, false)) {
			if (e.getBlock().getY() != e.getBlockAgainst().getY()) {
				Messages.local.sendTranslation(e.getPlayer(), "machines.CARGO_NODES.must-be-placed", true);
				e.setCancelled(true);
			}
		}
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.CARGO_OUTPUT_ADVANCED, false)) {
			if (e.getBlock().getY() != e.getBlockAgainst().getY()) {
				Messages.local.sendTranslation(e.getPlayer(), "machines.CARGO_NODES.must-be-placed", true);
				e.setCancelled(true);
			}
		}
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.CT_IMPORT_BUS, false)) {
			if (e.getBlock().getY() != e.getBlockAgainst().getY()) {
				Messages.local.sendTranslation(e.getPlayer(), "machines.CARGO_NODES.must-be-placed", true);
				e.setCancelled(true);
			}
		}
		
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent e) {
		boolean allow = true;
		List<ItemStack> drops = new ArrayList<ItemStack>();
		ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
		int fortune = 1;
		
		Block block2 = e.getBlock().getRelative(BlockFace.UP);
		if (StringUtils.equals(block2.getType().toString(), "SAPLING", "WOOD_PLATE", "STONE_PLATE", "IRON_PLATE", "GOLD_PLATE")) {
			SlimefunItem sfItem = BlockStorage.check(e.getBlock().getRelative(BlockFace.UP));
			if (sfItem != null && !(sfItem instanceof HandledBlock)) {
				if (SlimefunItem.blockhandler.containsKey(sfItem.getName())) {
					allow = SlimefunItem.blockhandler.get(sfItem.getName()).onBreak(e.getPlayer(), block2, sfItem, UnregisterReason.PLAYER_BREAK);
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
		if (sfItem != null && !(sfItem instanceof HandledBlock)) {
			if (SlimefunItem.blockhandler.containsKey(sfItem.getName())) {
				allow = SlimefunItem.blockhandler.get(sfItem.getName()).onBreak(e.getPlayer(), e.getBlock(), sfItem, UnregisterReason.PLAYER_BREAK);
			}
			if (allow) {
				drops.add(BlockStorage.retrieve(e.getBlock()));
			}
			else {
				e.setCancelled(true);
				return;
			}
		}
		else if (item != null){
			if (item.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS) && !item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
				fortune = SlimefunStartup.randomize(item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 2) - 1;
				if (fortune <= 0) fortune = 1;
				fortune = (e.getBlock().getType() == Material.LAPIS_ORE ? 4 + SlimefunStartup.randomize(5) : 1) * (fortune + 1);
			}
			
			for (ItemHandler handler: SlimefunItem.getHandlers("BlockBreakHandler")) {
				if (((BlockBreakHandler) handler).onBlockBreak(e, item, fortune, drops)) break;
			}
		}
		
		if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH) && e.getBlock().getType().toString().endsWith("_ORE")) {
			if (Talisman.checkFor(e, SlimefunItem.getByName("MINER_TALISMAN"))) {
				if (drops.isEmpty()) drops = (List<ItemStack>) e.getBlock().getDrops();
				for (ItemStack drop: new ArrayList<ItemStack>(drops)) {
					if (!drop.getType().isBlock()) drops.add(new CustomItem(drop, fortune * 2));
				}
			}
		}
		
		if (!drops.isEmpty()) {
			e.getBlock().setType(Material.AIR);
			for (ItemStack drop: drops) {
				if (drop != null) {
					e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), drop);
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onEntityExplode(EntityExplodeEvent e) {
		Iterator<Block> blocks = e.blockList().iterator();
		while (blocks.hasNext()) {
			Block block = blocks.next();
			SlimefunItem item = BlockStorage.check(block);
    		if (item != null) {
    			blocks.remove();
    			if (!item.getName().equalsIgnoreCase("HARDENED_GLASS") && !item.getName().equalsIgnoreCase("WITHER_PROOF_OBSIDIAN") && !item.getName().equalsIgnoreCase("WITHER_PROOF_GLASS") && !item.getName().equalsIgnoreCase("FORCEFIELD_PROJECTOR") && !item.getName().equalsIgnoreCase("FORCEFIELD_RELAY")) {
    				boolean success = true;
    				if (SlimefunItem.blockhandler.containsKey(item.getName())) {
    					success = SlimefunItem.blockhandler.get(item.getName()).onBreak(null, block, item, UnregisterReason.EXPLODE);
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
		SlimefunItem item = BlockStorage.check(block);
		if (item != null) e.setCancelled(true);
	}
}
