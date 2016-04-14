package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.Slimefun;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class EnderTalisman extends SlimefunItem {
	
	boolean consumed;
	boolean cancel;
	PotionEffect[] effects;
	String suffix;
	int chance;

	public EnderTalisman(Talisman parent) {
		super(Categories.TALISMANS_2, parent.upgrade(), "ENDER_" + parent.getName(), RecipeType.MAGIC_WORKBENCH, new ItemStack[] {SlimefunItem.getItem("ENDER_LUMP_3"), null, SlimefunItem.getItem("ENDER_LUMP_3"), null, parent.getItem(), null, SlimefunItem.getItem("ENDER_LUMP_3"), null, SlimefunItem.getItem("ENDER_LUMP_3")}, parent.upgrade());
		this.consumed = parent.isConsumable();
		this.cancel = parent.isEventCancelled();
		this.suffix = parent.getSuffix();
		this.effects = parent.getEffects();
		this.chance = parent.getChance();
		Slimefun.addDescription("ENDER_" + parent.getName(), "&eEnder Talismans have the advantage", "&eof still working while they", "&eare in your Ender Chest");
	}
	
	public PotionEffect[] getEffects()	{		return this.effects;	}
	public boolean isConsumable()	 	{		return this.consumed;	}
	public boolean isEventCancelled() 	{		return this.cancel;		}
	public String getSuffix() 			{		return this.suffix;		}
	public int getChance()				{		return this.chance;		}
}
