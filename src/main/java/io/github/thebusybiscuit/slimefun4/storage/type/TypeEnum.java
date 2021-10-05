package io.github.thebusybiscuit.slimefun4.storage.type;

/**
 * Order is important in here. Do not change it!
 * This enum represents all the supported types
 */
public enum TypeEnum {

    BYTE((byte) 0),
    SHORT((byte) 1),
    INT((byte) 2),
    DOUBLE((byte) 3),
    FLOAT((byte) 4),
    LONG((byte) 5),
    BOOLEAN((byte) 6),
    STRING((byte) 7),
    BYTE_ARRAY((byte) 8),
    SHORT_ARRAY((byte) 9),
    INT_ARRAY((byte) 10),
    DOUBLE_ARRAY((byte) 11),
    FLOAT_ARRAY((byte) 12),
    LONG_ARRAY((byte) 13),
    STRING_ARRAY((byte) 14),
    OBJECT((byte) 15);

    private final byte id;

    TypeEnum(byte id) {
        this.id = id;
    }

    public byte getId() {
        return id;
    }
}
