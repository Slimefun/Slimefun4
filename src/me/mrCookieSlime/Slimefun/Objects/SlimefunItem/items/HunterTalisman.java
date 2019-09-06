package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Talisman;
import me.mrCookieSlime.Slimefun.Objects.handlers.EntityKillHandler;

public class HunterTalisman extends Talisman {

	public HunterTalisman(ItemStack item, String id, ItemStack[] recipe, boolean consumable, boolean cancelEvent, String messageSuffix, int chance) {
		super(item, id, recipe, consumable, cancelEvent, messageSuffix, chance);
	}
	
	@Override
	public void preRegister() {
		addItemHandler(getItemHandler());
	}
	
	public EntityKillHandler getItemHandler() {
		return (e, entity, killer, item) -> {
			if (Talisman.checkFor(e, this) && !(e.getEntity() instanceof Player)) {
                if (!e.getEntity().getCanPickupItems()) {
                	List<ItemStack> extraDrops = new ArrayList<>(e.getDrops());
                    
                	if (e.getEntity() instanceof ChestedHorse) {
                		for (ItemStack invItem : ((ChestedHorse) e.getEntity()).getInventory().getStorageContents()) {
                			extraDrops.remove(invItem);
                		}
                		
                		//The chest is not included in getStorageContents()
                		extraDrops.remove(new ItemStack(Material.CHEST));
                	}
                	
                    for (ItemStack drop: extraDrops) {
                        e.getDrops().add(drop);
                    }
                }
                
                return true;
            }
			else {
				return false;
			}
		};
	}

}
