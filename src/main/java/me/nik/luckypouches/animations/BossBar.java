package me.nik.luckypouches.animations;

import me.nik.luckypouches.LuckyPouches;
import me.nik.luckypouches.api.AnimationType;
import me.nik.luckypouches.api.Pouch;
import me.nik.luckypouches.files.Config;
import me.nik.luckypouches.utils.ServerUtils;
import me.nik.luckypouches.utils.TaskUtils;
import me.nik.luckypouches.utils.reflection.bossbar.BarSender;
import me.nik.luckypouches.utils.reflection.bossbar.BarSender1_8;
import me.nik.luckypouches.utils.reflection.bossbar.BarSender1_9;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;

public class BossBar extends AnimationType {

    private final LuckyPouches plugin;

    private final Sound startSound;
    private final Sound endSound;

    public BossBar(LuckyPouches plugin) {
        super("BOSSBAR", Config.Setting.ANIMATIONS_BOSSBAR_COOLDOWN.getInt(), Config.Setting.ANIMATIONS_BOSSBAR_SHOW_PRIZE_MESSAGE.getBoolean());

        this.plugin = plugin;

        this.startSound = Config.Setting.ANIMATIONS_BOSSBAR_START_SOUND.getString().isEmpty() ? null : Sound.valueOf(Config.Setting.ANIMATIONS_BOSSBAR_START_SOUND.getString());
        this.endSound = Config.Setting.ANIMATIONS_BOSSBAR_END_SOUND.getString().isEmpty() ? null : Sound.valueOf(Config.Setting.ANIMATIONS_BOSSBAR_END_SOUND.getString());
    }

    @Override
    public void execute(Player player, Pouch pouch) {

        BarSender bar;

        if (ServerUtils.isOnePointEight()) {
            bar = new BarSender1_8();
            bar.setPercent(100f);
        } else {
            bar = new BarSender1_9();
            bar.setStyle(BarStyle.SOLID);
            bar.setColor(BarColor.PURPLE);
            bar.setPercent(1.0f);
        }

        bar.setPlayer(player);

        new BukkitRunnable() {

            final String number = (Config.Setting.ANIMATIONS_BOSSBAR_FORMAT.getBoolean() ? (new DecimalFormat("#,###").format(LuckyPouches.giveReward(player, pouch))) : String.valueOf(LuckyPouches.giveReward(player, pouch)));


            final String prefix = Config.Setting.ANIMATIONS_BOSSBAR_PREFIX_COLOR.getString() + pouch.getEconomy().getPrefix();
            final String suffix = Config.Setting.ANIMATIONS_BOSSBAR_SUFFIX_COLOR.getString() + pouch.getEconomy().getSuffix();

            int position = 0;

            @Override
            public void run() {

                if (player.isOnline()) {

                    if (startSound != null) {

                        player.playSound(player.getLocation(), startSound, 2, 2);
                    }

                    StringBuilder viewedTitle = new StringBuilder();

                    for (int i = 0; i < position; i++) {

                        final char c = number.charAt(number.length() - i - 1);

                        if (Config.Setting.ANIMATIONS_BOSSBAR_RIGHT_TO_LEFT.getBoolean()) {

                            viewedTitle.insert(0, c).insert(0, Config.Setting.ANIMATIONS_BOSSBAR_REVEAL_COLOR.getString());

                        } else {

                            viewedTitle.append(Config.Setting.ANIMATIONS_BOSSBAR_REVEAL_COLOR.getString()).append(number.charAt(i));
                        }

                        if ((i == (position - 1)) && (position != number.length())
                                && (Config.Setting.ANIMATIONS_BOSSBAR_RIGHT_TO_LEFT.getBoolean()
                                ? (Config.Setting.ANIMATIONS_BOSSBAR_REVEAL_COMMA.getBoolean() && c == ',')
                                : (Config.Setting.ANIMATIONS_BOSSBAR_REVEAL_COMMA.getBoolean() && (number.charAt(i + 1)) == ','))) {
                            position++;
                        }
                    }

                    for (int i = position; i < number.length(); i++) {

                        if (Config.Setting.ANIMATIONS_BOSSBAR_RIGHT_TO_LEFT.getBoolean()) {

                            char at = number.charAt(number.length() - i - 1);

                            if (at == ',') {

                                if (Config.Setting.ANIMATIONS_BOSSBAR_REVEAL_COMMA.getBoolean()) {

                                    viewedTitle.insert(0, at).insert(0, Config.Setting.ANIMATIONS_BOSSBAR_REVEAL_COLOR.getString());

                                } else
                                    viewedTitle.insert(0, ",").insert(0, ChatColor.MAGIC).insert(0, Config.Setting.ANIMATIONS_BOSSBAR_OBFUSCATE_COLOR.getString());

                            } else
                                viewedTitle.insert(0, "#").insert(0, ChatColor.MAGIC).insert(0, Config.Setting.ANIMATIONS_BOSSBAR_OBFUSCATE_COLOR.getString());

                        } else {

                            char at = number.charAt(i);

                            if (at == ',') {

                                if (Config.Setting.ANIMATIONS_BOSSBAR_REVEAL_COMMA.getBoolean()) {

                                    viewedTitle.append(Config.Setting.ANIMATIONS_BOSSBAR_REVEAL_COLOR.getString()).append(at);
                                } else
                                    viewedTitle.append(Config.Setting.ANIMATIONS_BOSSBAR_OBFUSCATE_COLOR.getString()).append(ChatColor.MAGIC).append(",");

                            } else
                                viewedTitle.append(Config.Setting.ANIMATIONS_BOSSBAR_OBFUSCATE_COLOR.getString()).append(ChatColor.MAGIC).append("#");
                        }
                    }

                    String reward = prefix + viewedTitle + suffix;

                    bar.setText(Config.Setting.ANIMATIONS_BOSSBAR_TEXT.getString().replace("%reward%", reward));
                    bar.remove();
                    bar.show();

                } else position = number.length();

                if (position == number.length()) {

                    this.cancel();

                    if (player.isOnline()) {

                        if (endSound != null) {

                            player.playSound(player.getLocation(), endSound, 2, 2);
                        }
                    }

                    TaskUtils.taskLaterAsync(bar::remove, 40);

                    return;
                }

                position++;
            }
        }.runTaskTimerAsynchronously(plugin, 5, Config.Setting.ANIMATIONS_BOSSBAR_TICKS.getInt());
    }
}