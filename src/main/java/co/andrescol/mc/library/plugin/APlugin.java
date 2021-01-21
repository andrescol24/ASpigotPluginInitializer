package co.andrescol.mc.library.plugin;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import co.andrescol.mc.library.utils.AUtils;

/**
 * This class define and implement basic methods to initialize and configure the
 * plugin. Extend this class the lang.properties file and config.yml will
 * automatically saved.
 * 
 * This class allows to access a unique instance of the plugin through {@link APlugin#getInstance()} 
 * 
 * @author andrescol24
 *
 */
public abstract class APlugin extends JavaPlugin {

	// ========================== Statics ================================
	private static APlugin instance;

	/**
	 * Set the static and the unique instance (could exists more instances)
	 * 
	 * @param plugin First instance created
	 */
	private static void setInstance(APlugin plugin) {
		if (instance == null) {
			instance = plugin;
		} else {
			instance.warn("A new instance of this plugin was created, check externals effects!");
		}
	}

	/**
	 * Get the plugin instance
	 * 
	 * @return the plugin instance
	 */
	public static APlugin getInstance() {
		if (instance != null) {
			return instance;
		}
		throw new IllegalStateException("Make sure that the plugin has started");
	}

	// ========================== End Statics ================================

	/**
	 * Constructor that initializes the unique instance, save the config.yml and
	 * lang.properties. Also loads the language
	 */
	protected APlugin() {
		APlugin.setInstance(this);
		this.loadConfiguration();
	}

	/**
	 * Write a console message
	 * 
	 * @param message the message
	 */
	public void info(String message, Object... replacements) {
		message = AUtils.replaceValues(message, replacements);
		this.getLogger().info(message);
	}

	/**
	 * Write a console warning message
	 * 
	 * @param message Message to write
	 */
	public void warn(String message, Object... replacements) {
		message = AUtils.replaceValues(message, replacements);
		this.getLogger().warning(message);

	}

	/**
	 * Write a severe error to console
	 * 
	 * @param message   Message
	 * @param exception the throws exception
	 */
	public void error(String message, Throwable exception, Object... replacements) {
		message = AUtils.replaceValues(message, replacements);
		this.getLogger().log(Level.SEVERE, message, exception);

	}

	/**
	 * Reload the configuration
	 */
	public void reload() {
		this.reloadConfig();
		// In the moment I am using the Language direct access
	}

	/**
	 * Load and save the configuration files
	 */
	private void loadConfiguration() {
		getConfig().options().copyDefaults(true);
		File config = new File(getDataFolder(), "config.yml");
		if (!config.exists()) {
			saveDefaultConfig();
		}
	}
}
