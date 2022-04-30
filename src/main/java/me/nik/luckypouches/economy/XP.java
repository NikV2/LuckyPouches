package me.nik.luckypouches.economy;

import me.nik.luckypouches.api.CurrencyType;
import me.nik.luckypouches.files.Config;
import org.bukkit.entity.Player;

public class XP extends CurrencyType {

    public XP() {
        super(Config.Setting.CURRENCIES_XP_PREFIX.getString(), Config.Setting.CURRENCIES_XP_SUFFIX.getString(), "XP");
    }

    @Override
    public void processPayment(Player player, long amount) {
        if (!player.isOnline()) return;
        player.giveExp((int) amount);
    }
}