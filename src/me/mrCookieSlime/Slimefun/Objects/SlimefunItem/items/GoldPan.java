package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.CSCoreLibPlugin.events.ItemUseEvent;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunGadget;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class GoldPan extends SlimefunGadget {
	
	private Random random = new Random();
	
	private int chanceSiftedOre;
	private int chanceFlint;
	private int chanceClay;

	public GoldPan(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack[] machineRecipes, String[] keys, Object[] values) {
		super(category, item, id, recipeType, recipe, machineRecipes, keys, values);
	}
	
	@Override
	public void postRegister() {
		chanceSiftedOre = (int) Slimefun.getItemValue(getID(), "chance.SIFTED_ORE");
		chanceClay = (int) Slimefun.getItemValue(getID(), "chance.CLAY");
		chanceFlint = (int) Slimefun.getItemValue(getID(), "chance.FLINT");
	}
	
	@Override
	public void register(boolean slimefun) {
		addItemHandler(new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.GOLD_PAN, true)) {
					if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.GRAVEL && SlimefunPlugin.getProtectionManager().hasPermission(p, e.getClickedBlock().getLocation(), ProtectableAction.BREAK_BLOCK)) {
						List<ItemStack> drops = new ArrayList<>();

						if (random.nextInt(100) < chanceSiftedOre) drops.add(SlimefunItems.SIFTED_ORE);
						else if (random.nextInt(100) < chanceClay) drops.add(new ItemStack(Material.CLAY_BALL));
						else if (random.nextInt(100) < chanceFlint) drops.add(new ItemStack(Material.FLINT));

						e.getClickedBlock().getWorld().playEffect(e.getClickedBlock().getLocation(), Effect.STEP_SOUND, e.getClickedBlock().getType());
						e.getClickedBlock().setType(Material.AIR);

						for (ItemStack drop: drops) {
							e.getClickedBlock().getWorld().dropItemNaturally(e.getClickedBlock().getLocation(), drop);
						}
					}
					e.setCancelled(true);
					return true;
				}
				else return false;
			}
		});
		
		super.register(slimefun);
	}

}
