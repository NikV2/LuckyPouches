package me.nik.luckypouches.animations;

import me.nik.luckypouches.LuckyPouches;
import me.nik.luckypouches.api.AnimationType;
import me.nik.luckypouches.api.Pouch;
import me.nik.luckypouches.files.Config;
import me.nik.luckypouches.utils.MiscUtils;
import org.bukkit.entity.Player;

public class Random extends AnimationType {

    private final LuckyPouches plugin;

    public Random(LuckyPouches plugin) {
        super("RANDOM", Config.Setting.ANIMATIONS_RANDOM_COOLDOWN.getInt(), Config.Setting.ANIMATIONS_RANDOM_SHOW_PRIZE_MESSAGE.getBoolean());

        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, Pouch pouch) {

        AnimationType animation = MiscUtils.randomElement(this.plugin.getAnimations());

        if (animation == null) { // Should never return null
            animation = this.plugin.getAnimations().stream().findAny().orElse(null);
        }

        animation.execute(player, pouch);
    }
}