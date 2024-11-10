package io.github.thebusybiscuit.slimefun4.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;

public class RecipeUtils {

    public static class BoundingBox {
        public final int top;
        public final int left;
        public final int bottom;
        public final int right;
        
        public BoundingBox(int top, int left, int bottom, int right) {
            this.top = top;
            this.left = left;
            this.bottom = bottom;
            this.right = right;
        }

        @Override
        public String toString() {
            return "(" + top + ", " + left + ", " + bottom + ", " + right + ")";
        }

        public int getWidth() {
            return right - left + 1;
        }
        
        public int getHeight() {
            return bottom - top + 1;
        }

        public boolean isSameShape(BoundingBox other) {
            return this.getWidth() == other.getWidth() && this.getHeight() == other.getHeight();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null) return false;
            if (obj.getClass() != getClass()) return false;
            BoundingBox box = (BoundingBox) obj;
            return box.top == top &&
                box.left == left &&
                box.bottom == bottom &&
                box.right == right;

        }
    }

    /**
     * Returns the [top, left, width, height] of the smallest rectangle that includes all non-empty elements in the list
     * @param items Items list (represents 2d-grid). Elements of the list can be null
     * @param width Width of the grid
     * @param height Height of the grid
     * @return
     */
    @ParametersAreNonnullByDefault
    public static <T> BoundingBox calculateBoundingBox(List<T> items, int width, int height) {
        return calculateBoundingBox(items, width, height, t -> t == null);
    }

    /**
     * Returns the [top, left, width, height] of the smallest rectangle that includes all non-empty elements in the list
     * @param items Items list (represents 2d-grid). Elements of the list can be null
     * @param width Width of the grid, > 0
     * @param height Height of the grid, > 0
     * @param isEmpty Predicate for determining empty values
     * @return
     */
    @ParametersAreNonnullByDefault
    public static <T> BoundingBox calculateBoundingBox(List<T> items, int width, int height, Predicate<T> isEmpty) {
        int left = width - 1;
        int right = 0;
        int top = height - 1;
        int bottom = 0;
        boolean fullyEmpty = true;
        for (int i = 0; i < items.size(); i++) {
            int x = i % width;
            int y = i / width;

            if (!isEmpty.test(items.get(i))) {
                fullyEmpty = false;
                if (left > x) {
                    left = x;
                }
                if (right < x) {
                    right = x;
                }
                if (top > y) {
                    top = y;
                }
                if (bottom < y) {
                    bottom = y;
                }
            }
        }
        if (fullyEmpty) {
            return new BoundingBox(0, 0, 0, 0);
        }
        return new BoundingBox(top, left, bottom, right);
    }

    public static <T> List<T> flipY(List<T> items, int width, int height) {
        List<T> flipped = new ArrayList<>(Collections.nCopies(width * height, null));
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int i = y * width + x;
                T item = i < items.size() ? items.get(i) : null;
                flipped.set(y * width + (width - 1 - x), item);
            }
        }
        return flipped;
    }

    /**
     * Rotates a 3x3 list 45 degrees clockwise
     */
    public static <T> List<T> rotate45deg3x3(List<T> items) {
        if (items.size() < 9) {
            List<T> temp = new ArrayList<>(9);
            for (T t : items) {
                temp.add(t);
            }
            items = temp;
        }
        List<T> rotated = new ArrayList<>(Collections.nCopies(9, null));
        rotated.set(0, items.get(3));
        rotated.set(1, items.get(0));
        rotated.set(2, items.get(1));
        rotated.set(3, items.get(6));
        rotated.set(4, items.get(4));
        rotated.set(5, items.get(2));
        rotated.set(6, items.get(7));
        rotated.set(7, items.get(8));
        rotated.set(8, items.get(5));
        return rotated;
    }

    public static int hashItemsIgnoreAmount(ItemStack[] items) {
        return hashItemsIgnoreAmount(Arrays.stream(items).toList());
    }

    public static int hashItemsIgnoreAmount(@Nonnull Iterable<ItemStack> items) {
        int hash = 1;
        for (ItemStack item : items) {
            if (item == null || item.getType().isAir()) {
                hash *= 31;
                continue;
            }

            hash = hash * 31 + item.getType().hashCode();
            hash = hash * 31 + (item.hasItemMeta() ? item.getItemMeta().hashCode() : 0);
        }
        return hash;
    }

    public static Optional<Tag<Material>> tagFromString(String string) {
        if (string == null) {
            return Optional.empty();
        }
        String[] split = string.split(":");
        if (split.length != 2) {
            return Optional.empty();
        }
        return tagFromString(split[0], split[1]);
    }

    public static Optional<Tag<Material>> tagFromString(String namespace, String id) {
        if (namespace == null || id == null) {
            return Optional.empty();
        }
        if (namespace.equals("minecraft")) {
            Tag<Material> tag = Bukkit.getTag("items", NamespacedKey.minecraft(id), Material.class);
            return tag == null ? Optional.empty() : Optional.of(tag);
        } else if (namespace.equals("slimefun")) {
            SlimefunTag tag = SlimefunTag.getTag(id.toUpperCase());
            return tag == null ? Optional.empty() : Optional.of(tag);
        }
        return Optional.empty();
    }

    /**
     * Returns the character used in a recipe key for the ith input
     * @param i A number between 0 and 93 (inclusive)
     */
    public static char getKeyCharByNumber(int i) {
        if (i < 0) {
            return ' ';
        } else if (i < 9) {
            return (char) ('1' + i); // 1 - 9
        } else if (i < 35) {
            return (char) ('A' + i - 9); // A - Z
        } else if (i < 61) {
            return (char) ('a' + i - 35); // a - z
        } else if (i < 77) {
            return (char) ('!' + i - 61); // ASCII 33 - 47 (! - 0)
        } else if (i < 84) {
            return (char) (':' + i - 77); // ASCII 58 - 64 (: - @)
        } else if (i < 90) {
            return (char) ('[' + i - 84); // ASCII 91 - 96 ([ - `)
        } else if (i < 94) {
            return (char) ('{' + i - 90); // ASCII 123 - 126 ({ - ~)
        }
        return ' ';
    }
    
}
