package io.github.thebusybiscuit.slimefun4.implementation.items.magical;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class WindStaff extends SimpleSlimefunItem<ItemUseHandler> {

    public WindStaff(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Player p = e.getPlayer();

            if (p.getFoodLevel() >= 2) {
                if (isItem(e.getItem()) && p.getGameMode() != GameMode.CREATIVE) {
                    FoodLevelChangeEvent event = new FoodLevelChangeEvent(p, p.getFoodLevel() - 2);
                    Bukkit.getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        p.setFoodLevel(event.getFoodLevel());
                    }
                }

                p.setVelocity(p.getEyeLocation().getDirection().multiply(4));
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_TNT_PRIMED, 1, 1);
                p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 1);
                p.setFallDistance(0F);
            } else {
                SlimefunPlugin.getLocalization().sendMessage(p, "messages.hungry", true);
            }
        };
    }

}
