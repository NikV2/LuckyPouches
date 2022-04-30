package me.nik.luckypouches.api;

import org.bukkit.entity.Player;

public abstract class CurrencyType {

    private final String prefix, suffix, name;

    public CurrencyType(String prefix, String suffix, String name) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getName() {
        return name;
    }

    public abstract void processPayment(Player player, long amount);
}