package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.auto_crafters;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.data.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.papermc.lib.PaperLib;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class EnhancedAutoCrafter extends AbstractAutoCrafter {

    @ParametersAreNonnullByDefault
    public EnhancedAutoCrafter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    @Nullable
    public AbstractRecipe getSelectedRecipe(@Nonnull Block b) {
        BlockState state = PaperLib.getBlockState(b, false).getState();

        if (state instanceof Skull) {
            // Read the stored value from persistent data storage
            String value = PersistentDataAPI.getString((Skull) state, recipeStorageKey);
            SlimefunItem item = SlimefunItem.getByID(value);

            if (item != null && item.getRecipeType().equals(RecipeType.ENHANCED_CRAFTING_TABLE)) {
                return AbstractRecipe.of(item);
            }
        }

        return null;
    }

    @Override
    protected void updateRecipe(@Nonnull Block b, @Nonnull Player p) {
        ItemStack itemInHand = p.getInventory().getItemInMainHand();
        SlimefunItem item = SlimefunItem.getByItem(itemInHand);

        if (item != null && item.getRecipeType().equals(RecipeType.ENHANCED_CRAFTING_TABLE)) {
            // Fixes #1161
            if (item.canUse(p, true)) {
                AbstractRecipe recipe = AbstractRecipe.of(item);

                if (recipe != null) {
                    setSelectedRecipe(b, recipe);
                    p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    SlimefunPlugin.getLocalization().sendMessage(p, "messages.auto-crafting.recipe-set");
                    showRecipe(p, b, recipe);
                } else {
                    SlimefunPlugin.getLocalization().sendMessage(p, "messages.auto-crafting.no-recipes");
                }
            }
        } else {
            SlimefunPlugin.getLocalization().sendMessage(p, "messages.auto-crafting.no-recipes");
        }
    }

}
