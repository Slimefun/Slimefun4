package io.github.thebusybiscuit.slimefun5.core.services.localization;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/** Reads the {@code translation.*} config that governs packet-based item translation. */
public final class TranslationConfig {

    private TranslationConfig() {}

    public enum FallbackMode {
        ENGLISH,
        ID;

        public static FallbackMode of(String raw) {
            return raw != null && raw.trim().equalsIgnoreCase("id") ? ID : ENGLISH;
        }
    }

    public enum LanguageSource {
        SERVER,
        CLIENT;

        public static LanguageSource of(String raw) {
            return raw != null && raw.trim().equalsIgnoreCase("server") ? SERVER : CLIENT;
        }
    }

    public static boolean packetsEnabled() {
        return !Slimefun.getCfg().contains("translation.packets") || Slimefun.getCfg().getBoolean("translation.packets");
    }

    public static FallbackMode fallback() {
        return FallbackMode.of(Slimefun.getCfg().getString("translation.fallback"));
    }

    public static LanguageSource languageSource() {
        return LanguageSource.of(Slimefun.getCfg().getString("translation.language-source"));
    }
}
