package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Player.PlayerInventory;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;

public class InfernalBonemeal extends SimpleSlimefunItem<ItemInteractionHandler> {

	public InfernalBonemeal(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
		super(category, item, id, recipeType, recipe, recipeOutput);
	}

	@Override
	public ItemInteractionHandler getItemHandler() {
		return (e, p, item) -> {
			if (SlimefunManager.isItemSimiliar(e.getItem(), getItem(), true)) {
				if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.NETHER_WART) {
					Ageable ageable = (Ageable)e.getClickedBlock().getBlockData();
					if (ageable.getAge() < ageable.getMaximumAge()) {
						ageable.setAge(ageable.getMaximumAge());
						e.getClickedBlock().setBlockData(ageable);
						e.getClickedBlock().getWorld().playEffect(e.getClickedBlock().getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
						PlayerInventory.consumeItemInHand(p);
					}
				}
				return true;
			}
			return false;
		};
	}

}
