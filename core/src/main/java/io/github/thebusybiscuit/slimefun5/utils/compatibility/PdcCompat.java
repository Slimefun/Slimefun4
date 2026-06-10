package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;

/**
 * Java-8 universal port: the persistent-data API (PDC) is 1.14+, so {@link PersistentDataHolder} is not
 * implemented by {@code ItemMeta}/{@code Player}/{@code TileState}/etc. at the 1.8.8 compile floor.
 * <p>
 * These helpers perform the cast in one place. At runtime on a modern server the holder really does
 * implement the (real) {@link PersistentDataHolder}, so the cast succeeds; on a legacy server the PDC
 * code paths are never reached. Use {@link #container(Object)} to read a holder's container and
 * {@link #holder(Object)} to pass a holder to an API that expects a {@link PersistentDataHolder}.
 */
public final class PdcCompat {

    private PdcCompat() {}

    public static PersistentDataHolder holder(Object holder) {
        return (PersistentDataHolder) holder;
    }

    public static PersistentDataContainer container(Object holder) {
        return ((PersistentDataHolder) holder).getPersistentDataContainer();
    }
}
