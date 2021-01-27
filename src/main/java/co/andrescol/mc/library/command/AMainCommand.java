package co.andrescol.mc.library.command;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import co.andrescol.mc.library.plugin.APlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import co.andrescol.mc.library.configuration.ALanguage;
import co.andrescol.mc.library.utils.AUtils;

/**
 * This abstracts the main command implementation. The name and the permission
 * of this command. If your command extends this class automatically will have
 * the info/help command
 *
 * @author andrescol24
 */
public abstract class AMainCommand implements TabCompleter, CommandExecutor {

    private final List<ASubCommand> subCommands = new LinkedList<>();

    /**
     * This method executes all command logic, it includes: permission and usage
     * verification. You have to configure the main command with its permission in
     * the plugin.yml file
     *
     * @param sender  The command sender
     * @param command the command information
     * @param label   Label
     * @param args    The command's arguments
     * @return <code>true</code> if the execution is success
     */
    public boolean handle(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission(command.getPermission())) {
            // info command
            if (this.isHelpCommand(args)) {
                AUtils.sendMessage(sender, this.getInfoMessage());
            } else if(this.isReloadCommand(args)) {
                if(sender.hasPermission(command.getPermission().concat(".reload"))) {
                    APlugin.getInstance().reload();
                    APlugin.getInstance().info("Configuration reloaded");
                }
            } else {
                // Sub command execution
                ASubCommand subcommand = this.getSubCommand(args);
                if (subcommand != null) {
                    return subcommand.handle(sender, command, label, args);
                } else {
                    // Unknown subcommand
                    String message = ALanguage.getMessage("UNKNOWN_SUBCOMMAND", args[0]);
                    AUtils.sendMessage(sender, message);
                }
            }
        } else {
            String message = ALanguage.getMessage("NOT_PERMISSION");
            AUtils.sendMessage(sender, message);
        }
        return true;
    }

    /**
     * Complete the tap for the command
     *
     * @param sender  Sender
     * @param command command
     * @param label   Label
     * @param args    arguments
     * @return list of options
     */
    protected List<String> completeTab(CommandSender sender, Command command, String label, String[] args) {
        List<String> list = new LinkedList<>();
        ASubCommand subcommand = this.getSubCommand(args);
        if (subcommand != null) {
            return subcommand.onTabComplete(sender, command, label, args);
        } else if(args.length <= 1) {
            String name = args.length == 1 ? args[0] : "";
            this.subCommands.forEach(x -> {
                if (x.getName().startsWith(name)) {
                    list.add(x.getName());
                }
            });
            if ("help".startsWith(name)) {
                list.add("help");
            }
            if ("reload".startsWith(name)) {
                list.add("reload");
            }
        }
        return list;
    }

    /**
     * This method find the subcommand to execute.
     *
     * @param args arguments
     * @return the sub-command or null if there aren't a subcommand with that name
     */
    protected ASubCommand getSubCommand(String[] args) {
        if (args != null && args.length > 0) {
            String name = args[0].toLowerCase();
            Optional<ASubCommand> subcommand = this.subCommands.stream().filter(x -> x.getName().equals(name))
                    .findAny();
            if (subcommand.isPresent()) {
                return subcommand.get();
            }
        }
        return null;
    }

    /**
     * @return the information message configured in the lang.properties file
     */
    protected String getInfoMessage() {
        return ALanguage.getMessage("COMMAND_INFO");
    }

    /**
     * Return true if the subcommand is null or is help or info
     *
     * @param args arguments
     * @return <code>true</code> if the intention is get information about de plugin
     */
    protected boolean isHelpCommand(String[] args) {
        return args.length == 0 || args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("info");
    }

    /**
     * Return true if the subcommand is null or is help or info
     *
     * @param args arguments
     * @return <code>true</code> if the intention is get information about de plugin
     */
    protected boolean isReloadCommand(String[] args) {
        return args.length == 0 || args[0].equalsIgnoreCase("reload");
    }

    /**
     * Add a subcommand if a command with the same name has not added
     *
     * @param command a new command
     */
    protected void addSubCommand(ASubCommand command) {
        Optional<ASubCommand> subcommand = this.subCommands.stream().filter(x -> x.getName().equals(command.getName()))
                .findAny();
        if (subcommand.isEmpty()) {
            this.subCommands.add(command);
        }
    }
}
