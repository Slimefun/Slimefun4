package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

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
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans.MagicianTalisman;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans.Talisman;
import io.github.thebusybiscuit.slimefun4.implementation.settings.TalismanEnchantment;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;

public class TalismanListener implements Listener {

    private final int[] armorSlots = { 39, 38, 37, 36 };

    public TalismanListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamageGet(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getCause() == DamageCause.LAVA) {
                Talisman.checkFor(e, SlimefunItems.TALISMAN_LAVA);
            }

            if (e.getCause() == DamageCause.DROWNING) {
                Talisman.checkFor(e, SlimefunItems.TALISMAN_WATER);
            }

            if (e.getCause() == DamageCause.FALL) {
                Talisman.checkFor(e, SlimefunItems.TALISMAN_ANGEL);
            }

            if (e.getCause() == DamageCause.FIRE) {
                Talisman.checkFor(e, SlimefunItems.TALISMAN_FIRE);
            }

            if (e.getCause() == DamageCause.ENTITY_ATTACK) {
                Talisman.checkFor(e, SlimefunItems.TALISMAN_KNIGHT);
                Talisman.checkFor(e, SlimefunItems.TALISMAN_WARRIOR);
            }

            if (e.getCause() == DamageCause.PROJECTILE && e instanceof EntityDamageByEntityEvent) {
                onProjectileDamage((EntityDamageByEntityEvent) e);
            }
        }
    }

    private void onProjectileDamage(@Nonnull EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Projectile && !(e.getDamager() instanceof Trident)) {
            Projectile projectile = (Projectile) e.getDamager();

            if (Talisman.checkFor(e, SlimefunItems.TALISMAN_WHIRLWIND)) {
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
            AbstractArrow firedArrow = (AbstractArrow) projectile;
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
            // We absolutely don't want to double the
            // drops from players or ArmorStands
            return;
        }

        // We are also excluding entities which can pickup items, this is not perfect
        // but it at least prevents dupes by tossing items to zombies
        if (!entity.getCanPickupItems() && Talisman.checkFor(e, SlimefunItems.TALISMAN_HUNTER)) {
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
            ChestedHorse horse = (ChestedHorse) entity;

            if (horse.isCarryingChest()) {
                // The chest is not included in getStorageContents()
                items.remove(new ItemStack(Material.CHEST));

                for (ItemStack item : horse.getInventory().getStorageContents()) {
                    items.remove(item);
                }
            }
        }

        // WARNING: This check is broken as entities now set their
        // equipment to NULL before calling the event!

        // Prevents duplication of handheld items or armor
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
        if (Talisman.checkFor(e, SlimefunItems.TALISMAN_ANVIL)) {
            PlayerInventory inv = e.getPlayer().getInventory();
            int slot = inv.getHeldItemSlot();

            // Did the tool in our hand broke or was it an Armorpiece?
            if (!inv.getItem(inv.getHeldItemSlot()).equals(e.getBrokenItem())) {
                for (int s : armorSlots) {
                    if (e.getBrokenItem().equals(inv.getItem(s))) {
                        slot = s;
                        break;
                    }
                }
            }

            ItemStack item = e.getBrokenItem().clone();
            ItemMeta meta = item.getItemMeta();

            if (meta instanceof Damageable) {
                ((Damageable) meta).setDamage(0);
            }

            item.setItemMeta(meta);

            int itemSlot = slot;

            // Update the item forcefully
            SlimefunPlugin.runSync(() -> inv.setItem(itemSlot, item), 1L);
        }
    }

    @EventHandler
    public void onSprint(PlayerToggleSprintEvent e) {
        if (e.isSprinting()) {
            Talisman.checkFor(e, SlimefunItems.TALISMAN_TRAVELLER);
        }
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent e) {
        Random random = ThreadLocalRandom.current();

        if (Talisman.checkFor(e, SlimefunItems.TALISMAN_MAGICIAN)) {
            MagicianTalisman talisman = (MagicianTalisman) SlimefunItems.TALISMAN_MAGICIAN.getItem();
            TalismanEnchantment enchantment = talisman.getRandomEnchantment(e.getItem());

            if (enchantment != null) {
                e.getEnchantsToAdd().put(enchantment.getEnchantment(), enchantment.getLevel());
            }
        }

        if (!e.getEnchantsToAdd().containsKey(Enchantment.SILK_TOUCH) && Enchantment.LOOT_BONUS_BLOCKS.canEnchantItem(e.getItem()) && Talisman.checkFor(e, SlimefunItems.TALISMAN_WIZARD)) {
            Set<Enchantment> enchantments = e.getEnchantsToAdd().keySet();

            for (Enchantment enchantment : enchantments) {
                if (random.nextInt(100) < 40) {
                    e.getEnchantsToAdd().put(enchantment, random.nextInt(3) + 1);
                }
            }

            e.getEnchantsToAdd().put(Enchantment.LOOT_BONUS_BLOCKS, random.nextInt(3) + 3);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockDropItems(BlockDropItemEvent e) {
        // We only want to double ores
        Material type = e.getBlockState().getType();
        if (type.name().endsWith("_ORE")) {
            ItemStack item = e.getPlayer().getInventory().getItemInMainHand();

            if (item.getType() != Material.AIR && item.getAmount() > 0 && !item.containsEnchantment(Enchantment.SILK_TOUCH)) {
                Collection<Item> drops = e.getItems();

                if (Talisman.checkFor(e, SlimefunItems.TALISMAN_MINER)) {
                    int dropAmount = getAmountWithFortune(type, item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS));
                    boolean doubledDrops = false;

                    for (Item drop : drops) {
                        ItemStack droppedItem = drop.getItemStack();

                        if (!droppedItem.getType().isBlock()) {
                            int amount = Math.max(1, (dropAmount * 2) - droppedItem.getAmount());
                            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new CustomItem(droppedItem, amount));
                            doubledDrops = true;
                        }
                    }

                    if (doubledDrops) {
                        SlimefunPlugin.getLocalization().sendMessage(e.getPlayer(), "messages.talisman.miner", true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (SlimefunTag.CAVEMAN_TALISMAN_TRIGGERS.isTagged(e.getBlock().getType())) {
            Talisman.checkFor(e, SlimefunItems.TALISMAN_CAVEMAN);
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
