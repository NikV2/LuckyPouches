package me.nik.luckypouches.economy;

import me.nik.luckypouches.api.CurrencyType;
import me.nik.luckypouches.files.Config;
import org.bukkit.entity.Player;

public class XP extends CurrencyType {

    public XP() {
        super(Config.Setting.CURRENCIES_XP_PREFIX.getString(), Config.Setting.CURRENCIES_XP_SUFFIX.getString(), "XP");
    }

    @Override
    public void deposit(Player player, long amount) {
        player.giveExp((int) amount);
    }

    @Override
    public boolean withdraw(Player player, long amount) {
        if (player.getTotalExperience() < amount) return false;

        player.giveExp((int) -amount);

        return true;
    }
}