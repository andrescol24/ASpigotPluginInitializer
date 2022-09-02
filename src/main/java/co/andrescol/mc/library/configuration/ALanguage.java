package co.andrescol.mc.library.configuration;

import co.andrescol.mc.library.plugin.APlugin;
import co.andrescol.mc.library.utils.AUtils;
import org.bukkit.ChatColor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Singleton that contains the language configuration. This class going to
 * access to the file <strong>lang.properties</strong> directly to read the
 * message.
 * <p>
 * Using this class you don't need to reload the language configuration
 *
 * @author andrescol24
 */

public final class ALanguage {

    private static final String LANG_FILE = "lang.properties";
    private static Properties properties = null;

    /**
     * Get a complete message replacing the parameters
     *
     * @param name         the name of the language field
     * @param replacements arguments
     * @return Message
     */
    public static String getMessage(String name, Object... replacements) {
        String message = getString(name);
        if (message != null) {
            message = AUtils.replaceValues(message, replacements);
            message = ChatColor.translateAlternateColorCodes('&', message);
            return message;
        }
        message = AUtils.replaceValues("Please check the {} language property", name);
        return message;
    }

    /**
     * Get the language property reading the file lang.properties
     *
     * @param name name of the language field in the language file
     * @return property
     */
    private static String getString(String name) {
        return properties.getProperty(name);
    }

    /**
     * Load the language file
     */
    public static void loadLanguageFile() {
        APlugin plugin = APlugin.getInstance();
        File lang = new File(plugin.getDataFolder(), LANG_FILE);
        if (!lang.exists()) {
            plugin.saveResource(LANG_FILE, false);
        }
        properties = new Properties();
        File langFile = new File(plugin.getDataFolder(), LANG_FILE);
        try (InputStream input = new FileInputStream(langFile)) {
            properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
        } catch (IOException e) {
            plugin.error("The language file could not be loaded", e);
        }
    }
}
