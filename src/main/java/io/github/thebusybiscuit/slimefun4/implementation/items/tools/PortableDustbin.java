package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

<<<<<<< HEAD
import javax.annotation.Nonnull;
=======
>>>>>>> 18cb30cad (First draft for the sound service)
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
<<<<<<< HEAD

/**
 * The {@link PortableDustbin} is one of the oldest items in Slimefun.
 * It simply opens an empty {@link Inventory} in which you can dump any
 * unwanted {@link ItemStack}. When closing the {@link Inventory}, all items
 * will be voided.
 * 
 * @author TheBusyBiscuit
=======
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
>>>>>>> 18cb30cad (First draft for the sound service)
 *
 */
public class PortableDustbin extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {

    @ParametersAreNonnullByDefault
<<<<<<< HEAD
    public PortableDustbin(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
=======
    public PortableDustbin(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
>>>>>>> 18cb30cad (First draft for the sound service)
    }

    @Override
    public @Nonnull ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();

            Player p = e.getPlayer();
            p.openInventory(Bukkit.createInventory(null, 9 * 3, ChatColor.DARK_RED + "Delete Items"));
<<<<<<< HEAD
            SoundEffect.PORTABLE_DUSTBIN_OPEN_SOUND.playFor(p);
=======
            SoundEffect.TRASH_CAN_OPEN_SOUND.play(p);
>>>>>>> 18cb30cad (First draft for the sound service)
        };
    }
}
