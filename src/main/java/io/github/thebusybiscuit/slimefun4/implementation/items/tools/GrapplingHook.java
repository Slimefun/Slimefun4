package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class GrapplingHook extends SimpleSlimefunItem<ItemUseHandler> {

    private final ItemSetting<Integer> despawnTicks = new ItemSetting<>("despawn-seconds", 60);

    public GrapplingHook(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemSetting(despawnTicks);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Player p = e.getPlayer();
            UUID uuid = p.getUniqueId();

            if (!e.getClickedBlock().isPresent() && !SlimefunPlugin.getGrapplingHookListener().isGrappling(uuid)) {
                e.cancel();

                if (p.getInventory().getItemInOffHand().getType() == Material.BOW) {
                    // Cancel, to fix dupe #740
                    return;
                }

                ItemStack item = e.getItem();

                if (item.getType() == Material.LEAD) {
                    item.setAmount(item.getAmount() - 1);
                }

                Vector direction = p.getEyeLocation().getDirection().multiply(2.0);
                Arrow arrow = p.getWorld().spawn(p.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), Arrow.class);
                arrow.setShooter(p);
                arrow.setVelocity(direction);

                Bat b = (Bat) p.getWorld().spawnEntity(p.getLocation(), EntityType.BAT);
                b.setCanPickupItems(false);
                b.setAI(false);
                b.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 100000));
                b.setLeashHolder(arrow);

                boolean state = item.getType() != Material.SHEARS;
                SlimefunPlugin.getGrapplingHookListener().addGrapplingHook(uuid, arrow, b, state, despawnTicks.getValue() * 20L);
            }
        };
    }
}
