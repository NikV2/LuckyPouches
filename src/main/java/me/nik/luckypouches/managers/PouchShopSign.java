package me.nik.luckypouches.managers;

import me.nik.luckypouches.LuckyPouches;
import me.nik.luckypouches.api.CurrencyType;
import me.nik.luckypouches.api.Pouch;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;
import java.util.stream.Collectors;

public class PouchShopSign {

    private final Location location;
    private final Pouch pouch;
    private final CurrencyType currencyType;
    private final long price;

    public PouchShopSign(String str) {

        String[] data = str.split(",");

        this.location = new Location(
                Bukkit.getWorld(data[0]),
                Double.parseDouble(data[1]),
                Double.parseDouble(data[2]),
                Double.parseDouble(data[3])
        );

        this.pouch = LuckyPouches.getInstance().getPouches().stream().filter(pouch ->
                pouch.getId().equals(data[4])).findFirst().orElse(null);

        this.currencyType = LuckyPouches.getInstance().getCurrencies().stream().filter(currencyType ->
                currencyType.getName().equals(data[5])).findFirst().orElse(null);

        this.price = Long.parseLong(data[6]);
    }

    public PouchShopSign(Location location, Pouch pouch, CurrencyType currencyType, long price) {
        this.location = location;
        this.pouch = pouch;
        this.currencyType = currencyType;
        this.price = price;
    }

    public static PouchShopSign createPouchShopSign(PouchShopSign pouchShopSign) {

        FileBuilder file = new FileBuilder(LuckyPouches.getInstance().getDataFolder().getPath(), "pouchshops.yml");

        List<String> values = file.getConfiguration().getStringList("signs");

        values.add(pouchShopSign.toString());

        file.setValue("signs", values);

        file.save();

        return pouchShopSign;
    }

    public static PouchShopSign removePouchShopSign(PouchShopSign pouchShopSign) {

        FileBuilder file = new FileBuilder(LuckyPouches.getInstance().getDataFolder().getPath(), "pouchshops.yml");

        List<String> values = file.getConfiguration().getStringList("signs");

        values.remove(pouchShopSign.toString());

        file.setValue("signs", values);

        file.save();

        return pouchShopSign;
    }

    public static List<PouchShopSign> getPouchShopSigns() {
        return new FileBuilder(LuckyPouches.getInstance().getDataFolder().getPath(), "pouchshops.yml")
                .getConfiguration()
                .getStringList("signs")
                .stream()
                .map(PouchShopSign::new)
                .collect(Collectors.toList());
    }

    public Pouch getPouch() {
        return pouch;
    }

    public Location getLocation() {
        return location;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public long getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return this.location.getWorld().getName() + ","
                + this.location.getX() + ","
                + this.location.getY() + ","
                + this.location.getZ() + ","
                + this.pouch.getId() + ","
                + this.currencyType.getName() + ","
                + this.price;
    }
}