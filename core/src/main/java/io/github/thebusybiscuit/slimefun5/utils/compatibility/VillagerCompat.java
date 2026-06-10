package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import javax.annotation.Nullable;

import org.bukkit.entity.Villager;

/**
 * Java-8 universal port: the {@link Villager.Profession} enum gained {@code NITWIT} (1.11) and
 * {@code NONE} (1.14), and the leveled-trade setters {@code setVillagerExperience}/{@code setVillagerLevel}
 * are 1.11+. Profession constants are resolved by name (null when absent) and the setters go through
 * reflection so the code compiles against the 1.8.8 floor while keeping full behaviour on modern servers.
 */
public final class VillagerCompat {

    private VillagerCompat() {}

    @Nullable
    public static Villager.Profession profession(String name) {
        try {
            return Villager.Profession.valueOf(name);
        } catch (Throwable ignored) {
            return null;
        }
    }

    public static void setVillagerExperience(Villager villager, int experience) {
        ReflectionCompat.invoke(villager, "setVillagerExperience", experience);
    }

    public static void setVillagerLevel(Villager villager, int level) {
        ReflectionCompat.invoke(villager, "setVillagerLevel", level);
    }
}
