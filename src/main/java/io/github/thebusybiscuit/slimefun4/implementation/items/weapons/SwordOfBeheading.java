package io.github.thebusybiscuit.slimefun4.implementation.items.weapons;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.settings.IntRangeSetting;
import io.github.thebusybiscuit.slimefun4.core.handlers.EntityKillHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class SwordOfBeheading extends SimpleSlimefunItem<EntityKillHandler> {

    private final ItemSetting<Integer> chanceZombie = new IntRangeSetting("chance.ZOMBIE", 0, 40, 100);
    private final ItemSetting<Integer> chanceSkeleton = new IntRangeSetting("chance.SKELETON", 0, 40, 100);
    private final ItemSetting<Integer> chanceWitherSkeleton = new IntRangeSetting("chance.WITHER_SKELETON", 0, 25, 100);
    private final ItemSetting<Integer> chanceCreeper = new IntRangeSetting("chance.CREEPER", 0, 40, 100);
    private final ItemSetting<Integer> chancePlayer = new IntRangeSetting("chance.PLAYER", 0, 70, 100);

    public SwordOfBeheading(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemSetting(chanceZombie, chanceSkeleton, chanceWitherSkeleton, chanceCreeper, chancePlayer);
    }

    @Override
    public EntityKillHandler getItemHandler() {
        return (e, entity, killer, item) -> {
            Random random = ThreadLocalRandom.current();

            if (e.getEntity() instanceof Zombie) {
                if (random.nextInt(100) < chanceZombie.getValue()) {
                    e.getDrops().add(new ItemStack(Material.ZOMBIE_HEAD));
                }
            } else if (e.getEntity() instanceof WitherSkeleton) {
                if (random.nextInt(100) < chanceWitherSkeleton.getValue()) {
                    e.getDrops().add(new ItemStack(Material.WITHER_SKELETON_SKULL));
                }
            } else if (e.getEntity() instanceof Skeleton) {
                if (random.nextInt(100) < chanceSkeleton.getValue()) {
                    e.getDrops().add(new ItemStack(Material.SKELETON_SKULL));
                }
            } else if (e.getEntity() instanceof Creeper) {
                if (random.nextInt(100) < chanceCreeper.getValue()) {
                    e.getDrops().add(new ItemStack(Material.CREEPER_HEAD));
                }
            } else if (e.getEntity() instanceof Player && random.nextInt(100) < chancePlayer.getValue()) {
                ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta meta = skull.getItemMeta();
                ((SkullMeta) meta).setOwningPlayer((Player) e.getEntity());
                skull.setItemMeta(meta);

                e.getDrops().add(skull);
            }
        };
    }

}
