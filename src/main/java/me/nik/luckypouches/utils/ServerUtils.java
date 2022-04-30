package me.nik.luckypouches.utils;

import org.bukkit.Bukkit;

public final class ServerUtils {

    private static final String VERSION = Bukkit.getVersion();

    private static final boolean ONE_POINT_EIGHT = areVersions("1.8");

    private static final boolean LEGACY = areVersions(
            "1.8",
            "1.9",
            "1.10",
            "1.11",
            "1.12");

    public static boolean isOnePointEight() {
        return ONE_POINT_EIGHT;
    }

    public static boolean isLegacy() {
        return LEGACY;
    }

    public static boolean areVersions(final String... versions) {

        for (String version : versions) {

            if (VERSION.contains(version)) return true;
        }

        return false;
    }
}