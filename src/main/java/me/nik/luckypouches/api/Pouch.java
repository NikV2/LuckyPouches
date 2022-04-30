package me.nik.luckypouches.api;

import org.bukkit.inventory.ItemStack;

public class Pouch {

    private final String id;
    private final long min;
    private final long max;
    private final ItemStack item;
    private final CurrencyType economy;
    private final AnimationType animation;

    public Pouch(String id, long min, long max, ItemStack item, CurrencyType economy, AnimationType animation) {
        this.id = id;
        this.min = Math.max(1L, min);
        this.max = Math.max(2L, max);
        this.item = item;
        this.economy = economy;
        this.animation = animation;
    }

    public String getId() {
        return id;
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }

    public ItemStack getItem() {
        return item;
    }

    public CurrencyType getEconomy() {
        return economy;
    }

    public AnimationType getAnimation() {
        return animation;
    }
}