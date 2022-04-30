package me.nik.luckypouches.commands.subcommands;

import me.nik.luckypouches.LuckyPouches;
import me.nik.luckypouches.commands.SubCommand;
import me.nik.luckypouches.gui.PlayerMenu;
import me.nik.luckypouches.gui.menus.ListGUI;
import me.nik.luckypouches.managers.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ListCommand extends SubCommand {

    private final LuckyPouches plugin;

    public ListCommand(LuckyPouches plugin) {
        this.plugin = plugin;
    }

    @Override
    protected String getName() {
        return "list";
    }

    @Override
    protected String getDescription() {
        return "List of all the pouches";
    }

    @Override
    protected String getSyntax() {
        return "/luckypouches list";
    }

    @Override
    protected String getPermission() {
        return Permissions.ADMIN.getPermission();
    }

    @Override
    protected int maxArguments() {
        return 1;
    }

    @Override
    protected boolean canConsoleExecute() {
        return false;
    }

    @Override
    protected void perform(CommandSender sender, String[] args) {
        new ListGUI(new PlayerMenu((Player) sender), plugin).open();
    }

    @Override
    protected List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
}