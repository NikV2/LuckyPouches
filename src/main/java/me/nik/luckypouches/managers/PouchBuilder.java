package me.nik.luckypouches.managers;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.nik.luckypouches.utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PouchBuilder {

    private final ItemStack itemStack;
    private Material material;
    private String name;
    private List<String> lores;
    private boolean glowing;
    private boolean head;
    private boolean isUrl;
    private String headData;

    public PouchBuilder() {
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
        } catch (IllegalArgumentException e) {
            this.material = Material.STONE;
        }
    }

    public void setHeadData(String data) {
        this.headData = data;

        if (data.length() > 16) this.isUrl = data.startsWith("http");
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
                item = this.isUrl
                        ? SkullCreator.itemWithUrl(item, this.headData)
                        : SkullCreator.itemWithBase64(item, this.headData);
            }
        } else {
            item = this.itemStack;
            item.setType(this.material);
        }

        ItemMeta meta = item.getItemMeta();

        if (this.name != null) {
            meta.setDisplayName(ChatUtils.format(this.name));
        }

        if (this.lores != null && this.lores.size() > 0) {
            meta.setLore(this.lores.stream().map(ChatUtils::format).collect(Collectors.toList()));
        }

        if (this.glowing && !meta.hasEnchants()) {

            //Apply by using both methods due to 1.8 -> Latest.
            item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            meta.addEnchant(Enchantment.DURABILITY, 1, true);

            //Hide them while keeping the glow effect.
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setBoolean("luckypouches", true);

        return nbtItem.getItem();
    }
}