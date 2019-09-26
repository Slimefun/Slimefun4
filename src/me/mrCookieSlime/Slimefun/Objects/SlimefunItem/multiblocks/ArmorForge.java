package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks;

import java.util.List;

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
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class ArmorForge extends MultiBlockMachine {

	public ArmorForge() {
		super(
				Categories.MACHINES_1, 
				SlimefunItems.ARMOR_FORGE, 
				"ARMOR_FORGE",
				new ItemStack[] {null, null, null, null, new ItemStack(Material.ANVIL), null, null, new CustomItem(Material.DISPENSER, "Dispenser (Facing up)"), null},
				new ItemStack[0],
				BlockFace.SELF
		);
	}
	
	@Override
	public void onInteract(Player p, Block b) {
		Block dispBlock = b.getRelative(BlockFace.DOWN);
		Dispenser disp = (Dispenser) dispBlock.getState();
		Inventory inv = disp.getInventory();
		List<ItemStack[]> inputs = RecipeType.getRecipeInputList(this);

		for (int i = 0; i < inputs.size(); i++) {
			boolean craft = true;
			for (int j = 0; j < inv.getContents().length; j++) {
				if (!SlimefunManager.isItemSimiliar(inv.getContents()[j], inputs.get(i)[j], true)) {
					craft = false;
					break;
				}
			}

			if (craft) {
				final ItemStack adding = RecipeType.getRecipeOutputList(this, inputs.get(i)).clone();
				if (Slimefun.hasUnlocked(p, adding, true)) {
					Inventory outputInv = findOutputInventory(adding, dispBlock, inv);
					if (outputInv != null) {
						for (ItemStack removing: inputs.get(i)) {
							if (removing != null) inv.removeItem(removing);
						}
						
						for (int j = 0; j < 4; j++) {
							int current = j;
							Bukkit.getScheduler().runTaskLater(SlimefunPlugin.instance, () -> {
								if (current < 3) {
									p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1F, 2F);
								} 
								else {
									p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
									outputInv.addItem(adding);
								}
							}, j*20L);
						}
					}
					else SlimefunPlugin.getLocal().sendMessage(p, "machines.full-inventory", true);
				}
				return;
			}
		}
		
		SlimefunPlugin.getLocal().sendMessage(p, "machines.pattern-not-found", true);
	}

}
