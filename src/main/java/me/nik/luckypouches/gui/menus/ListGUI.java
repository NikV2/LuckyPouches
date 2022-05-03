package me.nik.luckypouches.gui.menus;

import me.nik.luckypouches.LuckyPouches;
import me.nik.luckypouches.api.Pouch;
import me.nik.luckypouches.gui.PaginatedMenu;
import me.nik.luckypouches.gui.PlayerMenu;
import me.nik.luckypouches.managers.ItemBuilder;
import me.nik.luckypouches.utils.ChatUtils;
import me.nik.luckypouches.utils.MiscUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ListGUI extends PaginatedMenu {

    private final ConfigurationSection section = plugin.getConfiguration().getConfigurationSection("pouches");

    public ListGUI(PlayerMenu playerMenu, LuckyPouches plugin) {
        super(playerMenu, plugin);
    }

    @Override
    protected String getMenuName() {
        return ChatUtils.format("&9&lPouch List");
    }

    @Override
    protected int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        ItemStack item = e.getCurrentItem();
        if (item == null) return;

        switch (e.getSlot()) {
            //Exit
            case 49:
                p.closeInventory();
                new MainGUI(playerMenu, plugin).open();
                break;
            //Previous Page
            case 48:
                if (page != 0) {
                    page = page - 1;
                    super.open();
                }
                break;
            //Next Page
            case 50:
                page = page + 1;
                super.open();
                break;
            default:
                for (Pouch pouch : plugin.getPouches()) {
                    if (MiscUtils.isSimilar(item, pouch.getItem())) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "luckypouches give " + p.getName() + " " + pouch.getId());
                    }
                }
                break;
        }
    }

    @Override
    protected void setMenuItems() {

        addMenuBorder();

        if (this.section == null) return;

        List<ItemStack> pouches = new ArrayList<>();

        for (String pouch : this.section.getKeys(false)) {
            ItemBuilder pb = new ItemBuilder();
            pb.setName(this.section.getString(pouch + ".name"));
            pb.setMaterial(this.section.getString(pouch + ".material"));
            pb.setGlowing(this.section.getBoolean(pouch + ".glow"));
            pb.setHeadData(this.section.getString(pouch + ".head_data"));
            pb.setLores(this.section.getStringList(pouch + ".lore"));
            pb.setGlowing(this.section.getBoolean(pouch + ".glow"));
            pouches.add(pb.build());
        }

        if (!pouches.isEmpty()) {
            for (int i = 0; i < super.maxItemsPerPage; i++) {
                index = super.maxItemsPerPage * page + i;
                if (index >= pouches.size()) break;
                if (pouches.get(index) != null) {
                    inventory.addItem(pouches.get(index));
                }
            }
        }
    }
}