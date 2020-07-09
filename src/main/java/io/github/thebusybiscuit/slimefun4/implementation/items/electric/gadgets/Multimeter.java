package io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets;

import java.util.Optional;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.math.DoubleHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;

public class Multimeter extends SimpleSlimefunItem<ItemUseHandler> {

    public Multimeter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Optional<Block> block = e.getClickedBlock();

            if (block.isPresent()) {
                Block b = block.get();

                if (ChargableBlock.isChargable(b)) {
                    e.cancel();

                    String stored = DoubleHandler.getFancyDouble(ChargableBlock.getCharge(b)) + " J";
                    String capacity = DoubleHandler.getFancyDouble(ChargableBlock.getMaxCharge(b)) + " J";

                    Player p = e.getPlayer();
                    p.sendMessage("");
                    SlimefunPlugin.getLocalization().sendMessage(p, "messages.multimeter", false, str -> str.replace("%stored%", stored).replace("%capacity%", capacity));
                    p.sendMessage("");
                }
            }
        };
    }
}
