package me.tomisanhues2.bantrial.commands;

import me.tomisanhues2.bantrial.Ban;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class UnbanCommand implements TabExecutor {
    private final Ban plugin;

    public UnbanCommand(Ban plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!command.getName().equalsIgnoreCase("unban")) {
            return true;
        }

        if (!commandSender.hasPermission("bantrial.unban")) {
            commandSender.sendMessage(plugin.messages.getMessage("no-permission"));
            return true;
        }

        if (strings.length == 0) {
            commandSender.sendMessage(plugin.messages.getMessage("unban-usage"));
            return true;
        }
        System.out.println(strings[0]);
        OfflinePlayer target = Bukkit.getOfflinePlayer(strings[0]);

        if (target == null) {
            commandSender.sendMessage(plugin.messages.getMessage("player-not-found"));
            return true;
        }
        if (!plugin.database.isPlayerActiveBan(target.getUniqueId())) {
            commandSender.sendMessage(plugin.messages.getMessage("player-not-banned"));
            return true;
        }
        plugin.banManager.removeBan(target.getUniqueId());
        commandSender.sendMessage(plugin.messages.getMessage("unban-success").replace("%player%", target.getName()));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        //todo: Tab complete logic
        return List.of();
    }
}
