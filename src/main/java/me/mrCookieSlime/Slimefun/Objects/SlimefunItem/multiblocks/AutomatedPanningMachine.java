package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.collections.RandomizedSet;
import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.cscorelib2.scheduling.TaskQueue;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class AutomatedPanningMachine extends MultiBlockMachine {
	
	private final RandomizedSet<ItemStack> randomizer = new RandomizedSet<>();
	private int weights;
	
	private final RandomizedSet<ItemStack> randomizerNether = new RandomizedSet<>();
	private int weightsNether;

	public AutomatedPanningMachine() {
		super(
				Categories.MACHINES_1, 
				SlimefunItems.AUTOMATED_PANNING_MACHINE, 
				"AUTOMATED_PANNING_MACHINE",
				new ItemStack[] {null, null, null, null, new ItemStack(Material.OAK_TRAPDOOR), null, null, new ItemStack(Material.CAULDRON), null},
				new ItemStack[] {
					new ItemStack(Material.GRAVEL), new ItemStack(Material.FLINT), 
					new ItemStack(Material.GRAVEL), SlimefunItems.SIFTED_ORE, 
					new ItemStack(Material.GRAVEL), new ItemStack(Material.CLAY_BALL), 
					new ItemStack(Material.GRAVEL), new ItemStack(Material.IRON_NUGGET), 
					new ItemStack(Material.SOUL_SAND), new ItemStack(Material.QUARTZ), 
					new ItemStack(Material.SOUL_SAND), new ItemStack(Material.GOLD_NUGGET), 
					new ItemStack(Material.SOUL_SAND), new ItemStack(Material.NETHER_WART), 
					new ItemStack(Material.SOUL_SAND), new ItemStack(Material.BLAZE_POWDER), 
					new ItemStack(Material.SOUL_SAND), new ItemStack(Material.GLOWSTONE_DUST), 
					new ItemStack(Material.SOUL_SAND), new ItemStack(Material.GHAST_TEAR)
				}, 
				BlockFace.SELF
		);
	}
	
	@Override
	public void postRegister() {
		super.postRegister();
		
		String goldPan = "GOLD_PAN";
		String netherGoldPan = "NETHER_GOLD_PAN";
		
		add(false, SlimefunItems.SIFTED_ORE, (int) Slimefun.getItemValue(goldPan, "chance.SIFTED_ORE"));
		add(false, new ItemStack(Material.CLAY_BALL), (int) Slimefun.getItemValue(goldPan, "chance.CLAY"));
		add(false, new ItemStack(Material.FLINT), (int) Slimefun.getItemValue(goldPan, "chance.FLINT"));
		add(false, new ItemStack(Material.IRON_NUGGET), (int) Slimefun.getItemValue(goldPan, "chance.IRON_NUGGET"));

		if (weights < 100) {
			add(false, new ItemStack(Material.AIR), 100 - weights);
		}
		
		add(true, new ItemStack(Material.QUARTZ), (int) Slimefun.getItemValue(netherGoldPan, "chance.QUARTZ"));
		add(true, new ItemStack(Material.GOLD_NUGGET), (int) Slimefun.getItemValue(netherGoldPan, "chance.GOLD_NUGGET"));
		add(true, new ItemStack(Material.NETHER_WART), (int) Slimefun.getItemValue(netherGoldPan, "chance.NETHER_WART"));
		add(true, new ItemStack(Material.BLAZE_POWDER), (int) Slimefun.getItemValue(netherGoldPan, "chance.BLAZE_POWDER"));
		add(true, new ItemStack(Material.GLOWSTONE_DUST), (int) Slimefun.getItemValue(netherGoldPan, "chance.GLOWSTONE_DUST"));
		add(true, new ItemStack(Material.GHAST_TEAR), (int) Slimefun.getItemValue(netherGoldPan, "chance.GHAST_TEAR"));
		

		if (weightsNether < 100) {
			add(true, new ItemStack(Material.AIR), 100 - weightsNether);
		}
	}
	
	private void add(boolean nether, ItemStack item, int chance) {
		if (nether) {
			randomizerNether.add(item, chance);
			weightsNether += chance;
		}
		else {
			randomizer.add(item, chance);
			weights += chance;
		}
	}
	
	@Override
	public void onInteract(Player p, Block b) {
		final ItemStack input = p.getInventory().getItemInMainHand();
		
		if (SlimefunManager.isItemSimilar(input, new ItemStack(Material.GRAVEL), true) || SlimefunManager.isItemSimilar(input, new ItemStack(Material.SOUL_SAND), true)) {
			final Material block = input.getType();
			
			if (p.getGameMode() != GameMode.CREATIVE) {
				ItemUtils.consumeItem(input, false);
			}
			
			ItemStack output = getRandomDrop(block);
			TaskQueue queue = new TaskQueue();
			
			queue.thenRepeatEvery(20, 5, () ->
				b.getWorld().playEffect(b.getRelative(BlockFace.DOWN).getLocation(), Effect.STEP_SOUND, block)
			);
			
			queue.thenRun(20, () -> {
				if (output.getType() != Material.AIR) {
					Inventory outputChest = findOutputChest(b.getRelative(BlockFace.DOWN), output);
					
					if (outputChest != null) {
						outputChest.addItem(output.clone());
					}
					else {
						b.getWorld().dropItemNaturally(b.getLocation(), output.clone());
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

	private ItemStack getRandomDrop(Material input) {
		if (input == Material.GRAVEL) {
			return randomizer.getRandom();
		}
		else if (input == Material.SOUL_SAND) {
			return randomizerNether.getRandom();
		}
		
		return new ItemStack(Material.AIR);
	}

}
