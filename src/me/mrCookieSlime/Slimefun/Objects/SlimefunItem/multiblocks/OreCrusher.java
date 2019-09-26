package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class OreCrusher extends MultiBlockMachine {

	public OreCrusher() {
		super(
				Categories.MACHINES_1, 
				SlimefunItems.ORE_CRUSHER, 
				"ORE_CRUSHER",
				new ItemStack[] {null, null, null, null, new ItemStack(Material.NETHER_BRICK_FENCE), null, new ItemStack(Material.IRON_BARS), new CustomItem(Material.DISPENSER, "Dispenser (Facing up)"), new ItemStack(Material.IRON_BARS)},
				new ItemStack[] {
						new ItemStack(Material.IRON_ORE), new CustomItem(SlimefunItems.IRON_DUST, (boolean) Slimefun.getItemValue("ORE_CRUSHER", "double-ores") ? 2: 1), 
						new ItemStack(Material.GOLD_ORE), new CustomItem(SlimefunItems.GOLD_DUST, (boolean) Slimefun.getItemValue("ORE_CRUSHER", "double-ores") ? 2: 1), 
						new ItemStack(Material.NETHERRACK, 16), SlimefunItems.SULFATE, 
						SlimefunItems.SIFTED_ORE, SlimefunItems.CRUSHED_ORE, 
						SlimefunItems.CRUSHED_ORE, SlimefunItems.PULVERIZED_ORE, 
						SlimefunItems.PURE_ORE_CLUSTER, SlimefunItems.TINY_URANIUM, 
						new ItemStack(Material.COBBLESTONE, 8), new ItemStack(Material.SAND, 1), 
						new ItemStack(Material.GOLD_INGOT), SlimefunItems.GOLD_DUST, 
						SlimefunItems.GOLD_4K, SlimefunItems.GOLD_DUST
				},
				BlockFace.SELF
		);
	}
	
	@Override
	public List<ItemStack> getDisplayRecipes() {
		return recipes.stream().map(items -> items[0]).collect(Collectors.toList());
	}
	
	@Override
	public void install() {
		// Do nothing to prevent double-registration of recipes
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
						removing.setAmount(convert.getAmount());
						inv.removeItem(removing);
						outputInv.addItem(adding);
						p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, 1);
					}
					else SlimefunPlugin.getLocal().sendMessage(p, "machines.full-inventory", true);
					
					return;
				}
			}
		}
		
		SlimefunPlugin.getLocal().sendMessage(p, "machines.unknown-material", true);
	}

}
