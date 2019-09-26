package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.compatibility.MaterialHelper;
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
		
		for (Material log: Tag.LOGS.getValues()) {
			displayRecipes.add(new ItemStack(log));
			displayRecipes.add(new ItemStack(MaterialHelper.getWoodFromLog(log), 8));
		}
	}
	
	@Override
	public List<ItemStack> getDisplayRecipes() {
		return displayRecipes;
	}
	
	@Override
	public void onInteract(Player p, Block b) {
		ItemStack log = p.getInventory().getItemInMainHand();

		ItemStack item =  new ItemStack(MaterialHelper.getWoodFromLog(log.getType()), 8);
		if(item == null || item.getType() == Material.AIR) return;
		
		b.getWorld().dropItemNaturally(b.getLocation(), item);
		b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, log.getType());
		log.setAmount(log.getAmount() -1);
		
		if(log.getAmount() <= 0) {
			p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
		}
	}

}
