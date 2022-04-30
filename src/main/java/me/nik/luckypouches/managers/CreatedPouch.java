package me.nik.luckypouches.managers;

import me.nik.luckypouches.LuckyPouches;
import org.bukkit.Material;

import java.util.Arrays;

public class CreatedPouch {

    private boolean glow;
    private String id, name, animation, currency, headData;
    private long min, max;
    private Material material;

    public void reset() {
        this.id = null;
        this.name = null;
        this.material = null;
        this.glow = false;
        this.headData = null;
        this.animation = null;
        this.min = 0L;
        this.max = 0L;
        this.currency = null;
    }

    public void build() {

        FileBuilder fb = new FileBuilder(LuckyPouches.getInstance().getDataFolder().getPath(), "config.yml");

        String path = "pouches." + this.id;

        fb.setValue(path + ".name", this.name);
        fb.setValue(path + ".material", this.material.name());

        fb.setValue(path + ".head_data", (this.headData == null || this.headData.equalsIgnoreCase("none") ? "" : this.headData));

        fb.setValue(path + ".glow", this.glow);

        fb.setValue(path + ".animation",
                (LuckyPouches.getInstance().getAnimations().stream().anyMatch(
                        animationType -> animationType.getName().equalsIgnoreCase(this.animation))
                        ? this.animation
                        : "NONE"));

        fb.setValue(path + ".min", this.min);

        fb.setValue(path + ".max", this.max);

        fb.setValue(path + ".currency",
                (LuckyPouches.getInstance().getCurrencies().stream().anyMatch(
                        currencyType -> currencyType.getName().equalsIgnoreCase(this.currency))
                        ? this.currency
                        : "VAULT"));

        fb.setValue(path + ".lore", Arrays.asList("", "&7Open this Pouch to receive Money!", "", "&fRange: $" + this.min + " - $" + this.max));

        fb.save();

        LuckyPouches.getInstance().initializeFiles();
        LuckyPouches.getInstance().initializePouches();
    }

    public void setGlow(boolean glow) {
        this.glow = glow;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setHeadData(String headData) {
        this.headData = headData;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}