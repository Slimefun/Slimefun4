package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
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

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans.MagicianTalisman;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans.Talisman;
import io.github.thebusybiscuit.slimefun4.implementation.settings.TalismanEnchantment;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;

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
                    if (e instanceof EntityDamageByEntityEvent entityDamageByEntityEvent) {
                        onProjectileDamage(entityDamageByEntityEvent);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void onProjectileDamage(@Nonnull EntityDamageByEntityEvent e) {
        // "Fixes" #1022 - We just ignore Tridents now.
        if (e.getDamager() instanceof Projectile projectile && !(e.getDamager() instanceof Trident)) {
            if (Talisman.trigger(e, SlimefunItems.TALISMAN_WHIRLWIND)) {
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

        if (projectile instanceof AbstractArrow firedArrow) {
            AbstractArrow returnedArrow = (AbstractArrow) returnedProjectile;

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
        if (entity instanceof ChestedHorse chestedHorse) {
            if (chestedHorse.isCarryingChest()) {
                // The chest is not included in getStorageContents()
                items.remove(new ItemStack(Material.CHEST));

                for (ItemStack item : chestedHorse.getInventory().getStorageContents()) {
                    items.remove(item);
                }
            }
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

            items.remove(equipment.getItemInMainHand());
            items.remove(equipment.getItemInOffHand());
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
            if (brokenItem.equals(inv.getItemInMainHand())) {
                slot = inv.getHeldItemSlot();
            } else if (brokenItem.equals(inv.getItemInOffHand())) {
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

            if (meta instanceof Damageable damageable) {
                damageable.setDamage(0);
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
        if (!enchantments.containsKey(Enchantment.SILK_TOUCH) && Enchantment.LOOT_BONUS_BLOCKS.canEnchantItem(e.getItem()) && Talisman.trigger(e, SlimefunItems.TALISMAN_WIZARD)) {
            // Randomly lower some enchantments
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                if (entry.getValue() > 1 && random.nextInt(100) < 40) {
                    enchantments.put(entry.getKey(), entry.getValue() - 1);
                }
            }

            // Give an extra Fortune boost (Lvl 3 - 5)
            enchantments.put(Enchantment.LOOT_BONUS_BLOCKS, random.nextInt(3) + 3);
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

    @EventHandler(ignoreCancelled = true)
    public void onBlockDropItems(BlockDropItemEvent e) {
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();

        // We are going to ignore Silk Touch here
        if (item.getType() != Material.AIR && item.getAmount() > 0) {
            ItemMeta meta = item.getItemMeta();

            // Ignore Silk Touch Enchantment
            if (meta.hasEnchant(Enchantment.SILK_TOUCH)) {
                return;
            }

            Material type = e.getBlockState().getType();

            // Handle double drops for Miner Talisman
            doubleTalismanDrops(e, SlimefunItems.TALISMAN_MINER, SlimefunTag.MINER_TALISMAN_TRIGGERS, type, meta);

            // Handle double drops for Farmer Talisman
            doubleTalismanDrops(e, SlimefunItems.TALISMAN_FARMER, SlimefunTag.FARMER_TALISMAN_TRIGGERS, type, meta);
        }
    }

    private void doubleTalismanDrops(BlockDropItemEvent e, SlimefunItemStack talismanItemStack, SlimefunTag tag, Material type, ItemMeta meta) {
        if (tag.isTagged(type)) {
            Collection<Item> drops = e.getItems();

            if (Talisman.trigger(e, talismanItemStack, false)) {
                int dropAmount = getAmountWithFortune(type, meta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS));

                // Keep track of whether we actually doubled the drops or not
                boolean doubledDrops = false;

                // Loop through all dropped items
                for (Item drop : drops) {
                    ItemStack droppedItem = drop.getItemStack();

                    // We do not want to dupe blocks
                    if (!droppedItem.getType().isBlock()) {
                        int amount = Math.max(1, (dropAmount * 2) - droppedItem.getAmount());
                        e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new CustomItemStack(droppedItem, amount));
                        doubledDrops = true;
                    }
                }

                // Fixes #2077
                if (doubledDrops) {
                    Talisman talisman = talismanItemStack.getItem(Talisman.class);

                    // Fixes #2818
                    if (talisman != null) {
                        talisman.sendMessage(e.getPlayer());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (SlimefunTag.CAVEMAN_TALISMAN_TRIGGERS.isTagged(e.getBlock().getType())) {
            Talisman.trigger(e, SlimefunItems.TALISMAN_CAVEMAN);
        }
    }

    private int getAmountWithFortune(@Nonnull Material type, int fortuneLevel) {
        if (fortuneLevel > 0) {
            Random random = ThreadLocalRandom.current();
            int amount = random.nextInt(fortuneLevel + 2) - 1;
            amount = Math.max(amount, 1);
            amount = (type == Material.LAPIS_ORE ? 4 + random.nextInt(5) : 1) * (amount + 1);
            return amount;
        } else {
            return 1;
        }
    }
}