package io.github.thebusybiscuit.slimefun5.implementation.items.weapons;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Player;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.EntityCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.ReflectionCompat;

import io.github.thebusybiscuit.slimefun5.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.items.settings.IntRangeSetting;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.handlers.EntityKillHandler;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

/**
 * The {@link SwordOfBeheading} is a special kind of sword which allows you to obtain
 * {@link Zombie}, {@link Skeleton}, {@link Creeper} and {@link Piglin} skulls when killing the respective
 * {@link Monster}.
 * Additionally, you can also obtain the head of a {@link Player} by killing them too.
 * This sword also allows you to have a higher chance of getting the skull of a {@link WitherSkeleton} too.
 * 
 * All chances are managed by an {@link ItemSetting} and can be configured.
 * 
 * @author TheBusyBiscuit
 *
 */
public class SwordOfBeheading extends SimpleSlimefunItem<EntityKillHandler> {

    private final ItemSetting<Integer> chanceZombie = new IntRangeSetting(this, "chance.ZOMBIE", 0, 40, 100);
    private final ItemSetting<Integer> chanceSkeleton = new IntRangeSetting(this, "chance.SKELETON", 0, 40, 100);
    private final ItemSetting<Integer> chanceWitherSkeleton = new IntRangeSetting(this, "chance.WITHER_SKELETON", 0, 25, 100);
    private final ItemSetting<Integer> chanceCreeper = new IntRangeSetting(this, "chance.CREEPER", 0, 40, 100);
    private final ItemSetting<Integer> chancePiglin = new IntRangeSetting(this, "chance.PIGLIN", 0, 40, 100);
    private final ItemSetting<Integer> chancePlayer = new IntRangeSetting(this, "chance.PLAYER", 0, 70, 100);

    @ParametersAreNonnullByDefault
    public SwordOfBeheading(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        addItemSetting(chanceZombie, chanceSkeleton, chanceWitherSkeleton, chanceCreeper, chancePiglin, chancePlayer);
    }

    @Override
    public EntityKillHandler getItemHandler() {
        return (e, entity, killer, item) -> {
            Random random = ThreadLocalRandom.current();

            // Each mob only ever drops its OWN head. Stock Slimefun uses a switch WITH breaks, so the
            // cases are independent (a zombie drops only a zombie head). EntityCompat.entityType resolves
            // constants that do not exist at the 1.8.8 compile floor (WITHER_SKELETON, PIGLIN).
            EntityType type = e.getEntityType();

            if (type == EntityCompat.entityType("ZOMBIE")) {
                if (random.nextInt(100) < chanceZombie.getValue()) {
                    e.getDrops().add(MaterialCompat.stack(XMaterial.ZOMBIE_HEAD));
                }
            } else if (type == EntityCompat.entityType("SKELETON")) {
                if (random.nextInt(100) < chanceSkeleton.getValue()) {
                    e.getDrops().add(MaterialCompat.stack(XMaterial.SKELETON_SKULL));
                }
            } else if (type == EntityCompat.entityType("CREEPER")) {
                if (random.nextInt(100) < chanceCreeper.getValue()) {
                    e.getDrops().add(MaterialCompat.stack(XMaterial.CREEPER_HEAD));
                }
            } else if (type == EntityCompat.entityType("WITHER_SKELETON")) {
                if (random.nextInt(100) < chanceWitherSkeleton.getValue()) {
                    e.getDrops().add(MaterialCompat.stack(XMaterial.WITHER_SKELETON_SKULL));
                }
            } else if (type == EntityCompat.entityType("PIGLIN")) {
                if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_20) &&
                    random.nextInt(100) < chancePiglin.getValue()) {
                    e.getDrops().add(MaterialCompat.stack(XMaterial.PIGLIN_HEAD));
                }
            } else if (type == EntityCompat.entityType("PLAYER")) {
                if (random.nextInt(100) < chancePlayer.getValue()) {
                    ItemStack skull = MaterialCompat.stack(XMaterial.PLAYER_HEAD);

                    ItemMeta meta = skull.getItemMeta();
                    ReflectionCompat.invoke((SkullMeta) meta, "setOwningPlayer", (Player) e.getEntity());
                    skull.setItemMeta(meta);

                    e.getDrops().add(skull);
                }
            }
        };
    }

}

