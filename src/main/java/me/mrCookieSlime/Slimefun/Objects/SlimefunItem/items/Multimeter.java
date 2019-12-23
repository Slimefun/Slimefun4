package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.math.DoubleHandler;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;

public class Multimeter extends SimpleSlimefunItem<ItemInteractionHandler> {

	public Multimeter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	}

    @Override
    public ItemInteractionHandler getItemHandler() {
        return (e, p, item) -> {
        	if (isItem(item)) {
        		if (e.getClickedBlock() != null && ChargableBlock.isChargable(e.getClickedBlock())) {
					e.setCancelled(true);
					p.sendMessage("");
					p.sendMessage(ChatColors.color("&bStored Energy: &3" + DoubleHandler.getFancyDouble(ChargableBlock.getCharge(e.getClickedBlock())) + " J"));
					p.sendMessage(ChatColors.color("&bCapacity: &3" + DoubleHandler.getFancyDouble(ChargableBlock.getMaxCharge(e.getClickedBlock())) + " J"));
					p.sendMessage("");
				}
				return true;
			}
			else return false;
        };
    }
}
