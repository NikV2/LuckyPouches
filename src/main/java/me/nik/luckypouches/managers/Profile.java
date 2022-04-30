package me.nik.luckypouches.managers;

import me.nik.luckypouches.api.AnimationType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Profile {

    private final Player player;

    private final Map<AnimationType, Long> animationCooldowns = new HashMap<>();
    private final CreatedPouch createdPouch = new CreatedPouch();
    private PouchCreationState pouchCreationState = PouchCreationState.NONE;

    public Profile(UUID uuid) {
        this.player = Bukkit.getPlayer(uuid);
    }

    public PouchCreationState getPouchCreationState() {
        return pouchCreationState;
    }

    public void setPouchCreationState(PouchCreationState pouchCreationState) {

        if (pouchCreationState == PouchCreationState.NONE) {

            //Reset
            this.createdPouch.reset();
        }

        this.pouchCreationState = pouchCreationState;
    }

    public CreatedPouch getCreatedPouch() {
        return createdPouch;
    }

    public Map<AnimationType, Long> getAnimationCooldowns() {
        return animationCooldowns;
    }

    public Player getPlayer() {
        return player;
    }

    public void addAnimationCooldown(AnimationType animation) {
        if (animation.getCooldown() <= 0L) return;

        animationCooldowns.put(animation, (animation.getCooldown() * 1000L + System.currentTimeMillis()));
    }
}