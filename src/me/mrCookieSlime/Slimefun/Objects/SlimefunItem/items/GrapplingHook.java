package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.utils.Utilities;

public class GrapplingHook extends SimpleSlimefunItem<ItemInteractionHandler> {
	
    private long despawnTicks;

	public GrapplingHook(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, String[] keys, Object[] values) {
		super(category, item, id, recipeType, recipe, keys, values);
	}

    @Override
    public ItemInteractionHandler getItemHandler() {
        Utilities utilities = SlimefunPlugin.getUtilities();

        return (e, p, item) -> {
            if (SlimefunManager.isItemSimiliar(item, getItem(), true)) {
                UUID uuid = p.getUniqueId();
                
                if (e.getClickedBlock() == null && !utilities.jumpState.containsKey(uuid)) {
                    e.setCancelled(true);
                    
                    if (p.getInventory().getItemInOffHand().getType() == Material.BOW) {
                        // Cancel, to fix dupe #740
                        return false;
                    }
                    utilities.jumpState.put(uuid, p.getInventory().getItemInMainHand().getType() != Material.SHEARS);
                    
                    if (item.getType() == Material.LEAD) {
                    	item.setAmount(item.getAmount() - 1);
                    }

                    Vector direction = p.getEyeLocation().getDirection().multiply(2.0);
                    Arrow arrow = p.getWorld().spawn(p.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), Arrow.class);
                    arrow.setShooter(p);
                    arrow.setVelocity(direction);

                    Bat b = (Bat) p.getWorld().spawnEntity(p.getLocation(), EntityType.BAT);
                    b.setCanPickupItems(false);
                    b.setAI(false);
                    b.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 100000));
                    b.setLeashHolder(arrow);

                    utilities.damage.add(uuid);
                    utilities.remove.put(uuid, new Entity[]{b, arrow});

                    // To fix issue #253
                    Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> {
                        if (utilities.jumpState.containsKey(uuid)) {
                            utilities.arrows.remove(uuid);

                            for (Entity n : utilities.remove.get(uuid)) {
                                if (n.isValid()) n.remove();
                            }

                            Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> {
                                utilities.damage.remove(uuid);
                                utilities.jumpState.remove(uuid);
                                utilities.remove.remove(uuid);
                            }, 20L);
                        }
                    }, despawnTicks);
                }
                return true;
            }
            else return false;
        };
    }

    @Override
    public void postRegister() {
	    despawnTicks = (int) Slimefun.getItemValue("GRAPPLING_HOOK", "despawn-seconds") * 20L;
    }
}
