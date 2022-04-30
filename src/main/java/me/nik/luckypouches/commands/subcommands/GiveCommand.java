package me.nik.luckypouches.commands.subcommands;

import me.nik.luckypouches.LuckyPouches;
import me.nik.luckypouches.api.Pouch;
import me.nik.luckypouches.commands.SubCommand;
import me.nik.luckypouches.managers.MsgType;
import me.nik.luckypouches.managers.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GiveCommand extends SubCommand {

    private static final List<String> NUMBER_STRINGS = new ArrayList<>();

    static {
        for (int i = 1; i <= 64; i++) {
            NUMBER_STRINGS.add(String.valueOf(i));
        }
    }

    private final LuckyPouches plugin;

    public GiveCommand(LuckyPouches plugin) {
        this.plugin = plugin;
    }

    @Override
    protected String getName() {
        return "give";
    }

    @Override
    protected String getDescription() {
        return "Give players one or more pouches";
    }

    @Override
    protected String getSyntax() {
        return "/luckypouches give <player/all> <id> <amount>";
    }

    @Override
    protected String getPermission() {
        return Permissions.ADMIN.getPermission();
    }

    @Override
    protected int maxArguments() {
        return 3;
    }

    @Override
    protected boolean canConsoleExecute() {
        return true;
    }

    @Override
    protected void perform(CommandSender sender, String[] args) {

        Pouch pouch = this.plugin.getPouches().stream().filter(p -> p.getId().equals(args[2])).findFirst().orElse(null);

        if (pouch == null) {

            sender.sendMessage(MsgType.INVALID_POUCH.getMessage());

            return;
        }

        final int amount;

        if (args.length == 3 || args[3].equalsIgnoreCase(String.valueOf(0))) {

            amount = 1;

        } else {

            try {

                amount = Integer.parseInt(args[3]);

            } catch (NumberFormatException e) {

                sender.sendMessage(MsgType.INVALID_AMOUNT.getMessage());

                return;
            }
        }

        if (args[1].equalsIgnoreCase("all")) {

            Bukkit.getOnlinePlayers().forEach(player -> givePouch(player, amount, pouch));

            sender.sendMessage(MsgType.GIVEN_ALL.getMessage().replace("%amount%", String.valueOf(amount)).replace("%pouch%", pouch.getId()));

        } else {

            Player target = Bukkit.getOnlinePlayers().stream().filter(player -> player.getName().equalsIgnoreCase(args[1])).findFirst().orElse(null);

            if (target == null) {

                sender.sendMessage(MsgType.INVALID_PLAYER.getMessage().replace("%player%", args[2]));

                return;
            }

            givePouch(target, amount, pouch);

            sender.sendMessage(MsgType.GIVEN_PLAYER.getMessage().replace("%player%", target.getName()).replace("%amount%", String.valueOf(amount)).replace("%pouch%", pouch.getId()));
        }
    }

    private void givePouch(Player player, int amount, Pouch pouch) {

        amount = Math.min(amount, 64);

        for (int i = 0; i < amount; i++) {

            if (player.getInventory().firstEmpty() == -1) {

                player.getWorld().dropItem(player.getLocation(), pouch.getItem());

            } else player.getInventory().addItem(pouch.getItem());
        }

        player.sendMessage(MsgType.RECEIVED.getMessage().replace("%amount%", String.valueOf(amount)).replace("%pouch%", pouch.getId()));
    }

    @Override
    protected List<String> getSubcommandArguments(CommandSender sender, String[] args) {

        switch (args.length) {

            case 2:
                List<String> players = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
                players.add("all");
                return players;

            case 3:
                return this.plugin.getPouches().stream().map(Pouch::getId).collect(Collectors.toList());

            case 4:
                return NUMBER_STRINGS;

            default:
                return null;
        }
    }
}