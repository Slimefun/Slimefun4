package io.github.thebusybiscuit.slimefun4.implementation.settings;

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.GoldPan;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class GoldPanDrop extends ItemSetting<Integer> {

    private final GoldPan goldPan;
    private final ItemStack output;

    @ParametersAreNonnullByDefault
    public GoldPanDrop(GoldPan goldPan, String key, int defaultValue, ItemStack output) {
        super(goldPan, key, defaultValue);

        this.goldPan = goldPan;
        this.output = output;
    }

    @Override
    public boolean validateInput(Integer input) {
        return super.validateInput(input) && input >= 0;
    }

    @Nonnull
    public ItemStack getOutput() {
        return output;
    }

    @Override
    public void update(Integer newValue) {
        super.update(newValue);
        goldPan.updateRandomizer();
    }

}