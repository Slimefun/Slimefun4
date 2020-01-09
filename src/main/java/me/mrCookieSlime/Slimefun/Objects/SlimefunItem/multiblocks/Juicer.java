package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;

public class Juicer extends MultiBlockMachine {

	public Juicer() {
		super(
				Categories.MACHINES_1, 
				SlimefunItems.JUICER, 
				"JUICER",
				new ItemStack[] {null, new ItemStack(Material.GLASS), null, null, new ItemStack(Material.NETHER_BRICK_FENCE), null, null, new CustomItem(Material.DISPENSER, "Dispenser (Facing up)"), null},
				new ItemStack[0],
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
		
		for (ItemStack current : inv.getContents()) {
			for (ItemStack convert : RecipeType.getRecipeInputs(this)) {
				if (convert != null && SlimefunManager.isItemSimilar(current, convert, true)) {
					ItemStack adding = RecipeType.getRecipeOutput(this, convert);
					Inventory outputInv = findOutputInventory(adding, dispBlock, inv);
					
					if (outputInv != null) {
						ItemStack removing = current.clone();
						removing.setAmount(1);
						inv.removeItem(removing);
						outputInv.addItem(adding);
						p.getWorld().playSound(b.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1F, 1F);
						p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.HAY_BLOCK);
					}
					else SlimefunPlugin.getLocal().sendMessage(p, "machines.full-inventory", true);
					
					return;
				}
			}
		}
		
		SlimefunPlugin.getLocal().sendMessage(p, "machines.unknown-material", true);
	}

}
