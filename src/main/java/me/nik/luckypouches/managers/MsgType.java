package me.nik.luckypouches.managers;

import me.nik.luckypouches.LuckyPouches;
import me.nik.luckypouches.utils.ChatUtils;

public enum MsgType {
    PREFIX(ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("prefix"))),
    UPDATE_REMINDER(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("update_reminder"))),
    UPDATE_NOT_FOUND(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("update_not_found"))),
    NO_PERMISSION(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("no_perm"))),
    RELOADED(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("reloaded"))),
    PRIZE(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("prize"))),
    INVALID_PLAYER(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("invalid_player"))),
    INVALID_POUCH(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("invalid_pouch"))),
    INVALID_AMOUNT(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("invalid_amount"))),
    GIVEN_PLAYER(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("given_player"))),
    GIVEN_ALL(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("given_all"))),
    RECEIVED(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("received"))),
    POUCH_COOLDOWN(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("pouch_cooldown"))),
    CONSOLE_COMMANDS(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("console_commands"))),
    CREATING_ID(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("creating_id"))),
    CREATING_NAME(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("creating_name"))),
    CREATING_GLOW(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("creating_glow"))),
    CREATING_HEADDATA(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("creating_headdata"))),
    CREATING_ANIMATION(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("creating_animation"))),
    CREATING_MIN(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("creating_min"))),
    CREATING_MAX(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("creating_max"))),
    CREATING_CURRENCY(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("creating_currency"))),
    CREATING_CREATED(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("creating_created"))),
    CREATING_CANCELLED(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("creating_cancelled"))),
    CREATING_SEARCH_MATERIAL(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("creating_search_material"))),
    POUCH_SHOP_INVALID_POUCH(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("pouch_shop_invalid_pouch"))),
    POUCH_SHOP_INVALID_CURRENCY(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("pouch_shop_invalid_currency"))),
    POUCH_SHOP_INVALID_PRICE(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("pouch_shop_invalid_price"))),
    POUCH_SHOP_CREATED(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("pouch_shop_created"))),
    POUCH_SHOP_REMOVED(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("pouch_shop_removed"))),
    POUCH_SHOP_PURCHASE(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("pouch_shop_purchase"))),
    POUCH_SHOP_UNABLE(PREFIX.getMessage() + ChatUtils.format(LuckyPouches.getInstance().getLang().get().getString("pouch_shop_unable")));

    private final String message;

    MsgType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}