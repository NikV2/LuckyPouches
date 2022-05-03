package me.nik.luckypouches.files;

import me.nik.luckypouches.LuckyPouches;
import me.nik.luckypouches.files.commentedfiles.CommentedFileConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Config {

    private static final String[] HEADER = new String[]{
            "+----------------------------------------------------------------------------------------------+",
            "|                                                                                              |",
            "|                                           LuckyPouches                                       |",
            "|                                                                                              |",
            "|                               Discord: https://discord.gg/m7j2Y9H                            |",
            "|                                                                                              |",
            "|                                           Author: Nik                                        |",
            "|                                                                                              |",
            "+----------------------------------------------------------------------------------------------+"
    };
    private static boolean exists;
    /**
     * Credits to Nicole for this amazing Commented File Configuration
     * https://github.com/Esophose/PlayerParticles
     */

    private final LuckyPouches plugin;
    private CommentedFileConfiguration configuration;

    public Config(LuckyPouches plugin) {
        this.plugin = plugin;
    }

    public void setup() {

        File configFile = new File(this.plugin.getDataFolder(), "config.yml");
        exists = configFile.exists();
        boolean setHeaderFooter = !exists;
        boolean changed = setHeaderFooter;

        this.configuration = CommentedFileConfiguration.loadConfiguration(this.plugin, configFile);

        if (setHeaderFooter) {
            this.configuration.addComments(HEADER);
        }

        for (Setting setting : Setting.values()) {
            setting.reset();
            changed |= setting.setIfNotExists(this.configuration);
        }

        if (changed) {
            this.configuration.save();
        }
    }

    public void reset() {
        for (Setting setting : Setting.values())
            setting.reset();
    }

    /**
     * @return the config.yml as a CommentedFileConfiguration
     */
    public CommentedFileConfiguration getConfig() {
        return this.configuration;
    }

    public enum Setting {
        SETTINGS("settings", "", "Plugin Settings"),
        SETTINGS_CHECK_FOR_UPDATES("settings.check_for_updates", true, "Would you like to check for Updates on startup?"),
        SETTINGS_CACHE_INTERVAL("settings.cache_interval", 120, "How often should LuckyPouches clear invalid player cache? (Interval in seconds)"),

        POUCHES("pouches", "", "Pouch List"),
        POUCH_COMMON_NAME("pouches.common.name", "&6&lCommon Pouch", true, "The display name of the Pouch"),
        POUCH_COMMON_MATERIAL("pouches.common.material", "PLAYER_HEAD", true, "The material of the Pouch, Make sure the material exists on your Spigot Version"),
        POUCH_COMMON_HEADDATA("pouches.common.head_data", "http://textures.minecraft.net/texture/d5c6dc2bbf51c36cfc7714585a6a5683ef2b14d47d8ff714654a893f5da622", true, "Would you like the Pouch to be a Head with a texture?", "Set the material to 'PLAYER_HEAD' then visit https://minecraft-heads.com/custom-heads", "Grab either the minecraft texture URL, or the Hash and Enter it here!"),
        POUCH_COMMON_GLOW("pouches.common.glow", true, true, "Would you like this Pouch to Glow?"),
        POUCH_COMMON_ANIMATION("pouches.common.animation", "NONE", true, "Current Animations", "NONE, TITLE, ACTIONBAR, BOSSBAR, RANDOM"),
        POUCH_COMMON_MIN("pouches.common.min", 100, true, "The Minimum amount of money a Player can receive"),
        POUCH_COMMON_MAX("pouches.common.max", 500, true, "The Maximum amount of money a Player can receive"),
        POUCH_COMMON_CURRENCY("pouches.common.currency", "VAULT", true, "Current Currencies", "VAULT, XP, ELITEMOBS"),
        POUCH_COMMON_LORE("pouches.common.lore", Arrays.asList("", "&7Open this Pouch to receive Money!", "", "&7Range: &6$100 &7- &6$500"), true, "The Pouch's Lore"),

        POUCH_UNCOMMON_NAME("pouches.uncommon.name", "&e&lUncommon Pouch", true),
        POUCH_UNCOMMON_MATERIAL("pouches.uncommon.material", "PLAYER_HEAD", true),
        POUCH_UNCOMMON_HEADDATA("pouches.uncommon.head_data", "http://textures.minecraft.net/texture/f37cae5c51eb1558ea828f58e0dff8e6b7b0b1a183d737eecf714661761", true),
        POUCH_UNCOMMON_GLOW("pouches.uncommon.glow", true, true),
        POUCH_UNCOMMON_ANIMATION("pouches.uncommon.animation", "ACTIONBAR", true),
        POUCH_UNCOMMON_MIN("pouches.uncommon.min", 500, true),
        POUCH_UNCOMMON_MAX("pouches.uncommon.max", 1000, true),
        POUCH_UNCOMMON_CURRENCY("pouches.uncommon.currency", "VAULT", true),
        POUCH_UNCOMMON_LORE("pouches.uncommon.lore", Arrays.asList("", "&7Open this Pouch to receive Money!", "", "&7Range: &e$500 &7- &e$1000"), true),

        POUCH_RARE_NAME("pouches.rare.name", "&3&lRare Pouch", true),
        POUCH_RARE_MATERIAL("pouches.rare.material", "PLAYER_HEAD", true),
        POUCH_RARE_HEADDATA("pouches.rare.head_data", "http://textures.minecraft.net/texture/25807cc4c3b6958aea6156e84518d91a49c5f32971e6eb269a23a25a27145", true),
        POUCH_RARE_GLOW("pouches.rare.glow", true, true),
        POUCH_RARE_ANIMATION("pouches.rare.animation", "BOSSBAR", true),
        POUCH_RARE_MIN("pouches.rare.min", 1000, true),
        POUCH_RARE_MAX("pouches.rare.max", 1500, true),
        POUCH_RARE_CURRENCY("pouches.rare.currency", "VAULT", true),
        POUCH_RARE_LORE("pouches.rare.lore", Arrays.asList("", "&7Open this Pouch to receive Money!", "", "&7Range: &3$1000 &7- &3$1500"), true),

        POUCH_LEGENDARY_NAME("pouches.legendary.name", "&a&lLegendary Pouch", true),
        POUCH_LEGENDARY_MATERIAL("pouches.legendary.material", "PLAYER_HEAD", true),
        POUCH_LEGENDARY_HEADDATA("pouches.legendary.head_data", "http://textures.minecraft.net/texture/f3d5e43de5d4177c4baf2f44161554473a3b0be5430998b5fcd826af943afe3", true),
        POUCH_LEGENDARY_GLOW("pouches.legendary.glow", true, true),
        POUCH_LEGENDARY_ANIMATION("pouches.legendary.animation", "TITLE", true),
        POUCH_LEGENDARY_MIN("pouches.legendary.min", 1500, true),
        POUCH_LEGENDARY_MAX("pouches.legendary.max", 2000, true),
        POUCH_LEGENDARY_CURRENCY("pouches.legendary.currency", "VAULT", true),
        POUCH_LEGENDARY_LORE("pouches.legendary.lore", Arrays.asList("", "&7Open this Pouch to receive Money!", "", "&7Range: &a$1500 &7- &a$2000"), true),

        CURRENCIES("currencies", "", "Currency Settings"),
        CURRENCIES_VAULT_PREFIX("currencies.vault.prefix", "$"),
        CURRENCIES_VAULT_SUFFIX("currencies.vault.suffix", ""),
        CURRENCIES_XP_PREFIX("currencies.xp.prefix", ""),
        CURRENCIES_XP_SUFFIX("currencies.xp.suffix", "XP"),

        ANIMATIONS("animations", "", "Animation Settings"),

        ANIMATIONS_NONE("animations.none", "", "None Animation Settings"),
        ANIMATIONS_NONE_COOLDOWN("animations.none.cooldown", 0, "How long would you like players to be able to redeem Pouches with this Animation? (In seconds)"),
        ANIMATIONS_NONE_SHOW_PRIZE_MESSAGE("animations.none.show_prize_message", true, "Should this animation also send the Prize Message to the player?"),
        ANIMATIONS_NONE_SOUND("animations.none.sound", "", "What sound should be played? (Empty for no sound)"),

        ANIMATIONS_TITLE("animations.title", "", "Title Animation Settings"),
        ANIMATIONS_TITLE_TICKS("animations.title.ticks", 10, "How fast should the title progress be? (In ticks, 20 ticks = 1 Second)"),
        ANIMATIONS_TITLE_COOLDOWN("animations.title.cooldown", 3, "How long would you like players to be able to redeem Pouches with this Animation? (In seconds)"),
        ANIMATIONS_TITLE_START_SOUND("animations.title.start_sound", "", "What sound should be played? (Empty for no sound)"),
        ANIMATIONS_TITLE_END_SOUND("animations.title.end_sound", "", "What sound should be played? (Empty for no sound)"),
        ANIMATIONS_TITLE_SHOW_PRIZE_MESSAGE("animations.title.show_prize_message", false, "Should the title Animation also send the Prize Message to the player?"),
        ANIMATIONS_TITLE_PREFIX_COLOR("animations.title.prefix_color", "&a", "The color code that the Prefix will be displayed with"),
        ANIMATIONS_TITLE_SUFFIX_COLOR("animations.title.suffix_color", "&a", "The color code that the Suffix will be displayed with"),
        ANIMATIONS_TITLE_REVEAL_COLOR("animations.title.reveal_color", "&a", "The color code that the Amount will be displayed with"),
        ANIMATIONS_TITLE_OBFUSCATE_COLOR("animations.title.obfuscate_color", "&b", "The color code that the Obfuscated Digits will be displayed with"),
        ANIMATIONS_TITLE_FORMAT("animations.title.format", true, "Should the amount be formatted? (ie: 1,523,640)"),
        ANIMATIONS_TITLE_REVEAL_COMMA("animations.title.reveal_comma", false, "Should the comma be shown?"),
        ANIMATIONS_TITLE_RIGHT_TO_LEFT("animations.title.right_to_left", true, "Should the Amount start getting shown Right to Left?"),
        ANIMATIONS_TITLE_TITLE("animations.title.title", "%reward%", "The Title Message"),
        ANIMATIONS_TITLE_SUBTITLE("animations.title.subtitle", "&fOpening Pouch...", "The Subtitle Message"),

        ANIMATIONS_ACTIONBAR("animations.actionbar", "", "Actionbar Animation Settings"),
        ANIMATIONS_ACTIONBAR_TICKS("animations.actionbar.ticks", 10, "How fast should the actionbar progress be? (In ticks, 20 ticks = 1 Second)"),
        ANIMATIONS_ACTIONBAR_COOLDOWN("animations.actionbar.cooldown", 3, "How long would you like players to be able to redeem Pouches with this Animation? (In seconds)"),
        ANIMATIONS_ACTIONBAR_START_SOUND("animations.actionbar.start_sound", "", "What sound should be played? (Empty for no sound)"),
        ANIMATIONS_ACTIONBAR_END_SOUND("animations.actionbar.end_sound", "", "What sound should be played? (Empty for no sound)"),
        ANIMATIONS_ACTIONBAR_SHOW_PRIZE_MESSAGE("animations.actionbar.show_prize_message", false, "Should the actionbar Animation also send the Prize Message to the player?"),
        ANIMATIONS_ACTIONBAR_PREFIX_COLOR("animations.actionbar.prefix_color", "&a", "The color code that the Prefix will be displayed with"),
        ANIMATIONS_ACTIONBAR_SUFFIX_COLOR("animations.actionbar.suffix_color", "&a", "The color code that the Suffix will be displayed with"),
        ANIMATIONS_ACTIONBAR_REVEAL_COLOR("animations.actionbar.reveal_color", "&a", "The color code that the Amount will be displayed with"),
        ANIMATIONS_ACTIONBAR_OBFUSCATE_COLOR("animations.actionbar.obfuscate_color", "&b", "The color code that the Obfuscated Digits will be displayed with"),
        ANIMATIONS_ACTIONBAR_FORMAT("animations.actionbar.format", true, "Should the amount be formatted? (ie: 1,523,640)"),
        ANIMATIONS_ACTIONBAR_REVEAL_COMMA("animations.actionbar.reveal_comma", false, "Should the comma be shown?"),
        ANIMATIONS_ACTIONBAR_RIGHT_TO_LEFT("animations.actionbar.right_to_left", true, "Should the Amount start getting shown Right to Left?"),
        ANIMATIONS_ACTIONBAR_ACTIONBAR("animations.actionbar.actionbar", "&fYou have earned %reward%", "The Actionbar Message"),

        ANIMATIONS_BOSSBAR("animations.bossbar", "", "BossBar Animation Settings"),
        ANIMATIONS_BOSSBAR_TICKS("animations.bossbar.ticks", 10, "How fast should the bossbar progress be? (In ticks, 20 ticks = 1 Second)"),
        ANIMATIONS_BOSSBAR_COOLDOWN("animations.bossbar.cooldown", 6, "How long would you like players to be able to redeem Pouches with this Animation? (In seconds)"),
        ANIMATIONS_BOSSBAR_START_SOUND("animations.bossbar.start_sound", "", "What sound should be played? (Empty for no sound)"),
        ANIMATIONS_BOSSBAR_END_SOUND("animations.bossbar.end_sound", "", "What sound should be played? (Empty for no sound)"),
        ANIMATIONS_BOSSBAR_SHOW_PRIZE_MESSAGE("animations.bossbar.show_prize_message", false, "Should the bossbar Animation also send the Prize Message to the player?"),
        ANIMATIONS_BOSSBAR_PREFIX_COLOR("animations.bossbar.prefix_color", "&a", "The color code that the Prefix will be displayed with"),
        ANIMATIONS_BOSSBAR_SUFFIX_COLOR("animations.bossbar.suffix_color", "&a", "The color code that the Suffix will be displayed with"),
        ANIMATIONS_BOSSBAR_REVEAL_COLOR("animations.bossbar.reveal_color", "&a", "The color code that the Amount will be displayed with"),
        ANIMATIONS_BOSSBAR_OBFUSCATE_COLOR("animations.bossbar.obfuscate_color", "&b", "The color code that the Obfuscated Digits will be displayed with"),
        ANIMATIONS_BOSSBAR_FORMAT("animations.bossbar.format", true, "Should the amount be formatted? (ie: 1,523,640)"),
        ANIMATIONS_BOSSBAR_REVEAL_COMMA("animations.bossbar.reveal_comma", false, "Should the comma be shown?"),
        ANIMATIONS_BOSSBAR_RIGHT_TO_LEFT("animations.bossbar.right_to_left", true, "Should the Amount start getting shown Right to Left?"),
        ANIMATIONS_BOSSBAR_TEXT("animations.bossbar.text", "&fYour prize is %reward%", "The BossBar Message"),

        ANIMATIONS_RANDOM("animations.random", "", "Random Animation Settings"),
        ANIMATIONS_RANDOM_COOLDOWN("animations.random.cooldown", 6, "How long would you like players to be able to redeem Pouches with this Animation? (In seconds)"),
        ANIMATIONS_RANDOM_SHOW_PRIZE_MESSAGE("animations.random.show_prize_message", true, "Should the bossbar Animation also send the Prize Message to the player?");

        private final String key;
        private final Object defaultValue;
        private final String[] comments;
        private boolean excluded;
        private Object value = null;

        Setting(String key, Object defaultValue, String... comments) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.comments = comments != null ? comments : new String[0];
        }

        Setting(String key, Object defaultValue, boolean excluded, String... comments) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.comments = comments != null ? comments : new String[0];
            this.excluded = excluded;
        }

        /**
         * Gets the setting as a boolean
         *
         * @return The setting as a boolean
         */
        public boolean getBoolean() {
            this.loadValue();
            return (boolean) this.value;
        }

        public String getKey() {
            return this.key;
        }

        /**
         * @return the setting as an int
         */
        public int getInt() {
            this.loadValue();
            return (int) this.getNumber();
        }

        /**
         * @return the setting as a long
         */
        public long getLong() {
            this.loadValue();
            return (long) this.getNumber();
        }

        /**
         * @return the setting as a double
         */
        public double getDouble() {
            this.loadValue();
            return this.getNumber();
        }

        /**
         * @return the setting as a float
         */
        public float getFloat() {
            this.loadValue();
            return (float) this.getNumber();
        }

        /**
         * @return the setting as a String
         */
        public String getString() {
            this.loadValue();
            return String.valueOf(this.value);
        }

        private double getNumber() {
            if (this.value instanceof Integer) {
                return (int) this.value;
            } else if (this.value instanceof Short) {
                return (short) this.value;
            } else if (this.value instanceof Byte) {
                return (byte) this.value;
            } else if (this.value instanceof Float) {
                return (float) this.value;
            }

            return (double) this.value;
        }

        /**
         * @return the setting as a string list
         */
        @SuppressWarnings("unchecked")
        public List<String> getStringList() {
            this.loadValue();
            return (List<String>) this.value;
        }

        private boolean setIfNotExists(CommentedFileConfiguration fileConfiguration) {
            this.loadValue();

            if (exists && this.excluded) return false;

            if (fileConfiguration.get(this.key) == null) {
                List<String> comments = Stream.of(this.comments).collect(Collectors.toList());
                if (this.defaultValue != null) {
                    fileConfiguration.set(this.key, this.defaultValue, comments.toArray(new String[0]));
                } else {
                    fileConfiguration.addComments(comments.toArray(new String[0]));
                }

                return true;
            }

            return false;
        }

        /**
         * Resets the cached value
         */
        public void reset() {
            this.value = null;
        }

        /**
         * @return true if this setting is only a section and doesn't contain an actual value
         */
        public boolean isSection() {
            return this.defaultValue == null;
        }

        /**
         * Loads the value from the config and caches it if it isn't set yet
         */
        private void loadValue() {
            if (this.value != null) return;
            this.value = LuckyPouches.getInstance().getConfiguration().get(this.key);
        }
    }
}