package io.github.thebusybiscuit.slimefun4.storage.backend.binary;

import org.bukkit.NamespacedKey;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

public class CommonKeys {

    public static final NamespacedKey ID = new NamespacedKey(Slimefun.instance(), "id");
    public static final NamespacedKey NAME = new NamespacedKey(Slimefun.instance(), "name");
    // Location stuff
    public static final NamespacedKey LOCATION = new NamespacedKey(Slimefun.instance(), "location");
    public static final NamespacedKey WORLD = new NamespacedKey(Slimefun.instance(), "world");
    public static final NamespacedKey POSITION = new NamespacedKey(Slimefun.instance(), "position");
    public static final NamespacedKey PITCH = new NamespacedKey(Slimefun.instance(), "pitch");
    public static final NamespacedKey YAW = new NamespacedKey(Slimefun.instance(), "yaw");
}
