package me.nik.luckypouches.utils;

import org.bukkit.ChatColor;

public final class ChatUtils {

    private ChatUtils() {
    }

    public static String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}