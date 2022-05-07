package me.nik.luckypouches.api;

import org.bukkit.entity.Player;

public abstract class AnimationType {

    private final String name;
    private final int cooldown;
    private final boolean sendRewardMessage;

    public AnimationType(String name, int cooldown, boolean sendRewardMessage) {
        this.name = name;
        this.cooldown = cooldown;
        this.sendRewardMessage = sendRewardMessage;
    }

    public String getName() {
        return name;
    }

    public int getCooldown() {
        return cooldown;
    }

    public boolean shouldSendRewardMessage() {
        return sendRewardMessage;
    }

    public abstract void execute(Player player, Pouch pouch);
}