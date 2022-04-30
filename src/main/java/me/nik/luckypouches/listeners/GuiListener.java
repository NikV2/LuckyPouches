package me.nik.luckypouches.listeners;

import me.nik.luckypouches.gui.Menu;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class GuiListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        InventoryHolder holder = e.getInventory().getHolder();

        if (!(holder instanceof Menu) || e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
            return;

        e.setCancelled(true);

        Menu menu = (Menu) holder;
        menu.handleMenu(e);
    }
}