package me.tomisanhues2.bantrial.commands;

import me.tomisanhues2.bantrial.Ban;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

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
        Player target = Bukkit.getPlayer(strings[0]);
        if (target == null) {
            commandSender.sendMessage(plugin.messages.getMessage("player-not-found"));
            return true;
        }
        //todo Open history gui for player

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
