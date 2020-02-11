package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.Soulbound;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.NotPlaceable;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * Represents an Item that will not drop on death.
 * 
 * @author TheBusyBiscuit
 *
 */
public class SoulboundItem extends SlimefunItem implements Soulbound, NotPlaceable {

	public SoulboundItem(Category category, SlimefunItemStack item, ItemStack[] recipe) {
		this(category, item, RecipeType.MAGIC_WORKBENCH, recipe);
	}

	public SoulboundItem(Category category, SlimefunItemStack item, RecipeType type, ItemStack[] recipe) {
		super(category, item, type, recipe);
	}

}
