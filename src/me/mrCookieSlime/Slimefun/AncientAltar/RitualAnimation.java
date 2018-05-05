package me.mrCookieSlime.Slimefun.AncientAltar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import me.mrCookieSlime.CSCoreLibPlugin.general.Particles.MC_1_8.ParticleEffect;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.listeners.AncientAltarListener;
import me.mrCookieSlime.Slimefun.Variables;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
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
	}

	@Override
	public void run() {
		idle();
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
	
	private void idle() {
		try {
			ParticleEffect.SPELL_WITCH.display(l, 1.2F, 0F, 1.2F, 0, 16);
			ParticleEffect.FIREWORKS_SPARK.display(l, 0.2F, 0F, 0.2F, 0, 8);
			for (Location l2: particles) {
				ParticleEffect.ENCHANTMENT_TABLE.display(l2, 0.3F, 0.2F, 0.3F, 0, 16);
				ParticleEffect.CRIT_MAGIC.display(l2, 0.3F, 0.2F, 0.3F, 0, 8);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void checkPedestal(Block pedestal) {
		Item item = AncientAltarListener.findItem(pedestal);
		if (item == null) abort();
		else {
			particles.add(pedestal.getLocation().add(0.5, 1.5, 0.5));
			items.add(AncientAltarListener.fixItemStack(item.getItemStack(), item.getCustomName()));
			pedestal.getWorld().playSound(pedestal.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 5F, 2F);
			
			try {
				ParticleEffect.ENCHANTMENT_TABLE.display(pedestal.getLocation().add(0.5, 1.5, 0.5), 0.3F, 0.2F, 0.3F, 0, 16);
				ParticleEffect.CRIT_MAGIC.display(pedestal.getLocation().add(0.5, 1.5, 0.5), 0.3F, 0.2F, 0.3F, 0, 8);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			item.remove();
			pedestal.removeMetadata("item_placed", SlimefunStartup.instance);
		}
	}

	private void abort() {
		running = false;
//		for (ItemStack stack: items) {
//			l.getWorld().dropItemNaturally(l, stack);
//		}
		l.getWorld().playSound(l, Sound.BLOCK_NOTE_SNARE, 5F, 1F);
		altars.remove(altar);
	}
	
	private void finish() {
		l.getWorld().playSound(l, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1F, 1F);
		l.getWorld().playEffect(l, Effect.STEP_SOUND, Material.EMERALD_BLOCK);
		l.getWorld().dropItemNaturally(l.add(0, 1, 0), output);

		pedestals.forEach((pblock)->{
			Variables.altarinuse.remove(pblock.getLocation());
		});
		Variables.altarinuse.remove(altar.getLocation());  // should re-enable altar blocks on craft completion.
		
		altars.remove(altar);
	}

}
