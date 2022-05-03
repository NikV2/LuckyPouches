package me.nik.luckypouches.commands;

import me.nik.luckypouches.LuckyPouches;
import me.nik.luckypouches.commands.subcommands.CreateCommand;
import me.nik.luckypouches.commands.subcommands.GiveCommand;
import me.nik.luckypouches.commands.subcommands.ListCommand;
import me.nik.luckypouches.commands.subcommands.MenuCommand;
import me.nik.luckypouches.commands.subcommands.ReloadCommand;
import me.nik.luckypouches.managers.MsgType;
import me.nik.luckypouches.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements TabExecutor {

    private static final String INFO_MESSAGE = MsgType.PREFIX.getMessage() + ChatUtils.format(
            "&fThis server is running &b"
                    + LuckyPouches.getInstance().getDescription().getName()
                    + " &fversion &bv" + LuckyPouches.getInstance().getDescription().getVersion()
                    + " &fby &fNik"
    );
    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public CommandManager(LuckyPouches plugin) {
        subCommands.add(new ReloadCommand(plugin));
        subCommands.add(new ListCommand(plugin));
        subCommands.add(new GiveCommand(plugin));
        subCommands.add(new MenuCommand(plugin));
        subCommands.add(new CreateCommand(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {

            for (int i = 0; i < getSubcommands().size(); i++) {

                final SubCommand subCommand = getSubcommands().get(i);

                if (args[0].equalsIgnoreCase(subCommand.getName())) {

                    if (!subCommand.canConsoleExecute() && sender instanceof ConsoleCommandSender) {

                        sender.sendMessage(MsgType.CONSOLE_COMMANDS.getMessage());

                        return true;
                    }

                    if (!sender.hasPermission(subCommand.getPermission())) {

                        sender.sendMessage(MsgType.NO_PERMISSION.getMessage());

                        return true;
                    }

                    if (args.length < subCommand.maxArguments()) {

                        helpMessage(sender);

                        return true;
                    }

                    subCommand.perform(sender, args);

                    return true;
                }

                if (args[0].equalsIgnoreCase("help")) {

                    helpMessage(sender);

                    return true;
                }
            }

        } else {

            sender.sendMessage(INFO_MESSAGE);

            return true;
        }

        helpMessage(sender);

        return true;
    }

    public ArrayList<SubCommand> getSubcommands() {
        return subCommands;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {

            ArrayList<String> subcommandsArgs = new ArrayList<>();

            for (int i = 0; i < getSubcommands().size(); i++) {
                subcommandsArgs.add(getSubcommands().get(i).getName());
            }

            return subcommandsArgs;

        } else if (args.length >= 2) {

            for (int i = 0; i < getSubcommands().size(); i++) {

                if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                    return getSubcommands().get(i).getSubcommandArguments(sender, args);
                }
            }
        }

        return null;
    }

    private void helpMessage(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(MsgType.PREFIX.getMessage() + ChatColor.DARK_AQUA + "Available Commands");
        sender.sendMessage("");
        this.subCommands.stream().filter(subCommand -> sender.hasPermission(subCommand.getPermission()))
                .forEach(subCommand ->
                        sender.sendMessage(ChatColor.DARK_AQUA
                                + subCommand.getSyntax()
                                + ChatColor.GRAY + " - "
                                + ChatColor.DARK_AQUA + subCommand.getDescription()));
        sender.sendMessage("");
    }
}