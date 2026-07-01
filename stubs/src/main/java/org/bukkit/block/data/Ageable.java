package org.bukkit.block.data;

/**
 * Compile-only stub for {@code org.bukkit.block.data.Ageable} (Minecraft 1.13+). Signatures match the
 * real interface so calls resolve at runtime on modern servers.
 */
public interface Ageable extends BlockData {

    int getAge();

    void setAge(int age);

    int getMaximumAge();
}
