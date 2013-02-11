package tk.manf.mcbb.api.manager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import tk.manf.mcbb.util.IOTools;

//TODO: Documentation
public class LanguageManager {
	private Logger logger;
	private HashMap<String, Language> languages;

	public LanguageManager(Logger logger) {
		this.logger = logger;
		languages = new HashMap<String, LanguageManager.Language>();
	}

	public boolean contains(String locale) {
		return languages.containsKey(locale);
	}

	public String translate(final String locale, String key, final Object... args) {
		if (languages.containsKey(locale)) {
			return languages.get(locale).translate(logger, key, args);
		}
		return "";
	}

	public void addLanguage(String locale, InputStream is, File dataFolder) throws IOException {
		Language lang = null;
		lang = new Language(is, dataFolder, locale);
		if (lang != null) {
			languages.put(locale, lang);
		}
	}

	private static class Language {
		private HashMap<String, MessageFormat> cache;
		private FileConfiguration language;
		private String locale;

		private Language(InputStream is, File dataFolder, String locale) throws IOException {
			File languageFile = new File(dataFolder, locale + ".yml");
			IOTools.copyInputStreamToFile(is, languageFile);
			this.locale = locale;
			language = YamlConfiguration.loadConfiguration(languageFile);
			cache = new HashMap<String, MessageFormat>();
		}

		private String translate(final Logger logger, final String key, final Object[] args) {
			if (args.length == 0) {
				if (language.isSet(key)) {
					return language.getString(key);
				} else {
					logger.log(Level.INFO, "Key " + key + " is not found in " + locale);
					return "";
				}
			}

			MessageFormat format = cache.get(key);
			if (format == null) {
				String template = null;
				if (language.isSet(key)) {
					template = language.getString(key);
				} else {
					template = "";
					logger.log(Level.INFO, "Key " + key + " is not found in "+ locale);
				}
				format = new MessageFormat(template);
				cache.put(key, format);
			}
			return format.format(args);
		}
	}
}