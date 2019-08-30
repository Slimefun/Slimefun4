package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.events.ItemUseEvent;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunGadget;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class Composter extends SlimefunGadget {

	public Composter(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack[] machineRecipes) {
		super(category, item, id, recipeType, recipe, machineRecipes);
	}
	
	@Override
	public void register(boolean slimefun) {
		addItemHandler(new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, final Player p, ItemStack item) {
				if (e.getClickedBlock() != null) {
					SlimefunItem machine = BlockStorage.check(e.getClickedBlock());
					if (machine != null && machine.getID().equals(getID())) {
						if (CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), e.getClickedBlock(), true)) {
							final ItemStack input = p.getInventory().getItemInMainHand();
							final Block b = e.getClickedBlock();
							
							for (ItemStack convert: RecipeType.getRecipeInputs(machine)) {
								if (convert != null && SlimefunManager.isItemSimiliar(input, convert, true)) {
									ItemStack removing = input.clone();
									removing.setAmount(convert.getAmount());
									p.getInventory().removeItem(removing);
									final ItemStack adding = RecipeType.getRecipeOutput(machine, convert);

									for (int i = 1; i < 12; i++) {
										int j = i;
										Bukkit.getScheduler().runTaskLater(SlimefunStartup.instance, () -> {
											if (j < 11) {
												b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, input.getType().isBlock() ? input.getType() : Material.HAY_BLOCK);
											} 
											else {
												p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
												b.getWorld().dropItemNaturally(b.getRelative(BlockFace.UP).getLocation(), adding);
											}
										}, i*30L);
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
