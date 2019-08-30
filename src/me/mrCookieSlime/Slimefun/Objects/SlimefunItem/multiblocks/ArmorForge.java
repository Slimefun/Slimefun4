package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunMachine;
import me.mrCookieSlime.Slimefun.Objects.handlers.MultiBlockInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class ArmorForge extends SlimefunMachine {

	public ArmorForge() {
		super(
				Categories.MACHINES_1, 
				SlimefunItems.ARMOR_FORGE, 
				"ARMOR_FORGE",
				new ItemStack[] {null, null, null, null, new ItemStack(Material.ANVIL), null, null, new CustomItem(Material.DISPENSER, "Dispenser (Facing up)"), null},
				new ItemStack[] {},
				Material.ANVIL
		);
	}
	
	@Override
	public void register() {
		register(true, onInteract());
	}

	private MultiBlockInteractionHandler onInteract() {
		return (p, mb, b) -> {
			if (mb.isMultiBlock(this)) {
				if (!isDisabled() && CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true) && Slimefun.hasUnlocked(p, getItem(), true)) {
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
								Inventory outputInv = SlimefunMachine.findValidOutputInv(adding, dispBlock, inv);
								if (outputInv != null) {
									for (ItemStack removing: inputs.get(i)) {
										if (removing != null) inv.removeItem(removing);
									}
									
									for (int j = 0; j < 4; j++) {
										int current = j;
										Bukkit.getScheduler().runTaskLater(SlimefunStartup.instance, () -> {
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
								else Messages.local.sendTranslation(p, "machines.full-inventory", true);
							}
							return true;
						}
					}
					
					Messages.local.sendTranslation(p, "machines.pattern-not-found", true);
				}
				return true;
			}
			else {
				return false;
			}
		};
	}

}
