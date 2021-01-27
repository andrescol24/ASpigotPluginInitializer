package co.andrescol.mc.library.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import co.andrescol.mc.library.configuration.ALanguage;
import co.andrescol.mc.library.utils.AUtils;

/**
 * Spigot does not support subcommand so we need to validate manually the sub
 * commands. This class simplify that. To simplify the command implementation
 * you implement the main logic of the method without other validations
 * {@link CommandExecutor#onCommand(CommandSender, Command, String, String[])}
 *
 * @author andrescol
 */
public abstract class ASubCommand implements TabCompleter, CommandExecutor {

    protected String name;
    protected String permission;

    protected ASubCommand(String name, String permission) {
        this.name = name;
        this.permission = permission;
    }

    /**
     * This method executes all command logic, it includes: permission and usage
     * verification
     *
     * @param sender  The command sender
     * @param command the command information
     * @param label   Label
     * @param args    The command's arguments
     * @return <code>true</code> if the execution is success
     */
    public boolean handle(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission(this.permission)) {
            if (this.goodUsage(sender, command, label, args)) {
                return this.onCommand(sender, command, label, args);
            }
            String message = ALanguage.getMessage("INCORRECT_USAGE_" + this.name.toUpperCase());
            AUtils.sendMessage(sender, message);
        } else {
            String message = ALanguage.getMessage("NOT_PERMISSION");
            AUtils.sendMessage(sender, message);
        }
        return true;
    }

    /**
     * This method validates if the command's usage is right. This method validate
     * number of arguments and the value of the arguments. For example if the
     * command receives world, this method validate that
     *
     * @param sender  The command sender
     * @param command the command information
     * @param label   Label
     * @param args    The command's arguments
     * @return <code>true</code> if the usage is right
     */
    public abstract boolean goodUsage(CommandSender sender, Command command, String label, String[] args);

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }
}
