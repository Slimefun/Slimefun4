package io.github.thebusybiscuit.slimefun4.implementation.items.magical;

import org.bukkit.Sound;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class MagicEyeOfEnder extends SimpleSlimefunItem<ItemUseHandler> {

    public MagicEyeOfEnder(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();

            Player p = e.getPlayer();

            if (hasArmor(p.getInventory())) {
                p.launchProjectile(EnderPearl.class);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            }
        };
    }

    private boolean hasArmor(PlayerInventory inv) {
        return SlimefunUtils.isItemSimilar(inv.getHelmet(), SlimefunItems.ENDER_HELMET, true) && SlimefunUtils.isItemSimilar(inv.getChestplate(), SlimefunItems.ENDER_CHESTPLATE, true) && SlimefunUtils.isItemSimilar(inv.getLeggings(), SlimefunItems.ENDER_LEGGINGS, true) && SlimefunUtils.isItemSimilar(inv.getBoots(), SlimefunItems.ENDER_BOOTS, true);
    }
}
