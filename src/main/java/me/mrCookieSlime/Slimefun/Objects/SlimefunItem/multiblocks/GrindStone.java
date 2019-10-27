package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;

public class GrindStone extends MultiBlockMachine {

	public GrindStone() {
		super(
				Categories.MACHINES_1, 
				SlimefunItems.GRIND_STONE, 
				"GRIND_STONE",
				new ItemStack[] {null, null, null, null, new ItemStack(Material.OAK_FENCE), null, null, new CustomItem(Material.DISPENSER, "Dispenser (Facing up)"), null},
				new ItemStack[] {
						new ItemStack(Material.BLAZE_ROD), new ItemStack(Material.BLAZE_POWDER, 4), 
						new ItemStack(Material.BONE), new ItemStack(Material.BONE_MEAL, 4), 
						new ItemStack(Material.GRAVEL), new ItemStack(Material.FLINT), 
						new ItemStack(Material.NETHER_WART), new CustomItem(SlimefunItems.MAGIC_LUMP_1, 2), 
						new ItemStack(Material.ENDER_EYE), new CustomItem(SlimefunItems.ENDER_LUMP_1, 2), 
						new ItemStack(Material.COBBLESTONE), new ItemStack(Material.GRAVEL), 
						new ItemStack(Material.WHEAT), SlimefunItems.WHEAT_FLOUR, 
						new ItemStack(Material.DIRT), SlimefunItems.STONE_CHUNK, 
						new ItemStack(Material.SANDSTONE), new ItemStack(Material.SAND, 4), 
						new ItemStack(Material.RED_SANDSTONE), new ItemStack(Material.RED_SAND, 4)
				},
				BlockFace.SELF
		);
	}
	
	@Override
	public List<ItemStack> getDisplayRecipes() {
		return recipes.stream().map(items -> items[0]).collect(Collectors.toList());
	}
	
	@Override
	public void onInteract(Player p, Block b) {
		Block dispBlock = b.getRelative(BlockFace.DOWN);
		Dispenser disp = (Dispenser) dispBlock.getState();
		Inventory inv = disp.getInventory();
		
		for (ItemStack current: inv.getContents()) {
			for (ItemStack convert: RecipeType.getRecipeInputs(this)) {
				if (convert != null && SlimefunManager.isItemSimiliar(current, convert, true)) {
					ItemStack output = RecipeType.getRecipeOutput(this, convert);
					Inventory outputInv = findOutputInventory(output, dispBlock, inv);
					
					if (outputInv != null) {
						ItemStack removing = current.clone();
						removing.setAmount(1);
						inv.removeItem(removing);
						outputInv.addItem(output);
						p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
					}
					else {
						SlimefunPlugin.getLocal().sendMessage(p, "machines.full-inventory", true);
					}
					
					return;
				}
			}
		}
		SlimefunPlugin.getLocal().sendMessage(p, "machines.unknown-material", true);
	}

}
