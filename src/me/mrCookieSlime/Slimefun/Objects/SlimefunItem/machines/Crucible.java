package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.compatibility.MaterialHelper;
import me.mrCookieSlime.CSCoreLibPlugin.events.ItemUseEvent;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunGadget;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class Crucible extends SlimefunGadget {

	public Crucible(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack[] machineRecipes) {
		super(category, item, id, recipeType, recipe, machineRecipes);
	}
	
	@Override
	public void register(boolean slimefun) {
		addItemHandler(new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, final Player p, ItemStack item) {
				if (e.getClickedBlock() != null) {
					SlimefunItem machine = BlockStorage.check(e.getClickedBlock());
					if (machine != null && machine.getID().equals("CRUCIBLE")) {
						if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), e.getClickedBlock(), true)) {
							final ItemStack input = p.getInventory().getItemInMainHand();
							final Block block = e.getClickedBlock().getRelative(BlockFace.UP);
							for (ItemStack convert: RecipeType.getRecipeInputs(machine)) {
								if (input != null && SlimefunManager.isItemSimiliar(input, convert, true)) {
									e.setCancelled(true);
									ItemStack removing = input.clone();
									removing.setAmount(convert.getAmount());
									p.getInventory().removeItem(removing);

									for (int i = 1; i < 9; i++) {
										int j = 8 - i;
										Bukkit.getScheduler().runTaskLater(SlimefunPlugin.instance, () -> {
											if (input.getType() == Material.COBBLESTONE || input.getType() == Material.TERRACOTTA || MaterialHelper.isTerracotta(input.getType())) {
												block.setType(Material.LAVA);
												Levelled le = (Levelled) block.getBlockData();
												le.setLevel(j);
												block.setBlockData(le, false);
												block.getWorld().playSound(block.getLocation(), Sound.BLOCK_LAVA_POP, 1F, 1F);
											} 
											else if (MaterialHelper.isLeavesBlock(input.getType())) {
												block.setType(Material.WATER);
												Levelled le = (Levelled) block.getBlockData();
												le.setLevel(j);
												block.setBlockData(le, false);
												block.getWorld().playSound(block.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1F, 1F);
											}
										}, i*50L);
									}

									return true;
								}
							}
							Messages.local.sendTranslation(p, "machines.wrong-item", true);
							return true;
						}
						return true;
					}
				}
				return false;
			}
		});
		
		super.register(slimefun);
	}

}
