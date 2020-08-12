package io.github.thebusybiscuit.slimefun4.implementation.resources;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.attributes.PiglinBarterDrop;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class OrganicNetherGoo extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable, PiglinBarterDrop {

        private final ItemSetting<Integer> chance = new ItemSetting<>("barter-chance", 3);
    
        public OrganicNetherGoo(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
            super(category, item, recipeType, recipe);
            
            addItemSetting(chance);
        }
        
        @Override
        public ItemUseHandler getItemHandler() {
                return PlayerRightClickEvent::cancel;
        }

        @Override
        public int getBarteringLootChance() {
            return chance.getValue();
        }
        
}

