package io.github.thebusybiscuit.slimefun4.implementation.items.food;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class MagicSugar extends SimpleSlimefunItem<ItemUseHandler> {

    public MagicSugar(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            // Check if it is being placed into an ancient altar.
            if (e.getClickedBlock().isPresent()) {
                Material block = e.getClickedBlock().get().getType();

                if (block == Material.DISPENSER || block == Material.ENCHANTING_TABLE) {
                    return;
                }
            }

            Player p = e.getPlayer();
            if (p.getGameMode() != GameMode.CREATIVE) {
                ItemUtils.consumeItem(e.getItem(), false);
            }

            p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 3));
        };
    }

}
