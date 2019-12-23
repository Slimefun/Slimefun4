package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * @since 4.0
 */
public class EnderTalisman extends Talisman {

	public EnderTalisman(Talisman parent) {
		super(Categories.TALISMANS_2, parent.upgrade(), new ItemStack[] {SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3, null, parent.getItem(), null, SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3}, parent.isConsumable(), parent.isEventCancelled(), parent.getSuffix(), parent.getChance(), parent.getEffects());
		
		this.setHint("&eEnder Talismans have the advantage", "&eof still working while they", "&eare in your Ender Chest");
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
	public void create() {
		// Let's override that, otherwise we would be creating Ender Talismans
		// for every Ender Talisman
	}
}
