package me.tomisanhues2.bantrial.commands;

import me.tomisanhues2.bantrial.Ban;
import me.tomisanhues2.bantrial.data.History;
import me.tomisanhues2.bantrial.gui.impl.HistoryMenu;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HistoryCommand implements TabExecutor {
    private final Ban plugin;

    public HistoryCommand(Ban plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(plugin.messages.getMessage("sender-not-player"));
            return true;
        }

        if (!command.getName().equalsIgnoreCase("history")) {
            return true;
        }

        if (!commandSender.hasPermission("bantrial.history")) {
            commandSender.sendMessage(plugin.messages.getMessage("no-permission"));
            return true;
        }

        if (strings.length == 0) {
            commandSender.sendMessage(plugin.messages.getMessage("history-usage"));
            return true;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(strings[0]);
        if (target == null) {
            commandSender.sendMessage(plugin.messages.getMessage("player-not-found"));
            return true;
        }
        ArrayList<History> histories = plugin.database.getHistories(target.getUniqueId());
        if (histories == null || histories.size() == 0) {
            commandSender.sendMessage(plugin.messages.getMessage("player-not-found"));
            return true;
        }
        plugin.guiManager.openGUI(new HistoryMenu(histories), (Player) commandSender);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length > 1) {
            return List.of();
        }
        return null;
    }
}
