package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Particles.FireworkShow;
import me.mrCookieSlime.CSCoreLibPlugin.general.Player.PlayerInventory;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EasterEgg extends SimpleSlimefunItem<ItemInteractionHandler> {

    public EasterEgg(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, id, recipeType, recipe, recipeOutput);
    }

    @Override
    public ItemInteractionHandler getItemHandler() {
        return (e, p, item) -> {
            if (SlimefunManager.isItemSimiliar(item, SlimefunItems.EASTER_EGG, true)) {
                e.setCancelled(true);
                PlayerInventory.consumeItemInHand(e.getPlayer());
                FireworkShow.launchRandom(e.getPlayer(), 2);

                List<ItemStack> gifts = new ArrayList<>();

                for (int i = 0; i < 2; i++) {
                    gifts.add(new CustomItem(SlimefunItems.EASTER_CARROT_PIE, 4));
                    gifts.add(new CustomItem(SlimefunItems.CARROT_JUICE, 1));
                    gifts.add(new ItemStack(Material.EMERALD));
                    gifts.add(new ItemStack(Material.CAKE));
                    gifts.add(new ItemStack(Material.RABBIT_FOOT));
                    gifts.add(new ItemStack(Material.GOLDEN_CARROT, 4));
                }

                p.getWorld().dropItemNaturally(p.getLocation(), gifts.get(ThreadLocalRandom.current().nextInt(gifts.size())));
                return true;
            }
            else return false;
        };
    }

}
