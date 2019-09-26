package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Player.PlayerInventory;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class AutomatedPanningMachine extends MultiBlockMachine {

	private Random random = new Random();
	
	private int chanceSiftedOre;
	private int chanceFlint;
	private int chanceClay;

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
		
		chanceSiftedOre = (int) Slimefun.getItemValue("GOLD_PAN", "chance.SIFTED_ORE");
		chanceClay = (int) Slimefun.getItemValue("GOLD_PAN", "chance.CLAY");
		chanceFlint = (int) Slimefun.getItemValue("GOLD_PAN", "chance.FLINT");
	}
	
	@Override
	public void onInteract(Player p, Block b) {
		final ItemStack input = p.getInventory().getItemInMainHand();
		ItemStack output = null;
		
		if (random.nextInt(100) < chanceSiftedOre) output = SlimefunItems.SIFTED_ORE;
		else if (random.nextInt(100) < chanceClay) output = new ItemStack(Material.CLAY_BALL);
		else if (random.nextInt(100) < chanceFlint) output = new ItemStack(Material.FLINT);
		
		final ItemStack drop = output;
		
		if (input != null && input.getType() == Material.GRAVEL && (!input.hasItemMeta() || (input.hasItemMeta() && !input.getItemMeta().hasDisplayName()))) {
			PlayerInventory.consumeItemInHand(p);
			
			for (int i = 1; i < 7; i++) {
				int j = i;
				
				Bukkit.getScheduler().runTaskLater(SlimefunPlugin.instance, () -> {
					b.getWorld().playEffect(b.getRelative(BlockFace.DOWN).getLocation(), Effect.STEP_SOUND, Material.GRAVEL);
					
					if (j == 6) {
						if (drop != null) b.getWorld().dropItemNaturally(b.getLocation(), drop);
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
					}
					
				}, i * 30L);
				
			}
			
			return;
		}
		
		SlimefunPlugin.getLocal().sendMessage(p, "machines.wrong-item", true);
	}

}
