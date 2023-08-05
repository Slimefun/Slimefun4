package io.github.thebusybiscuit.slimefun4.implementation.items.magical;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.ItemUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;

/**
 * The {@link KnowledgeFlask} is a magical {@link SlimefunItem} which allows you to store
 * experience levels in a bottle when you right click.
 * 
 * @author TheBusyBiscuit
 *
 */
public class KnowledgeFlask extends SimpleSlimefunItem<ItemUseHandler> {

    @ParametersAreNonnullByDefault
    public KnowledgeFlask(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
    }

    @Override
    public @Nonnull ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();
            Player p = e.getPlayer();

            if (p.getLevel() >= 1 && (e.getClickedBlock().isEmpty() || !(e.getClickedBlock().get().getType().isInteractable()))) {
                p.setLevel(p.getLevel() - 1);

                ItemStack item = SlimefunItems.FILLED_FLASK_OF_KNOWLEDGE.clone();

                if (!p.getInventory().addItem(item).isEmpty()) {
                    // The Item could not be added, let's drop it to the ground (fixes #2728)
                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                }

                SoundEffect.FLASK_OF_KNOWLEDGE_FILLUP_SOUND.playFor(p);
                ItemUtils.consumeItem(e.getItem(), false);
            }
        };
    }

}
