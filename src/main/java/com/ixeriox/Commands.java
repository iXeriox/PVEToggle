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
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.invalid-command", "&lInvalid command. Try /pve toggle or /pve status or /pve help")));
            return false;
        }
        return switch (args[0].toLowerCase()) {
            case "reload" -> {
                if (sender.hasPermission("pvetoggle.pve-reload")) {
                    plugin.reloadConfig();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.config-reloaded", "&aConfig reloaded.")));
                    yield true;
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("bad-permissions.messages.no-permission-reload", "&cYou do not have permission to reload the config.")));
                    yield false;
                }
            }
            case "help" -> {
                if (sender.hasPermission("pvetoggle.pve-help")) {
                    for (String line : plugin.getConfig().getStringList("help-command.commands")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                    }
                    yield true;
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("bad-permissions.messages.no-permission-help", "&cYou do not have permission to access the help menu.")));
                    yield false;
                }
            }
            case "toggle" -> {
                if (sender instanceof Player player) {
                    if (player.hasPermission("pvetoggle.pve-toggle")) {
                        if (plugin.togglePlayer(player)) {
                            boolean isToggled = PVEToggleV2.isToggled(player);
                            String messagePath = isToggled ? "messages.pve-enabled" : "messages.pve-disabled";
                            String message = plugin.getConfig().getString(messagePath,
                                    (isToggled ? ChatColor.GREEN : ChatColor.RED) + "-> You are now " + (isToggled ? "in" : "out") + " PVE mode. Mobs " + (isToggled ? "will now" : "won't") + " target/attack you.");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                        } else {
                            String combatMessage = plugin.getConfig().getString("messages.combat-error",
                                    ChatColor.RED + "-> You cannot toggle PVE mode while in combat. Please wait for your combat to end.");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', combatMessage));
                        }
                        yield true;
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("bad-permissions.messages.no-permission-toggle", "&cYou do not have permission to toggle PVE mode.")));
                        yield false;
                    }
                }
                yield false;
            }
            case "status" -> {
                if (sender instanceof Player player) {
                    if (player.hasPermission("pvetoggle.pve-status")) {
                        boolean isToggled = PVEToggleV2.isToggled(player);
                        String statusMessage = plugin.getConfig().getString(
                                isToggled ? "status-enabled" : "status-disabled",
                                (isToggled ? ChatColor.GREEN : ChatColor.RED) + "-> You are " + (isToggled ? "in" : "out of") + " PVE mode. Mobs " + (isToggled ? "will" : "won't") + " target/attack you."
                        );
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', statusMessage));
                        yield true;
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("bad-permissions.messages.no-permission-status", "&cYou do not have permission to view PVE mode status.")));
                        yield false;
                    }
                }
                yield false;
            }
            default -> {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.invalid-command", "&cInvalid subcommand.")));
                yield false;
            }
        };
    }
}
