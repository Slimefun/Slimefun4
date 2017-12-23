package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import java.util.ArrayList;
import java.util.List;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.api.Slimefun;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

/**
 * @since 4.0
 */
public class Talisman extends SlimefunItem {

	private String suffix;
	private boolean consumable = true;
	private boolean cancel = true;
	private PotionEffect[] effects = new PotionEffect[0];
	private int chance = 100;

	public Talisman(ItemStack item, String id, ItemStack[] recipe, boolean consumable, boolean cancelEvent, String messageSuffix, PotionEffect... effects) {
		super(Categories.TALISMANS_1, item, id, RecipeType.MAGIC_WORKBENCH, recipe, new CustomItem(item, consumable ? 4: 1));
		this.consumable = consumable;
		this.cancel = cancelEvent;
		this.suffix = messageSuffix;
		this.effects = effects;
	}
	
	public Talisman(ItemStack item, String id, ItemStack[] recipe, boolean consumable, boolean cancelEvent, String messageSuffix, int chance, PotionEffect... effects) {
		super(Categories.TALISMANS_1, item, id, RecipeType.MAGIC_WORKBENCH, recipe, new CustomItem(item, consumable ? 4: 1));
		this.consumable = consumable;
		this.cancel = cancelEvent;
		this.suffix = messageSuffix;
		this.effects = effects;
		this.chance = chance;
	}
	
	public Talisman(ItemStack item, String id, ItemStack[] recipe, String messageSuffix, int chance, PotionEffect... effects) {
		super(Categories.TALISMANS_1, item, id, RecipeType.MAGIC_WORKBENCH, recipe, item);
		this.suffix = messageSuffix;
		this.effects = effects;
		this.chance = chance;
	}

	public String getSuffix() 			{		return this.suffix;		}
	public boolean isConsumable()	 	{		return this.consumable;	}
	public boolean isEventCancelled() 	{		return this.cancel;		}
	public PotionEffect[] getEffects()	{		return this.effects;	}
	public int getChance()				{		return this.chance;		}

	public static boolean checkFor(Event e, SlimefunItem talisman) {
		if (talisman != null) {
			if (talisman instanceof Talisman) {
				boolean message = !((Talisman) talisman).getSuffix().equalsIgnoreCase("");
				if (SlimefunStartup.chance(100, ((Talisman) talisman).getChance())) {
					Player p = null;
					
					if (e instanceof EntityDeathEvent) p = ((EntityDeathEvent) e).getEntity().getKiller();
					else if (e instanceof BlockBreakEvent) p = ((BlockBreakEvent) e).getPlayer();
					else if (e instanceof PlayerEvent) p = ((PlayerEvent) e).getPlayer();
					else if (e instanceof EntityEvent) p = (Player) ((EntityEvent) e).getEntity();
					else if (e instanceof EnchantItemEvent) p = ((EnchantItemEvent) e).getEnchanter();
					
					boolean pass = true;
					
					for (PotionEffect effect: ((Talisman) talisman).getEffects()) {
						if (effect != null && p.hasPotionEffect(effect.getType())) pass = false;
					}
					
					if (pass) {
						if (p.getInventory().containsAtLeast(talisman.getItem(), 1)) {
							if (Slimefun.hasUnlocked(p, talisman.getItem(), true)) {
								if (((Talisman) talisman).isConsumable()) p.getInventory().removeItem(talisman.getItem());
								for (PotionEffect effect: ((Talisman) talisman).getEffects()) {
									p.addPotionEffect(effect);
								}
								
								if (e instanceof Cancellable && ((Talisman) talisman).isEventCancelled()) ((Cancellable) e).setCancelled(true);
								
								if (message) Messages.local.sendTranslation(p, "messages.talisman." + ((Talisman) talisman).getSuffix(),true);
								
								return true;
							}
							else return false;
						}
						else if (p.getEnderChest().containsAtLeast(((Talisman) talisman).upgrade(), 1)) {
							if (Slimefun.hasUnlocked(p, ((Talisman) talisman).upgrade(), true)) {
								if (((Talisman) talisman).isConsumable()) p.getEnderChest().removeItem(((Talisman) talisman).upgrade());
								for (PotionEffect effect: ((Talisman) talisman).getEffects()) {
									p.addPotionEffect(effect);
								}
								if (e instanceof Cancellable && ((Talisman) talisman).isEventCancelled()) ((Cancellable) e).setCancelled(true);
								
								if (message) Messages.local.sendTranslation(p, "messages.talisman." + ((Talisman) talisman).getSuffix(), true);
								
								return true;
							}
							else return false;
						}
						else return false;
					}
					else return false;
				}
				else return false;
			}
			else return false;
		}
		else return false;
	}

	public ItemStack upgrade() {
		List<String> lore = new ArrayList<String>();
		lore.add("&7&oEnder Infused");
		lore.add("");
		for (String line: getItem().getItemMeta().getLore()) {
			lore.add(line);
		}
		ItemStack item = new CustomItem(getItem().getType(), "&5Ender " + ChatColor.stripColor(getItem().getItemMeta().getDisplayName()), getItem().getDurability(), lore.toArray(new String[lore.size()]));
		return item;
	}
	
	@Override
	public void create() {
		EnderTalisman talisman = new EnderTalisman(this);
		talisman.register(true);
	}
	
	@Override
	public void install() {
		EnderTalisman talisman = (EnderTalisman) SlimefunItem.getByItem(upgrade());
		Research research = Research.getByID(112);
		if (talisman != null) {
			Slimefun.addOfficialWikiPage(talisman.getID(), "Talismans");
			if (research != null) talisman.bindToResearch(research);
		}
		
		Slimefun.addOfficialWikiPage(getID(), "Talismans");
	}

}
