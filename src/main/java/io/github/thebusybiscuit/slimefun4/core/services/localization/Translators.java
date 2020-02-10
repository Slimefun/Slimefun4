package io.github.thebusybiscuit.slimefun4.core.services.localization;

import java.util.concurrent.ConcurrentMap;

import io.github.thebusybiscuit.slimefun4.core.services.github.Contributor;

public class Translators {
	
	private final ConcurrentMap<String, Contributor> contributors;
	
	public Translators(ConcurrentMap<String, Contributor> contributors) {
		this.contributors = contributors;
		
		// Translators - German
		addTranslator("TheBusyBiscuit", "de", false);
		addTranslator("MeerBiene", "de", true);
		addTranslator("daro2404", "de", true);
		
		// Translators - French
		addTranslator("JustDams", "D4ms_", "fr", true);
		addTranslator("edkerforne", "fr", true);
		addTranslator("tnthomastn", "fr", true);
		addTranslator("Noumaa", "fr", true);
		addTranslator("ishi-sama", "fr", true);
		
		// Translators - Italian
		addTranslator("xXDOTTORXx", "it", true);
		
		// Translators - Latvian
		addTranslator("AgnisT", "NIKNAIZ", "lv", true);
		
		// Translators - Hungarian
		addTranslator("andris155", "hu", true);
		
		// Translators - Vietnamese
		addTranslator("HSGamer", "vi", false);
		addTranslator("JustMangoT", "JFF_JustMango", "vi", true);
		addTranslator("Julie-Sigtuna", "vi", true);
		addTranslator("nahkd123", "vi", true);
		addTranslator("JustAPieOP", "vi", true);
		
		// Translators - Slovak
		addTranslator("KillerXCoder", "sk", true);
		addTranslator("PixelHotDog", "sk", true);
		
		// Translators - Czech
		addTranslator("Nekomitsuki", "cs", true);
		addTranslator("GGGEDR", "cs", true);
		addTranslator("jakmanda05", "cs", true);
		addTranslator("Aile-Minicraftcz", "cs", true);
		addTranslator("Tengoblinekcz", "cs", true);
		addTranslator("sirhCCC", "cs", true);
		addTranslator("Thezerix", "cs", true);
		
		// Translators - Russian
		addTranslator("SoSeDiK", "ru", false);
		addTranslator("KostaTV", "ru", true);
		addTranslator("TomWiskis", "ru", true);
		
		// Translators - Spanish
		addTranslator("Luu7", "_Luu", "es", true);
		addTranslator("Vravinite", "es", true);
		addTranslator("NotUmBr4", "es", true);
		addTranslator("dbzjjoe", "es", true);
		
		// Translators - Swedish
		addTranslator("NihilistBrew", "ma1yang2", "sv", false);
		addTranslator("Tra-sh", "TurretTrash", "sv", true);
		
		// Translators - Dutch
		addTranslator("Dr4gonD", "nl", true);
		addTranslator("svr333", "nl", false);
		
		// Translators - Polish
		addTranslator("kbartek05", "kbartek_", "pl", true);
		addTranslator("Xylitus", "pl", true);
		addTranslator("Wirusiu", "pl", true);
		
		// Translators - Chinese (China)
		addTranslator("StarWishsama", "StarWish_Sama", "zh-CN", false);
		
		// Translators - Chinese (Taiwan)
		addTranslator("BrineYT", "zh-TW", true);
		addTranslator("mio9", "zh-TW", true);
		
		// Translators - Arabic
		addTranslator("mohkamfer", "citBabY", "ar", false);
	}

	private void addTranslator(String name, String language, boolean lock) {
		addTranslator(name, name, language, lock);
	}

	private void addTranslator(String name, String alias, String language, boolean lock) {
		Contributor contributor = contributors.computeIfAbsent(name, user -> new Contributor(alias, "https://github.com/" + user));
		contributor.setContribution("translator," + language, 0);
		
		if (lock) contributor.lock();
	}

}
