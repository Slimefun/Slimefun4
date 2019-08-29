package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

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
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class Smeltery extends SlimefunMachine {

	public Smeltery() {
		super(
				Categories.MACHINES_1, 
				SlimefunItems.SMELTERY, 
				"SMELTERY",
				new ItemStack[] {null, new ItemStack(Material.NETHER_BRICK_FENCE), null, new ItemStack(Material.NETHER_BRICKS), new CustomItem(Material.DISPENSER, "Dispencer (Faced up)"), new ItemStack(Material.NETHER_BRICKS), null, new ItemStack(Material.FLINT_AND_STEEL), null},
				new ItemStack[] {SlimefunItems.IRON_DUST, new ItemStack(Material.IRON_INGOT)}, 
				Material.NETHER_BRICK_FENCE,
				new String[] {"chance.fireBreak"}, new Integer[] {34}
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
						Block dispBlock = b.getRelative(BlockFace.DOWN);
						Dispenser disp = (Dispenser) dispBlock.getState();
						Inventory inv = disp.getInventory();
						List<ItemStack[]> inputs = RecipeType.getRecipeInputList(this);

						for (int i = 0; i < inputs.size(); i++) {
							boolean craft = true;
							for (ItemStack converting: inputs.get(i)) {
								if (converting != null) {
									for (int j = 0; j < inv.getContents().length; j++) {
										if (j == (inv.getContents().length - 1) && !SlimefunManager.isItemSimiliar(converting, inv.getContents()[j], true)) {
											craft = false;
											break;
										}
										else if (SlimefunManager.isItemSimiliar(inv.getContents()[j], converting, true)) break;
									}
								}
							}

							if (craft) {
								ItemStack adding = RecipeType.getRecipeOutputList(this, inputs.get(i)).clone();
								if (Slimefun.hasUnlocked(p, adding, true)) {
									Inventory outputInv = SlimefunMachine.findValidOutputInv(adding, dispBlock, inv);
									if (outputInv != null) {
										for (ItemStack removing: inputs.get(i)) {
											if (removing != null) inv.removeItem(removing);
										}
										outputInv.addItem(adding);
										p.getWorld().playSound(p.getLocation(), Sound.BLOCK_LAVA_POP, 1, 1);
										p.getWorld().playEffect(b.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
										
										Hopper chamber = findHopper(dispBlock, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
										
										if (new Random().nextInt(100) < SlimefunStartup.instance.getSettings().SMELTERY_FIRE_BREAK_CHANCE) {
											if (chamber != null) {
												if (chamber.getInventory().contains(Material.FLINT_AND_STEEL)) {
													ItemStack item = chamber.getInventory().getItem(chamber.getInventory().first(Material.FLINT_AND_STEEL));
													ItemMeta meta = item.getItemMeta();
													((Damageable) meta).setDamage(((Damageable) meta).getDamage() + 1);
													item.setItemMeta(meta);
													
													if (((Damageable) item.getItemMeta()).getDamage() >= item.getType().getMaxDurability()) {
														item.setAmount(0); 
														p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
													}
													
													p.getWorld().playSound(p.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, 1, 1);
												} 
												else {
													Messages.local.sendTranslation(p, "machines.ignition-chamber-no-flint", true);
													
													Block fire = b.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN);
													fire.getWorld().playEffect(fire.getLocation(), Effect.STEP_SOUND, fire.getType());
													fire.setType(Material.AIR);
												}
											} 
											else {
												Block fire = b.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN);
												fire.getWorld().playEffect(fire.getLocation(), Effect.STEP_SOUND, fire.getType());
												fire.setType(Material.AIR);
											}
										}
									}
									else Messages.local.sendTranslation(p, "machines.full-inventory", true);
								}
								return true;
							}
						}
						Messages.local.sendTranslation(p, "machines.pattern-not-found", true);
					}
				}
				return true;
			}
			else return false;
		};
	}
	
	private Hopper findHopper(Block b, BlockFace... faces) {
		for (BlockFace face: faces) {
			if (b.getRelative(face).getType() == Material.HOPPER && BlockStorage.check(b.getRelative(face), "IGNITION_CHAMBER")) {
				return (Hopper) b.getRelative(face).getState();
			}
		}
		
		return null;
	}

}
