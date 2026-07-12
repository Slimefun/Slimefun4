package io.github.thebusybiscuit.slimefun5.core.services.localization;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.configuration.file.YamlConfiguration;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/** Per-language enchantment display names, read on the Netty thread by EnchantDisplay via {@link #name}. */
public final class EnchantTranslationService {

    // Outer: ConcurrentHashMap (no null values). Inner maps: unmodifiable, fully built before publish.
    private final Map<String, Map<String, String>> byLanguage = new ConcurrentHashMap<>();

    public void loadBundled() {
        for (Language language : Slimefun.getLocalization().getLanguages()) {
            InputStream stream = Slimefun.class.getResourceAsStream("/languages/" + language.getId() + "/enchantments.yml");
            if (stream != null) {
                load(language.getId(), stream);
            }
        }
    }

    private void load(@Nonnull String language, @Nonnull InputStream stream) {
        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));
            Map<String, String> map = new HashMap<>();
            for (String key : config.getKeys(false)) {
                String value = config.getString(key);
                if (value != null && !value.isEmpty()) {
                    map.put(key.toLowerCase(java.util.Locale.ROOT), value);
                }
            }
            byLanguage.put(language, Collections.unmodifiableMap(map));
        } catch (RuntimeException ignored) {
            // a broken enchantments.yml must not abort boot
        }
    }

    // Package-private seam for headless tests: Slimefun.getLocalization().getLanguages() is always empty
    // under the MockBukkit unit-test harness (see Slimefun#onUnitTestStart), so loadBundled() alone never
    // loads the bundled resource there.
    void loadTranslationsForTest(@Nonnull String language, @Nonnull InputStream stream) {
        load(language, stream);
    }

    /** Localized enchant name: language → English → null. Netty-safe (immutable maps). */
    @Nullable
    public String name(@Nullable String languageId, @Nonnull String enchantKey) {
        String key = enchantKey.toLowerCase(java.util.Locale.ROOT);
        if (languageId != null) {
            Map<String, String> m = byLanguage.get(languageId);
            if (m != null && m.containsKey(key)) {
                return m.get(key);
            }
        }
        Map<String, String> en = byLanguage.get("en");
        return en != null ? en.get(key) : null;
    }

    /** English keys present (for coverage: the total enchant units English defines). */
    @Nonnull
    public Set<String> englishKeys() {
        Map<String, String> en = byLanguage.get("en");
        return en != null ? en.keySet() : Collections.<String>emptySet();
    }

    /** Whether a language has a non-empty override for an English key (for coverage). */
    public boolean covers(@Nonnull String languageId, @Nonnull String enchantKey) {
        Map<String, String> m = byLanguage.get(languageId);
        return m != null && m.containsKey(enchantKey.toLowerCase(java.util.Locale.ROOT));
    }
}
