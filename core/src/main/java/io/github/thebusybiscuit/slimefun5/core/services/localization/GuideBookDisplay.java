package io.github.thebusybiscuit.slimefun5.core.services.localization;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.ChatColor;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Per-language, immutable-after-boot renderings of the Slimefun Guide book (normal + cheat). Built once on
 * the main thread so the Netty packet path can look up a viewer's guide-book display without touching the
 * main-thread-only localization API. Key = languageId + '|' + cheat.
 */
public final class GuideBookDisplay {

    private final Map<String, ItemTranslationService.RenderedDisplay> byKey = new ConcurrentHashMap<>();

    /** Builds the cache for every loaded language. Call once at boot, on the main thread. */
    public void build() {
        buildFrom(Slimefun.getLocalization().getLanguages());
    }

    /**
     * Package-private seam for headless tests: builds the cache from an explicit language collection,
     * bypassing {@code Slimefun.getLocalization().getLanguages()} - always empty under the MockBukkit
     * unit-test harness, whose {@code LocalizationService} is constructed with no default language and
     * never loads the embedded languages (see {@code Slimefun#onUnitTestStart}).
     */
    void buildFrom(@Nonnull Iterable<Language> languages) {
        byKey.clear();
        for (Language language : languages) {
            for (boolean cheat : new boolean[] { false, true }) {
                String name = msg(language, cheat ? "guide.item.cheat-name" : "guide.item.name");
                List<String> lore = new ArrayList<>();
                lore.add(cheat ? msg(language, "guide.item.cheat-only") : "");
                lore.add(msg(language, "guide.item.browse"));
                lore.add(msg(language, "guide.item.settings"));
                byKey.put(language.getId() + '|' + cheat, new ItemTranslationService.RenderedDisplay(name, lore));
            }
        }
    }

    /** The rendered guide book for a language + cheat flag, or English, or null if nothing resolved. */
    @Nullable
    public ItemTranslationService.RenderedDisplay rendered(@Nullable String languageId, boolean cheat) {
        if (languageId != null) {
            ItemTranslationService.RenderedDisplay d = byKey.get(languageId + '|' + cheat);
            if (d != null) {
                return d;
            }
        }
        return byKey.get("en" + '|' + cheat);
    }

    @Nonnull
    private static String msg(@Nonnull Language language, @Nonnull String key) {
        String raw = Slimefun.getLocalization().getStringOrDefault(language, LanguageFile.MESSAGES, key);
        return raw == null ? "" : ChatColor.translateAlternateColorCodes('&', raw);
    }
}
