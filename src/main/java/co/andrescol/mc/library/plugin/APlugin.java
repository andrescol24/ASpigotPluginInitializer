package co.andrescol.mc.library.plugin;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import co.andrescol.mc.library.configuration.ALanguageDirectAccess;
import co.andrescol.mc.library.utils.AUtils;

/**
 * This class define and implement basic methods to initialize and configure the
 * plugin
 * 
 * @author andrescol24
 *
 */
public abstract class APlugin extends JavaPlugin {

	protected APlugin() {
		this.loadConfiguration();
		ALanguageDirectAccess.init(this);
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
