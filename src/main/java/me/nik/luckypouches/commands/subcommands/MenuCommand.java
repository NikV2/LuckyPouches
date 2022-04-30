package me.nik.luckypouches.commands.subcommands;

import me.nik.luckypouches.LuckyPouches;
import me.nik.luckypouches.commands.SubCommand;
import me.nik.luckypouches.gui.PlayerMenu;
import me.nik.luckypouches.gui.menus.MainGUI;
import me.nik.luckypouches.managers.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MenuCommand extends SubCommand {

    private final LuckyPouches plugin;

    public MenuCommand(LuckyPouches plugin) {
        this.plugin = plugin;
    }

    @Override
    protected String getName() {
        return "menu";
    }

    @Override
    protected String getDescription() {
        return "Open the LuckyPouches menu";
    }

    @Override
    protected String getSyntax() {
        return "/luckypouches menu";
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
        new MainGUI(new PlayerMenu((Player) sender), plugin).open();
    }

    @Override
    protected List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
}