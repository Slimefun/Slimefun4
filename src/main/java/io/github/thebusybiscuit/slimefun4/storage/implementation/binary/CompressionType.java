package io.github.thebusybiscuit.slimefun4.storage.implementation.binary;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public enum CompressionType {

    /**
     * No compression, it will be saved with absolutely no compression done. This should only be used for testing.
     */
    NONE,
    /**
     * Use gzip for compression, very solid method and great if you don't want to package zstd.
     */
    GZIP,
    /**
     * <a href="https://github.com/facebook/zstd">Zstandard</a> provides a fantastic compression ratio while maintaing
     * a very quick speed. This is the preferred method.
     */
    ZSTD;

    public static CompressionType getType(@Nonnull File file) {
        if (!file.exists())
            return CompressionType.NONE;

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] signature = new byte[4];
            int read = fis.read(signature);
            if (read != 4)
                return NONE;
            else {
                if (signature[0] == (byte) 0x1f && signature[1] == (byte) 0x8b) {
                    return GZIP;
                } else if (signature[0] == (byte) 0xFD && signature[1] == (byte) 0x2F
                    && signature[2] == (byte) 0xB5 && signature[3] == (byte) 0x28
                ) {
                    return ZSTD;
                }
            }
            return NONE;
        } catch (IOException ignored) {
            return null;
        }
    }
}
