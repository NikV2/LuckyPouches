package me.nik.luckypouches;

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
import me.nik.luckypouches.listeners.PouchShopListener;
import me.nik.luckypouches.managers.MsgType;
import me.nik.luckypouches.managers.PouchBuilder;
import me.nik.luckypouches.managers.Profile;
import me.nik.luckypouches.managers.UpdateChecker;
import me.nik.luckypouches.metrics.MetricsLite;
import me.nik.luckypouches.tasks.CacheTask;
import me.nik.luckypouches.utils.reflection.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
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

        pouch.getEconomy().deposit(player, amount);

        if (pouch.getAnimation().shouldSendRewardMessage()) {

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
        Arrays.asList(
                new GuiListener(),
                new ChatListener(this),
                new PouchListener(this),
                new PouchShopListener(this)
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));

        //Initialize tasks
        new CacheTask(this).runTaskTimerAsynchronously(
                this,
                Config.Setting.SETTINGS_CACHE_INTERVAL.getLong() * 20L,
                Config.Setting.SETTINGS_CACHE_INTERVAL.getLong() * 20L
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

        CommentedFileConfiguration config = this.config.getConfig();

        config.getConfigurationSection("pouches").getKeys(false).forEach(key -> {

            String path = "pouches." + key;

            CurrencyType currencyType = this.currencies.stream()
                    .filter(currency -> currency.getName().equalsIgnoreCase(config.getString(path + ".currency")))
                    .findFirst()
                    .orElse(new Vault());

            AnimationType animationType = this.animations.stream()
                    .filter(animation -> animation.getName().equalsIgnoreCase(config.getString(path + ".animation")))
                    .findFirst()
                    .orElse(new None());

            PouchBuilder pb = new PouchBuilder();

            pb.setName(config.getString(path + ".name"));
            pb.setMaterial(config.getString(path + ".material"));
            pb.setLores(config.getStringList(path + ".lore"));
            pb.setGlowing(config.getBoolean(path + ".glow"));
            pb.setHeadData(config.getString(path + ".head_data"));

            this.pouches.add(new Pouch(
                    key.replace(" ", "_"),
                    config.getLong(path + ".min"),
                    config.getLong(path + ".max"),
                    pb.build(),
                    currencyType,
                    animationType
            ));
        });
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