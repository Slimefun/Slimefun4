package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialConverter;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;

public class TableSaw extends MultiBlockMachine {
	
	private final List<ItemStack> displayRecipes = new ArrayList<>();

	public TableSaw() {
		super(
				Categories.MACHINES_1, 
				SlimefunItems.TABLE_SAW, 
				"TABLE_SAW",
				new ItemStack[] {null, null, null, new ItemStack(Material.SMOOTH_STONE_SLAB), new ItemStack(Material.STONECUTTER), new ItemStack(Material.SMOOTH_STONE_SLAB), null, new ItemStack(Material.IRON_BLOCK), null},
				new ItemStack[0], 
				BlockFace.SELF
		);
		
		for (Material log : Tag.LOGS.getValues()) {
			Optional<Material> planks = MaterialConverter.getPlanksFromLog(log);
			
			if (planks.isPresent()) {
				displayRecipes.add(new ItemStack(log));
				displayRecipes.add(new ItemStack(planks.get(), 8));
			}
		}
	}
	
	@Override
	public List<ItemStack> getDisplayRecipes() {
		return displayRecipes;
	}
	
	@Override
	public void onInteract(Player p, Block b) {
		ItemStack log = p.getInventory().getItemInMainHand();

		Optional<Material> planks = MaterialConverter.getPlanksFromLog(log.getType());
		
		if (planks.isPresent()) {
			if (p.getGameMode() != GameMode.CREATIVE) {
				ItemUtils.consumeItem(log, true);
			}
			
			ItemStack output = new ItemStack(planks.get(), 8);
			Inventory outputChest = findOutputChest(b, output);
			
			if (outputChest != null) {
				outputChest.addItem(output);
			}
			else {
				b.getWorld().dropItemNaturally(b.getLocation(), output);
			}
			
			b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, log.getType());
		}
	}

}
