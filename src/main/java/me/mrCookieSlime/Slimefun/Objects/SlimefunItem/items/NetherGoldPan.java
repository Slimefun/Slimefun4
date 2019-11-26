package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class NetherGoldPan extends SimpleSlimefunItem<ItemInteractionHandler> implements RecipeDisplayItem {
	
	private final List<ItemStack> recipes;
	
	private int chanceQuartz;
	private int chanceGoldNuggets;
	private int chanceNetherWart;
	private int chanceBlazePowder;
	private int chanceGlowstoneDust;
	private int chanceGhastTear;

	public NetherGoldPan(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe, 
			new String[] {"chance.QUARTZ", "chance.GOLD_NUGGET", "chance.NETHER_WART", "chance.BLAZE_POWDER", "chance.GLOWSTONE_DUST", "chance.GHAST_TEAR"},
			new Integer[] {50, 25, 10, 8, 5, 2}
		);
		
		recipes = Arrays.asList(
			new ItemStack(Material.SOUL_SAND), new ItemStack(Material.QUARTZ), 
			new ItemStack(Material.SOUL_SAND), new ItemStack(Material.GOLD_NUGGET),
			new ItemStack(Material.SOUL_SAND), new ItemStack(Material.NETHER_WART), 
			new ItemStack(Material.SOUL_SAND), new ItemStack(Material.BLAZE_POWDER), 
			new ItemStack(Material.SOUL_SAND), new ItemStack(Material.GLOWSTONE_DUST),
			new ItemStack(Material.SOUL_SAND), new ItemStack(Material.GHAST_TEAR)
		);
	}
	
	@Override
	public void postRegister() {
		chanceQuartz = (int) Slimefun.getItemValue(getID(), "chance.QUARTZ");
		chanceGoldNuggets = (int) Slimefun.getItemValue(getID(), "chance.GOLD_NUGGET");
		chanceNetherWart = (int) Slimefun.getItemValue(getID(), "chance.NETHER_WART");
		chanceBlazePowder = (int) Slimefun.getItemValue(getID(), "chance.BLAZE_POWDER");
		chanceGlowstoneDust = (int) Slimefun.getItemValue(getID(), "chance.GLOWSTONE_DUST");
		chanceGhastTear = (int) Slimefun.getItemValue(getID(), "chance.GHAST_TEAR");
	}
	
	@Override
	public ItemInteractionHandler getItemHandler() {
		return (e, p, item) -> {
			if (isItem(item)) {
				if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.SOUL_SAND && SlimefunPlugin.getProtectionManager().hasPermission(p, e.getClickedBlock().getLocation(), ProtectableAction.BREAK_BLOCK)) {
					List<ItemStack> drops = new ArrayList<>();
					Random random = ThreadLocalRandom.current();

					if (random.nextInt(100) < chanceQuartz) drops.add(new ItemStack(Material.QUARTZ));
					else if (random.nextInt(100) < chanceGoldNuggets) drops.add(new ItemStack(Material.GOLD_NUGGET));
					else if (random.nextInt(100) < chanceNetherWart) drops.add(new ItemStack(Material.NETHER_WART));
					else if (random.nextInt(100) < chanceBlazePowder) drops.add(new ItemStack(Material.BLAZE_POWDER));
					else if (random.nextInt(100) < chanceGlowstoneDust) drops.add(new ItemStack(Material.GLOWSTONE_DUST));
					else if (random.nextInt(100) < chanceGhastTear) drops.add(new ItemStack(Material.GHAST_TEAR));

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
		};
	}

	@Override
	public List<ItemStack> getDisplayRecipes() {
		return recipes;
	}

}
