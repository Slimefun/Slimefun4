package io.github.thebusybiscuit.slimefun4.implementation.items.magical;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.EntityInteractHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * This {@link SlimefunItem} allows you to convert any {@link ZombieVillager} to
 * their {@link Villager} variant.
 *
 * @author Linox
 *
 * @see EntityInteractHandler
 *
 */
public class MagicPills extends SimpleSlimefunItem<EntityInteractHandler> {

    public MagicPills(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);
    }

    @Override
    public EntityInteractHandler getItemHandler() {
        return (e, item) -> {
            if (e.getRightClicked().getType() == EntityType.ZOMBIE_VILLAGER) {

                Player p = e.getPlayer();

                ZombieVillager zombieVillager = (ZombieVillager) e.getRightClicked();
                zombieVillager.setConversionTime(1);
                if (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_15)) {
                    zombieVillager.setConversionPlayer(p);
                }

                p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1, 1);

                int amount = item.getAmount();
                amount -= 1;
                item.setAmount(amount);

                if (e.getHand() == EquipmentSlot.OFF_HAND) {
                    p.getInventory().setItemInOffHand(item);
                } else {
                    p.getInventory().setItemInMainHand(item);
                }

            }
        };
    }

}