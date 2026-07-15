package io.github.thebusybiscuit.slimefun5.implementation.items.food;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.GameMode;
import org.bukkit.Material;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.InventoryCompat;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun5.implementation.items.SimpleSlimefunItem;

/**
 * {@link MagicSugar} is one of the oldest items in Slimefun, it is a special
 * kind of sugar which gives you the Speed {@link PotionEffect} when right clicked.
 * 
 * @author TheBusyBiscuit
 *
 */
public class MagicSugar extends SimpleSlimefunItem<ItemUseHandler> {

    @ParametersAreNonnullByDefault
    public MagicSugar(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public @Nonnull ItemUseHandler getItemHandler() {
        return e -> {
            // Check if it is being placed into an ancient altar.
            if (e.getClickedBlock().isPresent()) {
                Material block = e.getClickedBlock().get().getType();

                if (block == Material.DISPENSER || block == XMaterial.ENCHANTING_TABLE.parseMaterial()) {
                    return;
                }
            }

            Player p = e.getPlayer();

            if (p.getGameMode() != GameMode.CREATIVE) {
                InventoryCompat.consumeHeldItem(p, e.getHand(), 1, false);
            }

            SoundEffect.MAGIC_SUGAR_CONSUME_SOUND.playFor(p);
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 3));
        };
    }

}

