package io.github.thebusybiscuit.slimefun4.core.services.localization;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.core.services.github.Contributor;
import io.github.thebusybiscuit.slimefun4.core.services.github.GitHubService;

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

    private final GitHubService github;

    // We maybe should switch to a json file in our resources folder at some point.
    public Translators(@Nonnull GitHubService github) {
        this.github = github;

        // Translators - German
        addTranslator("TheBusyBiscuit", SupportedLanguage.GERMAN, false);
        addTranslator("MeerBiene", SupportedLanguage.GERMAN, true);
        addTranslator("daro2404", SupportedLanguage.GERMAN, true);

        // Translators - French
        addTranslator("JustDams", "D4ms_", SupportedLanguage.FRENCH, true);
        addTranslator("edkerforne", SupportedLanguage.FRENCH, true);
        addTranslator("tnthomastn", SupportedLanguage.FRENCH, true);
        addTranslator("Noumaa", SupportedLanguage.FRENCH, true);
        addTranslator("ishi-sama", SupportedLanguage.FRENCH, true);
        addTranslator("amarcais53", SupportedLanguage.FRENCH, true);
        addTranslator("NinoFutur", SupportedLanguage.FRENCH, true);

        // Translators - Italian
        addTranslator("xXDOTTORXx", SupportedLanguage.ITALIAN, true);
        addTranslator("Sfiguz7", SupportedLanguage.ITALIAN, false);
        addTranslator("ThatsCube", SupportedLanguage.ITALIAN, true);
        addTranslator("alessandrobasi", SupportedLanguage.ITALIAN, true);
        addTranslator("dracrus", SupportedLanguage.ITALIAN, true);
        addTranslator("prolletto64", SupportedLanguage.ITALIAN, true);

        // Translators - Latvian
        addTranslator("AgnisT", "NIKNAIZ", SupportedLanguage.LATVIAN, true);

        // Translators - Hungarian
        addTranslator("andris155", SupportedLanguage.HUNGARIAN, true);

        // Translators - Vietnamese
        addTranslator("HSGamer", SupportedLanguage.VIETNAMESE, false);
        addTranslator("JustMangoT", "JFF_JustMango", SupportedLanguage.VIETNAMESE, true);
        addTranslator("Julie-Sigtuna", SupportedLanguage.VIETNAMESE, true);
        addTranslator("nahkd123", SupportedLanguage.VIETNAMESE, true);
        addTranslator("JustAPieOP", SupportedLanguage.VIETNAMESE, true);
        addTranslator("that4life", SupportedLanguage.VIETNAMESE, true);

        // Translators - Slovak
        addTranslator("KillerXCoder", SupportedLanguage.SLOVAK, true);
        addTranslator("PixelHotDog", SupportedLanguage.SLOVAK, true);

        // Translators - Czech
        addTranslator("Nekomitsuki", SupportedLanguage.CZECH, true);
        addTranslator("GGGEDR", SupportedLanguage.CZECH, true);
        addTranslator("jakmanda05", SupportedLanguage.CZECH, true);
        addTranslator("Aile-Minicraftcz", SupportedLanguage.CZECH, true);
        addTranslator("Tengoblinekcz", SupportedLanguage.CZECH, true);
        addTranslator("sirhCCC", SupportedLanguage.CZECH, true);
        addTranslator("Thezerix", SupportedLanguage.CZECH, true);
        addTranslator("IsLineCZ", SupportedLanguage.CZECH, true);
        addTranslator("MrFriggo", SupportedLanguage.CZECH, true);
        addTranslator("100petr", SupportedLanguage.CZECH, true);
        addTranslator("frfole", SupportedLanguage.CZECH, true);

        // Translators - Russian
        addTranslator("SoSeDiK", SupportedLanguage.RUSSIAN, false);
        addTranslator("KostaTV", SupportedLanguage.RUSSIAN, true);
        addTranslator("TomWiskis", "MrWiskis", SupportedLanguage.RUSSIAN, true);
        addTranslator("cyb3rm4n", "GP_CyberMan", SupportedLanguage.RUSSIAN, true);

        // Translators - Ukrainian
        addTranslator("SoSeDiK", SupportedLanguage.UKRAINIAN, false);

        // Translators - Spanish
        addTranslator("Luu7", "_Luu", SupportedLanguage.SPANISH, true);
        addTranslator("Vravinite", SupportedLanguage.SPANISH, true);
        addTranslator("NotUmBr4", SupportedLanguage.SPANISH, true);
        addTranslator("dbzjjoe", SupportedLanguage.SPANISH, true);

        // Translators - Swedish
        addTranslator("NihilistBrew", "ma1yang2", SupportedLanguage.SWEDISH, false);
        addTranslator("Tra-sh", "TurretTrash", SupportedLanguage.SWEDISH, true);

        // Translators - Dutch
        addTranslator("Dr4gonD", "DragonD", SupportedLanguage.DUTCH, true);
        addTranslator("svr333", SupportedLanguage.DUTCH, false);
        addTranslator("PabloMarcendo", SupportedLanguage.DUTCH, true);
        addTranslator("milvantiou", SupportedLanguage.DUTCH, true);
        addTranslator("Sven313D", SupportedLanguage.DUTCH, true);
        addTranslator("TypischTeun", SupportedLanguage.DUTCH, true);

        // Translators - Danish
        addTranslator("Mini-kun", SupportedLanguage.DANISH, true);

        // Translators - Polish
        addTranslator("ascpixel", "kbartek_", SupportedLanguage.POLISH, true);
        addTranslator("Xylitus", SupportedLanguage.POLISH, true);
        addTranslator("Wirusiu", SupportedLanguage.POLISH, true);
        addTranslator("koloksk", SupportedLanguage.POLISH, true);

        // Translators - Chinese (China)
        addTranslator("StarWishsama", "StarWish_Sama", SupportedLanguage.CHINESE_CHINA, false);
        addTranslator("Rothes", SupportedLanguage.CHINESE_CHINA, true);
        addTranslator("Chihsiao", SupportedLanguage.CHINESE_CHINA, true);

        // Translators - Chinese (Taiwan)
        addTranslator("BrineYT", "HeroBrineKing", SupportedLanguage.CHINESE_TAIWAN, true);
        addTranslator("mio9", SupportedLanguage.CHINESE_TAIWAN, true);

        // Translators - Arabic
        addTranslator("mohkamfer", "AgentBabbie", SupportedLanguage.ARABIC, false);

        // Translators - Hebrew
        addTranslator("dhtdht020", SupportedLanguage.HEBREW, false);
        addTranslator("Eylonnn", SupportedLanguage.HEBREW, false);

        // Translators - Japanese
        addTranslator("bito-blosh", "Bloshop", SupportedLanguage.JAPANESE, false);

        // Translators - Korean
        addTranslator("kwonms871", SupportedLanguage.KOREAN, true);
        addTranslator("yumjunstar", SupportedLanguage.KOREAN, true);
        addTranslator("BlWon", SupportedLanguage.KOREAN, true);
        addTranslator("20181241", SupportedLanguage.KOREAN, true);
        addTranslator("kudansul", SupportedLanguage.KOREAN, true);

        // Translators - Indonesian
        addTranslator("diradho", SupportedLanguage.INDONESIAN, false);
        addTranslator("Frozenkamui", SupportedLanguage.INDONESIAN, false);
        addTranslator("aril3721", SupportedLanguage.INDONESIAN, false);
        addTranslator("JunederZ", SupportedLanguage.INDONESIAN, false);
        addTranslator("EnderWingZ", SupportedLanguage.INDONESIAN, false);

        // Translators - Thai
        addTranslator("phoomin2012", SupportedLanguage.THAI, false);
        addTranslator("film2860", SupportedLanguage.THAI, false);
        addTranslator("Rafrael17k", SupportedLanguage.THAI, false);
        addTranslator("Cupjok", SupportedLanguage.THAI, false);

        // Translators - Turkish
        addTranslator("Yunuskrn", SupportedLanguage.TURKISH, true);
        addTranslator("LinoxGH", "ajan_12", SupportedLanguage.TURKISH, false);

        // Translators - Macedonian
        addTranslator("TheSilentPro", SupportedLanguage.MACEDONIAN, true);

        // Translators - Bulgarian
        addTranslator("DNBGlol", SupportedLanguage.BULGARIAN, true);

        // Translators - Tagalog
        addTranslator("sccooottttie", SupportedLanguage.TAGALOG, true);

        // Translators - Portuguese (Brazil)
        addTranslator("G4stavoM1ster", SupportedLanguage.PORTUGUESE_BRAZIL, true);
        addTranslator("yurinogueira", SupportedLanguage.PORTUGUESE_BRAZIL, true);
        addTranslator("Sakanas", SupportedLanguage.PORTUGUESE_BRAZIL, true);
        addTranslator("krazybeat", SupportedLanguage.PORTUGUESE_BRAZIL, true);
        addTranslator("FaolanMalcadh", SupportedLanguage.PORTUGUESE_BRAZIL, true);
    }

    @ParametersAreNonnullByDefault
    private void addTranslator(String name, SupportedLanguage lang, boolean lock) {
        addTranslator(name, name, lang, lock);
    }

    @ParametersAreNonnullByDefault
    private void addTranslator(String username, String minecraftName, SupportedLanguage lang, boolean lock) {
        Contributor contributor = github.addContributor(minecraftName, "https://github.com/" + username, "translator," + lang.getLanguageId(), 0);

        if (lock) {
            contributor.lock();
        }
    }

}
