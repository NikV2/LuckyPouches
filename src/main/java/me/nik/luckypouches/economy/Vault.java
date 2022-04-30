package me.nik.luckypouches.economy;

import me.nik.luckypouches.api.CurrencyType;
import me.nik.luckypouches.files.Config;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Vault extends CurrencyType {

    private final Economy econ;

    public Vault() {
        super(Config.Setting.CURRENCIES_VAULT_PREFIX.getString(), Config.Setting.CURRENCIES_VAULT_SUFFIX.getString(), "VAULT");

        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            this.econ = null;
            return;
        }

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            this.econ = null;
            return;
        }

        this.econ = rsp.getProvider();
    }

    @Override
    public void processPayment(Player player, long amount) {
        if (econ == null) return;
        econ.depositPlayer(player, amount);
    }
}