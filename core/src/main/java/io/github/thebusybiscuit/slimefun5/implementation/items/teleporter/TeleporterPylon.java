package io.github.thebusybiscuit.slimefun5.implementation.items.teleporter;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.handlers.RainbowTickHandler;
import io.github.thebusybiscuit.slimefun5.implementation.items.blocks.RainbowBlock;

/**
 * The {@link TeleporterPylon} is a special kind of {@link RainbowBlock} which is required
 * for the {@link Teleporter}.
 * 
 * @author TheBusyBiscuit
 *
 * @see Teleporter
 * @see RainbowBlock
 * @see RainbowTickHandler
 */
public class TeleporterPylon extends RainbowBlock {

    @ParametersAreNonnullByDefault
    public TeleporterPylon(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput, new RainbowTickHandler(XMaterial.CYAN_STAINED_GLASS.parseMaterial(), XMaterial.PURPLE_STAINED_GLASS.parseMaterial()));
    }

}

