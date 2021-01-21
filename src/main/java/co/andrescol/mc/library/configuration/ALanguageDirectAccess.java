package co.andrescol.mc.library.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.bukkit.ChatColor;

import co.andrescol.mc.library.plugin.APlugin;
import co.andrescol.mc.library.utils.AUtils;

/**
 * Singleton that contains the language configuration. This class going to
 * access to the file <strong>lang.properties</strong> directly to read the
 * message.
 * 
 * Using this class you don't need to reload the language configuration
 * 
 * @author andrescol24
 */

public class ALanguageDirectAccess {

	private static final String LANG_FILE = "lang.properties";
	private APlugin plugin;
	
	/**
	 * Create the instance
	 * 
	 * @param plugin
	 */
	private ALanguageDirectAccess(APlugin plugin) {
		File lang = new File(plugin.getDataFolder(), LANG_FILE);
		if (!lang.exists()) {
			plugin.saveResource(LANG_FILE, false);
		}
		this.plugin = plugin;
	}

	/**
	 * Get a complete message replacing the parameters
	 * 
	 * @param name         the name of the languaee field
	 * @param replacements arguments
	 * @return Message
	 */
	public String getMessage(Enum<?> name, Object... replacements) {
		String message = this.getString(name);
		if (message != null) {
			message = AUtils.replaceValues(message, replacements);
			message = ChatColor.translateAlternateColorCodes('&', message);
			return message;
		}
		message = AUtils.replaceValues("Please check the {} languaje configuration", name);
		return message;
	}

	/**
	 * Get the language property reading the file lang.properties
	 * 
	 * @param name name of the language field in the language file
	 * @return property
	 */
	private String getString(Enum<?> name) {
		Properties properties = new Properties();
		File langFile = new File(plugin.getDataFolder(), LANG_FILE);
		try (InputStream input = new FileInputStream(langFile)) {
			properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
			return properties.getProperty(name.name());
		} catch (IOException e) {
			plugin.error("Can not load the languaje property: " + name, e);
		}
		return null;
	}
	
	// ========================== Singleton ======================================
	
	private static ALanguageDirectAccess instance;
	
	public static void init(APlugin plugin) {
		if(instance == null) {
			instance = new ALanguageDirectAccess(plugin);
		}
	}
	
	public static ALanguageDirectAccess getInstance() {
		if(instance != null) {
			return instance;
		}
		throw new IllegalStateException("Call ALanguaje.init(APlugin) before this method.");
	}
}
