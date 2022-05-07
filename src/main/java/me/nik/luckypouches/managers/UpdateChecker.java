package me.nik.luckypouches.managers;

import me.nik.luckypouches.LuckyPouches;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UpdateChecker extends BukkitRunnable implements Listener {

    private final LuckyPouches plugin;
    private String newVersion;

    public UpdateChecker(LuckyPouches plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        try {

            URLConnection connection = new URL("https://raw.githubusercontent.com/NikV2/LuckyPouches/master/version.txt").openConnection();

            connection.addRequestProperty("User-Agent", "Mozilla/4.0");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            final String line = reader.readLine();

            reader.close();

            this.newVersion = line;

        } catch (IOException e) {
            plugin.getLogger().warning("Couldn't check for updates, Is the server connected to the Internet?");
            return;
        }

        if (!plugin.getDescription().getVersion().equals(newVersion)) {
            plugin.getServer().getConsoleSender().sendMessage(MsgType.UPDATE_REMINDER.getMessage()
                    .replace("%current%", plugin.getDescription().getVersion())
                    .replace("%new%", newVersion)
            );

            Bukkit.getPluginManager().registerEvents(this, plugin);
        } else plugin.getServer().getConsoleSender().sendMessage(MsgType.UPDATE_NOT_FOUND.getMessage());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPermission(Permissions.ADMIN.getPermission())) return;

        e.getPlayer().sendMessage(MsgType.UPDATE_REMINDER.getMessage()
                .replace("%current%", plugin.getDescription().getVersion())
                .replace("%new%", newVersion)
        );
    }
}