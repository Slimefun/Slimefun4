package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems;

import org.bukkit.inventory.ItemStack;

// This class will be rewritten in the "Recipe Rewrite"
public class MachineRecipe {

    private final ItemStack[] input;
    private final ItemStack[] output;
    private int ticks;

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

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

}
