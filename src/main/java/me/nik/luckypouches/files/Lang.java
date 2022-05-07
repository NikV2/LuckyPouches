package me.nik.luckypouches.files;

import me.nik.luckypouches.LuckyPouches;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Lang {

    private File file;
    private FileConfiguration lang;

    public void setup(LuckyPouches plugin) {

        file = new File(plugin.getDataFolder(), "lang.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        lang = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration get() {
        return lang;
    }

    public void save() {
        try {
            lang.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        lang = YamlConfiguration.loadConfiguration(file);
    }

    public void addDefaults() {
        get().options().header("+----------------------------------------------------------------------------------------------+" + "\n" + "|                                                                                              |" + "\n" + "|                                           LuckyPouches                                       |" + "\n" + "|                                                                                              |" + "\n" + "|                               Discord: https://discord.gg/m7j2Y9H                            |" + "\n" + "|                                                                                              |" + "\n" + "|                                           Author: Nik                                        |" + "\n" + "|                                                                                              |" + "\n" + "+----------------------------------------------------------------------------------------------+" + "\n");
        get().addDefault("prefix", "&f&l[&bLuckyPouches&f&l]&f&l»&r ");
        get().addDefault("update_reminder", "&fThere is a new version available, Your version &b%current% &fnew version &b%new%");
        get().addDefault("update_not_found", "&fYou're running the Latest Version of LuckyPouches !");
        get().addDefault("reloaded", "&fYou have successfully reloaded &bLuckyPouches &f!");
        get().addDefault("no_perm", "&cI am not permitted to let you execute this command.");
        get().addDefault("prize", "&fYou have redeemed a pouch and received &a%amount%");
        get().addDefault("invalid_player", "&cCould not find the player &9%player%&c.");
        get().addDefault("invalid_pouch", "&cThat pouch does not exist.");
        get().addDefault("invalid_amount", "&cInvalid amount or integer, Please enter a valid amount.");
        get().addDefault("given_player", "&fYou have given &9%player% &f%amount% %pouch% pouch(s).");
        get().addDefault("given_all", "&fYou have given all players %amount% %pouch% pouch(s).");
        get().addDefault("received", "&fYou have received %amount% %pouch% pouch(s).");
        get().addDefault("pouch_cooldown", "&cYou must wait &7%time% &cbefore redeeming a &7%pouch%");
        get().addDefault("console_commands", "&cThis command cannot be executed through the Console!");
        get().addDefault("creating_id", "&fPlease enter a Pouch ID.");
        get().addDefault("creating_name", "&fPlease enter a Pouch Name.");
        get().addDefault("creating_glow", "&fWould you like the Pouch to Glow? (true / false)");
        get().addDefault("creating_headdata", "&fPlease enter the Head Data (Enter none for default head data)");
        get().addDefault("creating_animation", "&fPlease enter the Animation that the Pouch is going to show.");
        get().addDefault("creating_min", "&fPlease enter the Minimum amount that the Player can receive from this Pouch.");
        get().addDefault("creating_max", "&fPlease enter the Maximum amount that the Player can receive from this Pouch.");
        get().addDefault("creating_currency", "&fPlease enter the Currency that the Pouch is going to use.");
        get().addDefault("creating_created", "&fYour new Pouch has been created!");
        get().addDefault("creating_cancelled", "&fYou have cancelled the creation of a Pouch.");
        get().addDefault("creating_search_material", "&fPlease enter the material name.");
        get().addDefault("pouch_shop_sign", Arrays.asList(
                "&f&l[&bPouch Shop&f&l]",
                "&7Click to Purchase",
                "%pouch%",
                "%price%"
        ));
        get().addDefault("pouch_shop_invalid_pouch", "&cPlease provide a valid pouch id.");
        get().addDefault("pouch_shop_invalid_currency", "&cPlease provide a valid currency type.");
        get().addDefault("pouch_shop_invalid_price", "&cPlease provide a valid price.");
        get().addDefault("pouch_shop_created", "&fYou have successfully created a pouch shop sign.");
        get().addDefault("pouch_shop_removed", "&fYou have removed a pouch shop sign at &9%location%&f.");
        get().addDefault("pouch_shop_purchase", "&fYou've successfully purchased a %pouch% &f!");
        get().addDefault("pouch_shop_unable", "&cYou're unable to purchase this pouch!");
    }
}