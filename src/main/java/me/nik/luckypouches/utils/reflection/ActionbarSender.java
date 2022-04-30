package me.nik.luckypouches.utils.reflection;

import me.nik.luckypouches.utils.Messenger;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public final class ActionbarSender {

    private ActionbarSender() {
    }

    /**
     * Send an actionbar message to the player
     * First try to use Spigot's api, if that doesn't work
     * Go and use Reflection
     *
     * @param player The player to send the actionbar message to
     * @param text   The actionbar message
     */
    public static void sendActionbar(Player player, String text) {
        try {

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Messenger.format(text)));

        } catch (NoSuchMethodError e) {

            try {

                Object chatBaseComponent = ReflectionUtils.getMethod(ReflectionUtils.getNMSClass("IChatBaseComponent$ChatSerializer"), "a", String.class).invoke(null, "{\"text\":\"" + Messenger.format(text) + "\"}");

                Object packet = ReflectionUtils.getConstructor(ReflectionUtils.getNMSClass("PacketPlayOutChat"), ReflectionUtils.getNMSClass("IChatBaseComponent"), byte.class).newInstance(chatBaseComponent, (byte) 2);

                ReflectionUtils.sendPacket(player, packet);

            } catch (InstantiationException | InvocationTargetException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
    }
}