package me.mrCookieSlime.Slimefun.ancient_altar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.utils.Utilities;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class RitualAnimation implements Runnable {

	private List<Block> altars;

	private Block altar;
	private Location l;
	private ItemStack output;
	private List<Block> pedestals;
	private List<ItemStack> items;

	private List<Location> particles;
	private Map<Item, Location> itemLock = new HashMap<>();

	private boolean running;
	private int stage;
	
	private Utilities utilities = SlimefunPlugin.getUtilities();

	public RitualAnimation(List<Block> altars, Block altar, Location drop, ItemStack output, List<Block> pedestals, List<ItemStack> items) {
		this.l = drop;
		this.altar = altar;
		this.altars = altars;
		this.output = output;
		this.pedestals = pedestals;
		this.items = items;
		this.particles = new ArrayList<>();

		this.running = true;
		this.stage = 0;
		
		for (Block pedestal: this.pedestals) {
			Item item = AncientAltarListener.findItem(pedestal);
			this.itemLock.put(item, item.getLocation().clone());
		}
	}

	@Override
	public void run() {
		idle();
		
		if (!checkLockedItems()) {
			abort();
			return;
		}
		
		if (this.stage == 36) {
			finish();
			return;
		}
		
		if (this.stage > 0 && this.stage % 4 == 0) {
			checkPedestal(pedestals.get(this.stage / 4 - 1));
		}
		
		this.stage += 1;
		SlimefunPlugin.instance.getServer().getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, this, 8);
	}

	private boolean checkLockedItems() {
		for (Map.Entry<Item, Location> entry: itemLock.entrySet()) {
			if (entry.getKey().getLocation().distance(entry.getValue()) > 0.3) {
				return false;
			}
		}
			
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
		} catch (Exception x) {
			Slimefun.getLogger().log(Level.SEVERE, "An Error occured while playing Ritual Animation for Slimefun " + Slimefun.getVersion(), x);
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
			pedestal.getWorld().playSound(pedestal.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 2F);

			try {
				l.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE,pedestal.getLocation().add(0.5, 1.5, 0.5),16, 0.3F, 0.2F, 0.3F);
				l.getWorld().spawnParticle(Particle.CRIT_MAGIC,pedestal.getLocation().add(0.5, 1.5, 0.5), 8,0.3F, 0.2F, 0.3F);
			} catch (Exception x) {
				Slimefun.getLogger().log(Level.SEVERE, "An Error occured while playing Pedestal Animation for Slimefun " + Slimefun.getVersion(), x);
			}
			
			itemLock.remove(item);
			item.remove();
			
			pedestal.removeMetadata("item_placed", SlimefunPlugin.instance);
		}
	}

	private void abort() {
		running = false;
		pedestals.forEach(b -> utilities.altarinuse.remove(b.getLocation()));
    
		// This should re-enable altar blocks on craft failure.
		utilities.altarinuse.remove(altar.getLocation());
		l.getWorld().playSound(l, Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1F, 1F);
		itemLock.clear();
		altars.remove(altar);
	}
  
	private void finish() {
		if (running) {
			l.getWorld().playSound(l, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1F, 1F);
			l.getWorld().playEffect(l, Effect.STEP_SOUND, Material.EMERALD_BLOCK);
			l.getWorld().dropItemNaturally(l.add(0, -0.5, 0), output);
      
			pedestals.forEach(b -> utilities.altarinuse.remove(b.getLocation()));
			
			// This should re-enable altar blocks on craft completion.
			utilities.altarinuse.remove(altar.getLocation());
			altars.remove(altar);
		}
		else {
			l.getWorld().playSound(l, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1F, 1F);
		}
	}
}
