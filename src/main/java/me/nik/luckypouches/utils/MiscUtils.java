package me.nik.luckypouches.utils;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public final class MiscUtils {

    private MiscUtils() {
    }

    /**
     * Convert a millisecond duration to a string format
     *
     * @param millis A duration to convert to a string form
     * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
     */
    public static String getDurationBreakdown(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        if (days != 0) {
            sb.append(days);
            sb.append(" Days ");
        }
        if (hours != 0) {
            sb.append(hours);
            sb.append(" Hours ");
        }
        if (minutes != 0) {
            sb.append(minutes);
            sb.append(" Minutes ");
        }
        if (seconds != 0) {
            sb.append(seconds);
            sb.append(" Seconds");
        }

        return (sb.toString());
    }

    public static <E> E randomElement(final Collection<? extends E> collection) {
        if (collection.size() == 0) {
            return null;
        }

        int index = new Random().nextInt(collection.size());
        if (collection instanceof List) {
            return ((List<? extends E>) collection).get(index);
        } else {
            Iterator<? extends E> iter = collection.iterator();
            for (int i = 0; i < index; i++) {
                iter.next();
            }
            return iter.next();
        }
    }

    public static boolean isSimilar(final ItemStack one, final ItemStack two) {
        if (one.getType() == two.getType() &&
                one.hasItemMeta() &&
                one.getItemMeta().hasDisplayName() &&
                one.getItemMeta().getDisplayName().equalsIgnoreCase(two.getItemMeta().getDisplayName()) &&
                one.getItemMeta().hasLore() &&
                one.getItemMeta().getLore().size() == two.getItemMeta().getLore().size()) {

            byte b = 0;

            for (String str : one.getItemMeta().getLore()) {

                if (!str.equals(two.getItemMeta().getLore().get(b))) return false;

                b++;
            }

            return true;
        }

        return false;
    }

    public static boolean hasPouchNBTTag(final ItemStack item) {
        return new NBTItem(item).getBoolean("luckypouches");
    }
}