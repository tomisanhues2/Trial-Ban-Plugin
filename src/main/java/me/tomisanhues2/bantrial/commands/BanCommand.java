package me.tomisanhues2.bantrial.commands;

import me.tomisanhues2.bantrial.Ban;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class BanCommand implements TabExecutor {
    private final Ban plugin;

    public BanCommand(Ban plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(plugin.messages.getMessage("sender-not-player"));
            return true;
        }

        if (!command.getName().equalsIgnoreCase("ban") || !command.getName().equalsIgnoreCase("tempban")) {
            return true;
        }

        if (!commandSender.hasPermission("bantrial.ban")) {
            commandSender.sendMessage(plugin.messages.getMessage("no-permission"));
            return true;
        }

        if (strings.length == 0) {
            commandSender.sendMessage(plugin.messages.getMessage("ban-usage"));
            return true;
        }
        //todo: Ban logic

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        //todo: Tab complete logic
        return null;
    }
}
