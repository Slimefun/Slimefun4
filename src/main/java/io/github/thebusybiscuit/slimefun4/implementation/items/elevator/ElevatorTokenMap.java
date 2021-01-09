package io.github.thebusybiscuit.slimefun4.implementation.items.elevator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.block.Block;

class ElevatorTokenMap {

    /**
     * This is the length for our tokens.
     */
    private static final int TOKEN_LENGTH = 10;

    /**
     * This is our internal lookup table for tokens and their corresponding
     * {@link ElevatorFloor}.
     */
    private final Map<String, ElevatorFloor> floors = new HashMap<>();

    @Nullable
    public ElevatorFloor get(@Nonnull String token) {
        Validate.notNull(token, "Token cannot be null");

        return floors.get(token);
    }

    @Nonnull
    public String add(@Nonnull String name, @Nonnull Block block) {
        Validate.notNull(name, "Name cannot be null");
        Validate.notNull(block, "Block cannot be null");

        String token = nextToken();
        floors.put(token, new ElevatorFloor(name, block));
        return token;
    }

    @Nonnull
    public String add(@Nonnull ElevatorFloor floor) {
        Validate.notNull(floor, "An Elevator Floor cannot be null");

        String token = nextToken();
        floors.put(token, floor);
        return token;
    }

    /**
     * This will clear all floors and tokens from this {@link ElevatorTokenMap}.
     */
    public void clear() {
        floors.clear();
    }

    /**
     * This generates a random token for a {@link ElevatorFloor}.
     * 
     * @return A randomized {@link String} token
     */
    @Nonnull
    private String nextToken() {
        char start = 'a';
        char end = 'z';

        // @formatter:off
        return ThreadLocalRandom.current().ints(TOKEN_LENGTH, start, end)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        // @formatter:on
    }

}
