package io.github.thebusybiscuit.slimefun4.implementation.items.misc;

import org.bukkit.block.Block;
import org.bukkit.entity.Piglin;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.settings.IntRangeSetting;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.core.attributes.PiglinBarterDrop;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.VillagerRune;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

import javax.annotation.Nonnull;

/**
 * This {@link SlimefunItem} can only be obtained via bartering with a {@link Piglin}, its
 * only current uses is the recipe for crafting the {@link VillagerRune}.
 *
 * @author dNiym
 *
 * @see VillagerRune
 * @see PiglinBarterDrop
 *
 */
public class StrangeNetherGoo extends SimpleSlimefunItem<ItemUseHandler> implements PiglinBarterDrop {

    private final ItemSetting<Integer> chance = new IntRangeSetting("barter-chance", 0, 7, 100);

    public StrangeNetherGoo(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemSetting(chance);
    }

    @Override
    public int getBarteringLootChance() {
        return chance.getValue();
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return this::use;
    }

    public void use(PlayerRightClickEvent e) {
        if (e.getClickedBlock().isPresent()) {
            if (e.getClickedBlock().get().getType().name().contains("SIGN")) {
                e.cancel();
            }
        }
    }

}
