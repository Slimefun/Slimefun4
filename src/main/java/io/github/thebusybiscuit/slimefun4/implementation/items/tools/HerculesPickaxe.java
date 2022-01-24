package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;

public class HerculesPickaxe extends SimpleSlimefunItem<ToolUseHandler> {

    @ParametersAreNonnullByDefault
    public HerculesPickaxe(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public @Nonnull ToolUseHandler getItemHandler() {
        return (e, tool, fortune, drops) -> {
            Material mat = e.getBlock().getType();

            if (SlimefunTag.ORES.isTagged(mat)) {
                if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
                    switch (mat) {
                        case DEEPSLATE_IRON_ORE:
                            drops.add(new CustomItemStack(SlimefunItems.IRON_DUST, 2));
                            break;
                        case DEEPSLATE_GOLD_ORE:
                            drops.add(new CustomItemStack(SlimefunItems.GOLD_DUST, 2));
                            break;
                        case COPPER_ORE:
                        case DEEPSLATE_COPPER_ORE:
                            drops.add(new CustomItemStack(SlimefunItems.COPPER_DUST, 2));
                            break;
                        default:
                            break;
                    }
                }

                switch (mat) {
                    case IRON_ORE:
                        drops.add(new CustomItemStack(SlimefunItems.IRON_DUST, 2));
                        break;
                    case GOLD_ORE:
                        drops.add(new CustomItemStack(SlimefunItems.GOLD_DUST, 2));
                        break;
                    default:
                        for (ItemStack drop : e.getBlock().getDrops(tool)) {
                            drops.add(new CustomItemStack(drop, drop.getAmount() * 2));
                        }
                        break;
                }
            }
        };
    }

}
