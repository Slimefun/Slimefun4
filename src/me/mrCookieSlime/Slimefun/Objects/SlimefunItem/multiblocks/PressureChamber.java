package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks;

import org.bukkit.Bukkit;
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
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;

public class PressureChamber extends MultiBlockMachine {

	public PressureChamber() {
		super(
				Categories.MACHINES_1, 
				SlimefunItems.PRESSURE_CHAMBER, 
				"PRESSURE_CHAMBER",
				new ItemStack[] {new ItemStack(Material.STONE_SLAB), new CustomItem(Material.DISPENSER, "Dispenser (Facing down)"), new ItemStack(Material.STONE_SLAB), new ItemStack(Material.PISTON), new ItemStack(Material.GLASS), new ItemStack(Material.PISTON), new ItemStack(Material.PISTON), new ItemStack(Material.CAULDRON), new ItemStack(Material.PISTON)},
				new ItemStack[] {
						SlimefunItems.CARBON_CHUNK, SlimefunItems.SYNTHETIC_DIAMOND, 
						SlimefunItems.RAW_CARBONADO, SlimefunItems.CARBONADO
				},
				Material.CAULDRON
		);
	}
	
	@Override
	public void onInteract(Player p, Block b) {
		Block dispBlock = b.getRelative(BlockFace.UP).getRelative(BlockFace.UP);
		Dispenser disp = (Dispenser) dispBlock.getState();
		final Inventory inv = disp.getInventory();
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
							
							Bukkit.getScheduler().runTaskLater(SlimefunStartup.instance, () -> {
								p.getWorld().playSound(b.getLocation(), Sound.ENTITY_TNT_PRIMED, 1, 1);
								p.getWorld().playEffect(b.getRelative(BlockFace.UP).getLocation(), Effect.SMOKE, 4);
								p.getWorld().playEffect(b.getRelative(BlockFace.UP).getLocation(), Effect.SMOKE, 4);
								p.getWorld().playEffect(b.getRelative(BlockFace.UP).getLocation(), Effect.SMOKE, 4);
								
								if (j < 3) {
									p.getWorld().playSound(b.getLocation(), Sound.ENTITY_TNT_PRIMED, 1F, 1F);
								} 
								else {
									p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
									outputInv.addItem(adding);
								}
							}, i*20L);
						}
					}
					else Messages.local.sendTranslation(p, "machines.full-inventory", true);
					
					return;
				}
			}
		}
		Messages.local.sendTranslation(p, "machines.unknown-material", true);
	}

}
