package com.ixeriox.PVEToggle;


import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class Commands implements CommandExecutor {

    private final PVEToggle plugin;

    public Commands(PVEToggle pveToggle) {
        this.plugin = pveToggle;
    }

    // Central debug print method
    private void debug(String message) {
        if (plugin.isDebugMode()) {
            System.out.println("[PVE-Toggle][DEBUG] " + message);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("pve")) {
            if (args.length == 0) {
                handleNoArguments(sender);
            } else {
                switch (args[0].toLowerCase()) {
                    case "reload":
                        handleReload(sender);
                        break;
                    case "admin":
                        handleAdminCommands(sender, args);
                        break;
                    case "toggle":
                        handleToggle(sender, args);
                        break;
                    case "check":
                        handleCheck(sender, args);
                        break;
                    case "disable-world":
                    case "enable-world":
                        handleWorldToggle(sender, args[0]);
                        break;
                    case "include-pvp":
                        handleIncludePvp(sender);
                        break;
                    default:
                        sender.sendMessage(ChatColor.RED + "Unknown subcommand. Use /pve for help.");
                        break;
                }
            }
            return true;
        }
        return false;
    }

    // Handles the basic /pve command when no arguments are provided
    private void handleNoArguments(CommandSender sender) {
        String status = getCurrentStatus(sender.getName());
        String messageKey = "PVEToggle.status." + status;
        String message = conf(messageKey);
        sender.sendMessage(message != null ? message : "Status not found.");
    }

    // Handles /pve reload command
    private void handleReload(CommandSender sender) {
        plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Configuration reloaded.");
    }

    // Handles the admin commands for PVE management
    private void handleAdminCommands(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /pve admin <toggle/check> <player>");
            return;
        }
        String action = args[1];
        if (action.equalsIgnoreCase("toggle")) {
            handleToggle(sender, args);
        } else if (action.equalsIgnoreCase("check")) {
            handleCheck(sender, args);
        } else {
            sender.sendMessage(ChatColor.RED + "Invalid action for admin.");
        }
    }

    // Toggles the PVE state for a player
    private void handleToggle(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /pve toggle <player> <on/off>");
            return;
        }
        String playerName = args[1];
        String status = args[2].toLowerCase();
        if (!status.equals("on") && !status.equals("off")) {
            sender.sendMessage(ChatColor.RED + "Invalid status. Use 'on' or 'off'.");
            return;
        }
        saveTheConfig(playerName, status);
        sender.sendMessage(ChatColor.GREEN + "PVE status for " + playerName + " set to " + status);
    }

    // Checks the PVE status of a player
    private void handleCheck(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /pve check <player>");
            return;
        }
        String playerName = args[1];
        String status = getCurrentStatus(playerName);
        sender.sendMessage(ChatColor.GREEN + "PVE status for " + playerName + ": " + status);
    }

    // Toggles PVE rules in the world
    private void handleWorldToggle(CommandSender sender, String action) {
        if (action.equalsIgnoreCase("disable-world")) {
            // Logic to disable world PVE
            sender.sendMessage(ChatColor.RED + "World PVE disabled.");
        } else {
            // Logic to enable world PVE
            sender.sendMessage(ChatColor.GREEN + "World PVE enabled.");
        }
    }

    // Placeholder for include-pvp functionality
    private void handleIncludePvp(CommandSender sender) {
        // Implement functionality as needed
        sender.sendMessage(ChatColor.YELLOW + "PVP inclusion is a placeholder.");
    }

    // Helper methods for retrieving and saving data to the config
    private String getCurrentStatus(String name) {
        String status = conf("PVEToggle.users." + name + ".status");
        if (status == null || status.isEmpty()) {
            debug("Cannot find the user '" + name + "' in the database. Creating default status.");
            plugin.getConfig().set("PVEToggle.users." + name + ".status", "off");
            plugin.saveConfig();
            status = "off";  // Default status
        }
        return status;
    }

    private String conf(String what) {
        return plugin.getConfig().getString(what);
    }

    private void saveTheConfig(String name, String status) {
        debug("Saving '" + name + "' to the config with status '" + status + "'");
        plugin.getConfig().set("PVEToggle.users." + name + ".status", status);
        plugin.saveConfig();
    }

}
