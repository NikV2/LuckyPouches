package me.nik.luckypouches.utils.reflection.bossbar;

import me.nik.luckypouches.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BarSender1_9 implements BarSender {

    private String text;
    private float percent;
    private BarColor color;
    private Player player;
    private BarStyle style;
    private BossBar bar;

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setPercent(float percent) {
        this.percent = percent;
    }

    @Override
    public void setColor(BarColor color) {
        this.color = color;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void setStyle(BarStyle style) {
        this.style = style;
    }

    @Override
    public void show() {
        this.bar = Bukkit.createBossBar(ChatUtils.format(this.text), this.color, this.style);

        this.bar.setProgress(this.percent);
        this.bar.addPlayer(this.player);
        this.bar.setVisible(true);
    }

    @Override
    public void remove() {
        if (this.bar == null) return;

        this.bar.removeAll();
        this.bar.setVisible(false);
        this.bar = null;
    }
}