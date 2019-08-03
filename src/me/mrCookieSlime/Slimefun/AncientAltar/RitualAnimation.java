package me.mrCookieSlime.Slimefun.AncientAltar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.listeners.AncientAltarListener;
import me.mrCookieSlime.Slimefun.Variables;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class RitualAnimation implements Runnable {

	List<Block> altars;

	Block altar;
	Location l;
	ItemStack output;
	List<Block> pedestals;
	List<ItemStack> items;

	List<Location> particles;
	Map<Item,Location> itemLock = new HashMap<>();

	boolean running;
	int stage;

	public RitualAnimation(List<Block> altars, Block altar, Location drop, ItemStack output, List<Block> pedestals, List<ItemStack> items) {
		this.l = drop;
		this.altar = altar;
		this.altars = altars;
		this.output = output;
		this.pedestals = pedestals;
		this.items = items;
		this.particles = new ArrayList<Location>();

		this.running = true;
		this.stage = 0;
		for(Block ped:this.pedestals) {
			Item itm = AncientAltarListener.findItem(ped);
			this.itemLock.put(itm, itm.getLocation().clone());
		}
	}

	@Override
	public void run() {
		idle();
		if(!checkLockedItems()) {
			abort();
			return;
		}
		if(this.stage == 36) {
			finish();
			return;
		}
		if(this.stage > 0 && this.stage % 4 == 0) {
			checkPedestal(pedestals.get(this.stage / 4 - 1));
		}
		this.stage += 1;
		SlimefunStartup.instance.getServer().getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, this, 8);
	}

	private boolean checkLockedItems() {
		
		for(Item itm:this.itemLock.keySet())
			if(itm.getLocation().distance(this.itemLock.get(itm)) > 0.3)
				return false;
			
		return true;
	}

	private void idle() {
		try {
			l.getWorld().spawnParticle(Particle.SPELL_WITCH, l,16, 1.2F, 0F, 1.2F);
			l.getWorld().spawnParticle(Particle.FIREWORKS_SPARK,l,8, 0.2F, 0F, 0.2F);
			for (Location l2: particles) {
				l.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, l2,16, 0.3F, 0.2F, 0.3F);
				l.getWorld().spawnParticle(Particle.CRIT_MAGIC,l2,8, 0.3F, 0.2F, 0.3F);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkPedestal(Block pedestal) {
		Item item = AncientAltarListener.findItem(pedestal);
		
		
		if(item == null || itemLock.remove(item) == null) {	
			abort();
		}
		
		else {
			particles.add(pedestal.getLocation().add(0.5, 1.5, 0.5));
			items.add(AncientAltarListener.fixItemStack(item.getItemStack(), item.getCustomName()));
			pedestal.getWorld().playSound(pedestal.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 5F, 2F);

			try {
				l.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE,pedestal.getLocation().add(0.5, 1.5, 0.5),16, 0.3F, 0.2F, 0.3F);
				l.getWorld().spawnParticle(Particle.CRIT_MAGIC,pedestal.getLocation().add(0.5, 1.5, 0.5), 8,0.3F, 0.2F, 0.3F);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			itemLock.remove(item);
			item.remove();
			
			pedestal.removeMetadata("item_placed", SlimefunStartup.instance);
		}
	}

	private void abort() {
		running = false;
    
		pedestals.forEach((pblock)->{
			Variables.altarinuse.remove(pblock.getLocation());
		});
    
		Variables.altarinuse.remove(altar.getLocation());  // should re-enable altar blocks on craft failure.
		l.getWorld().playSound(l, Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 5F, 1F);
		itemLock.clear();
		altars.remove(altar);
	}
  
	private void finish() {
		if (running) {
			l.getWorld().playSound(l, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1F, 1F);
			l.getWorld().playEffect(l, Effect.STEP_SOUND, Material.EMERALD_BLOCK);
			l.getWorld().dropItemNaturally(l.add(0, 1, 0), output);
      
			pedestals.forEach((pblock)->{
				Variables.altarinuse.remove(pblock.getLocation());
			});
			Variables.altarinuse.remove(altar.getLocation());  // should re-enable altar blocks on craft completion.
			altars.remove(altar);
		}
		else {
			l.getWorld().playSound(l, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1F, 1F);
		}
	}
}
