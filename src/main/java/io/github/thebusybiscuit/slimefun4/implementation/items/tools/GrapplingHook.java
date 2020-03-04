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

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class GrapplingHook extends SimpleSlimefunItem<ItemUseHandler> {

    private long despawnTicks;

    public GrapplingHook(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String[] keys, Object[] values) {
        super(category, item, recipeType, recipe, keys, values);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Player p = e.getPlayer();
            UUID uuid = p.getUniqueId();

            if (!e.getClickedBlock().isPresent() && !SlimefunPlugin.getGrapplingHookListener().isJumping(uuid)) {
                e.cancel();

                ItemStack item = e.getItem();

                if (p.getInventory().getItemInOffHand().getType() == Material.BOW) {
                    // Cancel, to fix dupe #740
                    return;
                }

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

                boolean state = p.getInventory().getItemInMainHand().getType() != Material.SHEARS;
                SlimefunPlugin.getGrapplingHookListener().addGrapplingHook(uuid, arrow, b, state, despawnTicks);
            }
        };
    }

    @Override
    public void postRegister() {
        despawnTicks = (int) Slimefun.getItemValue(getID(), "despawn-seconds") * 20L;
    }
}
