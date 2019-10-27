package me.mrCookieSlime.Slimefun.api.item_transport;

import java.util.Comparator;

import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class RecipeSorter implements Comparator<Integer> {
	
	private BlockMenu menu;
	
	public RecipeSorter(BlockMenu menu) {
		this.menu = menu;
	}
	
	@Override
	public int compare(Integer slot1, Integer slot2) {
		return menu.getItemInSlot(slot1).getAmount() - menu.getItemInSlot(slot2).getAmount();
	}

}
