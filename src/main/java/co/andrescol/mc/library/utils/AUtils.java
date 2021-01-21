package co.andrescol.mc.library.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.andrescol.mc.library.plugin.APlugin;

public interface AUtils {

	/**
	 * Replace all {} values
	 * 
	 * @param message      message
	 * @param replacements replacements
	 * @return String values replaced
	 */
	public static String replaceValues(String message, Object... replacements) {
		for (Object replace : replacements) {
			message = message.replaceFirst("\\{\\}", String.valueOf(replace));
		}
		return message;
	}

	/**
	 * return the argument of a position
	 * 
	 * @param position position
	 * @param args     list of arguments
	 * @return Argument or null if not present
	 */
	public static String getArgument(int position, String[] args) {
		if (args != null && args.length > position) {
			return args[position];
		}
		return null;
	}

	/**
	 * Sends a message
	 * 
	 * @param destination the receiver
	 * @param msg         message
	 */
	public static void sendMessage(CommandSender destination, String msg) {
		if (destination instanceof Player) {
			destination.sendMessage(msg);
		} else {
			APlugin.getInstance().info(msg);
		}
	}
}
