package me.tomisanhues2.bantrial.commands;

import me.tomisanhues2.bantrial.Ban;
import me.tomisanhues2.bantrial.utils.BanType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
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

        if (!command.getName().equalsIgnoreCase("ban")) {
            return true;
        }

        if (!commandSender.hasPermission("bantrial.ban")) {
            commandSender.sendMessage(plugin.messages.getMessage("no-permission"));
            return true;
        }
        System.out.println("Strings: " + Arrays.toString(strings));

        if (strings.length == 0) {
            commandSender.sendMessage(plugin.messages.getMessage("ban-usage"));
            return true;
        }
        //todo: Ban logic
        Player target = plugin.getServer().getPlayer(strings[0]);
        if (target == null) {
            commandSender.sendMessage(plugin.messages.getMessage("player-not-found"));
            return true;
        }
        if (plugin.database.isPlayerActiveBan(target.getUniqueId())) {
            commandSender.sendMessage(plugin.messages.getMessage("player-already-banned"));
            return true;
        }
        if (strings.length == 1) {
            plugin.banManager.addBan(target.getUniqueId(), "You have been banned!", BanType.PERM_BAN.toString(), ((Player) commandSender).getUniqueId());
            return true;
        }
        String arg1 = strings[1];
        boolean hasDuration = Character.isDigit(arg1.charAt(0));
        String duration = BanType.PERM_BAN.toString();
        String reason = String.join(" ", Arrays.copyOfRange(strings, 1, strings.length));

        //Find out if arg1 is a time or a reason
        if (hasDuration) {
            duration = arg1;
            reason = String.join(" ", Arrays.copyOfRange(strings, 2, strings.length));
        }
        plugin.banManager.addBan(target.getUniqueId(), reason, duration, ((Player) commandSender).getUniqueId());

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        //todo: Tab complete logic
        return null;
    }
}
