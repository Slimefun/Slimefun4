package io.github.thebusybiscuit.slimefun5.implementation.listeners;

import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * This {@link Listener} grants the full Ender armor set an enderman-style dodge: when the wearer is
 * struck by a {@link Projectile}, the hit is cancelled and the player is teleported to a random safe
 * location nearby.
 *
 * @author TheBusyBiscuit
 */
public class EnderArmorListener implements Listener {

    private static final String[] ENDER_ARMOR_IDS = { "ENDER_HELMET", "ENDER_CHESTPLATE", "ENDER_LEGGINGS", "ENDER_BOOTS" };

    private static final int MIN_RADIUS = 8;
    private static final int MAX_RADIUS = 16;
    private static final int MAX_ATTEMPTS = 16;

    public EnderArmorListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onProjectileHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Projectile) || !(e.getEntity() instanceof Player)) {
            return;
        }

        Player p = (Player) e.getEntity();

        if (!isWearingFullEnderSet(p)) {
            return;
        }

        Location destination = findSafeLocation(p.getLocation());

        if (destination != null) {
            e.setCancelled(true);
            p.teleport(destination);
        }
    }

    private boolean isWearingFullEnderSet(@Nonnull Player p) {
        PlayerInventory inventory = p.getInventory();
        ItemStack[] armor = { inventory.getHelmet(), inventory.getChestplate(), inventory.getLeggings(), inventory.getBoots() };

        for (int slot = 0; slot < 4; slot++) {
            SlimefunItem sfItem = SlimefunItem.getByItem(armor[slot]);

            if (sfItem == null || !sfItem.getId().equals(ENDER_ARMOR_IDS[slot])) {
                return false;
            }
        }

        return true;
    }

    /**
     * Finds a random safe location around the given origin: two air blocks with solid, non-lava
     * ground below. Returns {@code null} if no safe spot was found within {@link #MAX_ATTEMPTS} tries.
     *
     * @param origin
     *            The {@link Location} to teleport away from
     *
     * @return A safe {@link Location}, or {@code null} if none was found
     */
    @Nullable
    private Location findSafeLocation(@Nonnull Location origin) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            int offsetX = randomOffset(random);
            int offsetZ = randomOffset(random);

            Block block = origin.clone().add(offsetX, 0, offsetZ).getBlock();
            Block ground = findGround(block);

            if (ground != null) {
                return ground.getLocation().add(0.5, 1, 0.5);
            }
        }

        return null;
    }

    private int randomOffset(@Nonnull ThreadLocalRandom random) {
        int magnitude = MIN_RADIUS + random.nextInt(MAX_RADIUS - MIN_RADIUS + 1);
        return random.nextBoolean() ? magnitude : -magnitude;
    }

    /**
     * Scans vertically around the given block for solid, non-lava ground with two air blocks above it.
     *
     * @param block
     *            The starting {@link Block} at the origin's Y level
     *
     * @return The ground {@link Block} to stand on, or {@code null} if none was found nearby
     */
    @Nullable
    private Block findGround(@Nonnull Block block) {
        for (int dy = 3; dy >= -3; dy--) {
            Block ground = block.getRelative(0, dy, 0);
            Block feet = ground.getRelative(0, 1, 0);
            Block head = ground.getRelative(0, 2, 0);

            if (isSolid(ground) && !isLava(ground) && isPassable(feet) && isPassable(head)) {
                return ground;
            }
        }

        return null;
    }

    private boolean isSolid(@Nonnull Block block) {
        Material type = block.getType();
        return type.isSolid();
    }

    private boolean isLava(@Nonnull Block block) {
        Material type = block.getType();
        return type == Material.LAVA || type.name().equals("STATIONARY_LAVA");
    }

    private boolean isPassable(@Nonnull Block block) {
        Material type = block.getType();
        return type == Material.AIR || !type.isSolid();
    }
}
