package me.nik.luckypouches.commands.subcommands;

import me.nik.luckypouches.LuckyPouches;
import me.nik.luckypouches.commands.SubCommand;
import me.nik.luckypouches.gui.PlayerMenu;
import me.nik.luckypouches.gui.menus.MaterialGUI;
import me.nik.luckypouches.managers.Permissions;
import me.nik.luckypouches.managers.PouchCreationState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CreateCommand extends SubCommand {

    private final LuckyPouches plugin;

    public CreateCommand(LuckyPouches plugin) {
        this.plugin = plugin;
    }

    @Override
    protected String getName() {
        return "create";
    }

    @Override
    protected String getDescription() {
        return "Create a new Pouch";
    }

    @Override
    protected String getSyntax() {
        return "/luckypouches create";
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

        Player player = (Player) sender;

        this.plugin.getProfile(player).setPouchCreationState(PouchCreationState.AWAITING_MATERIAL);

        new MaterialGUI(new PlayerMenu(player), this.plugin).open();
    }

    @Override
    protected List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
}