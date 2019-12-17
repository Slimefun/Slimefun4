package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunGadget;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class Crucible extends SlimefunGadget {

	public Crucible(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe, getMachineRecipes());
	}
	
	private static ItemStack[] getMachineRecipes() {
		List<ItemStack> items = new LinkedList<>();

		items.add(new ItemStack(Material.COBBLESTONE, 16));
		items.add(new ItemStack(Material.LAVA_BUCKET));

		items.add(new ItemStack(Material.NETHERRACK, 16));
		items.add(new ItemStack(Material.LAVA_BUCKET));

		items.add(new ItemStack(Material.STONE, 12));
		items.add(new ItemStack(Material.LAVA_BUCKET));

		items.add(new ItemStack(Material.OBSIDIAN, 1));
		items.add(new ItemStack(Material.LAVA_BUCKET));
		
		for (Material leave : MaterialCollections.getAllLeaves()) {
			items.add(new ItemStack(leave, 16));
			items.add(new ItemStack(Material.WATER_BUCKET));
		}
		
		for (Material sapling : MaterialCollections.getAllTerracottaColors()) {
			items.add(new ItemStack(sapling, 12));
			items.add(new ItemStack(Material.LAVA_BUCKET));
		}
		
		return items.toArray(new ItemStack[0]);
	}

	@Override
	public void preRegister() {
		addItemHandler((ItemInteractionHandler) (e, p, item) -> {
			if (e.getClickedBlock() != null) {
				String id = BlockStorage.checkID(e.getClickedBlock());
				if (id != null && id.equals("CRUCIBLE")) {
					if (p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, e.getClickedBlock().getLocation(), ProtectableAction.ACCESS_INVENTORIES)) {
						final ItemStack input = p.getInventory().getItemInMainHand();
						final Block block = e.getClickedBlock().getRelative(BlockFace.UP);
						SlimefunItem machine = SlimefunItem.getByID(id);

						for (ItemStack convert : RecipeType.getRecipeInputs(machine)) {
							if (SlimefunManager.isItemSimilar(input, convert, true)) {
								e.setCancelled(true);

								ItemStack removing = input.clone();
								removing.setAmount(convert.getAmount());
								p.getInventory().removeItem(removing);

								boolean water = Tag.LEAVES.isTagged(input.getType());
								if (block.getType() == (water ? Material.WATER : Material.LAVA)) {
									int level = ((Levelled) block.getBlockData()).getLevel();
									if (level > 7)
										level -= 8;
									if (level == 0) {
										block.getWorld().playSound(block.getLocation(), water ? Sound.ENTITY_PLAYER_SPLASH : Sound.BLOCK_LAVA_POP, 1F, 1F);
									} 
									else {
										int finalLevel = 7 - level;
										Slimefun.runSync(() -> runPostTask(block, water ? Sound.ENTITY_PLAYER_SPLASH : Sound.BLOCK_LAVA_POP, finalLevel), 50L);
									}
									return true;
								} 
								else if (block.getType() == (water ? Material.LAVA : Material.WATER)) {
									int level = ((Levelled) block.getBlockData()).getLevel();
									block.setType(level == 0 || level == 8 ? Material.OBSIDIAN : Material.STONE);
									block.getWorld().playSound(block.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1F, 1F);
									return true;
								}

								Slimefun.runSync(() -> {
									if (!block.getType().isAir()) {
										if (water) {
											if (block.getBlockData() instanceof Waterlogged) {
												Waterlogged wl = (Waterlogged) block.getBlockData();
												wl.setWaterlogged(true);
												block.setBlockData(wl, false);
												block.getWorld().playSound(block.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1F, 1F);
												return;
											}
											block.getWorld().playSound(block.getLocation(), Sound.BLOCK_METAL_BREAK, 1F, 1F);
											return;
										}
										if (BlockStorage.hasBlockInfo(block))
											BlockStorage.clearBlockInfo(block);
										block.getWorld().playSound(block.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1F, 1F);
									}
									block.setType(water ? Material.WATER : Material.LAVA);
									runPostTask(block, water ? Sound.ENTITY_PLAYER_SPLASH : Sound.BLOCK_LAVA_POP, 1);
								}, 50L);

								return true;
							}
						}
						SlimefunPlugin.getLocal().sendMessage(p, "machines.wrong-item", true);
						return true;
					}
					return true;
				}
			}
			return false;
		});
	}

	private void runPostTask(Block block, Sound sound, int times) {
		if (!(block.getBlockData() instanceof Levelled)) {
			block.getWorld().playSound(block.getLocation(), Sound.BLOCK_METAL_BREAK, 1F, 1F);
			return;
		}
		
		block.getWorld().playSound(block.getLocation(), sound, 1F, 1F);
		int level = 8 - times;
		Levelled le = (Levelled) block.getBlockData();
		le.setLevel(level);
		block.setBlockData(le, false);
		
		if (times < 8)
			Slimefun.runSync(() -> runPostTask(block, sound, times + 1), 50L);
	}

}
