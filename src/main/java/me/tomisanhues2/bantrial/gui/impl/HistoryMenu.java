package me.tomisanhues2.bantrial.gui.impl;

import me.tomisanhues2.bantrial.Ban;
import me.tomisanhues2.bantrial.data.History;
import me.tomisanhues2.bantrial.gui.abs.InventoryButton;
import me.tomisanhues2.bantrial.gui.abs.InventoryGUI;
import me.tomisanhues2.bantrial.gui.abs.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HistoryMenu extends InventoryGUI {

    private final Ban ban = Ban.getInstance();

    private ArrayList<History> history;

    public HistoryMenu(ArrayList<History> history) {
        super(history.size());
        this.history = history;
        System.out.println("History size: " + history.size());
    }

    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 54, "Ban Menu");
    }

    @Override
    public Inventory getInventory() {
        return super.getInventory();
    }

    @Override
    public void decorate(Player player) {
        InventoryItem backgroundItem =
                new InventoryItem().creator((p) -> createBackgroundItem());

        for (int i = 0; i < getInventory().getSize(); i++) {
            this.addItem(i, backgroundItem);
        }

        for (int i = 0; i < 45; i++) {
            int index = (i + (this.currentPage * 45));
            if (index >= this.history.size()) {
                break;
            }
            History history = this.history.get(index);
            InventoryItem historyItem =
                    new InventoryItem().creator((p) -> createHistoryItem(history));
            this.addItem(i, historyItem);
        }

        InventoryButton previousPage =
                new InventoryButton().creator(player1 -> {
                    ItemStack item = new ItemStack(Material.ARROW);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName("Previous Page");
                    meta.setLore(List.of("Click to go to the previous page"));
                    item.setItemMeta(meta);
                    return item;
                }).consumer(event -> {
                    this.decrementPage();
                    decorate(player);
                });

        InventoryButton nextPage =
                new InventoryButton().creator(player1 -> {
                    ItemStack item = new ItemStack(Material.ARROW);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName("Next Page");
                    meta.setLore(List.of("Click to go to the next page"));
                    item.setItemMeta(meta);
                    return item;
                }).consumer(event -> {
                    this.incrementPage();
                    decorate(player);
                });

        this.addButton(46, previousPage);
        this.addButton(52, nextPage);



        super.decorate(player);
    }

    private ItemStack createHistoryItem(History history) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Ban #" + history.getId());
        meta.setLore(List.of(
                "Reason: " + history.getReason(),
                "Date: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(history.getDate()),
                "Banned by: " + Bukkit.getOfflinePlayer(history.getBannerUUID()).getName(),
                "Duration: " + history.getDuration(),
                "Banned until: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(history.getUnbanDate()),
                "Status: " + history.getStatus()));
        item.setItemMeta(meta);
        return item;
    };

    @Override
    public void onClick(InventoryClickEvent event) {
        super.onClick(event);
    }
}
