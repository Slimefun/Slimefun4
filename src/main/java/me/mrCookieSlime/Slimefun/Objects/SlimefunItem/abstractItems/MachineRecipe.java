package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems;

import org.bukkit.inventory.ItemStack;

public class MachineRecipe {

    private int ticks;
    private final ItemStack[] input;
    private final ItemStack[] output;

    public MachineRecipe(int seconds, ItemStack[] input, ItemStack[] output) {
        this.ticks = seconds * 2;
        this.input = input;
        this.output = output;
    }

    public ItemStack[] getInput() {
        return this.input;
    }

    public ItemStack[] getOutput() {
        return this.output;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public int getTicks() {
        return ticks;
    }

}
