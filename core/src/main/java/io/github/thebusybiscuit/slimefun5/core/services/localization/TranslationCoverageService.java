package io.github.thebusybiscuit.slimefun5.core.services.localization;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Combines item-unit coverage (leaf name/type/description/stats/usage units, see
 * {@link ItemTranslationService#getItemUnitCoverage}) and message-unit coverage (message/menu/enchant
 * keys, see {@link io.github.thebusybiscuit.slimefun5.core.services.LocalizationService#getMessageUnitCoverage})
 * into one honest, weighted 0-100 translation-completeness percentage per language.
 *
 * <p>This supersedes the old two separate numbers shown in the Guide: a "messages %" (key presence over
 * the 5 message-file categories) and an "items %" (mere presence of any translation entry, effectively
 * "name translated"). A language with translated names but English lore used to read ~95-100% under the
 * old "items %"; this metric counts real leaf coverage instead, so it can't be inflated that way.
 */
public class TranslationCoverageService {

    /**
     * The overall translation-completeness percentage for a language, combining item-leaf units and
     * message units (messages + menus + enchants) into a single weighted 0-100 value. English is always
     * 100.
     *
     * @param languageId the language id
     * @return a percentage {@code (0 - 100)}
     */
    public int getOverallPercent(@Nonnull String languageId) {
        if ("en".equalsIgnoreCase(languageId)) {
            return 100;
        }

        int covered = 0;
        int total = 0;

        for (int[] c : Slimefun.getItemTranslationService().getItemUnitCoverage(languageId).values()) {
            covered += c[0];
            total += c[1];
        }

        int[] msg = Slimefun.getLocalization().getMessageUnitCoverage(languageId);
        covered += msg[0];
        total += msg[1];

        return total == 0 ? 0 : (covered * 100) / total;
    }
}
