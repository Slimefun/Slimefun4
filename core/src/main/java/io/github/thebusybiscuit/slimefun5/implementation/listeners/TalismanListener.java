package io.github.thebusybiscuit.slimefun5.implementation.listeners;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.InventoryCompat;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.HandCompat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Ravager;
import org.bukkit.entity.Trident;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun5.implementation.items.magical.talismans.MagicianTalisman;
import io.github.thebusybiscuit.slimefun5.implementation.items.magical.talismans.Talisman;
import io.github.thebusybiscuit.slimefun5.implementation.settings.TalismanEnchantment;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedEnchantment;
import io.github.thebusybiscuit.slimefun5.utils.tags.SlimefunTag;

/**
 * This {@link Listener} is responsible for handling any {@link Event}
 * that is required for activating a {@link Talisman}.
 *
 * @author TheBusyBiscuit
 * @author StarWishsama
 * @author svr333
 * @author martinbrom
 * @author Sfiguz7
 *
 * @see Talisman
 *
 */
public class TalismanListener implements Listener {

    private final int[] armorSlots = { 39, 38, 37, 36 };

    public TalismanListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamageGet(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            switch (e.getCause()) {
                case LAVA:
                    // Fire Resistance when hitting lava
                    Talisman.trigger(e, SlimefunItems.TALISMAN_LAVA);
                    break;
                case DROWNING:
                    // Water Breathing when starting to drown
                    Talisman.trigger(e, SlimefunItems.TALISMAN_WATER);
                    break;
                case FALL:
                    // 75% chance to prevent fall damage
                    Talisman.trigger(e, SlimefunItems.TALISMAN_ANGEL);
                    break;
                case FIRE:
                    // Fire Resistance when starting to burn
                    Talisman.trigger(e, SlimefunItems.TALISMAN_FIRE);
                    break;
                case ENTITY_ATTACK:
                    // 30% chance to get Regeneration
                    Talisman.trigger(e, SlimefunItems.TALISMAN_KNIGHT);

                    // Strength III when getting attacked
                    Talisman.trigger(e, SlimefunItems.TALISMAN_WARRIOR);
                    break;
                case PROJECTILE:
                    if (e instanceof EntityDamageByEntityEvent) {
                        EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) e;                        onProjectileDamage(entityDamageByEntityEvent);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void onProjectileDamage(@Nonnull EntityDamageByEntityEvent e) {
        // "Fixes" #1022 - We just ignore Tridents now.
        if (e.getDamager() instanceof Projectile && !(e.getDamager() instanceof Trident)) {
            Projectile projectile = (Projectile) e.getDamager();            if (Talisman.trigger(e, SlimefunItems.TALISMAN_WHIRLWIND)) {
                Player p = (Player) e.getEntity();
                returnProjectile(p, projectile);
            }
        }
    }

    /**
     * This method is used for the {@link Talisman} of the whirlwind, it returns a copy
     * of a {@link Projectile} that was fired at a {@link Player}.
     *
     * @param p
     *            The {@link Player} who was hit
     * @param projectile
     *            The {@link Projectile} that hit this {@link Player}
     */
    private void returnProjectile(@Nonnull Player p, @Nonnull Projectile projectile) {
        Vector direction = p.getEyeLocation().getDirection().multiply(2.0);
        Location loc = p.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ());

        Projectile returnedProjectile = (Projectile) p.getWorld().spawnEntity(loc, projectile.getType());
        returnedProjectile.setShooter(projectile.getShooter());
        returnedProjectile.setVelocity(direction);

        if (projectile instanceof AbstractArrow) {
            AbstractArrow firedArrow = (AbstractArrow) projectile;            AbstractArrow returnedArrow = (AbstractArrow) returnedProjectile;

            returnedArrow.setDamage(firedArrow.getDamage());
            returnedArrow.setPickupStatus(firedArrow.getPickupStatus());
            returnedArrow.setPierceLevel(firedArrow.getPierceLevel());
        }

        projectile.remove();
    }

    @EventHandler(ignoreCancelled = true)
    public void onKill(EntityDeathEvent e) {
        if (e.getDrops().isEmpty() || e.getEntity().getKiller() == null) {
            return;
        }

        LivingEntity entity = e.getEntity();

        if (entity instanceof Player || entity instanceof ArmorStand) {
            /*
             * We absolutely don't want to double the
             * drops from players or ArmorStands
             */
            return;
        }

        /*
         * We are also excluding entities which can pickup items,
         * this is not perfect but it at least prevents dupes
         * by tossing items to zombies.
         */
        if (!entity.getCanPickupItems() && Talisman.trigger(e, SlimefunItems.TALISMAN_HUNTER)) {
            Collection<ItemStack> extraDrops = getExtraDrops(e.getEntity(), e.getDrops());

            for (ItemStack drop : extraDrops) {
                if (drop != null && drop.getType() != Material.AIR) {
                    e.getDrops().add(drop.clone());
                }
            }
        }
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    private Collection<ItemStack> getExtraDrops(LivingEntity entity, Collection<ItemStack> drops) {
        List<ItemStack> items = new ArrayList<>(drops);

        // Prevent duplication of items stored inside a Horse's chest
        if (entity instanceof ChestedHorse) {
            ChestedHorse chestedHorse = (ChestedHorse) entity;            if (chestedHorse.isCarryingChest()) {
                // The chest is not included in getStorageContents()
                items.remove(new ItemStack(Material.CHEST));

                for (ItemStack item : InventoryCompat.getStorageContents(chestedHorse.getInventory())) {
                    items.remove(item);
                }
            }
        }

        /*
         * Fixes #3254
         * Prevents saddle duplication from entities that don't drop
         * saddle from their loot table
         */
        if (!(entity instanceof Ravager)) {
            items.removeIf(item -> item.getType() == Material.SADDLE);
        }

        /*
         * WARNING: This check is broken as entities now set their
         * equipment to NULL before calling the event!
         *
         * It prevents duplication of handheld items or armor.
         */
        EntityEquipment equipment = entity.getEquipment();

        if (equipment != null) {
            for (ItemStack item : equipment.getArmorContents()) {
                items.remove(item);
            }

            items.remove(HandCompat.getMainHand(equipment));
            items.remove(HandCompat.getOffHand(equipment));
        }

        return items;
    }

    @EventHandler
    public void onItemBreak(PlayerItemBreakEvent e) {
        if (Talisman.trigger(e, SlimefunItems.TALISMAN_ANVIL)) {
            PlayerInventory inv = e.getPlayer().getInventory();

            ItemStack brokenItem = e.getBrokenItem();

            int slot = -1;

            // Did the tool in our hand break or was it an armor piece?
            if (brokenItem.equals(HandCompat.getMainHand(inv))) {
                slot = inv.getHeldItemSlot();
            } else if (brokenItem.equals(HandCompat.getOffHand(inv))) {
                slot = 40;
            } else {
                for (int s : armorSlots) {
                    if (e.getBrokenItem().equals(inv.getItem(s))) {
                        slot = s;
                        break;
                    }
                }
            }

            // No item found, just return.
            if (slot < 0) {
                return;
            }

            ItemStack item = e.getBrokenItem().clone();
            ItemMeta meta = item.getItemMeta();

            if (meta instanceof Damageable) {
                Damageable damageable = (Damageable) meta;                damageable.setDamage(0);
            }

            item.setItemMeta(meta);

            int itemSlot = slot;

            // Update the item forcefully
            Slimefun.runSync(() -> inv.setItem(itemSlot, item), 1L);
        }
    }

    @EventHandler
    public void onSprint(PlayerToggleSprintEvent e) {
        if (e.isSprinting()) {
            Talisman.trigger(e, SlimefunItems.TALISMAN_TRAVELLER);
        }
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent e) {
        Random random = ThreadLocalRandom.current();
        Map<Enchantment, Integer> enchantments = e.getEnchantsToAdd();

        // Magician Talisman
        MagicianTalisman talisman = (MagicianTalisman) SlimefunItems.TALISMAN_MAGICIAN.getItem();
        TalismanEnchantment enchantment = talisman.getRandomEnchantment(e.getItem(), enchantments.keySet());

        if (enchantment != null && Talisman.trigger(e, SlimefunItems.TALISMAN_MAGICIAN)) {
            /*
             * Fixes #2679
             *
             * By default, the Bukkit API doesn't allow us to give enchantment books
             * extra enchantments.
             */
            if (talisman.isEnchantmentBookAllowed() && e.getItem().getType() == Material.BOOK) {
                e.getItem().addUnsafeEnchantment(enchantment.getEnchantment(), enchantment.getLevel());
            } else {
                enchantments.put(enchantment.getEnchantment(), enchantment.getLevel());
            }
        }

        // Wizard Talisman
        if (!enchantments.containsKey(Enchantment.SILK_TOUCH) && VersionedEnchantment.FORTUNE.canEnchantItem(e.getItem()) && Talisman.trigger(e, SlimefunItems.TALISMAN_WIZARD)) {
            // Randomly lower some enchantments
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                if (entry.getValue() > 1 && random.nextInt(100) < 40) {
                    enchantments.put(entry.getKey(), entry.getValue() - 1);
                }
            }

            // Give an extra Fortune boost (Lvl 3 - 5)
            enchantments.put(VersionedEnchantment.FORTUNE, random.nextInt(3) + 3);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onExperienceReceive(PlayerExpChangeEvent e) {
        // Check if the experience change was positive.
        if (e.getAmount() > 0 && Talisman.trigger(e, SlimefunItems.TALISMAN_WISE)) {
            // Double-XP
            e.setAmount(e.getAmount() * 2);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (SlimefunTag.CAVEMAN_TALISMAN_TRIGGERS.isTagged(e.getBlock().getType())) {
            Talisman.trigger(e, SlimefunItems.TALISMAN_CAVEMAN);
        }
    }
}

