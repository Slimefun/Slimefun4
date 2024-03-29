package io.github.thebusybiscuit.slimefun4.implementation.items.thornya;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class Spaceship extends SlimefunItem {
    public Spaceship(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe) {
        super(itemGroup, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        addItemHandler((ItemUseHandler) e -> {
            Optional<Block> clicked = e.getClickedBlock();
            if(clicked.isPresent()) {
                if(e.getInteractEvent().getAction() == Action.RIGHT_CLICK_BLOCK && e.getHand() == EquipmentSlot.HAND && e.getPlayer().isSneaking()) {
                    ItemStack itemInHand = e.getPlayer().getInventory().getItemInMainHand();
                    if(SlimefunItem.getByItem(itemInHand) != null){
                        SlimefunUtils.removeItemOnHand(e.getPlayer());
                        e.getPlayer().sendMessage("Voar voar subir subir");
                    }
                }
            }
        });
    }
}
