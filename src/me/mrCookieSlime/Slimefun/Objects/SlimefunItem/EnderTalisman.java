package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.Slimefun;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

/**
 * @since 4.0
 */
public class EnderTalisman extends SlimefunItem {

	private String suffix;
	private boolean consumable;
	private boolean cancel;
	private PotionEffect[] effects;
	private int chance;

	public EnderTalisman(Talisman parent) {
		super(Categories.TALISMANS_2, parent.upgrade(), "ENDER_" + parent.getID(), RecipeType.MAGIC_WORKBENCH, new ItemStack[] {SlimefunItem.getItem("ENDER_LUMP_3"), null, SlimefunItem.getItem("ENDER_LUMP_3"), null, parent.getItem(), null, SlimefunItem.getItem("ENDER_LUMP_3"), null, SlimefunItem.getItem("ENDER_LUMP_3")}, parent.upgrade());
		this.consumable = parent.isConsumable();
		this.cancel = parent.isEventCancelled();
		this.suffix = parent.getSuffix();
		this.effects = parent.getEffects();
		this.chance = parent.getChance();
		Slimefun.addHint("ENDER_" + parent.getID(), "&eEnder Talismans have the advantage", "&eof still working while they", "&eare in your Ender Chest");
	}

	public String getSuffix() 			{		return this.suffix;		}
	public boolean isConsumable()	 	{		return this.consumable;	}
	public boolean isEventCancelled() 	{		return this.cancel;		}
	public PotionEffect[] getEffects()	{		return this.effects;	}
	public int getChance()				{		return this.chance;		}
}
