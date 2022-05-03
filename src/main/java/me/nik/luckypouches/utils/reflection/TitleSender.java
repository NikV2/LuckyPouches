package me.nik.luckypouches.utils.reflection;

import me.nik.luckypouches.utils.ChatUtils;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class TitleSender {

    private TitleSender() {
    }

    /**
     * Send a title message to the player
     * First try to use Spigot's api, if that doesn't work
     * Go and use Reflection
     *
     * @param player The player to send the title to
     * @param title  The title message
     */
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {

        try {

            player.sendTitle(ChatUtils.format(title), ChatUtils.format(subtitle), fadeIn, stay, fadeOut);

        } catch (NoSuchMethodError e) {

            Class<?> clsPacketPlayOutTitle = ReflectionUtils.getNMSClass("PacketPlayOutTitle");
            Class<?> clsIChatBaseComponent = ReflectionUtils.getNMSClass("IChatBaseComponent");
            Class<?> clsChatSerializer = ReflectionUtils.getNMSClass("IChatBaseComponent$ChatSerializer");
            Class<?> clsEnumTitleAction = ReflectionUtils.getNMSClass("PacketPlayOutTitle$EnumTitleAction");
            Method aMethod = ReflectionUtils.getMethod(clsChatSerializer, "a", String.class);
            Constructor<?> packetPlayOutTitleConstructor = ReflectionUtils.getConstructor(clsPacketPlayOutTitle, clsEnumTitleAction, clsIChatBaseComponent);

            try {
                Object timesPacket = ReflectionUtils.getConstructor(clsPacketPlayOutTitle, int.class, int.class, int.class).newInstance(fadeIn, stay, fadeOut);

                ReflectionUtils.sendPacket(player, timesPacket);

                if (title != null && !title.isEmpty()) {

                    Object titleComponent = aMethod.invoke(null, "{\"text\": \"" + ChatUtils.format(title) + "\"}");

                    Object titlePacket = packetPlayOutTitleConstructor.newInstance(ReflectionUtils.getField(clsEnumTitleAction, "TITLE").get(null), titleComponent);

                    ReflectionUtils.sendPacket(player, titlePacket);
                }
                if (subtitle != null && !subtitle.isEmpty()) {

                    Object subtitleComponent = aMethod.invoke(null, "{\"text\": \"" + ChatUtils.format(subtitle) + "\"}");

                    Object subtitlePacket = packetPlayOutTitleConstructor.newInstance(ReflectionUtils.getField(clsEnumTitleAction, "SUBTITLE").get(null), subtitleComponent);

                    ReflectionUtils.sendPacket(player, subtitlePacket);
                }
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
    }
}