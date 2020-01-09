package me.mrCookieSlime.Slimefun.ancient_altar;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class RitualAnimation implements Runnable {
	
	private final AncientAltarListener listener;

	private final List<Block> altars;

	private final Block altar;
	private final Location dropLocation;
	private final ItemStack output;
	private final List<Block> pedestals;
	private final List<ItemStack> items;

	private final Collection<Location> particleLocations = new LinkedList<>();
	private final Map<Item, Location> itemLock = new HashMap<>();

	private boolean running;
	private int stage;

	public RitualAnimation(AncientAltarListener listener, List<Block> altars, Block altar, Location drop, ItemStack output, List<Block> pedestals, List<ItemStack> items) {
		this.listener = listener;
		
		this.dropLocation = drop;
		this.altar = altar;
		this.altars = altars;
		this.output = output;
		this.pedestals = pedestals;
		this.items = items;

		this.running = true;
		this.stage = 0;
		
		for (Block pedestal : this.pedestals) {
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
		Slimefun.runSync(this, 8);
	}

	private boolean checkLockedItems() {
		for (Map.Entry<Item, Location> entry : itemLock.entrySet()) {
			if (entry.getKey().getLocation().distance(entry.getValue()) > 0.3) {
				return false;
			}
		}
			
		return true;
	}

	private void idle() {
		dropLocation.getWorld().spawnParticle(Particle.SPELL_WITCH, dropLocation,16, 1.2F, 0F, 1.2F);
		dropLocation.getWorld().spawnParticle(Particle.FIREWORKS_SPARK,dropLocation,8, 0.2F, 0F, 0.2F);
		
		for (Location loc : particleLocations) {
			dropLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, loc,16, 0.3F, 0.2F, 0.3F);
			dropLocation.getWorld().spawnParticle(Particle.CRIT_MAGIC,loc,8, 0.3F, 0.2F, 0.3F);
		}
	}

	private void checkPedestal(Block pedestal) {
		Item item = AncientAltarListener.findItem(pedestal);
		
		if(item == null || itemLock.remove(item) == null) {	
			abort();
		}
		else {
			particleLocations.add(pedestal.getLocation().add(0.5, 1.5, 0.5));
			items.add(AncientAltarListener.fixItemStack(item.getItemStack(), item.getCustomName()));
			pedestal.getWorld().playSound(pedestal.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 2F);

			dropLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE,pedestal.getLocation().add(0.5, 1.5, 0.5), 16, 0.3F, 0.2F, 0.3F);
			dropLocation.getWorld().spawnParticle(Particle.CRIT_MAGIC,pedestal.getLocation().add(0.5, 1.5, 0.5), 8, 0.3F, 0.2F, 0.3F);
			
			itemLock.remove(item);
			item.remove();
			
			pedestal.removeMetadata("item_placed", SlimefunPlugin.instance);
		}
	}

	private void abort() {
		running = false;
		pedestals.forEach(b -> listener.getAltarsInUse().remove(b.getLocation()));
    
		// This should re-enable altar blocks on craft failure.
		listener.getAltarsInUse().remove(altar.getLocation());
		dropLocation.getWorld().playSound(dropLocation, Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1F, 1F);
		itemLock.clear();
		altars.remove(altar);
	}
  
	private void finish() {
		if (running) {
			dropLocation.getWorld().playSound(dropLocation, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1F, 1F);
			dropLocation.getWorld().playEffect(dropLocation, Effect.STEP_SOUND, Material.EMERALD_BLOCK);
			dropLocation.getWorld().dropItemNaturally(dropLocation.add(0, -0.5, 0), output);
      
			pedestals.forEach(b -> listener.getAltarsInUse().remove(b.getLocation()));
			
			// This should re-enable altar blocks on craft completion.
			listener.getAltarsInUse().remove(altar.getLocation());
			altars.remove(altar);
		}
		else {
			dropLocation.getWorld().playSound(dropLocation, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1F, 1F);
		}
	}
}
