package me.nik.luckypouches.api.events;

import me.nik.luckypouches.api.Pouch;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PouchUseEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Pouch pouch;
    private boolean cancel = false;

    public PouchUseEvent(Player player, Pouch pouch) {
        this.player = player;
        this.pouch = pouch;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}