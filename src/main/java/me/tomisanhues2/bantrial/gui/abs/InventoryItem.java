package me.tomisanhues2.bantrial.gui.abs;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

public class InventoryItem {
    private Function<Player, ItemStack> iconCreator;

    public InventoryItem creator(Function<Player, ItemStack> iconCreator) {
        this.iconCreator = iconCreator;
        return this;
    }

    public Function<Player, ItemStack> getIconCreator() {
        return this.iconCreator;
    }
}
