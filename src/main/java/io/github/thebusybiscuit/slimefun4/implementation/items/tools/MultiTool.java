package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ChargableItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ItemEnergy;

public class MultiTool extends ChargableItem {
	
	private static final String PREFIX = "mode.";
	private static final float COST = 0.3F;
	
	private Map<UUID, Integer> selectedMode = new HashMap<>();
	private List<Integer> modes;

	public MultiTool(SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String... items) {
		super(Categories.TECH, item, recipeType, recipe, getKeys(items), getValues(items));
	}

	private static String[] getKeys(String... items) {
		String[] keys = new String[items.length * 2];
		
		for (int i = 0; i < items.length; i++) {
			keys[i * 2] = PREFIX + i + ".enabled";
			keys[i * 2 + 1] = PREFIX + i + ".item";
		}
		
		return keys;
	}
	
	private static Object[] getValues(String... items) {
		Object[] values = new Object[items.length * 2];
		
		for (int i = 0; i < items.length; i++) {
			values[i * 2] = true;
			values[i * 2 + 1] = items[i];
		}
		
		return values;
	}
	
	protected ItemUseHandler getItemUseHandler() {
		return e -> {
			Player p = e.getPlayer();
			ItemStack item = e.getItem();
			e.cancel();
			
			int index = selectedMode.getOrDefault(p.getUniqueId(), 0);

			if (!p.isSneaking()) {
				float charge = ItemEnergy.getStoredEnergy(item);
				
				if (charge >= COST) {
					ItemEnergy.chargeItem(item, -COST);
					
					String itemID = (String) Slimefun.getItemValue(getID(), "mode." + modes.get(index) + ".item");
					SlimefunItem sfItem = SlimefunItem.getByID(itemID);
					
					if (sfItem != null) {
						sfItem.callItemHandler(ItemUseHandler.class, handler -> handler.onRightClick(e));
					}
				}
			}
			else {
				index++;
				if (index == modes.size()) index = 0;

				SlimefunItem selectedItem = SlimefunItem.getByID((String) Slimefun.getItemValue(getID(), "mode." + modes.get(index) + ".item"));
				String itemName = selectedItem != null ? selectedItem.getItemName(): "Unknown";
				SlimefunPlugin.getLocal().sendMessage(p, "messages.mode-change", true, msg -> msg.replace("%device%", "Multi Tool").replace("%mode%", ChatColor.stripColor(itemName)));
				selectedMode.put(p.getUniqueId(), index);
			}
		};
	}
	
	private BlockBreakHandler getBlockBreakHandler() {
		return (e, item, fortune, drops) -> {
			if (isItem(item)) {
				e.setCancelled(true);
				return true;
			}
			
			return false;
		};
	}
	
	@Override
	public void preRegister() {
		super.preRegister();
		
		addItemHandler(getItemUseHandler());
		addItemHandler(getBlockBreakHandler());
	}

	@Override
	public void postRegister() {
		List<Integer> list = new ArrayList<>();
		
		int i = 0;
		
		while (Slimefun.getItemValue(this.getID(), PREFIX + i + ".enabled") != null) {
			if ((boolean) Slimefun.getItemValue(this.getID(), PREFIX + i + ".enabled")) {
				list.add(i);
			}
			i++;
		}
		
		this.modes = list;
	}
	
	public List<Integer> getModes() {
		return this.modes;
	}

}
