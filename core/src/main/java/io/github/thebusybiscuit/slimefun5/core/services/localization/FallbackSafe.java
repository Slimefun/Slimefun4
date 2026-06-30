package io.github.thebusybiscuit.slimefun5.core.services.localization;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Item ids and message keys that are deliberately shown in English in every language — proper nouns,
 * brand names, universal terms (e.g. "ONLINE", a material called "Adamantite"). The translation
 * coverage UI counts these as covered, so a language can reach 100% without inventing fake
 * translations, and the untranslated dump skips them so it only lists genuine gaps.
 *
 * <p>Runtime display is unaffected: an item/key with no real translation already falls back to
 * English. Loaded once (lazily) from {@code /languages/translation-fallback.yml}.
 */
public final class FallbackSafe {

    private static Set<String> itemIds;
    private static Set<String> messageKeys;

    private FallbackSafe() {}

    @Nonnull
    public static Set<String> itemIds() {
        load();
        return itemIds;
    }

    @Nonnull
    public static Set<String> messageKeys() {
        load();
        return messageKeys;
    }

    private static synchronized void load() {
        if (itemIds != null) {
            return;
        }

        Set<String> items = new HashSet<>();
        Set<String> messages = new HashSet<>();

        try (InputStream in = FallbackSafe.class.getResourceAsStream("/languages/translation-fallback.yml")) {
            if (in != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                    YamlConfiguration cfg = new YamlConfiguration();
                    cfg.loadFromString(reader.lines().collect(Collectors.joining("\n")));
                    items.addAll(cfg.getStringList("items"));
                    messages.addAll(cfg.getStringList("messages"));
                }
            }
        } catch (Exception ignored) {
            // A missing/invalid file just means nothing is tagged; coverage behaves as before.
        }

        itemIds = Collections.unmodifiableSet(items);
        messageKeys = Collections.unmodifiableSet(messages);
    }
}
