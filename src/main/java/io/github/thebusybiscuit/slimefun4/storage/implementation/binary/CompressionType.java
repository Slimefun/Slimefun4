package io.github.thebusybiscuit.slimefun4.storage.implementation.binary;

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
}
