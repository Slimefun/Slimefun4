package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunGadget;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockUseHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class Composter extends SlimefunGadget {

	public Composter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe, getMachineRecipes());
	}
	
	private static ItemStack[] getMachineRecipes() {
		List<ItemStack> items = new LinkedList<>();
		
		for (Material leave : MaterialCollections.getAllLeaves()) {
			items.add(new ItemStack(leave, 8));
			items.add(new ItemStack(Material.DIRT));
		}
		
		for (Material sapling : MaterialCollections.getAllSaplings()) {
			items.add(new ItemStack(sapling, 8));
			items.add(new ItemStack(Material.DIRT));
		}
		
		items.add(new ItemStack(Material.STONE, 4));
		items.add(new ItemStack(Material.NETHERRACK));
		
		items.add(new ItemStack(Material.SAND, 2));
		items.add(new ItemStack(Material.SOUL_SAND));
		
		items.add(new ItemStack(Material.WHEAT, 4));
		items.add(new ItemStack(Material.NETHER_WART));
		
		return items.toArray(new ItemStack[0]);
	}

	@Override
	public void preRegister() {
		addItemHandler((BlockUseHandler) e -> {
			Optional<Block> block = e.getClickedBlock();
			
			if (block.isPresent()) {
				Player p = e.getPlayer();
				Block b = block.get();
				
				if (p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES)) {
					ItemStack input = e.getItem();
					
					for (ItemStack convert : RecipeType.getRecipeInputs(Composter.this)) {
						if (convert != null && SlimefunManager.isItemSimilar(input, convert, true)) {
							ItemStack removing = input.clone();
							removing.setAmount(convert.getAmount());
							p.getInventory().removeItem(removing);
							ItemStack adding = RecipeType.getRecipeOutput(Composter.this, convert);

							for (int i = 1; i < 12; i++) {
								int j = i;
								
								Slimefun.runSync(() -> {
									if (j < 11) {
										b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, input.getType().isBlock() ? input.getType() : Material.HAY_BLOCK);
									} 
									else {
										p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
										b.getWorld().dropItemNaturally(b.getRelative(BlockFace.UP).getLocation(), adding);
									}
								}, i * 30L);
							}
						}
					}
					
					SlimefunPlugin.getLocal().sendMessage(p, "machines.wrong-item", true);
				}
			}
		});
	}

}
