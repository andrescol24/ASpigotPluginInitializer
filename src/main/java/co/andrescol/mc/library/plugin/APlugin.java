package co.andrescol.mc.library.plugin;

import co.andrescol.mc.library.configuration.AConfigurationObject;
import co.andrescol.mc.library.configuration.AMessage;
import co.andrescol.mc.library.utils.AUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

/**
 * This class define and implement basic methods to initialize and configure the
 * plugin. Extend this class the lang.properties file and config.yml will
 * automatically save.
 * This class allows to access a unique instance of the plugin through {@link APlugin#getInstance()}
 *
 * @author andrescol24
 */
public abstract class APlugin<C extends AConfigurationObject> extends JavaPlugin {

	protected C configurationObject;

	/**
	 * Constructor that initializes the unique instance, save the config.yml and
	 * lang.properties. Also loads the language
	 */
	protected APlugin() {
		APlugin.setInstance(this);
		getConfig().options().copyDefaults(true);
		File config = new File(getDataFolder(), "config.yml");
		if (!config.exists()) {
			saveDefaultConfig();
		}
		this.initializeCustomConfiguration();
		AMessage.loadLanguageFile();
		this.configurationObject.setValues();
	}

	/**
	 * Should set the instances of {@link APlugin#configurationObject}
	 * just like this ex: this.configuration = new CustomConfiguration();
	 */
	protected abstract void initializeCustomConfiguration();

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
		AMessage.loadLanguageFile();
		this.configurationObject.setValues();

		this.onDisable();
		this.onEnable();
	}

	// ========================== Statics ================================
	private static APlugin<?> instance;

	/**
	 * Set the static and the unique instance (could exists more instances)
	 *
	 * @param plugin First instance created
	 */
	private static <C extends AConfigurationObject> void setInstance(APlugin<C> plugin) {
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
	public static <T extends APlugin<?>> T getInstance() {
		if (instance != null) {
			return (T) instance;
		}
		throw new IllegalStateException("Make sure that the plugin has started");
	}

	/**
	 * Get the plugin instance
	 *
	 * @return the plugin instance
	 */
	public static <C extends AConfigurationObject> C getConfigurationObject() {
		if (instance != null) {
			return (C) instance.configurationObject;
		}
		throw new IllegalStateException("Make sure that the plugin has started");
	}

	// ========================== End Statics ================================
}
