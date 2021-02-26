package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.TrashCan;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link PortableDustbin} is a portable version of the {@link TrashCan}.
 * It allows you to void items.
 * When right clicking it will open a temporary {@link Inventory} in which you can put items.
 * The items will be discarded when the {@link Inventory} is closed.
 * 
 * @author TheBusyBiscuit
 * 
 * @see TrashCan
 *
 */
public class PortableDustbin extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {

    @ParametersAreNonnullByDefault
    public PortableDustbin(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();

            Player p = e.getPlayer();
            p.openInventory(Bukkit.createInventory(null, 9 * 3, ChatColor.DARK_RED + "Delete Items"));
            SoundEffect.TRASH_CAN_OPEN_SOUND.playFor(p);
        };
    }
}
