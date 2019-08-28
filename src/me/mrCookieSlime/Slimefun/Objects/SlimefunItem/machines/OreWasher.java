package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunMachine;
import me.mrCookieSlime.Slimefun.Objects.handlers.MultiBlockInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class OreWasher extends SlimefunMachine {
	
	public static boolean legacy;
	public static ItemStack[] items;

	public OreWasher() {
		super(
				Categories.MACHINES_1, 
				SlimefunItems.ORE_WASHER, 
				"ORE_WASHER",
				new ItemStack[] {null, new ItemStack(Material.DISPENSER), null, null, new ItemStack(Material.OAK_FENCE), null, null, new ItemStack(Material.CAULDRON), null},
				new ItemStack[] {SlimefunItems.SIFTED_ORE, SlimefunItems.IRON_DUST, SlimefunItems.SIFTED_ORE, SlimefunItems.GOLD_DUST, SlimefunItems.SIFTED_ORE, SlimefunItems.COPPER_DUST, SlimefunItems.SIFTED_ORE, SlimefunItems.TIN_DUST, SlimefunItems.SIFTED_ORE, SlimefunItems.ZINC_DUST, SlimefunItems.SIFTED_ORE, SlimefunItems.ALUMINUM_DUST, SlimefunItems.SIFTED_ORE, SlimefunItems.MAGNESIUM_DUST, SlimefunItems.SIFTED_ORE, SlimefunItems.LEAD_DUST, SlimefunItems.SIFTED_ORE, SlimefunItems.SILVER_DUST},
				Material.OAK_FENCE
		);
	}
	
	@Override
	public void register() {
		register(true, onInteract());
	}

	private MultiBlockInteractionHandler onInteract() {
		return (p, mb, b) -> {
			if (mb.isMultiBlock(this)) {
				if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true)) {
					if (Slimefun.hasUnlocked(p, getItem(), true)) {
						Block dispBlock = b.getRelative(BlockFace.UP);
						Dispenser disp = (Dispenser) dispBlock.getState();
						Inventory inv = disp.getInventory();
						
						for (ItemStack current: inv.getContents()) {
							if (current != null) {
								if (SlimefunManager.isItemSimiliar(current, SlimefunItems.SIFTED_ORE, true)) {
									ItemStack adding = items[new Random().nextInt(items.length)];
									Inventory outputInv = null;
									
									if (!legacy) {
										// This is a fancy way of checking if there is empty space in the inv; by checking if an unobtainable item could fit in it.
										// However, due to the way the method findValidOutputInv() functions, the dummyAdding will never actually be added to the real inventory,
										// so it really doesn't matter what item the ItemStack is made by. SlimefunItems.DEBUG_FISH however, signals that it's
										// not supposed to be given to the player.
										ItemStack dummyAdding = SlimefunItems.DEBUG_FISH;
										outputInv = SlimefunMachine.findValidOutputInv(dummyAdding, dispBlock, inv);
									} 
									else outputInv = SlimefunMachine.findValidOutputInv(adding, dispBlock, inv);
				
									if (outputInv != null) {
										ItemStack removing = current.clone();
										removing.setAmount(1);
										inv.removeItem(removing);
										outputInv.addItem(adding);
										p.getWorld().playSound(b.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1, 1);
										p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.WATER);
										if (InvUtils.fits(outputInv, SlimefunItems.STONE_CHUNK)) outputInv.addItem(SlimefunItems.STONE_CHUNK);
									}
									else Messages.local.sendTranslation(p, "machines.full-inventory", true);
									return true;
								}
								else if (SlimefunManager.isItemSimiliar(current, new ItemStack(Material.SAND, 4), false)) {
									ItemStack adding = SlimefunItems.SALT;
									Inventory outputInv = SlimefunMachine.findValidOutputInv(adding, dispBlock, inv);
									
									if (outputInv != null) {
										ItemStack removing = current.clone();
										removing.setAmount(4);
										inv.removeItem(removing);
										outputInv.addItem(adding);
										p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.WATER);
										p.getWorld().playSound(b.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1, 1);
									}
									else Messages.local.sendTranslation(p, "machines.full-inventory", true);
									
									return true;
								}
								else if (SlimefunManager.isItemSimiliar(current, SlimefunItems.PULVERIZED_ORE, true)) {
									ItemStack adding = SlimefunItems.PURE_ORE_CLUSTER;
									Inventory outputInv = SlimefunMachine.findValidOutputInv(adding, dispBlock, inv);
									
									if (outputInv != null) {
										ItemStack removing = current.clone();
										removing.setAmount(1);
										inv.removeItem(removing);
										outputInv.addItem(adding);
										p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.WATER);
										p.getWorld().playSound(b.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1, 1);
									}
									else Messages.local.sendTranslation(p, "machines.full-inventory", true);
									
									return true;
								}
							}
						}
						Messages.local.sendTranslation(p, "machines.unknown-material", true);
					}
				}
				return true;
			}
			else return false;
		};
	}

}
