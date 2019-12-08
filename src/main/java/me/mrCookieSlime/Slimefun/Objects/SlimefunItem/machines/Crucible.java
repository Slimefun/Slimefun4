package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.CSCoreLibPlugin.events.ItemUseEvent;
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
		
		for (Material leave: MaterialCollections.getAllLeaves()) {
			items.add(new ItemStack(leave, 16));
			items.add(new ItemStack(Material.WATER_BUCKET));
		}
		
		for (Material sapling: MaterialCollections.getAllTerracottaColors()) {
			items.add(new ItemStack(sapling, 12));
			items.add(new ItemStack(Material.LAVA_BUCKET));
		}
		
		return items.toArray(new ItemStack[0]);
	}
	@Override
	public void preRegister() {
		addItemHandler(new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, final Player p, ItemStack item) {
				if (e.getClickedBlock() != null) {
					String id = BlockStorage.checkID(e.getClickedBlock());
					if (id != null && id.equals("CRUCIBLE")) {
						if (p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, e.getClickedBlock().getLocation(), ProtectableAction.ACCESS_INVENTORIES)) {
							final ItemStack input = p.getInventory().getItemInMainHand();
							final Block block = e.getClickedBlock().getRelative(BlockFace.UP);
							SlimefunItem machine = SlimefunItem.getByID(id);
							
							for (ItemStack convert: RecipeType.getRecipeInputs(machine)) {
								if (SlimefunManager.isItemSimilar(input, convert, true)) {
									e.setCancelled(true);
									ItemStack removing = input.clone();
									removing.setAmount(convert.getAmount());
									
									p.getInventory().removeItem(removing);

									for (int i = 1; i < 9; i++) {int j = 8 - i;
										Slimefun.runSync(() -> {
											if (input.getType() == Material.COBBLESTONE || input.getType() == Material.TERRACOTTA || MaterialCollections.getAllTerracottaColors().contains(input.getType())) {
												block.setType(Material.LAVA);
												Levelled le = (Levelled) block.getBlockData();
												le.setLevel(j);
												block.setBlockData(le, false);
												block.getWorld().playSound(block.getLocation(), Sound.BLOCK_LAVA_POP, 1F, 1F);
											} 
											else if (Tag.LEAVES.isTagged(input.getType())) {
												block.setType(Material.WATER);
												Levelled le = (Levelled) block.getBlockData();
												le.setLevel(j);
												block.setBlockData(le, false);
												block.getWorld().playSound(block.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1F, 1F);
											}
										}, i * 50L);
									}

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
			}
		});
	}

}
