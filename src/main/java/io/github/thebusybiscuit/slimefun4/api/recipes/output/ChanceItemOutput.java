package io.github.thebusybiscuit.slimefun4.api.recipes.output;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ChanceItemOutput extends ItemOutput {

    private final double chance;

    /**
     * @param output The output of the recipe
     * @param chance The chance [0...1] of the item being crafted
     */
    public ChanceItemOutput(ItemStack output, double chance) {
        super(output);

        this.chance = Math.max(Math.min(chance, 1), 0);
    }

    public double getChance() {
        return chance;
    }

    @Override
    public ItemStack generateOutput() {
        return ThreadLocalRandom.current().nextDouble() < chance ? new ItemStack(Material.AIR) : super.generateOutput();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ChanceItemOutput other = (ChanceItemOutput) obj;
        return Objects.equals(other.getOutputTemplate(), getOutputTemplate()) && chance == other.chance;
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 31 + Double.hashCode(chance);
    }
    
}
