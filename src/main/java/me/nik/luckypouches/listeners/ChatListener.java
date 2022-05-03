package me.nik.luckypouches.listeners;

import me.nik.luckypouches.LuckyPouches;
import me.nik.luckypouches.gui.PlayerMenu;
import me.nik.luckypouches.gui.menus.search.SearchMaterialGUI;
import me.nik.luckypouches.managers.MsgType;
import me.nik.luckypouches.managers.PouchCreationState;
import me.nik.luckypouches.managers.Profile;
import me.nik.luckypouches.utils.ChatUtils;
import me.nik.luckypouches.utils.TaskUtils;
import me.nik.luckypouches.utils.reflection.ActionbarSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Objects;

public class ChatListener implements Listener {

    private static final String regex = "[^\\d.]";
    private final LuckyPouches plugin;

    public ChatListener(LuckyPouches plugin) {
        this.plugin = plugin;

        TaskUtils.taskTimerAsync(() -> plugin.getProfileMap().values().stream()
                .filter(Objects::nonNull)
                .filter(profile -> profile.getPouchCreationState() != PouchCreationState.NONE)
                .forEach(profile -> ActionbarSender.sendActionbar(
                        profile.getPlayer(),
                        ChatUtils.format("&f&lType &b&lcancel&f&l to return"))
                ), 40, 40);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {

        Player player = e.getPlayer();

        Profile profile = plugin.getProfile(player);

        if (profile.getPouchCreationState() == PouchCreationState.NONE) return;

        e.setCancelled(true);

        String message = e.getMessage();

        if (message.equalsIgnoreCase("cancel")) {

            profile.setPouchCreationState(PouchCreationState.NONE);

            player.sendMessage(MsgType.CREATING_CANCELLED.getMessage());

            return;
        }

        String messageToSend;

        long parsedString;

        try {

            parsedString = Long.parseLong(message.trim().replaceAll(regex, ""));

        } catch (NumberFormatException ex) {

            parsedString = 1L;
        }

        switch (profile.getPouchCreationState()) {

            case AWAITING_MATERIAL:

                //Has to be on the main thread
                TaskUtils.task(() -> new SearchMaterialGUI(new PlayerMenu(player), this.plugin, message).open());

                return;

            case AWAITING_HEAD_DATA:

                profile.getCreatedPouch().setHeadData(message);
                profile.setPouchCreationState(PouchCreationState.AWAITING_ID);

                messageToSend = MsgType.CREATING_ID.getMessage();

                break;

            case AWAITING_ID:

                profile.getCreatedPouch().setId(message.toLowerCase().trim());
                profile.setPouchCreationState(PouchCreationState.AWAITING_NAME);

                messageToSend = MsgType.CREATING_NAME.getMessage();

                break;

            case AWAITING_NAME:

                profile.getCreatedPouch().setName(message);
                profile.setPouchCreationState(PouchCreationState.AWAITING_GLOW);

                messageToSend = MsgType.CREATING_GLOW.getMessage();

                break;

            case AWAITING_GLOW:

                boolean shouldGlow = false;

                try {

                    shouldGlow = Boolean.parseBoolean(message);

                } catch (Exception ignored) {
                }

                profile.getCreatedPouch().setGlow(shouldGlow);
                profile.setPouchCreationState(PouchCreationState.AWAITING_ANIMATION);

                messageToSend = MsgType.CREATING_ANIMATION.getMessage();

                break;

            case AWAITING_ANIMATION:

                profile.getCreatedPouch().setAnimation(message);
                profile.setPouchCreationState(PouchCreationState.AWAITING_MIN);

                messageToSend = MsgType.CREATING_MIN.getMessage();

                break;

            case AWAITING_MIN:

                profile.getCreatedPouch().setMin(parsedString);
                profile.setPouchCreationState(PouchCreationState.AWAITING_MAX);

                messageToSend = MsgType.CREATING_MAX.getMessage();

                break;

            case AWAITING_MAX:

                profile.getCreatedPouch().setMax(parsedString);
                profile.setPouchCreationState(PouchCreationState.AWAITING_CURRENCY);

                messageToSend = MsgType.CREATING_CURRENCY.getMessage();

                break;

            case AWAITING_CURRENCY:

                profile.getCreatedPouch().setCurrency(message);
                profile.getCreatedPouch().build();
                profile.setPouchCreationState(PouchCreationState.NONE);

                messageToSend = MsgType.CREATING_CREATED.getMessage();

                break;

            default:

                return;
        }

        player.sendMessage(messageToSend);
    }
}