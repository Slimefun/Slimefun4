package io.github.thebusybiscuit.slimefun4.utils;

import org.apache.commons.lang.Validate;

/**
 * Some helper methods for dealing with experience converting.
 *
 * @author kingdom84521
 *
 * @see io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideImplementation
 * @see <a href="https://minecraft.fandom.com/wiki/Experience"></a>
 *
 */
public final class ExperienceUtils {

    public static class LevelAndProgress {
        public Integer level;
        public Float progress;

        public LevelAndProgress(int level, float progress) {
            this.level = level;
            this.progress = progress;
        }
    }

    /**
     * Helper method to calculate the level to exact xp
     *
     * @param level
     *            The level to convert
     *
     * @return xp
     */

    public static float convertLevelToFloatExp(int level) {
        Validate.isTrue(level >= 0, "Cannot process with a negative level number");

        if (level > 31) {
            return (float) (4.5 * level * level - 162.5 * level + 2220);
        } else if (level > 16) {
            return (float) (2.5 * level * level - 40.5 * level + 360);
        } else {
            return level * level + 6 * level;
        }
    }

    private static float getExpToNextLevel(int currentLevel) {
        if (currentLevel > 30) {
            return (float) 9 * currentLevel - 158;
        } else if (currentLevel > 15) {
            return (float) 5 * currentLevel - 38;
        } else {
            return (float) 2 * currentLevel + 7;
        }
    }

    /**
     * Helper method to know the real xp player currently have
     *
     * @param level
     *            Level that should be coming from Player.getLevel
     * @param progress
     *            Progress that currently show on the player's exp bar,
     *            which should be coming from Player.getExp
     *
     * @return player current xp
     */
    public static float getPlayerCurrentExp(int level, float progress) {
        return convertLevelToFloatExp(level) + (getExpToNextLevel(level) * progress);
    }

    private static float convertExpToLevel(float exp) {
        if (exp >= 1508) {
            return (float) (325 + Math.sqrt(72 * exp - 54215)) / 18;
        } else if (exp >= 353) {
            return (float) (81 + Math.sqrt(40 * exp - 7839)) / 10;
        } else {
            return (float) Math.sqrt(exp + 9) - 3;
        }
    }

    /**
     * Helper method to convert the amount of xp to exact level and progress
     *
     * @param exp
     *            The exp to convert
     *
     * @return
     */
    public static LevelAndProgress convertExpToLevelAndProgress(float exp) {
        int level = (int) Math.floor(convertExpToLevel(exp));
        float progress = convertExpToLevel(exp) - level;

        LevelAndProgress levelAndProgress = new LevelAndProgress(level, progress);

        return levelAndProgress;
    }
}
