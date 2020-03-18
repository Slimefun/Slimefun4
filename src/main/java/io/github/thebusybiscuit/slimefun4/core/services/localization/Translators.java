package io.github.thebusybiscuit.slimefun4.core.services.localization;

import java.util.concurrent.ConcurrentMap;

import io.github.thebusybiscuit.slimefun4.core.services.github.Contributor;

/**
 * This class holds all {@link Translators} of this project.
 * A translator is equivalent to the class {@link Contributor} as it also uses that internally.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Contributor
 *
 */
public class Translators {

    private final ConcurrentMap<String, Contributor> contributors;

    // We maybe should switch to a json file in our resources folder at some point.
    public Translators(ConcurrentMap<String, Contributor> contributors) {
        this.contributors = contributors;

        // Translators - German
        addTranslator("TheBusyBiscuit", EmbeddedLanguage.GERMAN, false);
        addTranslator("MeerBiene", EmbeddedLanguage.GERMAN, true);
        addTranslator("daro2404", EmbeddedLanguage.GERMAN, true);

        // Translators - French
        addTranslator("JustDams", "D4ms_", EmbeddedLanguage.FRENCH, true);
        addTranslator("edkerforne", EmbeddedLanguage.FRENCH, true);
        addTranslator("tnthomastn", EmbeddedLanguage.FRENCH, true);
        addTranslator("Noumaa", EmbeddedLanguage.FRENCH, true);
        addTranslator("ishi-sama", EmbeddedLanguage.FRENCH, true);

        // Translators - Italian
        addTranslator("xXDOTTORXx", EmbeddedLanguage.ITALIAN, true);
        addTranslator("Sfiguz7", EmbeddedLanguage.ITALIAN, false);

        // Translators - Latvian
        addTranslator("AgnisT", "NIKNAIZ", EmbeddedLanguage.LATVIAN, true);

        // Translators - Hungarian
        addTranslator("andris155", EmbeddedLanguage.HUNGARIAN, true);

        // Translators - Vietnamese
        addTranslator("HSGamer", EmbeddedLanguage.VIETNAMESE, false);
        addTranslator("JustMangoT", "JFF_JustMango", EmbeddedLanguage.VIETNAMESE, true);
        addTranslator("Julie-Sigtuna", EmbeddedLanguage.VIETNAMESE, true);
        addTranslator("nahkd123", EmbeddedLanguage.VIETNAMESE, true);
        addTranslator("JustAPieOP", EmbeddedLanguage.VIETNAMESE, true);
        addTranslator("that4life", EmbeddedLanguage.VIETNAMESE, true);

        // Translators - Slovak
        addTranslator("KillerXCoder", EmbeddedLanguage.SLOVAK, true);
        addTranslator("PixelHotDog", EmbeddedLanguage.SLOVAK, true);

        // Translators - Czech
        addTranslator("Nekomitsuki", EmbeddedLanguage.CZECH, true);
        addTranslator("GGGEDR", EmbeddedLanguage.CZECH, true);
        addTranslator("jakmanda05", EmbeddedLanguage.CZECH, true);
        addTranslator("Aile-Minicraftcz", EmbeddedLanguage.CZECH, true);
        addTranslator("Tengoblinekcz", EmbeddedLanguage.CZECH, true);
        addTranslator("sirhCCC", EmbeddedLanguage.CZECH, true);
        addTranslator("Thezerix", EmbeddedLanguage.CZECH, true);
        addTranslator("IsLineCZ", EmbeddedLanguage.CZECH, true);
        addTranslator("MrFriggo", EmbeddedLanguage.CZECH, true);

        // Translators - Russian
        addTranslator("SoSeDiK", EmbeddedLanguage.RUSSIAN, false);
        addTranslator("KostaTV", EmbeddedLanguage.RUSSIAN, true);
        addTranslator("TomWiskis", "MrWiskis", EmbeddedLanguage.RUSSIAN, true);

        // Translators - Spanish
        addTranslator("Luu7", "_Luu", EmbeddedLanguage.SPANISH, true);
        addTranslator("Vravinite", EmbeddedLanguage.SPANISH, true);
        addTranslator("NotUmBr4", EmbeddedLanguage.SPANISH, true);
        addTranslator("dbzjjoe", EmbeddedLanguage.SPANISH, true);

        // Translators - Swedish
        addTranslator("NihilistBrew", "ma1yang2", EmbeddedLanguage.SWEDISH, false);
        addTranslator("Tra-sh", "TurretTrash", EmbeddedLanguage.SWEDISH, true);

        // Translators - Dutch
        addTranslator("Dr4gonD", "DragonD", EmbeddedLanguage.DUTCH, true);
        addTranslator("svr333", EmbeddedLanguage.DUTCH, false);
        addTranslator("PabloMarcendo", EmbeddedLanguage.DUTCH, true);

        // Translators - Polish
        addTranslator("kbartek05", "kbartek_", EmbeddedLanguage.POLISH, true);
        addTranslator("Xylitus", EmbeddedLanguage.POLISH, true);
        addTranslator("Wirusiu", EmbeddedLanguage.POLISH, true);
        addTranslator("koloksk", EmbeddedLanguage.POLISH, true);

        // Translators - Chinese (China)
        addTranslator("StarWishsama", "StarWish_Sama", EmbeddedLanguage.CHINESE_CHINA, false);

        // Translators - Chinese (Taiwan)
        addTranslator("BrineYT", "HeroBrineKing", EmbeddedLanguage.CHINESE_TAIWAN, true);
        addTranslator("mio9", EmbeddedLanguage.CHINESE_TAIWAN, true);

        // Translators - Arabic
        addTranslator("mohkamfer", "citBabY", EmbeddedLanguage.ARABIC, false);

        // Translators - Japanese
        addTranslator("bito-blosh", "Bloshop", EmbeddedLanguage.JAPANESE, true);

        // Translators - Turkish
        addTranslator("Yunuskrn", EmbeddedLanguage.TURKISH, true);
        addTranslator("LinoxGH", "ajan_12", EmbeddedLanguage.TURKISH, false);

        // Translators - Portuguese (Brazil)
        addTranslator("G4stavoM1ster", EmbeddedLanguage.PORTUGUESE_BRAZIL, true);
        addTranslator("yurinogueira", EmbeddedLanguage.PORTUGUESE_BRAZIL, true);
        addTranslator("Sakanas", EmbeddedLanguage.PORTUGUESE_BRAZIL, true);
    }

    private void addTranslator(String name, EmbeddedLanguage lang, boolean lock) {
        addTranslator(name, name, lang, lock);
    }

    private void addTranslator(String name, String alias, EmbeddedLanguage lang, boolean lock) {
        Contributor contributor = contributors.computeIfAbsent(name, user -> new Contributor(alias, "https://github.com/" + user));
        contributor.setContribution("translator," + lang.getId(), 0);

        if (lock) {
            contributor.lock();
        }
    }

}
