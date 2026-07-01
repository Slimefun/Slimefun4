package io.github.thebusybiscuit.slimefun5.core.guide;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.PdcCompat;

/**
 * Per-player set of addon ids (category {@code NamespacedKey} namespaces) hidden from that player's guide.
 * Persisted via {@link PdcCompat} as a single comma-separated string, so it works on every Minecraft
 * version (PDC on 1.14+, legacy YAML fallback on 1.8-1.13). Default: empty (everything visible).
 */
public final class AddonVisibility {

    private static final NamespacedKey KEY = new NamespacedKey(Slimefun.instance(), "guide_hidden_addons");

    private AddonVisibility() {}

    @Nonnull
    public static Set<String> getHidden(@Nonnull Player p) {
        String raw = PdcCompat.getString(p, KEY);

        if (raw == null || raw.isEmpty()) {
            return new HashSet<>();
        }

        return new HashSet<>(Arrays.asList(raw.split(",")));
    }

    public static boolean isHidden(@Nonnull Player p, @Nonnull String addonId) {
        return getHidden(p).contains(addonId.toLowerCase(Locale.ROOT));
    }

    public static void setHidden(@Nonnull Player p, @Nonnull String addonId, boolean hidden) {
        Set<String> hiddenSet = getHidden(p);
        String id = addonId.toLowerCase(Locale.ROOT);

        if (hidden) {
            hiddenSet.add(id);
        } else {
            hiddenSet.remove(id);
        }

        PdcCompat.setString(p, KEY, String.join(",", hiddenSet));
    }
}
