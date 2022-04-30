package me.nik.luckypouches.tasks;

import me.nik.luckypouches.LuckyPouches;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class CacheTask extends BukkitRunnable {

    private final LuckyPouches plugin;

    public CacheTask(LuckyPouches plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.getProfileMap().keySet().removeIf(uuid -> Bukkit.getPlayer(uuid) == null);
    }
}