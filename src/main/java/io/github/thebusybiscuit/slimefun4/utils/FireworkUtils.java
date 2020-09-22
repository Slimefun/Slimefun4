package io.github.thebusybiscuit.slimefun4.utils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

/**
 * This is a simple utility class for spawning random and colorful {@link Firework} rockets.
 * 
 * @author TheBusyBiscuit
 *
 */
public final class FireworkUtils {

    private static final Color[] COLORS = { Color.AQUA, Color.BLACK, Color.BLUE, Color.FUCHSIA, Color.GRAY, Color.GREEN, Color.LIME, Color.MAROON, Color.NAVY, Color.OLIVE, Color.ORANGE, Color.PURPLE, Color.RED, Color.SILVER, Color.TEAL, Color.WHITE, Color.YELLOW };

    private FireworkUtils() {}

    public static void launchFirework(@Nonnull Location l, @Nonnull Color color) {
        createFirework(l, color);
    }

    @Nonnull
    public static Firework createFirework(@Nonnull Location l, @Nonnull Color color) {
        Firework fw = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
        FireworkMeta meta = fw.getFireworkMeta();

        meta.setDisplayName(ChatColor.GREEN + "Slimefun Research");
        FireworkEffect effect = getRandomEffect(ThreadLocalRandom.current(), color);
        meta.addEffect(effect);
        meta.setPower(ThreadLocalRandom.current().nextInt(2) + 1);
        fw.setFireworkMeta(meta);

        return fw;
    }

    public static void launchRandom(@Nonnull Entity n, int amount) {
        Random random = ThreadLocalRandom.current();

        for (int i = 0; i < amount; i++) {
            Location l = n.getLocation().clone();
            l.setX(l.getX() + random.nextInt(amount * 2) - amount);
            l.setZ(l.getZ() + random.nextInt(amount * 2) - amount);

            launchFirework(l, getRandomColor());
        }
    }

    @Nonnull
    public static FireworkEffect getRandomEffect(@Nonnull Random random, @Nonnull Color color) {
        return FireworkEffect.builder().flicker(random.nextBoolean()).withColor(color).with(random.nextBoolean() ? Type.BALL : Type.BALL_LARGE).trail(random.nextBoolean()).build();
    }

    @Nonnull
    private static Color getRandomColor() {
        return COLORS[ThreadLocalRandom.current().nextInt(COLORS.length)];
    }
}
