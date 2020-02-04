package me.mrCookieSlime.Slimefun.Objects.handlers;

import org.bukkit.Location;

import io.github.thebusybiscuit.slimefun4.api.blocks.SlimefunBlock;

interface TickHandler extends ItemHandler {

    void tick(Location l, SlimefunBlock block);

    default boolean isSynchronized() {
        return this instanceof SynchronizedTickHandler;
    }

}