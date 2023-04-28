package io.github.thebusybiscuit.slimefun4.utils;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Some helper methods for dealing with experience converting.
 *
 * @author kingdom84521
 *
 * @see io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideImplementation
 * @see <a href="https://minecraft.fandom.com/wiki/Experience"></>
 *
 */
public final class ExperienceUtils {

    /**
     * Helper method to calculate the level to exact xp
     *
     * @param level
     *            The level to convert ( in type of {@link Integer} )
     *
     * @return xp in type {@link Float}
     */

    public static @Nonnull float convertLevelToFloatExp(@Nonnull int level) {
        if (level > 31) {
            return (float) (4.5 * level * level - 162.5 * level + 2220);
        } else if (level > 16) {
            return (float) (2.5 * level * level - 40.5 * level + 360);
        } else {
            return level * level + 6 * level;
        }
    }

    private static @Nonnull float getExpToNextLevel(@Nonnull int currentLevel) {
        if (currentLevel > 30) {
            return (float) 9 * currentLevel - 158;
        } else if (currentLevel > 15) {
            return (float) 5 * currentLevel - 38;
        } else {
            return (float) 2 * currentLevel + 7;
        }
    }

    public static @Nonnull float getPlayerCurrentExp(@Nonnull int level, @Nonnull float progress) {
        return convertLevelToFloatExp(level) + (getExpToNextLevel(level) * progress);
    }

    private static @Nonnull float convertExpToLevel(@Nonnull float exp) {
        if (exp >= 1508) {
            return (float) (325 + Math.sqrt(72 * exp - 54215)) / 18;
        } else if (exp >= 353) {
            return (float) (81 + Math.sqrt(40 * exp - 7839)) / 10;
        } else {
            return (float) Math.sqrt(exp + 9) - 3;
        }
    }

    public static @Nonnull Number[] convertExpToLevelAndProgress(@Nonnull float exp) {
        int level = (int) Math.floor(convertExpToLevel(exp));
        float progress = convertExpToLevel(exp) - level;

        Number[] levelAndProgress = new Number[2];

        levelAndProgress[0] = level;
        levelAndProgress[1] = progress;

        return levelAndProgress;
    }
}
