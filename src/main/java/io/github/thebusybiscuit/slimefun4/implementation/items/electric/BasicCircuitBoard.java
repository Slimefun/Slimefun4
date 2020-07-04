package io.github.thebusybiscuit.slimefun4.implementation.items.electric;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.ChanceDrop;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class BasicCircuitBoard extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable, ChanceDrop {

    private final ItemSetting<Boolean> dropSetting = new ItemSetting<>("drop-from-golems", true);
    private final ItemSetting<Integer> chance = new ItemSetting<> ("golem-drop-chance", 75);
    
    public BasicCircuitBoard(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemSetting(dropSetting);
        addItemSetting(chance);
    }

    @Override
    public int getChance() {
    	return chance.getValue();
    }
    public boolean isDroppedFromGolems() {
        return dropSetting.getValue();
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return PlayerRightClickEvent::cancel;
    }

}