package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
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

public class Compressor extends MultiBlockMachine {

	public Compressor() {
		super(
				Categories.MACHINES_1, 
				SlimefunItems.COMPRESSOR, 
				"COMPRESSOR",
				new ItemStack[] {null, null, null, null, new ItemStack(Material.NETHER_BRICK_FENCE), null, new ItemStack(Material.PISTON), new CustomItem(Material.DISPENSER, "Dispenser (Facing up)"), new ItemStack(Material.PISTON)},
				new ItemStack[] {
						new ItemStack(Material.COAL, 8), SlimefunItems.CARBON, 
						new CustomItem(SlimefunItems.STEEL_INGOT, 8), SlimefunItems.STEEL_PLATE, 
						new CustomItem(SlimefunItems.CARBON, 4), SlimefunItems.COMPRESSED_CARBON, 
						new CustomItem(SlimefunItems.STONE_CHUNK, 4), new ItemStack(Material.COBBLESTONE), 
						new CustomItem(SlimefunItems.REINFORCED_ALLOY_INGOT, 8), SlimefunItems.REINFORCED_PLATE
				},
				BlockFace.SELF
		);
	}
	
	@Override
	public List<ItemStack> getDisplayRecipes() {
		return recipes.stream().map(items -> items[0]).collect(Collectors.toList());
	}
	
	@Override
	public void install() {
		// Do nothing to prevent double-registration of recipes
	}
	
	@Override
	public void onInteract(Player p, Block b) {
		Block dispBlock = b.getRelative(BlockFace.DOWN);
		Dispenser disp = (Dispenser) dispBlock.getState();
		Inventory inv = disp.getInventory();
		for (ItemStack current: inv.getContents()) {
			for (ItemStack convert: RecipeType.getRecipeInputs(this)) {
				if (convert != null && SlimefunManager.isItemSimiliar(current, convert, true)) {
					final ItemStack adding = RecipeType.getRecipeOutput(this, convert);
					Inventory outputInv = findOutputInventory(adding, dispBlock, inv);
					if (outputInv != null) {
						ItemStack removing = current.clone();
						removing.setAmount(convert.getAmount());
						inv.removeItem(removing);
						for (int i = 0; i < 4; i++) {
							int j = i;
							
							Bukkit.getScheduler().runTaskLater(SlimefunPlugin.instance, () -> {
								if (j < 3) {
									p.getWorld().playSound(p.getLocation(), j == 1 ? Sound.BLOCK_PISTON_CONTRACT : Sound.BLOCK_PISTON_EXTEND, 1F, j == 0 ? 1F : 2F);
								} 
								else {
									p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
									outputInv.addItem(adding);
								}
							}, i*20L);
						}
					}
					else SlimefunPlugin.getLocal().sendMessage(p, "machines.full-inventory", true);
					
					return;
				}
			}
		}
		SlimefunPlugin.getLocal().sendMessage(p, "machines.unknown-material", true);
	}

}
