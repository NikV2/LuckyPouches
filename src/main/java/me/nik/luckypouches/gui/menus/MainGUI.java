package me.nik.luckypouches.gui.menus;

import me.nik.luckypouches.LuckyPouches;
import me.nik.luckypouches.gui.Menu;
import me.nik.luckypouches.gui.PlayerMenu;
import me.nik.luckypouches.managers.MsgType;
import me.nik.luckypouches.managers.PouchCreationState;
import me.nik.luckypouches.utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class MainGUI extends Menu {
    public MainGUI(PlayerMenu playerMenu, LuckyPouches plugin) {
        super(playerMenu, plugin);
    }

    @Override
    protected String getMenuName() {
        return ChatUtils.format("&9&lLuckyPouches Menu");
    }

    @Override
    protected int getSlots() {
        return 36;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        switch (e.getSlot()) {
            case 11:
                p.closeInventory();
                new ListGUI(playerMenu, plugin).open();
                break;
            case 13:
                p.closeInventory();
                this.plugin.getProfile(p).setPouchCreationState(PouchCreationState.AWAITING_MATERIAL);
                new MaterialGUI(playerMenu, plugin).open();
                break;
            case 15:
                p.closeInventory();
                plugin.onDisable();
                plugin.onEnable();
                p.sendMessage(MsgType.RELOADED.getMessage());
                break;
            case 31:
                p.closeInventory();
                break;
        }
    }

    @Override
    protected void setMenuItems() {

        ItemStack close = makeItem(Material.BARRIER, 1, "&cExit", null);
        inventory.setItem(31, close);

        ItemStack pouches = makeItem(Material.BOOK, 1, "&ePouch List", Arrays.asList("", "&7List of all the Pouches"));
        inventory.setItem(11, pouches);

        ItemStack create = makeItem(Material.BOOK, 1, "&eCreate Pouch", Arrays.asList("", "&7Create a new Pouch"));
        inventory.setItem(13, create);

        ItemStack reload = makeItem(Material.BOOK, 1, "&eReload", Arrays.asList("", "&7Reload LuckyPouches", "", "&c(Warning)", "&7Reloading might cause issues"));
        inventory.setItem(15, reload);
    }
}