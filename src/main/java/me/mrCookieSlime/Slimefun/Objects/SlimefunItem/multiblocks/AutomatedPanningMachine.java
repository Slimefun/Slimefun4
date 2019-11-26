package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.cscorelib2.scheduling.TaskQueue;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class AutomatedPanningMachine extends MultiBlockMachine {
	
	// Gold Pan
	private int chanceSiftedOre;
	private int chanceFlint;
	private int chanceClay;
	private int chanceIronNuggets;
	
	// Nether Gold Pan
	private int chanceQuartz;
	private int chanceGoldNuggets;
	private int chanceNetherWart;
	private int chanceBlazePowder;
	private int chanceGlowstoneDust;
	private int chanceGhastTear;

	public AutomatedPanningMachine() {
		super(
				Categories.MACHINES_1, 
				SlimefunItems.AUTOMATED_PANNING_MACHINE, 
				"AUTOMATED_PANNING_MACHINE",
				new ItemStack[] {null, null, null, null, new ItemStack(Material.OAK_TRAPDOOR), null, null, new ItemStack(Material.CAULDRON), null},
				new ItemStack[] {
					new ItemStack(Material.GRAVEL), new ItemStack(Material.FLINT), 
					new ItemStack(Material.GRAVEL), new ItemStack(Material.CLAY_BALL), 
					new ItemStack(Material.GRAVEL), SlimefunItems.SIFTED_ORE
				}, 
				BlockFace.SELF
		);
	}
	
	@Override
	public void postRegister() {
		super.postRegister();
		
		String goldPan = "GOLD_PAN";
		String netherGoldPan = "NETHER_GOLD_PAN";
		
		chanceSiftedOre = (int) Slimefun.getItemValue(goldPan, "chance.SIFTED_ORE");
		chanceClay = (int) Slimefun.getItemValue(goldPan, "chance.CLAY");
		chanceFlint = (int) Slimefun.getItemValue(goldPan, "chance.FLINT");
		chanceIronNuggets = (int) Slimefun.getItemValue(goldPan, "chance.IRON_NUGGET");

		chanceQuartz = (int) Slimefun.getItemValue(netherGoldPan, "chance.QUARTZ");
		chanceGoldNuggets = (int) Slimefun.getItemValue(netherGoldPan, "chance.GOLD_NUGGET");
		chanceNetherWart = (int) Slimefun.getItemValue(netherGoldPan, "chance.NETHER_WART");
		chanceBlazePowder = (int) Slimefun.getItemValue(netherGoldPan, "chance.BLAZE_POWDER");
		chanceGlowstoneDust = (int) Slimefun.getItemValue(netherGoldPan, "chance.GLOWSTONE_DUST");
		chanceGhastTear = (int) Slimefun.getItemValue(netherGoldPan, "chance.GHAST_TEAR");
	}
	
	@Override
	public void onInteract(Player p, Block b) {
		final ItemStack input = p.getInventory().getItemInMainHand();
		
		if (SlimefunManager.isItemSimiliar(input, new ItemStack(Material.GRAVEL), true) || SlimefunManager.isItemSimiliar(input, new ItemStack(Material.SOUL_SAND), true)) {
			final Material block = input.getType();
			
			if (p.getGameMode() != GameMode.CREATIVE) ItemUtils.consumeItem(input, false);
			
			ItemStack output = getRandomDrop(ThreadLocalRandom.current(), block);
			TaskQueue queue = new TaskQueue();
			
			queue.thenRepeatEvery(20, 5, () ->
				b.getWorld().playEffect(b.getRelative(BlockFace.DOWN).getLocation(), Effect.STEP_SOUND, block)
			);
			
			queue.thenRun(20, () -> {
				if (output != null) {
					Inventory outputChest = findOutputChest(b.getRelative(BlockFace.DOWN), output);
					
					if (outputChest != null) {
						outputChest.addItem(output);
					}
					else {
						b.getWorld().dropItemNaturally(b.getLocation(), output);
					}
					
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
				}
				else {
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARMOR_STAND_BREAK, 1F, 1F);
				}
			});
			
			queue.execute(SlimefunPlugin.instance);
		}
		else {
			SlimefunPlugin.getLocal().sendMessage(p, "machines.wrong-item", true);
		}
	}

	private ItemStack getRandomDrop(Random random, Material input) {
		if (input == Material.GRAVEL) {
			if (random.nextInt(100) < chanceSiftedOre) {
				return SlimefunItems.SIFTED_ORE;
			}
			else if (random.nextInt(100) < chanceClay) {
				return new ItemStack(Material.CLAY_BALL);
			}
			else if (random.nextInt(100) < chanceFlint) {
				return new ItemStack(Material.FLINT);
			}
			else if (random.nextInt(100) < chanceIronNuggets) {
				return new ItemStack(Material.IRON_NUGGET);
			}
		}
		else if (input == Material.SOUL_SAND) {
			if (random.nextInt(100) < chanceQuartz) {
				return new ItemStack(Material.QUARTZ);
			}
			else if (random.nextInt(100) < chanceGoldNuggets)  {
				return new ItemStack(Material.GOLD_NUGGET);
			}
			else if (random.nextInt(100) < chanceNetherWart)  {
				return new ItemStack(Material.NETHER_WART);
			}
			else if (random.nextInt(100) < chanceBlazePowder)  {
				return new ItemStack(Material.BLAZE_POWDER);
			}
			else if (random.nextInt(100) < chanceGlowstoneDust)  {
				return new ItemStack(Material.GLOWSTONE_DUST);
			}
			else if (random.nextInt(100) < chanceGhastTear)  {
				return new ItemStack(Material.GHAST_TEAR);
			}
		}
		return null;
	}

}
