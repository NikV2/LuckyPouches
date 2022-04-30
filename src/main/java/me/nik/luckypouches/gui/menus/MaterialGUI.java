package me.nik.luckypouches.gui.menus;

import me.nik.luckypouches.LuckyPouches;
import me.nik.luckypouches.gui.PaginatedMenu;
import me.nik.luckypouches.gui.PlayerMenu;
import me.nik.luckypouches.managers.MsgType;
import me.nik.luckypouches.managers.PouchCreationState;
import me.nik.luckypouches.managers.Profile;
import me.nik.luckypouches.utils.Messenger;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MaterialGUI extends PaginatedMenu {
    public MaterialGUI(PlayerMenu playerMenu, LuckyPouches plugin) {
        super(playerMenu, plugin);
    }

    @Override
    protected String getMenuName() {
        return Messenger.format("&9&lSelect the Pouch Material");
    }

    @Override
    protected int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();

        Profile profile = plugin.getProfile(p);

        switch (e.getSlot()) {

            //Exit
            case 49:
                p.closeInventory();
                profile.setPouchCreationState(PouchCreationState.NONE);
                p.sendMessage(MsgType.CREATING_CANCELLED.getMessage());
                return;

            //Previous Page
            case 48:
                if (page != 0) {
                    page = page - 1;
                    super.open();
                }
                return;

            //Next Page
            case 50:
                page = page + 1;
                super.open();
                return;

            //Search
            case 53:
                p.closeInventory();
                p.sendMessage(MsgType.CREATING_SEARCH_MATERIAL.getMessage());
                return;
        }

        if (e.getCurrentItem().getType() == Material.AIR) return;

        final ItemStack item = e.getCurrentItem();

        profile.getCreatedPouch().setMaterial(item.getType());

        if (item.getType().name().contains("HEAD") || item.getType().name().contains("SKULL")) {

            profile.setPouchCreationState(PouchCreationState.AWAITING_HEAD_DATA);

            p.sendMessage(MsgType.CREATING_HEADDATA.getMessage());

        } else {

            profile.setPouchCreationState(PouchCreationState.AWAITING_ID);

            p.sendMessage(MsgType.CREATING_ID.getMessage());
        }

        p.closeInventory();
    }

    @Override
    protected void setMenuItems() {

        addMenuBorder();

        inventory.setItem(53, makeItem(Material.BOOK, 1, "&6Search", null));

        List<ItemStack> materials = Arrays.stream(Material.values())
                .filter(material -> material != Material.AIR)
                .map(ItemStack::new)
                .collect(Collectors.toList());

        if (!materials.isEmpty()) {
            for (int i = 0; i < super.maxItemsPerPage; i++) {
                index = super.maxItemsPerPage * page + i;
                if (index >= materials.size()) break;
                if (materials.get(index) != null) {
                    inventory.addItem(materials.get(index));
                }
            }
        }
    }
}