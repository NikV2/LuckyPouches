package me.nik.luckypouches;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.nik.luckypouches.animations.Actionbar;
import me.nik.luckypouches.animations.BossBar;
import me.nik.luckypouches.animations.None;
import me.nik.luckypouches.animations.Random;
import me.nik.luckypouches.animations.Title;
import me.nik.luckypouches.api.AnimationType;
import me.nik.luckypouches.api.CurrencyType;
import me.nik.luckypouches.api.Pouch;
import me.nik.luckypouches.commands.CommandManager;
import me.nik.luckypouches.economy.Vault;
import me.nik.luckypouches.economy.XP;
import me.nik.luckypouches.files.Config;
import me.nik.luckypouches.files.Lang;
import me.nik.luckypouches.files.commentedfiles.CommentedFileConfiguration;
import me.nik.luckypouches.listeners.ChatListener;
import me.nik.luckypouches.listeners.GuiListener;
import me.nik.luckypouches.listeners.PouchListener;
import me.nik.luckypouches.managers.ItemBuilder;
import me.nik.luckypouches.managers.MsgType;
import me.nik.luckypouches.managers.Profile;
import me.nik.luckypouches.managers.UpdateChecker;
import me.nik.luckypouches.metrics.MetricsLite;
import me.nik.luckypouches.tasks.CacheTask;
import me.nik.luckypouches.utils.reflection.ReflectionUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public final class LuckyPouches extends JavaPlugin {

    private static LuckyPouches plugin;

    private final Map<UUID, Profile> players = new ConcurrentHashMap<>();

    private final Set<Pouch> pouches = new HashSet<>();
    private final Set<AnimationType> animations = new HashSet<>();
    private final Set<CurrencyType> currencies = new HashSet<>();


    private final String[] STARTUP_MESSAGE = {
            " ",
            ChatColor.AQUA + " LuckyPouches v" + this.getDescription().getVersion(),
            " ",
            ChatColor.AQUA + "     Author: Nik",
            " "
    };

    private Config config;
    private Lang lang;

    public static LuckyPouches getInstance() {
        return plugin;
    }

    public static void registerAnimation(AnimationType... animation) {
        plugin.getAnimations().addAll(Arrays.asList(animation));
        plugin.initializePouches();
    }

    public static void registerCurrency(CurrencyType... currency) {
        plugin.getCurrencies().addAll(Arrays.asList(currency));
        plugin.initializePouches();
    }

    public static long giveReward(Player player, Pouch pouch) {

        long amount = ThreadLocalRandom.current().nextLong(pouch.getMin(), pouch.getMax());

        pouch.getEconomy().processPayment(player, amount);

        if (pouch.getAnimation().isSendRewardMessage()) {

            player.sendMessage(MsgType.PRIZE.getMessage().replace("%amount%",
                    pouch.getEconomy().getPrefix() + amount + pouch.getEconomy().getSuffix())
            );
        }

        return amount;
    }

    public Profile getProfile(Player player) {
        return players.computeIfAbsent(player.getUniqueId(), Profile::new);
    }

    public Map<UUID, Profile> getProfileMap() {
        return players;
    }

    @Override
    public void onEnable() {
        plugin = this;

        this.getServer().getConsoleSender().sendMessage(STARTUP_MESSAGE);

        //Initialize files
        initializeFiles();

        //Initialize animations
        registerAnimation(
                new None(),
                new Title(this),
                new Actionbar(this),
                new BossBar(this),
                new Random(this)
        );

        //Initialize currencies
        registerCurrency(
                new Vault(),
                new XP()
        );

        //Initialize pouches
        initializePouches();

        //Initialize listeners
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new GuiListener(), this);
        pm.registerEvents(new ChatListener(this), this);
        pm.registerEvents(new PouchListener(this), this);

        //Initialize tasks
        new CacheTask(this).runTaskTimerAsynchronously(
                this,
                Config.Setting.SETTINGS_CACHE_INTERVAL.getInt(),
                Config.Setting.SETTINGS_CACHE_INTERVAL.getInt()
        );

        getCommand("luckypouches").setExecutor(new CommandManager(this));

        if (Config.Setting.SETTINGS_CHECK_FOR_UPDATES.getBoolean()) {
            new UpdateChecker(this).runTaskAsynchronously(this);
        }

        new MetricsLite(this, 8889);
    }

    public void reload() {

        this.config.reset();
        this.lang.reload();
        this.lang.save();

        this.config.setup();
        this.lang.setup(this);
        this.lang.addDefaults();
        this.lang.get().options().copyDefaults(true);
        this.lang.save();

        initializePouches();
    }

    public void initializePouches() {

        this.pouches.clear();

        final CommentedFileConfiguration config = this.config.getConfig();

        for (String s : config.getConfigurationSection("pouches").getKeys(false)) {

            String path = "pouches." + s;

            ItemBuilder pb = new ItemBuilder();

            pb.setName(config.getString(path + ".name"));
            pb.setMaterial(config.getString(path + ".material"));
            pb.setLores(config.getStringList(path + ".lore"));
            pb.setGlowing(config.getBoolean(path + ".glow"));
            pb.setHeadData(config.getString(path + ".head_data"));

            long min = config.getLong(path + ".min");
            long max = config.getLong(path + ".max");

            String economy = config.getString(path + ".currency") == null ? "VAULT" : config.getString(path + ".currency");

            CurrencyType currencyType = new Vault();

            for (CurrencyType econ : this.currencies) {

                if (!economy.equalsIgnoreCase(econ.getName())) continue;

                currencyType = econ;
            }

            String animation = config.getString(path + ".animation") == null ? "NONE" : config.getString(path + ".animation");

            AnimationType animationType = new None();

            for (AnimationType anim : this.animations) {

                if (!animation.equalsIgnoreCase(anim.getName())) continue;

                animationType = anim;
            }

            NBTItem nbtItem = new NBTItem(pb.build());
            nbtItem.setBoolean("luckypouches", true);

            pouches.add(new Pouch(s.replace(" ", "_"), min, max, nbtItem.getItem(), currencyType, animationType));
        }
    }

    @Override
    public void onDisable() {
        config.reset();
        lang.reload();
        lang.save();

        ReflectionUtils.clear();

        HandlerList.unregisterAll(this);
        this.getServer().getScheduler().cancelTasks(this);
    }

    public Set<Pouch> getPouches() {
        return pouches;
    }

    public Set<AnimationType> getAnimations() {
        return animations;
    }

    public Set<CurrencyType> getCurrencies() {
        return currencies;
    }

    public void initializeFiles() {
        this.config = new Config(this);
        this.config.setup();
        this.lang = new Lang();
        this.lang.setup(this);
        this.lang.addDefaults();
        this.lang.get().options().copyDefaults(true);
        this.lang.save();
    }

    public CommentedFileConfiguration getConfiguration() {
        return config.getConfig();
    }

    public Lang getLang() {
        return lang;
    }
}