package io.github.thebusybiscuit.slimefun4.api.items;

import org.apache.commons.lang.Validate;

public class RangedItemSetting extends ItemSetting<Integer> {

    private final int min;
    private final int max;

    public RangedItemSetting(String key, int min, int defaultValue, int max) {
        super(key, defaultValue);

        this.min = min;
        this.max = max;

        Validate.isTrue(defaultValue >= min && defaultValue <= max, "The default value must be equal to or inbetween the minimum and maximum.");
    }

    @Override
    public boolean validateInput(Integer input) {
        return super.validateInput(input) && input.intValue() >= min && input.intValue() <= max;
    }

    public int getIntValue() {
        return getValue().intValue();
    }

}
