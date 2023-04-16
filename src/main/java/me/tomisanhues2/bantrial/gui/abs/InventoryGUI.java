package me.tomisanhues2.bantrial.gui.abs;

import me.tomisanhues2.bantrial.Ban;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class InventoryGUI implements InventoryHandler {
    private final Ban plugin = Ban.getInstance();
    private final Inventory inventory;
    private final Map<Integer, InventoryButton> buttonMap = new HashMap<>();
    private final Map<Integer, InventoryItem> itemMap = new HashMap<>();

    protected int currentPage;
    protected int maxPages;

    public InventoryGUI(int maxPages) {
        this.inventory = this.createInventory();
        this.maxPages = (maxPages / 45);
        currentPage = 0;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void decorate(Player player) {
        System.out.println("Current page: " + this.currentPage);

        this.itemMap.forEach((slot, item) -> {
            ItemStack itemStack = item.getIconCreator().apply(player);
            this.inventory.setItem(slot, itemStack);
        });
        this.buttonMap.forEach((slot, button) -> {
            ItemStack item = button.getIconCreator().apply(player);
            this.inventory.setItem(slot, item);
        });
    }

    public void addItem(int slot, InventoryItem item) {
        this.itemMap.put(slot, item);
    }

    public void addButton(int slot, InventoryButton button) {
        this.buttonMap.put(slot, button);
    }

    public void incrementPage() {
        if (this.currentPage < this.maxPages) {
            this.currentPage++;
        }
    }

    public void decrementPage() {
        if (this.currentPage > 0) {
            this.currentPage--;
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        int slot = event.getSlot();
        InventoryButton button = this.buttonMap.get(slot);
        if (button != null) {
            button.getEventConsumer().accept(event);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        this.decorate((Player) event.getPlayer());
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }

    protected abstract Inventory createInventory();

    protected ItemStack createBackgroundItem() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }
}
