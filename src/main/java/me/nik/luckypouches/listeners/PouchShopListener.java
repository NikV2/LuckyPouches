package me.nik.luckypouches.listeners;

import me.nik.luckypouches.LuckyPouches;
import me.nik.luckypouches.api.CurrencyType;
import me.nik.luckypouches.api.Pouch;
import me.nik.luckypouches.managers.MsgType;
import me.nik.luckypouches.managers.Permissions;
import me.nik.luckypouches.managers.PouchShopSign;
import me.nik.luckypouches.utils.ChatUtils;
import me.nik.luckypouches.utils.MiscUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PouchShopListener implements Listener {

    private final List<PouchShopSign> pouchShopSigns = PouchShopSign.getPouchShopSigns();

    private final LuckyPouches plugin;

    public PouchShopListener(LuckyPouches plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSignCreate(SignChangeEvent e) {
        if (!e.getPlayer().hasPermission(Permissions.ADMIN.getPermission())) return;

        String[] lines = e.getLines();

        if (lines.length < 4 || !lines[0].equalsIgnoreCase("[pouch shop]")) return;

        Player player = e.getPlayer();

        Pouch pouch = this.plugin.getPouches().stream()
                .filter(p -> p.getId().equalsIgnoreCase(lines[1]))
                .findFirst()
                .orElse(null);

        if (pouch == null) {
            player.sendMessage(MsgType.POUCH_SHOP_INVALID_POUCH.getMessage());
            return;
        }

        CurrencyType currencyType = this.plugin.getCurrencies().stream()
                .filter(c -> c.getName().equalsIgnoreCase(lines[2]))
                .findFirst()
                .orElse(null);

        if (currencyType == null) {
            player.sendMessage(MsgType.POUCH_SHOP_INVALID_CURRENCY.getMessage());
            return;
        }

        long price;

        try {

            price = Long.parseLong(lines[3]);

        } catch (NumberFormatException ex) {
            player.sendMessage(MsgType.POUCH_SHOP_INVALID_PRICE.getMessage());
            return;
        }

        this.pouchShopSigns.add(PouchShopSign.createPouchShopSign(
                        new PouchShopSign(e.getBlock().getLocation(), pouch, currencyType, price)
                )
        );

        List<String> values = this.plugin.getLang().get().getStringList("pouch_shop_sign");

        String pouchName = pouch.getItem().getItemMeta().getDisplayName();
        String priceName = currencyType.getPrefix() + MiscUtils.PRICE_FORMAT.format(price) + currencyType.getSuffix();

        for (int i = 0; i < values.size(); i++) {
            e.setLine(i, ChatUtils.format(
                    values.get(i).replace("%pouch%", pouchName).replace("%price%", priceName))
            );
        }

        player.sendMessage(MsgType.POUCH_SHOP_CREATED.getMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSignDestroy(BlockBreakEvent e) {
        if (this.pouchShopSigns.isEmpty()
                || !e.getPlayer().hasPermission(Permissions.ADMIN.getPermission())
                || !e.getBlock().getType().name().contains("SIGN")) return;

        Location brokenBlockLocation = e.getBlock().getLocation();

        this.pouchShopSigns.stream()
                .filter(pouchShopSign -> pouchShopSign.getLocation().equals(brokenBlockLocation))
                .findFirst()
                .ifPresent(pouchShopSign -> {

                    this.pouchShopSigns.remove(PouchShopSign.removePouchShopSign(pouchShopSign));

                    String location = "X: " + brokenBlockLocation.getX()
                            + " Y: " + brokenBlockLocation.getY()
                            + " Z: " + brokenBlockLocation.getZ();

                    e.getPlayer().sendMessage(MsgType.POUCH_SHOP_REMOVED.getMessage().replace("%location%", location));
                });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null
                || this.pouchShopSigns.isEmpty()
                || e.getAction() != Action.RIGHT_CLICK_BLOCK
                || !e.getClickedBlock().getType().name().contains("SIGN")) return;

        Player player = e.getPlayer();

        this.pouchShopSigns.stream()
                .filter(pouchShopSign -> pouchShopSign.getLocation().equals(e.getClickedBlock().getLocation()))
                .findFirst()
                .ifPresent(pouchShopSign -> {

                    if (pouchShopSign.getCurrencyType().withdraw(player, pouchShopSign.getPrice())) {

                        ItemStack pouchItem = pouchShopSign.getPouch().getItem();

                        if (player.getInventory().firstEmpty() == -1) {

                            player.getWorld().dropItem(player.getLocation(), pouchItem);

                        } else player.getInventory().addItem(pouchItem);

                        player.sendMessage(MsgType.POUCH_SHOP_PURCHASE.getMessage()
                                .replace("%pouch%", pouchItem.getItemMeta().getDisplayName()));

                    } else player.sendMessage(MsgType.POUCH_SHOP_UNABLE.getMessage());
                });
    }
}