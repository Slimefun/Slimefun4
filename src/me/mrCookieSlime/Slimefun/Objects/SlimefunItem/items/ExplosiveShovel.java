package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.materials.MaterialTools;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule.Action;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Interfaces.NotPlaceable;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.utils.DamageableItem;

public class ExplosiveShovel extends SlimefunItem implements NotPlaceable, DamageableItem {
	
	private boolean damageOnUse;
	
	public ExplosiveShovel(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, String[] keys, Object[] values) {
		super(category, item, id, recipeType, recipe, keys, values);
	}
	
	@Override
	public void register(boolean slimefun) {
		addItemHandler(new BlockBreakHandler() {

			@Override
			public boolean onBlockBreak(BlockBreakEvent e, ItemStack item, int fortune, List<ItemStack> drops) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.EXPLOSIVE_SHOVEL, true)) {
					e.getBlock().getWorld().createExplosion(e.getBlock().getLocation(), 0.0F);
					e.getBlock().getWorld().playSound(e.getBlock().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
					
					for (int x = -1; x <= 1; x++) {
						for (int y = -1; y <= 1; y++) {
							for (int z = -1; z <= 1; z++) {
								Block b = e.getBlock().getRelative(x, y, z);
								boolean correctType = false;
								
								for (Material mat : MaterialTools.getShovelItems()) {
									if (b.getType() == mat) {
										correctType = true;
										break;
									}
								}
								
								if (correctType && SlimefunPlugin.getProtectionManager().hasPermission(e.getPlayer(), b.getLocation(), Action.BREAK_BLOCK)) {
									if (SlimefunPlugin.getHooks().isCoreProtectInstalled()) {
										SlimefunPlugin.getHooks().getCoreProtectAPI().logRemoval(e.getPlayer().getName(), b.getLocation(), b.getType(), b.getBlockData());
									}

									b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
									for (ItemStack drop: b.getDrops()) {
										b.getWorld().dropItemNaturally(b.getLocation(), drop);
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
			}
		});
		
		super.register(slimefun);
		damageOnUse = ((Boolean) Slimefun.getItemValue(getID(), "damage-on-use"));
	}

	@Override
	public boolean isDamageable() {
		return damageOnUse;
	}

}
