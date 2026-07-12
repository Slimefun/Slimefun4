package io.github.thebusybiscuit.slimefun5.core.services.localization;

import java.util.function.Predicate;

/**
 * Pure resolution logic for the effective packet-translation language id. Kept in its own class, free
 * of Bukkit/Netty imports, so it loads cleanly under a plain JUnit runtime with no server platform on
 * the classpath.
 */
final class LanguageResolver {

    private LanguageResolver() {}

    /**
     * Semantics: an explicit (player-picked) language wins if it is still loaded; otherwise, in
     * {@link TranslationConfig.LanguageSource#CLIENT} mode, the client locale wins if it is loaded;
     * otherwise the server default (which may itself be null).
     */
    static String resolveLanguageId(String explicit, String clientLocale, TranslationConfig.LanguageSource source,
            Predicate<String> isLoaded, String serverDefault) {
        if (explicit != null && isLoaded.test(explicit)) {
            return explicit;
        }

        if (source == TranslationConfig.LanguageSource.CLIENT && clientLocale != null && isLoaded.test(clientLocale)) {
            return clientLocale;
        }

        return serverDefault;
    }
}
