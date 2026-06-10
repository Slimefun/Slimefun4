package io.github.thebusybiscuit.slimefun5.libraries.keys;

/**
 * Java-8 universal port: an own, relocated stand-in for {@code org.bukkit.Keyed} (MC 1.12+).
 * <p>
 * See {@link NamespacedKey}. Many always-loaded Slimefun types {@code implements Keyed}; declaring the
 * real {@code org.bukkit.Keyed} would make those classes fail to load on 1.8&ndash;1.11 servers (the
 * interface is absent there and Bukkit refuses to load plugin-shipped {@code org.bukkit.*} classes).
 * This own interface lives in Slimefun's package, is bundled, and loads on every version.
 */
public interface Keyed {

    NamespacedKey getKey();
}
