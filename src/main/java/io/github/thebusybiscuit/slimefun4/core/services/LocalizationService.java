package io.github.thebusybiscuit.slimefun4.core.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.config.Localization;
import io.github.thebusybiscuit.cscorelib2.data.PersistentDataAPI;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class LocalizationService extends Localization implements Keyed {
	
	private static final String LANGUAGE_PATH = "language";

	private final Map<String, FileConfiguration> languages = new HashMap<>();
	private final SlimefunPlugin plugin;
	private final NamespacedKey languageKey;

	public LocalizationService(SlimefunPlugin plugin) {
		super(plugin);
		
		this.plugin = plugin;
		languageKey = new NamespacedKey(plugin, LANGUAGE_PATH);
		
		String selectedLanguage = SlimefunPlugin.getSelectedLanguage();
		String language = getLanguage();
		
		if (hasLanguage(selectedLanguage)) {
			setLanguage(selectedLanguage, !selectedLanguage.equals(language));
		}
		else {
			plugin.getLogger().log(Level.WARNING, "Could not recognize the given language: \"{0}\"", selectedLanguage);
		}
		
		setPrefix("&aSlimefun 4 &7> ");
		save();
	}
	
	public String getPrefix() {
		return getMessage("prefix");
	}
	
	public String getMessage(Player p, String key) {
		Optional<String> language = PersistentDataAPI.getOptionalString(p, languageKey);
		
		if (language.isPresent()) {
			FileConfiguration cfg = languages.computeIfAbsent(language.get(), this::loadLanguage);
			return cfg.getString(key);
		}
		else {
			return getMessage(key);
		}
	}
	
	public List<String> getMessages(Player p, String key) {
		Optional<String> language = PersistentDataAPI.getOptionalString(p, languageKey);
		
		if (language.isPresent()) {
			FileConfiguration cfg = languages.computeIfAbsent(language.get(), this::loadLanguage);
			return cfg.getStringList(key);
		}
		else {
			return getMessages(key);
		}
	}
	
	@Override
	public void sendMessage(CommandSender sender, String key) {
		String prefix = getPrefix();
		
		if (sender instanceof Player) {
			sender.sendMessage(ChatColors.color(prefix + getMessage((Player) sender, key)));
		}
		else {
			sender.sendMessage(ChatColor.stripColor(ChatColors.color(prefix + getMessage(key))));
		}
	}
	
	@Override
	public void sendMessage(CommandSender sender, String key, boolean addPrefix) {
		sendMessage(sender, key);
	}
	
	public void sendMessage(CommandSender sender, String key, UnaryOperator<String> function) {
		String prefix = getPrefix();
		
		if (sender instanceof Player) {
			sender.sendMessage(ChatColors.color(prefix + function.apply(getMessage((Player) sender, key))));
		}
		else {
			sender.sendMessage(ChatColor.stripColor(ChatColors.color(prefix + function.apply(getMessage(key)))));
		}
	}
	
	@Override
	public void sendMessage(CommandSender sender, String key, boolean addPrefix, UnaryOperator<String> function) {
		sendMessage(sender, key, function);
	}
	
	@Override
	public void sendMessages(CommandSender sender, String key) {
		String prefix = getPrefix();
		
		if (sender instanceof Player) {
			for (String translation : getMessages((Player) sender, key)) {
				String message = ChatColors.color(prefix + translation);
				sender.sendMessage(message);
			}
		}
		else {
			for (String translation : getMessages(key)) {
				String message = ChatColors.color(prefix + translation);
				sender.sendMessage(ChatColor.stripColor(message));
			}
		}
	}
	
	@Override
	public void sendMessages(CommandSender sender, String key, boolean addPrefix, UnaryOperator<String> function) {
		sendMessages(sender, key, function);
	}
	
	public void sendMessages(CommandSender sender, String key, UnaryOperator<String> function) {
		String prefix = getPrefix();
		
		if (sender instanceof Player) {
			for (String translation : getMessages((Player) sender, key)) {
				String message = ChatColors.color(prefix + function.apply(translation));
				sender.sendMessage(message);
			}
		}
		else {
			for (String translation : getMessages(key)) {
				String message = ChatColors.color(prefix + function.apply(translation));
				sender.sendMessage(ChatColor.stripColor(message));
			}
		}
	}
	
	private String getLanguage() {
		String language = getConfig().getString(LANGUAGE_PATH);
		return language == null ? "en": language;
	}
	
	private void setLanguage(String language, boolean reset) {
		
		if (reset) {
			for (String key : getConfig().getKeys()) {
				getConfig().setValue(key, null);
			}
		}
		
		Slimefun.getLogger().log(Level.INFO, "Loading language \"{0}\"", language);
		getConfig().setValue(LANGUAGE_PATH, language);
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(plugin.getClass().getResourceAsStream("/languages/messages_" + language + ".yml")))) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(reader);
			getConfig().getConfiguration().setDefaults(config);
        } catch (IOException e) {
            Slimefun.getLogger().log(Level.SEVERE, "Failed to load language file: \"messages_" + language + ".yml\"", e);
        }
		
		save();
	}
	
	private FileConfiguration loadLanguage(String id) {
		if (!hasLanguage(id)) {
			return getConfig().getConfiguration();
		}
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(plugin.getClass().getResourceAsStream("/languages/messages_" + id + ".yml")))) {
			return YamlConfiguration.loadConfiguration(reader);
        } catch (IOException e) {
            Slimefun.getLogger().log(Level.SEVERE, "Failed to load language file into memory: \"messages_" + id + ".yml\"", e);
			return getConfig().getConfiguration();
        }
	}
	
	public boolean hasLanguage(String language) {
		return plugin.getClass().getResource("/languages/messages_" + language + ".yml") != null;
	}

	@Override
	public NamespacedKey getKey() {
		return languageKey;
	}
}
