package me.nik.luckypouches.gui;

import me.nik.luckypouches.LuckyPouches;
import org.bukkit.Material;

public abstract class PaginatedMenu extends Menu {

    protected int page = 0;

    protected int maxItemsPerPage = 45;

    protected int index = 0;

    public PaginatedMenu(PlayerMenu playerMenu, LuckyPouches plugin) {
        super(playerMenu, plugin);
    }

    public void addMenuBorder() {
        inventory.setItem(48, makeItem(Material.BOOK, 1, "&6Previous Page", null));
        inventory.setItem(49, makeItem(Material.BARRIER, 1, "&cExit", null));
        inventory.setItem(50, makeItem(Material.BOOK, 1, "&6Next Page", null));
    }
}