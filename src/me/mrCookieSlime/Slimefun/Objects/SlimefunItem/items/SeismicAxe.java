package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.DamageableItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.NotPlaceable;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;

public class SeismicAxe extends SimpleSlimefunItem<ItemInteractionHandler> implements NotPlaceable, DamageableItem {

	public SeismicAxe(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
	}

	@Override
	public ItemInteractionHandler getItemHandler() {
		return (e, p, item) -> {
			if (SlimefunManager.isItemSimiliar(item, SlimefunItems.SEISMIC_AXE, true)) {
				List<Block> blocks = p.getLineOfSight(null, 10);
				for (int i = 0; i < blocks.size(); i++) {
					Block b = blocks.get(i);
					Location ground = b.getLocation();
					
					if (b.getType() == null || b.getType() == Material.AIR) {
						for (int y = ground.getBlockY(); y > 0; y--) {
							if (b.getWorld().getBlockAt(b.getX(), y, b.getZ()) != null && b.getWorld().getBlockAt(b.getX(), y, b.getZ()).getType() != null && b.getWorld().getBlockAt(b.getX(), y, b.getZ()).getType() != Material.AIR) {
								ground = new Location(b.getWorld(), b.getX(), y, b.getZ());
								break;
							}
						}
					}
					
					b.getWorld().playEffect(ground, Effect.STEP_SOUND, ground.getBlock().getType());
					
					if (ground.getBlock().getRelative(BlockFace.UP).getType() == null || ground.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
						Location loc = ground.getBlock().getRelative(BlockFace.UP).getLocation().add(0.5, 0.0, 0.5);
						FallingBlock block = ground.getWorld().spawnFallingBlock(loc, ground.getBlock().getBlockData());
						block.setDropItem(false);
						block.setVelocity(new Vector(0, 0.4 + i * 0.01, 0));
						SlimefunPlugin.getUtilities().blocks.add(block.getUniqueId());
					}
					for (Entity n: ground.getChunk().getEntities()) {
						if (n instanceof LivingEntity && n.getLocation().distance(ground) <= 2.0D && !n.getUniqueId().equals(p.getUniqueId())) {
							Vector vector = n.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().multiply(1.4);
							vector.setY(0.9);
							n.setVelocity(vector);
							
							if (p.getWorld().getPVP()) {
								EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(p, n, DamageCause.ENTITY_ATTACK, 6D);
								Bukkit.getPluginManager().callEvent(event);
								if (!event.isCancelled()) ((LivingEntity) n).damage(6D);
							}
						}
					}
				}

				for (int i = 0; i < 4; i++) {
					damageItem(p, item);
				}
				
				return true;
			}
			else return false;
		};
	}

	@Override
	public boolean isDamageable() {
		return true;
	}

}
