package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.HandledBlock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.DamageableItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.NotPlaceable;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class ExplosivePickaxe extends SimpleSlimefunItem<BlockBreakHandler> implements NotPlaceable, DamageableItem {

	private String[] blacklist;
	private boolean damageOnUse;
	
	public ExplosivePickaxe(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String[] keys, Object[] values) {
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
							
							if (b.getType() != Material.AIR && !b.isLiquid() && !StringUtils.equals(b.getType().toString(), blacklist) && SlimefunPlugin.getProtectionManager().hasPermission(e.getPlayer(), b.getLocation(), ProtectableAction.BREAK_BLOCK)) {
								SlimefunPlugin.getProtectionManager().logAction(e.getPlayer(), b, ProtectableAction.BREAK_BLOCK);

								b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
								SlimefunItem sfItem = BlockStorage.check(b);
								boolean allow = false;
								
								if (sfItem != null && !(sfItem instanceof HandledBlock)) {
									if (SlimefunPlugin.getUtilities().blockHandlers.containsKey(sfItem.getID())) {
										allow = SlimefunPlugin.getUtilities().blockHandlers.get(sfItem.getID()).onBreak(e.getPlayer(), e.getBlock(), sfItem, UnregisterReason.PLAYER_BREAK);
									}
									if (allow) {
										drops.add(BlockStorage.retrieve(e.getBlock()));
									}
								}
								else if (b.getType() == Material.PLAYER_HEAD) {
									b.breakNaturally();
								}
								else if (b.getType().name().endsWith("_SHULKER_BOX")) {
									b.breakNaturally();
								}
								else {
									for (ItemStack drop : b.getDrops(getItem())) {
										b.getWorld().dropItemNaturally(b.getLocation(), (b.getType().toString().endsWith("_ORE") && b.getType() != Material.IRON_ORE && b.getType() != Material.GOLD_ORE) ? new CustomItem(drop, fortune): drop);
									}
									b.setType(Material.AIR);
								}
								
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

		List<?> list = (List<?>) Slimefun.getItemValue(getID(), "unbreakable-blocks");
		blacklist = list.toArray(new String[list.size()]);
	}

	@Override
	public boolean isDamageable() {
		return damageOnUse;
	}

}
