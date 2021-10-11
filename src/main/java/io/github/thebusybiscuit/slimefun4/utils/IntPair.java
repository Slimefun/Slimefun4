package io.github.thebusybiscuit.slimefun4.utils;

import javax.annotation.Nullable;

public final class IntPair {

    private final int key;
    private final int value;

    public IntPair(int key, int value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof IntPair) {
            return key == ((IntPair) obj).key && value == ((IntPair) obj).value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + this.key;
        result = 31 * result + this.value;
        return result;
    }

    @Override
    public String toString() {
        return "IntPair{key=" + this.key + ",value=" + this.value + '}';
    }
}
