package me.nik.luckypouches.listeners;

import me.nik.luckypouches.LuckyPouches;
import me.nik.luckypouches.api.AnimationType;
import me.nik.luckypouches.api.events.PouchUseEvent;
import me.nik.luckypouches.managers.MsgType;
import me.nik.luckypouches.managers.Profile;
import me.nik.luckypouches.utils.MiscUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PouchListener implements Listener {

    private final LuckyPouches plugin;

    public PouchListener(LuckyPouches plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;

        Player player = e.getPlayer();

        ItemStack handItem = player.getItemInHand();

        if (handItem == null || handItem.getType() == Material.AIR || !MiscUtils.hasPouchNBTTag(handItem)) return;

        this.plugin.getPouches().stream().filter(pouch -> MiscUtils.isSimilar(handItem, pouch.getItem())).forEach(pouch -> {

            e.setCancelled(true);

            PouchUseEvent event = new PouchUseEvent(player, pouch);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;

            Profile profile = plugin.getProfile(player);
            AnimationType anim = pouch.getAnimation();

            if (profile.getAnimationCooldowns().containsKey(anim)) {

                long currentTime = System.currentTimeMillis();

                long animationCooldown = profile.getAnimationCooldowns().get(anim);

                if (animationCooldown <= currentTime) {

                    profile.getAnimationCooldowns().remove(anim);

                } else {

                    player.sendMessage(MsgType.POUCH_COOLDOWN.getMessage().replace("%time%", MiscUtils.getDurationBreakdown(animationCooldown - currentTime)).replace("%pouch%", pouch.getItem().getItemMeta().getDisplayName()));

                    return;
                }
            }

            if (handItem.getAmount() == 1) {

                player.setItemInHand(null);

                player.updateInventory();

            } else handItem.setAmount(handItem.getAmount() - 1);

            profile.addAnimationCooldown(anim);

            pouch.getAnimation().execute(player, pouch);
        });
    }
}