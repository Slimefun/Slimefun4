package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

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

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.EntityKillHandler;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class SwordOfBeheading extends SimpleSlimefunItem<EntityKillHandler> {
	
	private int chanceZombie;
	private int chanceSkeleton;
	private int chanceCreeper;
	private int chanceWitherSkeleton;
	private int chancePlayer;
	
	public SwordOfBeheading(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String[] keys, Object[] values) {
		super(category, item, recipeType, recipe, keys, values);
	}

	@Override
	protected boolean areItemHandlersPrivate() {
		return false;
	}

	@Override
	public EntityKillHandler getItemHandler() {
		return (e, entity, killer, item) -> {
			if (isItem(item)) {
				Random random = ThreadLocalRandom.current();
				
				if (e.getEntity() instanceof Zombie) {
                    if (random.nextInt(100) < chanceZombie) {
                        e.getDrops().add(new ItemStack(Material.ZOMBIE_HEAD));
                    }
                }
                else if (e.getEntity() instanceof WitherSkeleton) {
                    if (random.nextInt(100) < chanceWitherSkeleton)
                        e.getDrops().add(new ItemStack(Material.WITHER_SKELETON_SKULL));
                }
                else if (e.getEntity() instanceof Skeleton) {
                    if (random.nextInt(100) < chanceSkeleton)
                        e.getDrops().add(new ItemStack(Material.SKELETON_SKULL));
                }
                else if (e.getEntity() instanceof Creeper) {
                    if (random.nextInt(100) < chanceCreeper) {
                        e.getDrops().add(new ItemStack(Material.CREEPER_HEAD));
                    }
                }
                else if (e.getEntity() instanceof Player && random.nextInt(100) < chancePlayer) {
                	ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                    ItemMeta meta = skull.getItemMeta();
                    ((SkullMeta) meta).setOwningPlayer((Player) e.getEntity());
                    skull.setItemMeta(meta);

                    e.getDrops().add(skull);
                }
				
				return true;
			}
			else return false;
		};
	}
	
	@Override
	public void postRegister() {
		chanceZombie = (int) Slimefun.getItemValue(getID(), "chance.ZOMBIE");
		chanceSkeleton = (int) Slimefun.getItemValue(getID(), "chance.SKELETON");
		chanceCreeper = (int) Slimefun.getItemValue(getID(), "chance.CREEPER");
		chanceWitherSkeleton = (int) Slimefun.getItemValue(getID(), "chance.WITHER_SKELETON");
		chancePlayer = (int) Slimefun.getItemValue(getID(), "chance.PLAYER");
	}

}
