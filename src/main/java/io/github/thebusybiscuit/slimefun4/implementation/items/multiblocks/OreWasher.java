package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
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

import io.github.thebusybiscuit.cscorelib2.inventory.InvUtils;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks.MultiBlockMachine;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class OreWasher extends MultiBlockMachine {

	public OreWasher() {
		super(
				Categories.MACHINES_1, 
				(SlimefunItemStack) SlimefunItems.ORE_WASHER, 
				new ItemStack[] {null, new ItemStack(Material.DISPENSER), null, null, new ItemStack(Material.OAK_FENCE), null, null, new ItemStack(Material.CAULDRON), null},
				new ItemStack[] {
						SlimefunItems.SIFTED_ORE, SlimefunItems.IRON_DUST, 
						SlimefunItems.SIFTED_ORE, SlimefunItems.GOLD_DUST, 
						SlimefunItems.SIFTED_ORE, SlimefunItems.COPPER_DUST, 
						SlimefunItems.SIFTED_ORE, SlimefunItems.TIN_DUST, 
						SlimefunItems.SIFTED_ORE, SlimefunItems.ZINC_DUST, 
						SlimefunItems.SIFTED_ORE, SlimefunItems.ALUMINUM_DUST, 
						SlimefunItems.SIFTED_ORE, SlimefunItems.MAGNESIUM_DUST, 
						SlimefunItems.SIFTED_ORE, SlimefunItems.LEAD_DUST, 
						SlimefunItems.SIFTED_ORE, SlimefunItems.SILVER_DUST
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
		Block dispBlock = b.getRelative(BlockFace.UP);
		Dispenser disp = (Dispenser) dispBlock.getState();
		Inventory inv = disp.getInventory();

		for (ItemStack current : inv.getContents()) {
			if (current != null) {
				if (SlimefunManager.isItemSimilar(current, SlimefunItems.SIFTED_ORE, true)) {
					ItemStack adding = getRandomDust();
					Inventory outputInv = null;

					if (!SlimefunPlugin.getSettings().legacyOreWasher) {
						// This is a fancy way of checking if there is empty space in the inv; by checking if an unobtainable item could fit in it.
						// However, due to the way the method findValidOutputInv() functions, the dummyAdding will never actually be added to the real inventory,
						// so it really doesn't matter what item the ItemStack is made by. SlimefunItems.DEBUG_FISH however, signals that it's
						// not supposed to be given to the player.
						ItemStack dummyAdding = SlimefunItems.DEBUG_FISH;
						outputInv = findOutputInventory(dummyAdding, dispBlock, inv);
					}
					else outputInv = findOutputInventory(adding, dispBlock, inv);

					if (outputInv != null) {
						ItemStack removing = current.clone();
						removing.setAmount(1);
						inv.removeItem(removing);
						outputInv.addItem(adding);
						p.getWorld().playSound(b.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1, 1);
						p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.WATER);
						if (InvUtils.fits(outputInv, SlimefunItems.STONE_CHUNK)) outputInv.addItem(SlimefunItems.STONE_CHUNK);
					}
					else SlimefunPlugin.getLocal().sendMessage(p, "machines.full-inventory", true);
					
					return;
				}
				else if (SlimefunManager.isItemSimilar(current, new ItemStack(Material.SAND, 4), false)) {
					ItemStack adding = SlimefunItems.SALT;
					Inventory outputInv = findOutputInventory(adding, dispBlock, inv);

					if (outputInv != null) {
						ItemStack removing = current.clone();
						removing.setAmount(4);
						inv.removeItem(removing);
						outputInv.addItem(adding);
						p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.WATER);
						p.getWorld().playSound(b.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1, 1);
					}
					else SlimefunPlugin.getLocal().sendMessage(p, "machines.full-inventory", true);

					return;
				}
				else if (SlimefunManager.isItemSimilar(current, SlimefunItems.PULVERIZED_ORE, true)) {
					ItemStack adding = SlimefunItems.PURE_ORE_CLUSTER;
					Inventory outputInv = findOutputInventory(adding, dispBlock, inv);

					if (outputInv != null) {
						ItemStack removing = current.clone();
						removing.setAmount(1);
						inv.removeItem(removing);
						outputInv.addItem(adding);
						p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.WATER);
						p.getWorld().playSound(b.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1, 1);
					}
					else SlimefunPlugin.getLocal().sendMessage(p, "machines.full-inventory", true);

					return;
				}
			}
		}
		SlimefunPlugin.getLocal().sendMessage(p, "machines.unknown-material", true);
	}

	public ItemStack getRandomDust() {
		int index = ThreadLocalRandom.current().nextInt(shownRecipes.size() / 2);
		return shownRecipes.get(index * 2 + 1).clone();
	}

}
