package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.auto_crafters;

import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import io.github.thebusybiscuit.cscorelib2.data.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import io.papermc.lib.PaperLib;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class VanillaAutoCrafter extends AbstractAutoCrafter {

    @ParametersAreNonnullByDefault
    public VanillaAutoCrafter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    @Nullable
    public AbstractRecipe getSelectedRecipe(@Nonnull Block b) {
        return AbstractRecipe.of(getRecipe(b));
    }

    @Nullable
    private Recipe getRecipe(@Nonnull Block b) {
        BlockState state = PaperLib.getBlockState(b, false).getState();

        if (state instanceof Skull) {
            // Read the stored value from persistent data storage
            String value = PersistentDataAPI.getString((Skull) state, recipeStorageKey);

            if (value != null) {
                String[] values = PatternUtils.COLON.split(value);

                /*
                 * Normally this constructor should not be used.
                 * But it is completely fine for this purpose since we only use
                 * it for lookups.
                 */
                @SuppressWarnings("deprecation")
                NamespacedKey key = new NamespacedKey(values[0], values[1]);

                return Bukkit.getRecipe(key);
            }
        }

        return null;
    }

    @Override
    protected boolean matches(@Nonnull ItemStack item, @Nonnull Predicate<ItemStack> predicate) {
        SlimefunItem sfItem = SlimefunItem.getByItem(item);

        // Slimefunitems should be ignored (unless allowed)
        if (sfItem == null || sfItem.isUseableInWorkbench()) {
            return super.matches(item, predicate);
        } else {
            return false;
        }
    }

    @Override
    protected void updateRecipe(@Nonnull Block b, @Nonnull Player p) {
        // TODO Choose vanilla recipe
    }

}
