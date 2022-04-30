package me.nik.luckypouches.managers;

import me.nik.luckypouches.LuckyPouches;
import me.nik.luckypouches.utils.Messenger;

public enum MsgType {
    PREFIX(Messenger.format(LuckyPouches.getInstance().getLang().get().getString("prefix"))),
    UPDATE_REMINDER(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("update_reminder"))),
    UPDATE_NOT_FOUND(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("update_not_found"))),
    NO_PERMISSION(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("no_perm"))),
    RELOADED(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("reloaded"))),
    PRIZE(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("prize"))),
    INVALID_PLAYER(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("invalid_player"))),
    INVALID_POUCH(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("invalid_pouch"))),
    INVALID_AMOUNT(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("invalid_amount"))),
    GIVEN_PLAYER(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("given_player"))),
    GIVEN_ALL(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("given_all"))),
    RECEIVED(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("received"))),
    POUCH_COOLDOWN(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("pouch_cooldown"))),
    CONSOLE_COMMANDS(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("console_commands"))),
    CREATING_ID(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("creating_id"))),
    CREATING_NAME(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("creating_name"))),
    CREATING_GLOW(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("creating_glow"))),
    CREATING_HEADDATA(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("creating_headdata"))),
    CREATING_ANIMATION(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("creating_animation"))),
    CREATING_MIN(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("creating_min"))),
    CREATING_MAX(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("creating_max"))),
    CREATING_CURRENCY(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("creating_currency"))),
    CREATING_CREATED(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("creating_created"))),
    CREATING_CANCELLED(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("creating_cancelled"))),
    CREATING_SEARCH_MATERIAL(PREFIX.getMessage() + Messenger.format(LuckyPouches.getInstance().getLang().get().getString("creating_search_material")));

    private final String message;

    MsgType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}