package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import java.util.function.Predicate;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

public class ButcherAndroid extends ProgrammableAndroid {

    private static final String METADATA_KEY = "android_killer";

    @ParametersAreNonnullByDefault
    public ButcherAndroid(ItemGroup itemGroup, int tier, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, tier, item, recipeType, recipe);
    }

    @Override
    public AndroidType getAndroidType() {
        return AndroidType.FIGHTER;
    }

    @Override
    protected void attack(Block b, BlockFace face, Predicate<LivingEntity> predicate) {
        double damage = getTier() >= 3 ? 20D : 4D * getTier();
        double radius = 4.0 + getTier();

        for (Entity n : b.getWorld().getNearbyEntities(b.getLocation(), radius, radius, radius, n -> n instanceof LivingEntity livingEntity && !(n instanceof ArmorStand) && !(n instanceof Player) && n.isValid() && predicate.test(livingEntity))) {
            // Check if our android is facing this entity.
            boolean willAttack = switch (face) {
                case NORTH -> n.getLocation().getZ() < b.getZ();
                case EAST -> n.getLocation().getX() > b.getX();
                case SOUTH -> n.getLocation().getZ() > b.getZ();
                case WEST -> n.getLocation().getX() < b.getX();
                default -> false;
            };

            if (willAttack) {
                if (n.hasMetadata(METADATA_KEY)) {
                    n.removeMetadata(METADATA_KEY, Slimefun.instance());
                }

                n.setMetadata(METADATA_KEY, new FixedMetadataValue(Slimefun.instance(), new AndroidInstance(this, b)));

                ((LivingEntity) n).damage(damage);
                break;
            }
        }
    }

}
