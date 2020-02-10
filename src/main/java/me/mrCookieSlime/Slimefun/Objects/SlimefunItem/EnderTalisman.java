package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * @since 4.0
 */
public class EnderTalisman extends Talisman {

	public EnderTalisman(Talisman parent) {
		super(Categories.TALISMANS_2, parent.upgrade(), new ItemStack[] {SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3, null, parent.getItem(), null, SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3}, parent.isConsumable(), parent.isEventCancelled(), parent.getSuffix(), parent.getChance(), parent.getEffects());
	}
	
	@Override
	public SlimefunItemStack upgrade() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void install() {
		// Let's override that, otherwise we would be creating Ender Talismans
		// for every Ender Talisman
	}
	
	@Override
	public void postRegister() {
		// Let's override that, otherwise we would be creating Ender Talismans
		// for every Ender Talisman
	}
}
