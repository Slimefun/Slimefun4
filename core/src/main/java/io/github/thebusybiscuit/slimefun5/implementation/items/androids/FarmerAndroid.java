package io.github.thebusybiscuit.slimefun5.implementation.items.androids;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.BlockDataCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.ReflectionCompat;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.events.AndroidFarmEvent;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;

import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

public class FarmerAndroid extends ProgrammableAndroid {

    @ParametersAreNonnullByDefault
    public FarmerAndroid(ItemGroup itemGroup, int tier, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, tier, item, recipeType, recipe);
    }

    @Override
    public AndroidType getAndroidType() {
        return getTier() == 1 ? AndroidType.FARMER : AndroidType.ADVANCED_FARMER;
    }

    @Override
    protected void farm(Block b, BlockMenu menu, Block block, boolean isAdvanced) {
        Material blockType = block.getType();
        Object data = BlockDataCompat.getBlockData(block);
        ItemStack drop = null;

        // WorldBorder#isInside(Location) is 1.20.4+; on older servers treat the location as inside.
        Object inside = ReflectionCompat.invoke(block.getWorld().getWorldBorder(), "isInside", block.getLocation());
        if (Boolean.FALSE.equals(inside)) {
            return;
        }

        if (BlockDataCompat.isInstance(data, "org.bukkit.block.data.Ageable")
                && BlockDataCompat.getInt(data, "getAge") >= BlockDataCompat.getInt(data, "getMaximumAge")) {
            drop = getDropFromCrop(blockType);
        }

        AndroidInstance instance = new AndroidInstance(this, b);

        AndroidFarmEvent event = new AndroidFarmEvent(block, instance, isAdvanced, drop);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            drop = event.getDrop();

            if (drop != null && menu.pushItem(drop, getOutputSlots()) == null) {
                block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, blockType);

                if (BlockDataCompat.isInstance(data, "org.bukkit.block.data.Ageable")) {
                    BlockDataCompat.set(data, "setAge", 0);
                    BlockDataCompat.setBlockData(block, data);
                }
            }
        }
    }

    private ItemStack getDropFromCrop(Material crop) {
        Random random = ThreadLocalRandom.current();

        // Java-8 universal port: switch labels for crops added after 1.8 (POTATOES, CARROTS,
        // BEETROOTS, NETHER_WART, SWEET_BERRY_BUSH) do not exist on the 1.8.8 enum, so we compare
        // against XMaterial-resolved constants instead. parseMaterial() yields null on versions
        // lacking the crop, which simply never matches the (non-null) block material.
        if (crop == XMaterial.WHEAT.parseMaterial()) {
            return new ItemStack(Material.WHEAT, random.nextInt(2) + 1);
        } else if (crop == XMaterial.POTATOES.parseMaterial()) {
            return new ItemStack(Material.POTATO, random.nextInt(3) + 1);
        } else if (crop == XMaterial.CARROTS.parseMaterial()) {
            return new ItemStack(Material.CARROT, random.nextInt(3) + 1);
        } else if (crop == XMaterial.BEETROOTS.parseMaterial()) {
            return MaterialCompat.stack(XMaterial.BEETROOT, random.nextInt(3) + 1);
        } else if (crop == XMaterial.COCOA.parseMaterial()) {
            return MaterialCompat.stack(XMaterial.COCOA_BEANS, random.nextInt(3) + 1);
        } else if (crop == XMaterial.NETHER_WART.parseMaterial()) {
            return MaterialCompat.stack(XMaterial.NETHER_WART, random.nextInt(3) + 1);
        } else if (crop == XMaterial.SWEET_BERRY_BUSH.parseMaterial()) {
            return MaterialCompat.stack(XMaterial.SWEET_BERRIES, random.nextInt(3) + 1);
        } else {
            return null;
        }
    }

}

