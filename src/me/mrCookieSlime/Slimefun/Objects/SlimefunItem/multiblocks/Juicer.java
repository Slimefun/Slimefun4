package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;

public class Juicer extends MultiBlockMachine {

	public Juicer() {
		super(
				Categories.MACHINES_1, SlimefunItems.JUICER, "JUICER",
				new ItemStack[] {null, new ItemStack(Material.GLASS), null, null, new ItemStack(Material.NETHER_BRICK_FENCE), null, null, new CustomItem(Material.DISPENSER, "Dispenser (Facing up)"), null},
				new ItemStack[] {
						new ItemStack(Material.APPLE), SlimefunItems.APPLE_JUICE,
						new ItemStack(Material.MELON), SlimefunItems.MELON_JUICE,
						new ItemStack(Material.CARROT), SlimefunItems.CARROT_JUICE,
						new ItemStack(Material.PUMPKIN), SlimefunItems.PUMPKIN_JUICE
				},
				Material.NETHER_BRICK_FENCE
		);
	}
	
	@Override
	public void onInteract(Player p, Block b) {
		Block dispBlock = b.getRelative(BlockFace.DOWN);
		Dispenser disp = (Dispenser) dispBlock.getState();
		Inventory inv = disp.getInventory();
		for (ItemStack current: inv.getContents()) {
			for (ItemStack convert: RecipeType.getRecipeInputs(this)) {
				if (convert != null && SlimefunManager.isItemSimiliar(current, convert, true)) {
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
					else Messages.local.sendTranslation(p, "machines.full-inventory", true);
					
					return;
				}
			}
		}
		
		Messages.local.sendTranslation(p, "machines.unknown-material", true);
	}

}
