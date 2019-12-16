package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.materials.MaterialTools;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.DamageableItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.NotPlaceable;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class ExplosiveShovel extends SimpleSlimefunItem<BlockBreakHandler> implements NotPlaceable, DamageableItem {

	private boolean damageOnUse;
	
	public ExplosiveShovel(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String[] keys, Object[] values) {
		super(category, item, recipeType, recipe, keys, values);
	}

	@Override
	protected boolean areItemHandlersPrivate() {
		return false;
	}

	@Override
	public BlockBreakHandler getItemHandler() {
		return (e, item, fortune, drops) -> {
			if (isItem(item)) {
				e.getBlock().getWorld().createExplosion(e.getBlock().getLocation(), 0.0F);
				e.getBlock().getWorld().playSound(e.getBlock().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.3F, 1F);
				
				for (int x = -1; x <= 1; x++) {
					for (int y = -1; y <= 1; y++) {
						for (int z = -1; z <= 1; z++) {
							if (x == 0 && y == 0 && z == 0) {
								continue;
							}
							
							Block b = e.getBlock().getRelative(x, y, z);
							
							if (MaterialTools.getBreakableByShovel().contains(b.getType()) && SlimefunPlugin.getProtectionManager().hasPermission(e.getPlayer(), b.getLocation(), ProtectableAction.BREAK_BLOCK)) {
								SlimefunPlugin.getProtectionManager().logAction(e.getPlayer(), b, ProtectableAction.BREAK_BLOCK);

								b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
								
								for (ItemStack drop : b.getDrops(getItem())) {
									if (drop != null) {
										b.getWorld().dropItemNaturally(b.getLocation(), drop);
									}
								}
								
								b.setType(Material.AIR);
								damageItem(e.getPlayer(), item);
							}
						}
					}
				}

				return true;
			}
			else return false;
		};
	}

	@Override
	public void postRegister() {
		damageOnUse = ((boolean) Slimefun.getItemValue(getID(), "damage-on-use"));
	}
	
	@Override
	public boolean isDamageable() {
		return damageOnUse;
	}

}
