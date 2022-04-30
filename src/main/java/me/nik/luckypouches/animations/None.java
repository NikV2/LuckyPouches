package me.nik.luckypouches.animations;

import me.nik.luckypouches.LuckyPouches;
import me.nik.luckypouches.api.AnimationType;
import me.nik.luckypouches.api.Pouch;
import me.nik.luckypouches.files.Config;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class None extends AnimationType {

    private final Sound sound;

    public None() {
        super("NONE", Config.Setting.ANIMATIONS_NONE_COOLDOWN.getInt(), Config.Setting.ANIMATIONS_NONE_SHOW_PRIZE_MESSAGE.getBoolean());

        this.sound = Config.Setting.ANIMATIONS_NONE_SOUND.getString().isEmpty() ? null : Sound.valueOf(Config.Setting.ANIMATIONS_NONE_SOUND.getString());
    }

    @Override
    public void execute(Player player, Pouch pouch) {
        if (this.sound != null) {
            player.playSound(player.getLocation(), sound, 2, 2);
        }

        LuckyPouches.giveReward(player, pouch);
    }
}