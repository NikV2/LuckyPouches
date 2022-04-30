package me.nik.luckypouches.managers;

public enum PouchCreationState {
    NONE,
    AWAITING_MATERIAL,
    AWAITING_HEAD_DATA,
    AWAITING_ID,
    AWAITING_NAME,
    AWAITING_GLOW,
    AWAITING_ANIMATION,
    AWAITING_MIN,
    AWAITING_MAX,
    AWAITING_CURRENCY
}