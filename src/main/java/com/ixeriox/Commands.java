package com.ixeriox;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    private final PVEToggleV2 plugin;

    public Commands(PVEToggleV2 instance) {
        this.plugin = instance;
        plugin.write("Commands registered", "Event");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.BOLD + "Invalid command. Try /pve toggle or /pve status or /pve help");
            return false;
        }
        return switch (args[0].toLowerCase()) {
            case "help" -> {
                sender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + " • PVEToggleV2 commands •");
                sender.sendMessage(ChatColor.AQUA + "-> /pve toggle - Toggle PVE mode");
                sender.sendMessage(ChatColor.AQUA + "-> /pve status - Check PVE mode status");
                yield true;
            }
            case "toggle" -> {
                if (sender instanceof Player player) {
                    if(plugin.togglePlayer(player)) {
                        boolean isToggled = PVEToggleV2.isToggled(player);
                        player.sendMessage((isToggled ? ChatColor.GREEN : ChatColor.RED) +"-> You are now " + (isToggled ? "in" : "out") + " PVE mode. Mobs " + (isToggled ? "will now" : "won't") + " target/attack you.");
                    } else {
                        player.sendMessage(ChatColor.RED + "-> You cannot toggle PVE mode while in combat. Please wait for your combat to end.");
                    }
                }
                yield true;
            }
            case "status" -> {
                if (sender instanceof Player player) {
                    boolean isToggled = PVEToggleV2.isToggled(player);
                    player.sendMessage((isToggled ? ChatColor.GREEN : ChatColor.RED) + "-> You are " + (PVEToggleV2.isToggled(player) ? "in" : "out of") + " PVE mode. Mobs " + (isToggled ? "will" : "won't") + " target/attack you. ");
                }
                yield true;
            }
            default -> false;
        };
    }
}
