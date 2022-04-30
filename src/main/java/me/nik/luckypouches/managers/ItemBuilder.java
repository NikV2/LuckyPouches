package me.nik.luckypouches.managers;

import me.nik.luckypouches.utils.Messenger;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private final ItemStack itemStack;
    private Material material;
    private String name;
    private List<String> lores;
    private boolean glowing;
    private boolean head;
    private boolean isUrl;
    private String headData;

    public ItemBuilder() {
        this.itemStack = new ItemStack(Material.STONE);
        this.name = "";
        this.material = Material.STONE;
        this.lores = new ArrayList<>();
        this.glowing = false;
        this.head = false;
        this.isUrl = false;
        this.headData = "";
    }

    public void setMaterial(String material) {
        switch (material) {
            case "SKULL_ITEM":
            case "PLAYER_HEAD":
                this.head = true;
                return;
        }

        try {
            this.material = Material.valueOf(material);
        } catch (Exception e) {
            this.material = Material.STONE;
        }
    }

    public void setHeadData(String data) {
        this.headData = data;

        if (data.length() > 16) {
            this.isUrl = data.startsWith("http");
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLores(List<String> lores) {
        this.lores = lores;
    }

    public void setGlowing(boolean glowing) {
        this.glowing = glowing;
    }

    public ItemStack build() {
        ItemStack item;
        if (this.head) {
            try {
                item = new ItemStack(Material.valueOf("PLAYER_HEAD"));
            } catch (IllegalArgumentException e) {
                item = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (byte) 3);
            }

            if (!this.headData.isEmpty()) {
                if (this.isUrl) {
                    item = SkullCreator.itemWithUrl(item, this.headData);
                } else {
                    item = SkullCreator.itemWithBase64(item, this.headData);
                }
            }
        } else {
            item = this.itemStack;
            item.setType(this.material);
        }

        ItemMeta meta = item.getItemMeta();

        if (this.name != null) {
            meta.setDisplayName(Messenger.format(this.name));
        }

        if (this.lores != null && this.lores.size() > 0) {

            List<String> loresToAdd = new ArrayList<>();

            for (String l : this.lores) {
                loresToAdd.add(Messenger.format(l));
            }

            meta.setLore(loresToAdd);
        }

        if (this.glowing) {

            meta.addEnchant(Enchantment.DURABILITY, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        item.setItemMeta(meta);

        return item;
    }
}