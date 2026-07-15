package io.github.thebusybiscuit.slimefun5.implementation.items.magical;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.BlockDataCompat;
import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.block.Block;
import org.bukkit.event.Event.Result;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.InventoryCompat;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun5.implementation.items.SimpleSlimefunItem;

/**
 * The {@link InfernalBonemeal} is a special type of bone meal which will work on
 * Nether Warts.
 * 
 * @author TheBusyBiscuit
 *
 */
public class InfernalBonemeal extends SimpleSlimefunItem<ItemUseHandler> {

    @ParametersAreNonnullByDefault
    public InfernalBonemeal(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Optional<Block> block = e.getClickedBlock();
            e.setUseBlock(Result.DENY);

            if (block.isPresent()) {
                Block b = block.get();

                if (b.getType() == XMaterial.NETHER_WART.parseMaterial()) {
                    Object ageable = BlockDataCompat.getBlockData(b);

                    if (BlockDataCompat.getInt(ageable, "getAge") < BlockDataCompat.getInt(ageable, "getMaximumAge")) {
                        BlockDataCompat.set(ageable, "setAge", BlockDataCompat.getInt(ageable, "getMaximumAge"));
                        BlockDataCompat.setBlockData(b, ageable);
                        b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);

                        if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                            InventoryCompat.consumeHeldItem(e.getPlayer(), e.getHand(), 1, false);
                        }
                    }
                }
            }
        };
    }

}

