package me.nik.luckypouches.utils.reflection.bossbar;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

public interface BarSender {

    void show();

    void remove();

    void setText(String text);

    void setPlayer(Player player);

    void setPercent(float percent);

    void setStyle(BarStyle style);

    void setColor(BarColor color);
}