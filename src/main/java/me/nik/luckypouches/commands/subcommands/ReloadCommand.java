package me.nik.luckypouches.commands.subcommands;

import me.nik.luckypouches.LuckyPouches;
import me.nik.luckypouches.commands.SubCommand;
import me.nik.luckypouches.managers.MsgType;
import me.nik.luckypouches.managers.Permissions;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends SubCommand {

    private final LuckyPouches plugin;

    public ReloadCommand(LuckyPouches plugin) {
        this.plugin = plugin;
    }

    @Override
    protected String getName() {
        return "reload";
    }

    @Override
    protected String getDescription() {
        return "Reload the plugin";
    }

    @Override
    protected String getSyntax() {
        return "/luckypouches reload";
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
        return true;
    }

    @Override
    protected void perform(CommandSender sender, String[] args) {
        this.plugin.reload();
        sender.sendMessage(MsgType.RELOADED.getMessage());
    }

    @Override
    protected List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
}