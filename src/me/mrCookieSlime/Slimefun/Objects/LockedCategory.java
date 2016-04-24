package me.mrCookieSlime.Slimefun.Objects;

import java.util.Arrays;
import java.util.List;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LockedCategory extends Category {
	
	List<Category> parents;

	public LockedCategory(ItemStack item, Category... parents) {
		super(item);
		this.parents = Arrays.asList(parents);
	}

	public LockedCategory(ItemStack item, int tier, Category... parents) {
		super(item, tier);
		this.parents = Arrays.asList(parents);
	}
	
	public List<Category> getParents() {
		return parents;
	}
	
	public void addParent(Category category) {
		this.parents.add(category);
	}
	
	public void removeParent(Category category) {
		this.parents.remove(category);
	}
	
	public boolean hasUnlocked(Player p) {
		for (Category category: parents) {
			for (SlimefunItem item: category.getItems()) {
				if (Slimefun.isEnabled(p, item.getItem(), false) && Slimefun.hasPermission(p, item, false)) {
					if (item.getResearch() != null) {
						if (!item.getResearch().hasUnlocked(p)) return false;
					}
				}
			}
		}
		return true;
	}
}
