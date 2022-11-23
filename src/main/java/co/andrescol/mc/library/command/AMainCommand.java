package co.andrescol.mc.library.command;

import co.andrescol.mc.library.configuration.AMessage;
import co.andrescol.mc.library.plugin.APlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * This abstracts the main command implementation. The name and the permission
 * of this command. If your command extends this class automatically will have
 * the info/help command.
 * <br/><br/>
 * To add a new command use the {@link AMainCommand#addSubCommand(ASubCommand)}
 * <br/><br/>
 * If you want implement a logic for this main sub command set the variable {@link AMainCommand#defaultCommand} to an
 * {@link ASubCommand} implementation
 *
 * @author andrescol24
 */
public abstract class AMainCommand implements TabCompleter, CommandExecutor {

    private final List<ASubCommand> subCommands = new LinkedList<>();
    protected ASubCommand defaultCommand;

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
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(Objects.requireNonNull(command.getPermission()))) {
            AMessage.sendMessage(sender, "NOT_PERMISSION");
        }
        // help command
        if (this.isHelpCommand(args)) {
            AMessage.sendMessage(sender, "COMMAND_INFO");
        } else if (this.isReloadCommand(args)) {
            if (sender.hasPermission(command.getPermission().concat(".reload"))) {
                APlugin.getInstance().reload();
                AMessage.sendMessage(sender, "CONFIGURATION_RELOAD");
            } else {
                AMessage.sendMessage(sender, "NOT_PERMISSION");
            }
        } else {
            // Sub command execution
            ASubCommand subcommand = this.getAccessibleSubCommand(args, sender);
            if (subcommand != null) {
                return subcommand.handle(sender, command, label, args);
            } else if (defaultCommand != null) {
                // main command execution
                return defaultCommand.handle(sender, command, label, args);
            } else {
                // Unknown subcommand
                AMessage.sendMessage(sender, "UNKNOWN_SUBCOMMAND", args[0]);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ASubCommand subcommand = this.getAccessibleSubCommand(args, sender);
        List<String> list = new LinkedList<>();
        if (subcommand != null) {
            return subcommand.onTabComplete(sender, command, label, args);
        } else if (args.length <= 1) {
            String name = args.length == 1 ? args[0] : "";
            list = new java.util.ArrayList<>(this.subCommands.stream()
                    .filter(x -> x.getName().startsWith(name) && sender.hasPermission(x.getPermission()))
                    .map(ASubCommand::getName).toList());
            if ("help".startsWith(name)) {
                list.add("help");
            }
            if ("reload".startsWith(name) && sender.hasPermission(Objects.requireNonNull(command.getPermission()).concat(".reload"))) {
                list.add("reload");
            }
            if (defaultCommand != null) {
                List<String> defaultImplList = defaultCommand.onTabComplete(sender, command, label, args);
                if (defaultImplList != null) list.addAll(defaultImplList);
            }
        }
        return list;
    }

    /**
     * This method find the subcommand to execute and filter by name and permission.
     *
     * @param args   arguments
     * @param sender The sender
     * @return the sub-command or null if there aren't a subcommand with that name
     */
    private ASubCommand getAccessibleSubCommand(String[] args, CommandSender sender) {
        if (args != null && args.length > 0) {
            String name = args[0].toLowerCase();
            Optional<ASubCommand> subcommand = this.subCommands.stream()
                    .filter(x -> x.getName().equals(name) && sender.hasPermission(x.getPermission()))
                    .findAny();
            if (subcommand.isPresent()) {
                return subcommand.get();
            }
        }
        return null;
    }

    /**
     * Return true if the subcommand is null or is help or info
     *
     * @param args arguments
     * @return <code>true</code> if the intention is get information about de plugin
     */
    private boolean isHelpCommand(String[] args) {
        return this.defaultCommand == null && args.length == 0 ||
                args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("info");
    }

    /**
     * Return true if the subcommand is null or is help or info
     *
     * @param args arguments
     * @return <code>true</code> if the intention is get information about de plugin
     */
    private boolean isReloadCommand(String[] args) {
        return args.length != 0 && args[0].equalsIgnoreCase("reload");
    }
}
