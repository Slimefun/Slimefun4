package io.github.thebusybiscuit.slimefun4.api.items.settings;

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import org.apache.commons.lang.Validate;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * This variation of {@link ItemSetting} allows you to define an {@link Float} range
 * and enforces this range using the {@link #validateInput(Float)} method.
 * 
 * @author TheBusyBiscuit
 *
 * @see ItemSetting
 * @see IntRangeSetting
 * @see DoubleRangeSetting
 */
public class FloatRangeSetting extends ItemSetting<Float> {

    private final float min;
    private final float max;

    @ParametersAreNonnullByDefault
    public FloatRangeSetting(SlimefunItem item, String key, float min, float defaultValue, float max) {
        super(item, key, defaultValue);
        Validate.isTrue(defaultValue >= min && defaultValue <= max, "The default value is not in range.");

        this.min = min;
        this.max = max;
    }

    @Nonnull
    @Override
    protected String getErrorMessage() {
        return "Only decimal numbers from " + min + '-' + max + "(inclusive) are allowed!";
    }

    @Override
    public boolean validateInput(Float input) {
        return super.validateInput(input) && input >= min && input <= max;
    }

    /**
     * This returns the minimum value of this {@link FloatRangeSetting}.
     * 
     * @return The minimum value
     */
    public final float getMinimum() {
        return min;
    }

    /**
     * This returns the maximum value of this {@link FloatRangeSetting}.
     * 
     * @return The maximum value
     */
    public final float getMaximum() {
        return max;
    }

}
